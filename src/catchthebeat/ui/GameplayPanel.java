/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui;

import catchthebeat.GameCreator;
import catchthebeat.ui.sprites.GameDrumSprite;
import catchthebeat.ui.sprites.GameDrums;
import catchthebeat.ui.sprites.Sprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


/**
 * Class GameplayPanel
 * 
 * This is a JPanel that incorporates core GUI functionality. It communicates
 * with front to back link and to beat drums. It provides methods for display
 * of current beat sequence
 * 
 * 1) When object of this class is created it paints a background image
 * 2) Once a game is started it creates a number of drums (beats) that move from
 *    left to right. But first result of previous play is displayed and then round
 *    counted down from 3 - 1 to give player time to prepare.
 * 3) Text anti-aliasing is turned ON before each repaint of messages
 * 4) When panel shows drums, it paints them to an offscreen image first and
 *    synchronizes its frames to achieve steady refresh rate and to avoid choppy animation
 * 5) Tick does not move but its reference is passed to each drum for detection
 *    of their boundary intersection
 * 6) If game is over panel waits for end of repainting then calls game creator.
 *    Last painted frame stays on the panel
 * 7) Drums are reinitialized before each round as the sequence is of different
 *    length (and properties)
 * 
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class GameplayPanel extends JPanel implements Runnable{
    
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 90;
    private ImageLoader iLoader;
    private BufferedImage backgroundImage;
    private FrontBackLink gameLink;
    // the sprites
    private GameDrums gameDrums; //all game drums
    private int countDown; // countdown before new round
    private int drumInTick; // which drum is currently in tick? (the last one in tick)
    
    private static final String[] tickName = {"Tick.png"};
    public static final int TICK_WIDTH = 50;
    private Sprite tickSprite = null;
    
    private Thread theRunner = null; // run the animation
    // controls start / stop of animation / round
    private volatile boolean isRunning = false;
    // sleep interval between "frames" (animation refresh operations ~50fps)
    public static long FRAME_INTERVAL = 20L;
    // start time of current animation cycle
    private long theCycleStartTime = 0;

    // game "off-screen" image
    private BufferedImage theOffScreenImage = null ;
    private BannerPanel bannerPanel; // not used but could be to display more info
    private GameCreator gameCreator;
    
    
    public GameplayPanel(ImageLoader imageLoader) {
        // init panel
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        // use JPanel as canvas only
        setLayout(null) ;
        iLoader = imageLoader;
        // load background image
        backgroundImage = iLoader.getSprite("GameplayBgc.png");
        // load tickSprite
        tickSprite = new Sprite(PANEL_WIDTH/2 - TICK_WIDTH/2, 0, PANEL_WIDTH, PANEL_HEIGHT, iLoader, tickName);
        // tick will not be moving
        tickSprite.setStep(0, 0);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (theOffScreenImage != null){
            // draw off screen image on JPanel
            g.drawImage(theOffScreenImage, 0, 0, this);   
        } 
        // just paint the background when the panel loads
        else {
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }
    
        
    public void startRound(boolean hasResult){        
        if (hasResult){
           countDown = 6; // has message from previous round
        } else {
           countDown = 5; // at the start of new game
        }       
        initDrums(); // reload drums for each round
        if (theRunner == null || ! isRunning) {
            theRunner = new Thread(this) ;
            theRunner.start();
        }
        isRunning = true;
        bannerPanel.updateInfo(gameLink.getCurrentPlayerName(), gameLink.getCurrentPoints());
    }
    
    public void nextRound() {
        startRound(true); // there is a previous state
        
    }
    
    // drum in tick will end the round/game once it is out of tick
    public void endRound(boolean endGame) {
        gameDrums.get(drumInTick).setEndsRound(true);
        gameDrums.get(drumInTick).setEndsGame(endGame);
    }
    
    public void gameOver() {
        isRunning = false;
    }
    
    // drum that ticks invokes this method
    public void tick(int drumNo) {
        if(isRunning) { // tick only when game is running
           drumInTick = drumNo;
           gameLink.tick(); // pass tick to game
        }
    }
    
    // set correct beat and whether drum was played correctly
    // this comes from FrontBackLink
    public void beat(int beat, boolean correct){
        gameDrums.get(drumInTick).setCorrectBeat(beat);
        gameDrums.get(drumInTick).played(correct);
    }
    
    // reload game drums (different for each round/player)
    public void initDrums(){
        gameDrums = new GameDrums(); // reset
        for (int i = 0 ; i < gameLink.getNoBeats(); i++){
            gameDrums.add(new GameDrumSprite(PANEL_WIDTH, PANEL_HEIGHT,
                iLoader, this, tickSprite, gameLink.getDifficulty(), i, false)) ;
        }
        // last (new) drum added if player can add one
        if (gameLink.currentPlayerWillAddBeat()) {
            gameDrums.add(new GameDrumSprite(PANEL_WIDTH, PANEL_HEIGHT,
                iLoader, this, tickSprite, gameLink.getDifficulty(), gameDrums.size(), true)) ;
        }
        gameDrums.get(gameDrums.size()-1).setIsLast(true);
    }

    // update round state
    // render round state
    // sleep
    @Override
    public void run(){       
        while(isRunning){
            theCycleStartTime = System.nanoTime() / 1000000;
            roundUpdate() ; // state of round (gameDrums)
            roundRender() ; // round rendering (what will be painted)
            repaint() ; // schedule repaint request on swing event queue
            synchFramerate() ; // try to achieve steady refresh rate
        }
        //once game is over, finish repainting then run creator
        gameCreator.restart();
    }
    
    //updates positions and display of game drums
    private void roundUpdate(){
        gameDrums.updateSprites();
    }

    private void roundRender(){
        // create off screen image if necessary
        if (theOffScreenImage == null){
            GraphicsConfiguration gc = getGraphicsConfiguration();
            theOffScreenImage = gc.createCompatibleImage(PANEL_WIDTH, PANEL_HEIGHT);
        }
        Graphics g = theOffScreenImage.getGraphics();
        // redraw background (erasing previous content)
        g.drawImage(backgroundImage, 0, 0, null);
        // turn on anti-aliasing for text
        if (countDown > 0) {
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = g2.getRenderingHints();
            rh.put (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
        }
        // new round displays results of previous round first (if any)
        if (countDown == 6){
            Font playerFont = new Font("Georgia", Font.BOLD + Font.ITALIC, 40);
            FontMetrics fm = g.getFontMetrics(playerFont);
            g.setFont(playerFont);
            String s;
            if (gameLink.getRoundResult() < 0){
                g.setColor(new Color(254,131,83));
                s = "Timeout! You lose!";
            } else if (gameLink.getRoundResult() == 0) {
                g.setColor(new Color(254,131,83));
                s = "Wrong! You lose!";
            } else {
                g.setColor(new Color(207,221,52));
                s = "That's right!";
            }
            g.drawString(s, (PANEL_WIDTH - fm.stringWidth(s))/2, (PANEL_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
            countDown--;
        }
        // round appears to start here by displaying player's name
        else if (countDown == 5){
            Font playerFont = new Font("Georgia", Font.BOLD + Font.ITALIC, 40);
            FontMetrics fm = g.getFontMetrics(playerFont);
            g.setFont(playerFont);
            g.setColor(new Color(221,199,180));
            String s = gameLink.getCurrentPlayerName();
            g.drawString(s, (PANEL_WIDTH - fm.stringWidth(s))/2, (PANEL_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
            countDown--;
        }
        // countdown for new round
        else if (countDown > 1){
            Font playerFont = new Font("Georgia", Font.BOLD + Font.ITALIC, 50);
            FontMetrics fm = g.getFontMetrics(playerFont);
            g.setFont(playerFont);
            g.setColor(new Color(207,221,52));
            String s = countDown-1 + ""; // countdown number: 3,2,1
            g.drawString(s, (PANEL_WIDTH - fm.stringWidth(s))/2, (PANEL_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
            countDown--;
        }
        // wait one more second after countdown (0)
        else if (countDown == 1){
            countDown--;
        }
        // round is running (game drums are moving)
        else {
             gameDrums.drawSprites(g);
             tickSprite.drawSprite(g);  
        }            
        g.dispose();
    }
        
    // active control over refresh rate
    private void synchFramerate() {
        try {
            // we only need need to refresh once every second during countdown
            if (countDown > 0){
                Thread.sleep(1000);
            } else {
                // cycleEndTime is calculated ideal time at which
                // we want to end current animation cycle
                long cycleEndTime = theCycleStartTime + FRAME_INTERVAL;
                long difference = cycleEndTime - /* now */ System.nanoTime()/1000000 ;
                Thread.sleep(Math.max(0, difference));
            }
        } catch(InterruptedException ie){
            ie.printStackTrace() ;
        }
    }
    
    public void setBannerPanel(BannerPanel bp) {
        bannerPanel = bp;
    }
    
    // set the front back link
    public void setFrontBackLink(FrontBackLink link) {
        gameLink = link;
    }
   
    // set the game creator
    public void setGameCreator(GameCreator creator) {
        gameCreator = creator;
    }
}
// Credits: Roman Velic