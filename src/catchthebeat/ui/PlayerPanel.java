/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui;

import catchthebeat.ui.sprites.PlayerDrums;
import catchthebeat.ui.sprites.PlayerDrumSprite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Class PlayerPanel
 * 
 * This JPanel displays drums that are animated each time player plays a beat.
 * (including computed players)
 * 
 * 1) When object of this class is created it paints a background image
 * 2) Once a game is started it creates 4 drums that are identical sprites
 * 3) Sprites are positioned in center with the same space between them. They go
 *    off the panel display a number of pixels to make more graphical sense when animated
 * 4) When panel shows drums, it paints them to an offscreen image first and
 *    synchronizes its frames to achieve steady refresh rate and to avoid choppy animation
 *    even though it is a vertical movement only
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class PlayerPanel extends JPanel implements Runnable {
    
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 480;
    private ImageLoader iLoader;
    private BufferedImage backgroundImage;
    
    // the sprites
    private PlayerDrums playerDrums = null;
    // specify a number of drums to be displayed
    private static final int NUM_PLAYER_DRUMS = 4;
    // margin between two drums
    private static final int PLAYER_DRUM_MARGIN = 8;
    
    private Thread theRunner = null ; // run the animation
    // controls start / stop of animation (any of the drums moving)
    private volatile boolean isRunning = false ;
    // sleep interval between "frames" (animation refresh operations ~50fps)
    public static long FRAME_INTERVAL = 20L ;
    // start time of current animation cycle
    private long theCycleStartTime = 0;
    // game "off-screen" image
    private BufferedImage theOffScreenImage = null ;
    
      
    public PlayerPanel(ImageLoader imageLoader) {
        iLoader = imageLoader;
        
        // init panel
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        // we are just going to use JPanel as canvas
        setLayout(null);
        // setOpaque(true);
        // load background image
        backgroundImage = iLoader.getSprite("PlayerBgc.png");
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if (theOffScreenImage != null){
            // draw off screen image on JPanel
            g.drawImage(theOffScreenImage, 0, 0, this);
        } 
        // just paint the background and drums when the panel loads
        else {
            g.drawImage(backgroundImage, 0, 0, this);
            initDrums();
            playerDrums.drawSprites(g);
        }
    }
    
    public void drumPlayed(int drumNo){
        initDrums(); // init drums start animating
        playerDrums.get(drumNo).setIsPlaying(true);       
        if (theRunner == null || ! isRunning) {
            theRunner = new Thread(this) ;
            theRunner.start();
        }
        isRunning = true;
    }
    
    public void initDrums() {
        // construct the drums
        playerDrums = new PlayerDrums() ;
        for (int i = 0 ; i < NUM_PLAYER_DRUMS ; i++) {
            PlayerDrumSprite pds = new PlayerDrumSprite(PANEL_WIDTH, PANEL_HEIGHT, iLoader, this);
            // drum width plus left and right margin
            int drumPlacerWidth = pds.getWidth() + (PLAYER_DRUM_MARGIN * 2);
            // initial offset form left margin of the panel
            int drumOffset = (PANEL_WIDTH - (drumPlacerWidth * NUM_PLAYER_DRUMS)) / 2;
            // position each drum
            pds.setPosition(drumOffset + (drumPlacerWidth) * i, 
                            PANEL_HEIGHT - (pds.getHeight() - pds.getHeight() / 8)); //set drums (1/8 off their height) off the screen
            playerDrums.add(pds);
        }
    }

    @Override
    public void run(){
        while(isRunning){            
            theCycleStartTime = System.nanoTime() / 1000000;
            drumsUpdate();
            drumsRender() ; // re-render drums
            repaint() ; // schedule repaint request on swing event queue
            synchFramerate() ; // synch refresh rate
        }
        // end of animation thread
    }


    private void drumsUpdate(){
        // update state of drums
        int drumsPlaying = 0;
        for (int i = 0 ; i < playerDrums.size() ; i++) {
            // move each drum that is still playing (animating)
            if (playerDrums.get(i).isPlaying()){
                playerDrums.get(i).move(); //drum decides where to move
                drumsPlaying++;
            }
        }       
        playerDrums.updateSprites();
        //if no more drums are animating, stop the thread
        if (drumsPlaying == 0) {        
            isRunning = false;
        }    
    }

    private void drumsRender(){
        // create off screen image if necessary
        if (theOffScreenImage == null){
            GraphicsConfiguration gc = getGraphicsConfiguration();
            theOffScreenImage = gc.createCompatibleImage(PANEL_WIDTH, PANEL_HEIGHT);
        }
        Graphics g = theOffScreenImage.getGraphics();
        // erase previous image by painting the background
        g.drawImage(backgroundImage, 0, 0, null);
        playerDrums.drawSprites(g);
        g.dispose();
    }
    
        
    // trying to achieve steady animation
    // not very much needed for vertical movement but helps
    private void synchFramerate() {
        long cycleEndTime = theCycleStartTime + FRAME_INTERVAL;
        long difference = cycleEndTime - /* now */ System.nanoTime()/1000000 ;
        try {
            Thread.sleep(Math.max(0, difference));
        } catch(InterruptedException ie){
            ie.printStackTrace() ;
        }
    }
}
// Credits: Roman Velic