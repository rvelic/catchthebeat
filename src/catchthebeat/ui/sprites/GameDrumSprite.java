/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui.sprites;

import catchthebeat.ui.GameplayPanel;
import catchthebeat.ui.ImageLoader;

/**
 * Class GameDrumSprite
 * 
 * This extended sprite is a display of drum in a beat sequence
 * 
 * 1) Drum does not know its correct value unless played only its position in sequence
 * 2) Sprite holds all images that can be used and switches to appropriate image as a response
 *    to game events (correct play, wrong play, etc)
 * 3) Sprite is positioned based on its position in sequence and its dimensions
 * 4) There is a space of tick width between each drum for out of tick (of current drum) detection.
 *    This also gives larger time frames that are easier to play in fast tempos
 * 5) As difficulty increases tempo in which drum moves increases as well but stays
 *    same once set for the game. This tempo is a step of which drum moves in +x coordinate

 * @author Roman Velic
 * @version 2012.04
 */
public class GameDrumSprite extends Sprite {
    
//    private static final int DRUM_1 = 0;
//    private static final int DRUM_2 = 1;
//    private static final int DRUM_3 = 2;
//    private static final int DRUM_4 = 3;
//    private static final int DRUM_CORRECT = 4;
    private static final int DRUM_WRONG = 5;
    private static final int DRUM_UNKNOWN = 6;
    private static final int DRUM_NEW = 7;
    
    private GameplayPanel gamePanel;
    
    private Sprite tick;
    private int drumId;
    private boolean isNew;
    private int correctBeat;
    private boolean endsRound = false;
    private boolean wasPlayed = false;
    private boolean isCorrect = false;
    private boolean ticked = false;
    private boolean inTick = false;
    private boolean gameOver = false;
    private boolean isLast = false;
    private int tempo = 4; //speed / tempo is based on difficulty
    // images used for drum
    private static final String[] drumNames = {"GameDrum_1.png", 
                                               "GameDrum_2.png",
                                               "GameDrum_3.png",
                                               "GameDrum_4.png",
                                               "GameDrum_correct.png",
                                               "GameDrum_wrong.png",
                                               "GameDrum_unknown.png",
                                               "GameDrum_new.png"};

   
    
    public GameDrumSprite(
            int w, int h, 
            ImageLoader iLoader, 
            GameplayPanel gamePlayPanel,
            Sprite tickSprite,
            int difficulty, // speed / tempo of the drum
            int gameDrumId, // which drum in sequence
            boolean isNewDrum) { // is it new drum?
        
        super( 0, 0, w, h,iLoader, drumNames);
        tick = tickSprite;
        drumId = gameDrumId; //drum number starts with 0
        isNew = isNewDrum;
        gamePanel = gamePlayPanel;
        tempo = difficulty;
        // init drum
        initDrum();
    }
    
    private void initDrum() {
        // initialise the drum's images, position and step values (speed)
        setImages(drumNames);

        // off the screen based on which drum number it is in sequence
        // plus width of tick (so ticks don't collide)
        setPosition( (int)(0 - (getWidth() + tick.getWidth()) * (drumId+1)), 0); 
        
        //set tempo according to difficulty
        if (tempo == 0) {
            setStep(2,0); //easy
        } else if (tempo == 1){
            setStep(4,0); //medium
        } else {
            setStep(6,0); //hard
        }
        
        if (isNew) {
            setSeqNo(DRUM_NEW);
        } else {
            setSeqNo(DRUM_UNKNOWN);
        }
    } 
    
    @Override
    public void updateSprite() {
        updateTick(); // drum logic
        super.updateSprite();
    } 
    
    private void updateTick(){
        // if a drum intersects with tick the drum is in tick
        inTick = getBounds().intersects(tick.getBounds());       
        // if drum is in tick but not ticked yet it ticks
        if (inTick && !ticked) {
            gamePanel.tick(drumId);
            ticked = true;
        }        
        // if user plays the drum it will change to correct/wrong state
        if (wasPlayed) {            
            if (isCorrect){
                //setSeqNo(DRUM_CORRECT);
                setSeqNo(correctBeat); // CHANGED: Better to show its correct beat
            } else {
                setSeqNo(DRUM_WRONG);
            }
        }        
        // timeout makes the drum wrong
        if (!inTick && ticked && !wasPlayed) {
            setSeqNo(DRUM_WRONG);
            endsRound = true; // this drum will end round once it is out of tick
        }      
        // once drum not in tick display its correct beat
        if (wasPlayed && !inTick && getSeqNo() != correctBeat) {
            setSeqNo(correctBeat);
        }
        
        // last drum ticks twice (second time once it's out of tick)
        if (isLast && !inTick && ticked) {
            gamePanel.tick(drumId);
        }
        
        // drum ends round and/or game
        if (!inTick && endsRound){               
            if (gameOver) {  
                gamePanel.gameOver();
            } else {
                gamePanel.nextRound();
            }  
        }
        
    }
    
    // drums was played, but was it correct?
    public void played(boolean correct){
        wasPlayed = true;
        isCorrect = correct;
    }
    
    // correct beat for the drum
    public void setCorrectBeat(int beat) {
        correctBeat = beat;
    }
    
    // drum is in tick
    public boolean inTick(){
        return inTick;
    }
    
    public void setEndsRound(boolean ends) {
        endsRound = ends;
    }

    // any drum can end the game
    public void setEndsGame(boolean endGame) {
        gameOver = endGame;
    }
    
    // but last drum ends the game if no other drum did and no more players can play
    public void setIsLast(boolean is) {
        isLast = is;
    }
}
// Credits: Roman Velic