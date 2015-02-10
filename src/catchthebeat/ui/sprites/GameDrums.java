/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui.sprites;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Class GameDrums
 * 
 * An extended array list that makes updating of drum sprites easier. It iterates
 * of all of its drums, updates them and makes them repaint.
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class GameDrums extends ArrayList<GameDrumSprite> {

    public GameDrums() {
        super();
    }
    //update all drum sprites in the list
    public void updateSprites(){
        for (int i = 0; i < size() ; i++){
            GameDrumSprite gs = get(i) ;
            gs.updateSprite();
        }
    }
    //draw all drum sprites in the list
    public void drawSprites(Graphics g){
        for (int i = 0; i < size() ; i++){
            get(i).drawSprite(g);
        }
    }
    
}
// Credits: Roman Velic