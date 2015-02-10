package catchthebeat.game;

import catchthebeat.ui.FrontBackLink;

/**
 * Class SinglePlayerGame
 * 
 * There are 2 game modes that incorporate slightly different rules. This class 
 * inherits from Game class and implements rules specific for Singleplayer
 * mode. 
 * 
 * The class fully implements an abstract method currentPlayerWillAddBeat
 * and completes partially implemented method playerOut. The constructor covers
 * the initial player creation.
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public class SinglePlayerGame extends Game {

    /**
     * Constructor first creates the "zeroth" player - Beatstarter, who is 
     * actually a classic computer player, but it is called only at the beginning
     * of the game (hence currentPlayer variable set to -1 and all the actual players
     * numbered from 1).
     * 
     * Then the constructor creates the only human player and takes his name out
     * of the array of Strings (containing a single object).
     * 
     * The last player to be created is the CPU enemy that will repeat the sequence
     * and add a beat at the end.
     * 
     * At the end, the nextPlayer is called, currentPlayer incremented to 0 and
     * therefore the Beatstarted opens the game.
     * 
     * @param  playerNames   Array of Strings containing players' names.
     * @param  difficulty    Integer representing the difficulty: 0=easy, 1=medium, 2=hard.
     * @param  frontLink    FrontBackLink object serves to connect frontend and backend of the game.
     */
    public SinglePlayerGame(String[] playerNames, int difficulty, FrontBackLink frontLink) {
        super(playerNames, difficulty, frontLink);
        this.noPlayers = 2;
        this.players = new Player[this.noPlayers + 1];
        this.players[0] = new Player("Beatstarter", true);
        this.players[1] = new Player(playerNames[0], false);
        this.players[2] = new Player("CPU", true);
        super.nextPlayer();
    }

    /**
     * After marking the current player out by the super class, the class
     * needs to decide whether another round is necessary to decide the winner.
     * 
     * In Singleplayer mode, the only player that can be out is the only human
     * player, therefore no additional checks are needed and the game is 
     * ended straight away. In case the player does not guess the sequence
     * correctly at the first try, the game does not have any winner.
     */
    @Override
    protected void playerOut() {
        super.playerOut();
        endGame();
    }

    /**
     * This method has different behaviour depending on the game mode.
     * In case of Singleplayer mode only the CPU players (Beatstarter and the 
     * actual CPU "Repeater" add a beat at the end of each round.
     * 
     * @return In this round, the player is (true) / is not (false) supposed to add a beat at the end.
     */
    @Override
    public boolean currentPlayerWillAddBeat() {
        if (players[currentPlayer].isComputer()) {
            return true;
        } else {
            return false;
        }
    }
}
