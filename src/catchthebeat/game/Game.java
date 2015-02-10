package catchthebeat.game;

import catchthebeat.ui.FrontBackLink;
import catchthebeat.ui.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Class Game
 * 
 * This is the base class for the game backend 
 * including game model (logic) and current game state,
 * excluding any UI (graphics, animation, sound, key listener) and threading.
 * 
 * The class holds information about state, i.e.
 * 1) Current game settings (difficulty, tempo, number of players).
 * 2) Player data (number of players, who is still in the game and who is currently playing).
 * 3) Beat sequence data and player's current position in the sequence
 *   (+ information about the beat sequence at tick-time).
 * 4) Round data (what is the result of current round).
 * 5) Game score data.
 * 
 * The class communicates with the frontend via FrontBackLink class.
 * 
 * This class is abstract as there are 2 game modes that incorporate slightly
 * different rules.
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public abstract class Game {
    // initialise class attributes

    protected boolean isRunning; // true = game in progress; false = game over
    
    // each item of the list represents a beat in the sequence
    // "it" iterates through the list during a round and compares
    // the input beat to the beat in the list
    protected ArrayList<Integer> beats;
    protected Iterator<Integer> it;
    
    // each item of the list represents a player
    // noPlayers is used to determine the size of array at the construction time 
    // currentPlayer represents the index of current player in the array
    // playersOut is increased every time a player is out and it is used
    // to decide when to finish the game
    protected Player[] players;
    protected int noPlayers;
    protected int currentPlayer;
    protected int playersOut;
    
    // used to generate random beats at the beginning and
    // when the player is computer
    protected Random rand;
    
    // the link through which the class communicates with the frontend
    protected FrontBackLink frontLink;
    
    // the following state attributes are self-explanatory
    // and are needed to determine the right state change
    protected int currentCorrectBeat;
    protected boolean lastBeatFlag;
    protected boolean nextTickEndsRound;
    protected boolean currentBeatAnsweredFlag;
    protected int roundResult;
    private static final int TIMEOUT = -1;
    private static final int FALSE = 0;
    private static final int CORRECT = 1;
    
    // holds the score for the current game
    protected Score score;
    
    // Difficulty value is used as an index for BEATS_TO_START_WITH and
    // NO_OF_BEAT_TYPES array. Currently, the game supports exactly 4 beat types,
    // but it is ready to be extended in the future.
    protected int difficulty;
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;
    protected static final int BEATS_TO_START_WITH[] = {3, 5, 7}; // index position = difficulty
    protected static final int NO_OF_BEAT_TYPES[] = {4, 4, 4}; // index position = difficulty

    /**
     * Constructor first sets the state variables, then creates and fills the
     * beat array and then returns back to the implementing class where the players
     * should be created according to the game type-specific rules.
     * 
     * @param  playerNames   Array of Strings containing players' names.
     * @param  difficulty    Integer representing the difficulty: 0=easy, 1=medium, 2=hard.
     * @param  frontLink    FrontBackLink object serves to connect frontend and backend of the game.
     */
    public Game(String[] playerNames, int difficulty, FrontBackLink frontLink) {
        this.currentBeatAnsweredFlag = true;
        this.currentPlayer = -1;
        this.playersOut = 0;
        this.score = new Score();
        this.difficulty = difficulty;
        this.frontLink = frontLink;
        rand = new Random();
        beats = new ArrayList<Integer>();
        this.generateBeats(BEATS_TO_START_WITH[difficulty] - 1);
        isRunning = true;
        nextTickEndsRound = false;
    }

    /**
     * This method generates the beats to start with (depending on chosen
     * difficulty). There number of beats is actually one less as the Beatstarter
     * adds an extra beat at the time the sequence is presented to the players
     * for the first time.
     * 
     * @param  amount   number of beats to start with
     */
    private void generateBeats(int amount) {
        for (int i = 0; i < amount; i++) {
            beats.add(rand.nextInt(NO_OF_BEAT_TYPES[difficulty]) + 1);
        }
    }

    /**
     * Method called by frontend everytime a new beat enters the frame.
     * It checks whether/how the previous beat was answered and, where applicable,
     * ends the current round.
     * 
     * Detailed description of the procedure is commented inbetween the code.
     * 
     * NB: It is the tick method that ALWAYS ends the round.
     */
    public void tick() {
        // Was this tick supposed to end the round?
        if (nextTickEndsRound) {
            // If so, start a new one.
            nextTickEndsRound = false;
            nextPlayer();
            return;
        }

        // Was the previous beat answered within the specified timeframe?
        if (currentBeatAnsweredFlag) {
            currentBeatAnsweredFlag = false;
            // If so, check whether there is another beat to answer.
            if (it.hasNext()) {
                // If there is another beat to answer, move the beat iterator one step forward.
                currentCorrectBeat = it.next().intValue();
            } else {
                // If not, the player successfuly answered all the beats.
                roundResult = CORRECT;
                // In that case it needs to find out whether the player is
                // supposed to add a beat (depends on player and game type).
                if (currentPlayerWillAddBeat()) {
                    // If so, allow one more tick for the player to add a beat.
                    lastBeatFlag = true;
                } else {
                    // If not, end the round straight away.
                    updateScoreAndWinner();
                    nextPlayer();
                }
            }

            // If the current player is computer, it automatically calls the 
            // method to simulate a real player.
            if ((players[currentPlayer].isComputer()) && (currentCorrectBeat != 0)) {
                cpuBeat();
            }
        } else {
            // If the previous beat was not answered at all, the player loses
            // because of timeout and if this is not the end of the game,
            // next round is announced.
            roundResult = TIMEOUT;
            playerOut();
            if (isRunning) {
                this.nextPlayer();
            }
        }
    }

    /**
     * Method called every time a user "hits a drum" (or CPU simulates this).
     * 
     * It checks whether the user has not already hit a drum in the current
     * timeframe and, where applicable, checks whether the beat was correct.
     * Based on this information, it changes the state variables so that the
     * following tick reacts accordingly.
     * 
     * Detailed description of the procedure is commented inbetween the code.
     * 
     * @param beat  Numeric representation of the drum that was hit (range 1...number of drums).
     */
    public void beat(int beat) {
        // If the player has already hit a drum in this timeframe, any additional
        // hits are not evaluated.
        if (currentBeatAnsweredFlag) {
            return;
        }

        // If this is the first hit within the timeframe, this flag ensures
        // the timeframe will be evaluated only once.
        currentBeatAnsweredFlag = true;

        // If lastBeatFlag is raised, the player is adding his own beat in this
        // timeframe and the next tick will end the round.
        // Frontend is notified to show the added beat on the screen.
        if (lastBeatFlag) {
            beats.add(beat);
            currentBeatAnsweredFlag = true;
            currentCorrectBeat = beat;
            frontLink.guiSendBeat(currentCorrectBeat - 1, true);
            updateScoreAndWinner();
            nextTickEndsRound = true;
            return;
        } else {
            // If the aforementioned flag is not raised, the player is guessing
            // the beat sequence. Therefore his beat is checked with the beat array.
            if (beat == currentCorrectBeat) {
                // If the beat is correct, it notifies the frontend.
                frontLink.guiSendBeat(currentCorrectBeat - 1, true);
                return;
            } else {
                // If the beat is incorrect, it notifies the frontend and sets 
                // the following tick to be the last of this round. 
                frontLink.guiEndRound(false);
                frontLink.guiSendBeat(currentCorrectBeat - 1, false);
                roundResult = FALSE;
                this.playerOut();
                nextTickEndsRound = true;
                return;
            }
        }
    }

    /**
     * Method called every time a round needs to be ended.
     *
     * Apart from some communication with the frontend, based on data stored in
     * players array it decides who is the next player and also updates 
     * the iterator with the current version of beat array.
     * 
     * Detailed description of the procedure is commented inbetween the code.
     */
    protected void nextPlayer() {
        // frontend: lets the gui know there is a new round
        if (roundResult != FALSE && currentPlayer != -1) {
            frontLink.guiEndRound(false);
        }
        // frontend: if the player was successful, plays a sound effect
        if (roundResult == CORRECT) {
            frontLink.playEffect(SoundEffect.CORRECT_FX);
        }

        // iterate through the array as long as the new player is still in the game
        this.currentPlayer = ((this.currentPlayer) % noPlayers) + 1;
        while (players[currentPlayer].isOut()) {
            this.currentPlayer = (this.currentPlayer % noPlayers) + 1;
        }

        // frontend: if the next player is computer lock the keyboard input
        if (this.players[currentPlayer].isComputer()) {
            frontLink.keyboardLocked(true);
        } else {
            frontLink.keyboardLocked(false);
        }

        // reset the round-specific state variables and update the iterator
        lastBeatFlag = false;
        currentBeatAnsweredFlag = true;
        currentCorrectBeat = 0;
        this.it = beats.iterator();
    }

    /**
     * This method is called everytime the class receives a tick and the current
     * player is computer. 
     * 
     * If the current timeframe requires a beat to be guessed it simply asks 
     * the frontend link to simulate the current player to hit the correct drum. 
     * 
     * If the current timeframe requires a new beat to be added it randomly selects
     * a drum and asks the frontend link to simulate the current player to hit 
     * the correct drum. 
     */
    public void cpuBeat() {
        if (lastBeatFlag) {
            currentCorrectBeat = rand.nextInt(NO_OF_BEAT_TYPES[difficulty]) + 1;
            frontLink.computerKeyPressed(currentCorrectBeat);
            nextTickEndsRound = true;
        } else {
            frontLink.computerKeyPressed(currentCorrectBeat);
        }

    }

    /**
     * This method is called everytime a human player guesses the whole 
     * sequence correctly.
     * It updates the score with the current player and number of beats contained
     * in the beat array.
     */
    protected void updateScoreAndWinner() {
        if (!(players[currentPlayer].isComputer())) {
            score.setPoints(beats.size());
            score.setWinner(players[currentPlayer]);
        }
    }

    /**
     * This method has different behaviour at different game modes.
     * 
     * @return In this round, the player is (true) / is not (false) supposed to add a beat at the end.
     */
    public abstract boolean currentPlayerWillAddBeat();

    /**
     * This method has different behaviour at different game modes, however some
     * of the functionality is shared.
     * In both game modes, the current player is marked as out of the game and
     * the frontend link is contacted to play a sound effect.
     *
     * Additional functionality is described in the extending classes.
     */
    protected void playerOut() {
        players[currentPlayer].playerOut();
        playersOut++;
        frontLink.playEffect(SoundEffect.FAIL_FX);
    }

    /**
     * This method is called as a part of playerOut method if the winner has
     * been decided. It notifies the GUI via the frontend link and sets the 
     * internal state variable (used to check whether new round is necessary) 
     * appropriately.
     */
    protected void endGame() {
        frontLink.guiEndRound(true);
        isRunning = false;
    }

    /**
     * Getter method used by frontend-backend link to find out the current size
     * of the array, so that the appropriate number of boxes is painted on the screen.
     * 
     * @return ArrayList of Integer objects representing the sequence of beats.
     */
    public ArrayList<Integer> getBeats() {
        return beats;
    }

    /**
     * Getter method used by frontend-backend link to draw the current score 
     * on the screen, to announce game results and to check if high score was
     * beaten.
     * 
     * @return Score object holding data about score of current game.
     */
    public Score getCurrentScore() {
        return score;
    }

    /**
     * Getter method used by frontend-backend link to get the player's name.
     * 
     * @return Player object holding data about the current player.
     */
    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    /**
     * Getter method used by frontend-backend link to show the player appropriate
     * message inbetween the rounds.
     * 
     * @return Integer representing the round state: -1=timeout, 0=wrong answer, 1=correct.
     */
    public int getRoundResult() {
        return roundResult;
    }

    /**
     * Getter method used by frontend-backend link to set the appropriate tempo
     * for given difficulty.
     * 
     * @return Integer representing the difficulty: 0=easy, 1=medium, 2=hard.
     */
    public int getDifficulty() {
        return this.difficulty;
    }
}
