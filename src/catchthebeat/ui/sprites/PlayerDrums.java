/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui.sprites;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Class PlayerDrums
 * 
 * An extended array list that makes updating of drum sprites easier. It iterates
 * of all of its drums, updates them and makes them repaint.
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class PlayerDrums extends ArrayList<PlayerDrumSprite> {

    public PlayerDrums() {
        super();
    }
    //update all drum sprites in the list
    public void updateSprites(){
        for (int i = 0; i < size() ; i++){
            PlayerDrumSprite ds = get(i) ;
            ds.updateSprite();
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