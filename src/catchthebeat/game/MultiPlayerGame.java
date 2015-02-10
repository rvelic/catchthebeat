package catchthebeat.game;

import catchthebeat.ui.FrontBackLink;

/**
 * Class MultiPlayerGame
 * 
 * There are 2 game modes that incorporate slightly different rules. This class 
 * inherits from Game class and implements rules specific for Multiplayer
 * mode. 
 * 
 * The class fully implements an abstract method currentPlayerWillAddBeat
 * and completes partially implemented method playerOut. The constructor covers
 * the initial player creation.
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public class MultiPlayerGame extends Game {

    /**
     * Constructor first creates the "zeroth" player - Beatstarter, who is 
     * actually a classic computer player, but it is called only at the beginning
     * of the game (hence currentPlayer variable set to -1 and all the actual players
     * numbered from 1).
     * 
     * Then the constructor iterates through the given array of Strings containing
     * players' names and creates respective Player objects.
     * 
     * At the end, the nextPlayer is called, currentPlayer incremented to 0 and
     * therefore the Beatstarted opens the game.
     * 
     * @param  playerNames   Array of Strings containing players' names.
     * @param  difficulty    Integer representing the difficulty: 0=easy, 1=medium, 2=hard.
     * @param  frontLink    FrontBackLink object serves to connect frontend and backend of the game.
     */
    public MultiPlayerGame(String[] playerNames, int difficulty, FrontBackLink frontLink) {
        super(playerNames, difficulty, frontLink);
        this.noPlayers = playerNames.length;
        this.players = new Player[noPlayers + 1];
        this.players[0] = new Player("Beatstarter", true);
        for (int i = 1; i <= noPlayers; i++) {
            this.players[i] = new Player(playerNames[i - 1], false);
        }
        super.nextPlayer();
    }

    /**
     * After marking the current player out by the super class, the class
     * needs to decide whether another round is necessary to decide the winner.
     * 
     * In Multiplayer, the last human player in the game is the winner. If no 
     * player has answered the sequence correctly, the winner is the last player
     * with score 0.
     */
    @Override
    protected void playerOut() {
        super.playerOut();
        if (playersOut == noPlayers - 1) {
            if (score.getWinner() == null) {
                score.setWinner(players[noPlayers]);
            }
            super.endGame();
        }
    }

    /**
     * This method has different behaviour depending on the game mode.
     * In case of Multiplayer every player (CPU at the beginning and humans) 
     * add a beat at the end of each round.
     * 
     * @return In this round, the player is (true) / is not (false) supposed to add a beat at the end.
     */
    public boolean currentPlayerWillAddBeat() {
        return true;
    }
}
