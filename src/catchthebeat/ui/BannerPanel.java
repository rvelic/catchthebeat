/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catchthebeat.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Class BannerPanel
 * 
 * This is a JPanel that provides information about current player
 * 
 * This class displays:
 * 1) Name of the player currently playing.
 * 2) Game score (note that highest game score is shared by all players once
 *    they repeat last sequence successfully).
 * 3) Information are updated and the panel repainted by GameplayPanel.
 * 4) Panel uses Sprite to load and paint its background picture.
 * 5) Text anti-aliasing is turned ON by default before each repaint. 
 * 
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class BannerPanel extends JPanel {
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 30;
    public static final int PANEL_MARGIN = 15;
    private ImageLoader iLoader;
    private BufferedImage backgroundImage;
    private int score = 0;
    private String player = ""; // always the first player
        
    public BannerPanel(ImageLoader imageLoader) {
        
        // init panel
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null); // canvas only
        iLoader = imageLoader;
        // load background image
        backgroundImage = iLoader.getSprite("BannerBgc.png");
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g) ;
        // draw background
        g.drawImage(backgroundImage, 0, 0, null);
        Font playerFont = new Font("Georgia", Font.BOLD + Font.ITALIC, 14);
        FontMetrics fm = g.getFontMetrics(playerFont);
        g.setFont(playerFont);
        g.setColor(Color.black);
       
        // turn on anti-aliasing for text
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = g2.getRenderingHints();
        rh.put (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        // draw current player name
        String info = "Playing: " + player;
        g.drawString(info, PANEL_MARGIN, (PANEL_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
        // draw game score
        info = "Game Score: " + score;
        g.drawString(info, PANEL_WIDTH - fm.stringWidth(info) - PANEL_MARGIN, (PANEL_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
    }   
    
    // update display of the banner (current player and game score after each round)
    public void updateInfo(String playerName, int gameScore) {
        player = playerName;
        score = gameScore;
        repaint();
    }
}
// Credits: Roman Velic