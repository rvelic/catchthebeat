/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui.sprites;

import catchthebeat.ui.ImageLoader;
import catchthebeat.ui.PlayerPanel;

/**
 * Class PlayerDrumSprite
 * 
 * This extended sprite is a display of each player drum.
 * 
 * 1) Drum starts animating once user plays a beat
 * 2) Drum receives move command and moves a step down (max 5 moves) then a step up
 *    (max 5 moves) to its initial position. Then sets itself to not playing so
 *    player panel can detect if repainting is needed. (if any of the drum is still animating)

 * @author Roman Velic
 * @version 2012.04
 */
public class PlayerDrumSprite extends Sprite {
    
    // the drum's y- step values are STEP
    private static final int STEP = 3;
    private static final int MOVES = 5;
    // image used for the drum
    private static final String[] spriteNames = {"PlayerDrum.png"};
    // animate drum beat
    private boolean isPlaying = false;
    private int move = 0; // distance of drum from its init position
    
    public PlayerDrumSprite(
            int w, int h,
            ImageLoader il,
            PlayerPanel pp) {
        // the ship is notionally positioned in the middle
        // at the top left of panel but this will be changed
        // immediately by call to initPosition()
        super( 0, 0, w, h, il, spriteNames);
        setImages(spriteNames);
        
        setStep(0,0);
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
    
    public void setIsPlaying(boolean b){
        isPlaying = b;
    }
    
    // animation logic of drum
    public void move(){
        if (isPlaying)
        {   //go down, when you reach bottom (MOVES) go up
             if (move >= MOVES){
                moveUp();
             } else {
                moveDown();
             }
             move++;
        }
        if (move > MOVES * 2){ // drum returned to its init position
            isPlaying = false;
            setStep(0,0);
            move = 0;   
        }
    }
    
    public void moveUp(){
        setStep(0,-STEP);
    }

    public void moveDown(){
        setStep(0,STEP);       
    }
    
}
// Credits: Roman Velic