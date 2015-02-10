/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package catchthebeat.ui;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;


/**
 * Class ImageLoader
 * 
 * This class loads image files as buffered images and cashes them for future reuse
 * in sprites.
 * 
 * 
 * @author Roman Velic
 * @version 2012.04
 * 
 * original author: David G. Davidson
 */
public class ImageLoader {
    public static final String  IMAGES_DIR = "../images/";
    private HashMap<String,BufferedImage> sprites; // cache names
    
    public ImageLoader(){
        sprites = new HashMap<String,BufferedImage>();
    }
    
    public BufferedImage getSprite(String fileName){
        BufferedImage image = sprites.get(fileName);
        if (image == null){
            image = loadImage(fileName);
            sprites.put(fileName, image); // cache image
        }
        return image;
       
    }
    
    private BufferedImage loadImage(String fileName) {
        URL url = null;
        try { 
            url = getClass().getResource(IMAGES_DIR + fileName);
            return ImageIO.read(url); 
        } catch (Exception e) {
            System.out.println("Can't load image " +fileName +" from "+url);
            System.out.println("Error in : "+e.getClass().getName()+" "+e.getMessage());
            System.exit(0); 
            return null;
        }
    }
}
// Credits: Roman Velic