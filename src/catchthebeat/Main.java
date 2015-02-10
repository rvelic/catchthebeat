package catchthebeat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
import catchthebeat.ui.PlayerPanel;
import catchthebeat.ui.ImageLoader;
import catchthebeat.ui.FrontBackLink;
import catchthebeat.ui.BannerPanel;
import catchthebeat.ui.GameplayPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class Main
 * 
 * This class constructs necessary application components, connects them and
 * passes the control to game creator

 * 1) All these components are reusable and are not created again until the 
 *    application is restarted
 * 2) After above is finished the class notifies game creator it can show
 *    new game dialog
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class Main {

    public static void main(String args[]) {
        Runnable doCreateAndShowGUI = new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }
    
    private static void createAndShowGUI() { 
            // create handling components
            ImageLoader iLoader = new ImageLoader();
            FrontBackLink frontBackLink = new FrontBackLink();
                        
            // create GUI components
            BannerPanel bannerPanel = new BannerPanel(iLoader);
            GameplayPanel gameplayPanel = new GameplayPanel(iLoader);
            gameplayPanel.setBannerPanel(bannerPanel);
            PlayerPanel playerPanel = new PlayerPanel(iLoader);
            
            // set frontend-backend link           
            frontBackLink.setPlayerPanel(playerPanel);
            frontBackLink.setGameplayPanel(gameplayPanel);
            
            // create GUI
            JFrame gui = new JFrame("Catch The Beat");
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Container c = gui.getContentPane() ;
            c.add(bannerPanel, BorderLayout.NORTH);
            c.add(gameplayPanel, BorderLayout.CENTER);
            c.add(playerPanel, BorderLayout.SOUTH); 
            gui.setLocationByPlatform(true);
            // this is required in orded to capture user interaction
            gui.setFocusable(true);
            gui.requestFocusInWindow();
            // front to back link is a key listener
            gui.addKeyListener(frontBackLink);
            gui.setPreferredSize(new Dimension(800,628));
            gui.pack();
            gui.setVisible(true);
            gui.setResizable(false);
            
            // create game creator
            GameCreator gameCreator = new GameCreator(gui, gameplayPanel, frontBackLink);
            gameCreator.newGame();              
    }
}
// Credits: Roman Velic