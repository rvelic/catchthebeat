package catchthebeat.ui;

import catchthebeat.game.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class FrontBackLink
 * 
 * This class serves as a middleman between the Frontend (keyboard listener,
 * sound effects player, graphical user interface, threading) and the
 * Backend (game logic and state) part of the application as these need to
 * communicate with each other on many occasions.
 * 
 * It also connects the Frontend components together to provide a coherent UI experience
 * (e.g. when player hits a key, the appropriate drum is animated, sound is played
 * and the beat sequence panel updated - note all of these actions are part of
 * Frontend). 
 * 
 * The connected elements are: 
 * game logic and state (Game), 
 * panel with animated drums (PlayerPanel),
 * panel with beat sequence (GamePlayPanel),
 * sound effects player (SoundEffect),
 * and the class itself handles actions triggered by keypress (implements KeyListener, which is added to GUI).
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public class FrontBackLink implements KeyListener {

    private Game game;
    private PlayerPanel pp;
    private GameplayPanel gpp;
    private SoundEffect sound;
    private boolean locked;
    private static final int DRUM_1 = 49;
    private static final int DRUM_2 = 50;
    private static final int DRUM_3 = 51;
    private static final int DRUM_4 = 52;

    /**
     * Constructor is only responsible for creating SoundEffect object,
     * other frontend elements are created externally and are set later
     * using setter methods.
     */
    public FrontBackLink() {
        sound = new SoundEffect();
        locked = true;
    }

    /**
     * Using this method GameCreator class registers the current game in this
     * class.
     * 
     * @param game Current game state/logic object.
     */
    public void setGame(Game game) {
        this.game = game;
    }
    
    /*************************************************************************
     * METHODS IN THIS SECTION ARE SETTERS THAT REGISTER THE GAME COMPONENTS *
     * IN THIS CLASS.                                                        *
     * The methods are self-explanatory.                                     *
     *************************************************************************/
    public void setPlayerPanel(PlayerPanel pp) {
        this.pp = pp;
    }

    public void setGameplayPanel(GameplayPanel gpp) {
        this.gpp = gpp;
    }

    /**
     * This method is called by this class when a valid key is pressed and
     * broadcasts this information to the backend, animated drums panel and
     * to the sound player.
     * 
     * @param beat Numerical representation of the drum hit by the player.
     */
    private void passBeat(int beat) {
        game.beat(beat);
        pp.drumPlayed(beat - 1);
        sound.play(beat - 1);
    }

    /**
     * In this section, all the necessary methods of KeyListener interface
     * are implemented. Key codes for different drums are hardcoded in this
     * class and use alphanumeric keys 1, 2, 3, 4. 
     * This could be elaborated in future versions.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (locked) {
            return;
        }
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case DRUM_1:
                passBeat(1);
                break;
            case DRUM_2:
                passBeat(2);
                break;
            case DRUM_3:
                passBeat(3);
                break;
            case DRUM_4:
                passBeat(4);
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * This method is called by the Backend when a keypress needs to be
     * simulated (the current player is CPU). Going through the FrontBackLink.passBeat
     * method it ensures the effects will be exactly the same as if a human player
     * physically pressed the key.
     * 
     * @param beat Numerical representation of the drum hit by the player.
     */
    public void computerKeyPressed(int beat) {
        passBeat(beat);
    }

    /**
     * This method is called by the Backend anytime the current player is CPU.
     * Then, all the physical keypresses are ignored.
     * 
     * @param locked True = lock / False = unlock the keyboard.
     */
    public void keyboardLocked(boolean locked) {
        this.locked = locked;
    }

    
    /************************************************************************
     * METHODS IN THIS SECTION ARE CALLED BY GAME BACKEND AND THE CALLS ARE *
     * THEN FORWARDED TO THE GUI TO PERFORM RESPECTIVE ACTIONS.             *
     ************************************************************************/
    
    /**
     * This method results in showing a message inbetween the rounds
     * ("Correct!", "Timeout - you lose!", "False - you lose!").
     * 
     * @param endGame If true, it also notifies the game is over.
     */
    public void guiEndRound(boolean endGame) {
        this.gpp.endRound(endGame);
    }

    /**
     * This method results in notifying the GUI a keypress has been processed
     * by Backend and therefore the beat sequence panel needs to be updated.
     * 
     * @param beat Numerical representation of the drum hit by the player.
     * @param correct Was it a correct answer
     */
    public void guiSendBeat(int beat, boolean correct) {
        this.gpp.beat(beat, correct);
    }

    /**
     * This method is used by Backend to play sound effects connected to 
     * a state change (namely when a player loses and when a player wins the round).
     * NB: Effects connected to a keypress are controlled solely by implemented KeyListener.
     */
    public void playEffect(int effect) {
        sound.play(effect);
    }

    /*************************************************************************
     * METHODS IN THIS SECTION ARE CALLED BY GAME FRONTEND AND THE CALLS ARE *
     * THEN FORWARDED TO THE BACKEND TO RETURN DESIRED DATA.                 *
     * The methods are self-explanatory.                                     *
     *************************************************************************/
    public String getCurrentPlayerName() {
        return game.getCurrentPlayer().getName();
    }

    public int getCurrentPoints() {
        return game.getCurrentScore().getPoints();
    }

    public int getRoundResult() {
        return game.getRoundResult();
    }

    public int getDifficulty() {
        return game.getDifficulty();
    }

    public boolean currentPlayerWillAddBeat() {
        return game.currentPlayerWillAddBeat();
    }

    public int getNoBeats() {
        return game.getBeats().size();
    }

    /**
     * This method is the only non-getter method from the block. It is the only
     * element of the game determined by the Frontend. It announces the tick, 
     * i.e. the fact that the timeframe has moved to the next beat.
     */
    public void tick() {
        game.tick();
    }
}
