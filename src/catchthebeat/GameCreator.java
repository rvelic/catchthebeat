/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat;

import catchthebeat.ui.GameDialog;
import catchthebeat.ui.GameplayPanel;
import catchthebeat.ui.FrontBackLink;
import catchthebeat.ui.PlayersDialog;
import catchthebeat.ui.ScoreDialog;
import catchthebeat.game.Game;
import catchthebeat.game.MultiPlayerGame;
import catchthebeat.game.Player;
import catchthebeat.game.Score;
import catchthebeat.game.SinglePlayerGame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class GameCreator
 * 
 * This class creates new games, shows dialogs to set new game, edit player names
 * and high scores.

 * 1) Once game is over restart method is called and if there was a winner for
 *    this game his/her score is compared with current high scores and displayed.
 *    If there was no score class shows new game dialog instead.
 * 2) Users can change player names and validation makes sure if one of the names
 *    is left blank it is filled with default e.g. Player 7 name.
 * 3) Data from new game dialog are validated by this dialog before passed to the class
 * 4) Class uses buffered string to write new data to a file all at once so this
 *    string has to be created in advance after data are read (and edited)
 * 
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class GameCreator {
    
    private GameplayPanel gpp;
    private JFrame gui;
    private FrontBackLink frontBackLink;
    private ScoreDialog scoreDialog = null;
    private GameDialog gameDialog = null;
    private PlayersDialog playersDialog = null;
    private Game game;
    
    private String[] playerNames;
    private int playersInGame;
    private ArrayList<Score> highScores;
    
    public static final int PLAYERS_DATA = 1;
    public static final int SCORE_DATA = 2;
    private StringBuffer stringBuffer;
        
    public GameCreator(JFrame frame, GameplayPanel panel, FrontBackLink link) {      
        gpp = panel;
        gui = frame;
        frontBackLink = link;
    }
    
    public void newGame(){        
        //reset all
        game = null;
        frontBackLink.setGame(null);
        if (scoreDialog != null) scoreDialog.dispose();
        if (playersDialog != null) playersDialog.dispose();
        
        // wait for gui to finish it's job, then create GameDialog!
        Runnable doCreateGameDialog = new Runnable() {
            public void run() {
                createGameDialog();
            }
        };
        SwingUtilities.invokeLater(doCreateGameDialog);
    }
    
    public void createGameDialog() {
        gameDialog = new GameDialog(gui, true, this);
        gameDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        gameDialog.setVisible(true);
    }
    
    public void startGame(boolean singleplayer, int noPlayers, int difficulty) {
        // read player names and create a game
        playersInGame = noPlayers;
        readGameData(PLAYERS_DATA);
        if (singleplayer) {           
           game = new SinglePlayerGame(playerNames, difficulty, frontBackLink);
        } else {
           game = new MultiPlayerGame(playerNames, difficulty, frontBackLink);
        }
        
        ///set required references (connect game with GUI)
        frontBackLink.setGame(game);
        gpp.setFrontBackLink(frontBackLink);
        gpp.setGameCreator(this);
       
        gameDialog.dispose();
        gpp.startRound(false); // start playing (no previous state)
    }
    
    public void showHighScore(Score gameScore){
        // read scores in and display
        if (readGameData(SCORE_DATA)) {
            if (gameDialog != null) gameDialog.dispose();
            scoreDialog = new ScoreDialog(gui, true, this, highScores, gameScore);
            scoreDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            scoreDialog.setVisible(true);
        }
    }
    
    public void saveHighScore(Score gameScore){
        if (readGameData(SCORE_DATA)) {
            // add this score to all loaded scores
            highScores.add(gameScore); 
            // sort scores high to low, take highest 3 and make buffered string
            Collections.sort(highScores);
            stringBuffer = new StringBuffer();
            for(int i = 0; i < 3; i++){
                String winner = highScores.get(i).getWinner().getName();
                String points = highScores.get(i).getPoints() + "";
                stringBuffer.append(winner).append("\r\n"); // add winner name
                stringBuffer.append(points).append("\r\n"); // all winner points (beats)
            }
        }
        saveGameData(SCORE_DATA, stringBuffer.toString());
        showHighScore(gameScore);
    }
    
    public void restart() {
        // save score from last game and show dialog for new one
        Score score = game.getCurrentScore();
        // if no one wins (ie CPU wins) just go to new game
        if (score.getWinner() == null) {
            newGame();
        } else { // else save the score (but comparing with previous)
            saveHighScore(game.getCurrentScore()); 
        }
        
    }
    
    public void editPlayers() {
        playersInGame = 10; // max 10 players
        if (readGameData(PLAYERS_DATA)) { //read player names and display
            if (gameDialog != null) gameDialog.dispose();
            playersDialog = new PlayersDialog(gui, true, this, playerNames);
            playersDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            playersDialog.setVisible(true);
        }
        
    }
    
    public void savePlayers(String[] playerNames) {
        stringBuffer = new StringBuffer();
        for (int i = 0; i < playerNames.length; i++) {
            String name = playerNames[i];
            if (name.equals("")) { //empty strings will be converted to "Player <player number>"
                name = "Player " + (i+1);
            }
            stringBuffer.append(name).append("\r\n"); // one player name one line
        }
        saveGameData(PLAYERS_DATA, stringBuffer.toString());
        newGame();
    }
    
    // read players or high scores
    private boolean readGameData(int read) {
        playerNames = new String[playersInGame];
        highScores = new ArrayList<Score>();
        
        String fileName = "players.txt";
        if (read == SCORE_DATA) {
            fileName = "highScore.txt";
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))));
            int lineNumber = 0;
            for (String line; (line = reader.readLine()) != null;) {
                if (read == PLAYERS_DATA && lineNumber < playersInGame) { //don't need all of them
                    playerNames[lineNumber] = line;
                }
                else if (read == SCORE_DATA) {
                    //read two lines (first = winner, second = score)
                    highScores.add(new Score(new Player(line, false), Integer.parseInt(reader.readLine())));
                }
                lineNumber++;
            }
        } catch (IOException ex) { // handle errors
            System.out.println("The file " + fileName + " could not be found or opened! "+ex.getMessage());
            System.exit(0);
            return false;
        } finally {
            return true;
        }
        
    }
    
    // write players or high scores
    // data have to be already formatted as one string
    private boolean saveGameData(int write, String data) {
        String fileName = "players.txt";
        if (write == SCORE_DATA) {
            fileName = "highScore.txt";
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(data);//write the new data to file
            writer.close();//close the file
            return true;
        } catch (Exception e) {//if an exception occurs
            System.out.println("Error occured while attempting to write to file: " + e.getMessage());
            System.exit(0);
            return false;
        } finally {
            return true;
        }
    }
}
// Credits: Roman Velic