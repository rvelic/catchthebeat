/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package catchthebeat.ui.sprites;

/**
 * Class Sprite
 * 
 * This class holds a list of buffered images that are used for animating
 * (translating previous x,y position of image to new positions)
 * 
 * We don't need to animate sprite by changing sequence number
 * but we use the sequence to change the image of sprite (the state) as needed
 * only animating its movement
 * 
 * @author Roman Velic
 * @version 2012.04
 * 
 * original author: David G. Davidson
 */
import catchthebeat.ui.ImageLoader;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Sprite 
{
    // default step sizes (how far to move in each update)
    private static final int XSTEP = 5; 
    private static final int YSTEP = 5;

    // default dimensions when there is no image
    private static final int SIZE = 12;   

    // instance variables describing sprite image(s)
    private ImageLoader theImageLoader;
    private String[] theImageNames;
    private ArrayList<BufferedImage> theImages;
    private int theWidth, theHeight;     // image dimensions

    // instance variables describing containing game panel
    private int thePanelWidth, thePanelHeight;   // panel dimensions

    // sprite state - a sprite is updated and drawn only when it is active
    private boolean theSpriteIsActive = true;      

    // instance variables describing coordinates and change of position
    // protected to allow direct access in inheriting sprite subclasses
    protected int theX, theY;                 // location of sprite
    protected int theDeltaX, theDeltaY;       // amount to move for each update
    
    // assuming that sprite is rendered as sequence (of possibly one) images
    // then we need to keep a track of the current image
    // used to render the sprite
    protected int theSeqNo ;
    // determine whether the sprite image sequence is repeating or not
    private boolean isRepeating ;
    // in order to give finer control of sprite update
    // keep timebase for each sprite which is incremented
    // on each call to updateSprite()
    // only which timebase reaches rollover value do we perform actual
    // update on sprite
    private int theTimebase;
    private int theTimebaseRollover;
    // default rollover
    public static final int ROLLOVER = 1;
    

    public Sprite(int x, int y, int pw, int ph,
            ImageLoader iloader, String[] names) { 
        theX = x; theY = y;
        thePanelWidth = pw; thePanelHeight = ph;
        theDeltaX = XSTEP; theDeltaY = YSTEP;

        theImageLoader = iloader;
        setImages(names);    // the sprite's default image is 'name'
        theSeqNo = 0 ; // reset sequence number
        isRepeating = true ; // assume repeating sequence of images
        theTimebaseRollover = ROLLOVER ;
    } 

    public void setImages(String[] names) {
        // assign the name image(s) to the sprite
        theImageNames = names;
        theImages = new ArrayList<BufferedImage>() ;
        for (int i = 0 ; i < names.length; i++){
            BufferedImage bimage = theImageLoader.getSprite(theImageNames[i]);
            if (bimage != null) {
                theImages.add(bimage);
            }
        }
        if (theImages.size() == 0){
            System.out.println("No sprite images for " + theImageNames);
            theWidth = SIZE;
            theHeight = SIZE;
        } else {
            // assume all images are same size
            // therefore take width and height from first image in sequence
            theWidth = theImages.get(0).getWidth();
            theHeight = theImages.get(0).getHeight();
        }
    } 

    public int getWidth(){    // of the sprite's image
        return theWidth;
    }

    public int getHeight() {   // of the sprite's image
        return theHeight; 
    }

    public int getPanelWidth() {   // of the enclosing panel
        return thePanelWidth;
    }

    public int getPanelHeight() {  // of the enclosing panel  
      return thePanelHeight;
    }
    
    public boolean isActive() {
        return theSpriteIsActive;
    }

    public void setActive(boolean a) {
        theSpriteIsActive = a;
        // reset sequence number
        theSeqNo = 0 ;
    }

    public void setPosition(int x, int y) {
        theX = x; theY = y;
    }

    public void translate(int xDist, int yDist) {
        theX += xDist;  theY += yDist;
    }

    public int getX() {
        return theX;
    }

    public int getY() {
        return theY;
    }

    public void setStep(int dx, int dy) {
        theDeltaX = dx; theDeltaY = dy; 
    }

    public int getDeltaX() {
        return theDeltaX;
    }

    public int getDeltaY() {
        return theDeltaY;  
    }

    public Rectangle getBounds() {
        return new Rectangle(theX, theY, theWidth, theHeight);
    }

    public boolean isRepeating(){
        return isRepeating;
    }
    
    public void setRepeating(boolean repeating){
        isRepeating = repeating ;
    }
    
    public int getTimebaseRollover(){
        return theTimebaseRollover;
    }
    
    public void setTimebaseRollover(int rollover){
        theTimebaseRollover = rollover ;
    }
    
    public void updateSprite() {
        // move the sprite
        if (isActive()) {
            theTimebase = (theTimebase + 1) % theTimebaseRollover;
            if (theTimebase == 0){
                // only perform update when sprite timebase rolls over
                //theSeqNo = (theSeqNo + 1); RV: don't animate the sequence
                if (theSeqNo < theImages.size()){
                    theX += theDeltaX;
                    theY += theDeltaY;
                }
                else if (theSeqNo >= theImages.size() && isRepeating){
                    theX += theDeltaX;
                    theY += theDeltaY;
                    //theSeqNo = 0; not repeating
                }
                else {
                    setActive(false) ;
                }
            }
        }
    } 
    // change image of sprite        
    protected void setSeqNo(int seq){
        theSeqNo = seq;
    }
    // get current image of sprite
    protected int getSeqNo(){
        return theSeqNo;
    }

    public void drawSprite(Graphics g) {
        if (isActive()) {
            if (theImages.size() == 0) {   // the sprite has no image
                g.setColor(Color.yellow);   // draw a yellow circle instead
                g.fillOval(theX, theY, SIZE, SIZE);
                g.setColor(Color.black);
            } else {
                g.drawImage(theImages.get(theSeqNo), theX, theY, null);
            }
        }
    } 
}
// Credits: Roman Velic