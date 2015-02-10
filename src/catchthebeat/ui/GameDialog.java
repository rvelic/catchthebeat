/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package catchthebeat.ui;

import catchthebeat.GameCreator;
import catchthebeat.game.Game;

/**
 * Class GameDialog
 * 
 * This JDialog provides tools for users to set game difficulty, player mode and
 * number of players for each new game. Game starts after pressing the Start Game button.
 * 
 * The additional features:
 * 1) Start game from JMenu
 * 2) Quit game from JMenu
 * 3) View list of high scores
 * 4) Edit player names
 * 5) Use keyboard shortcuts to do all of the above
 * 
 * The JDialog is created by and communicates with GameCreator. It is responsibility
 * of this class to make sure valid settings are passed for the new game.
 * 
 * @author Roman Velic
 * @version 2012.04
 */
public class GameDialog extends javax.swing.JDialog {
    
    private GameCreator gameCreator;

    /** Creates new form GameDialog */
    public GameDialog(java.awt.Frame parent, boolean modal, GameCreator gmc) {
        super(parent, modal);
        initComponents();
        gameCreator = gmc;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        difficultyGroup = new javax.swing.ButtonGroup();
        gameGroup = new javax.swing.ButtonGroup();
        easyRadio = new javax.swing.JRadioButton();
        singleplayerRadio = new javax.swing.JRadioButton();
        mediumRadio = new javax.swing.JRadioButton();
        multiplayerRadio = new javax.swing.JRadioButton();
        hardRadio = new javax.swing.JRadioButton();
        quitBtn = new javax.swing.JButton();
        playersSpinner = new javax.swing.JSpinner();
        playersLabel = new javax.swing.JLabel();
        startBtn = new javax.swing.JButton();
        gameMenu = new javax.swing.JMenuBar();
        gameItems = new javax.swing.JMenu();
        startItem = new javax.swing.JMenuItem();
        scoreItem = new javax.swing.JMenuItem();
        exitItem = new javax.swing.JMenuItem();
        playersItems = new javax.swing.JMenu();
        editPlayerItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Game");
        setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.background"));
        setLocationByPlatform(true);
        setModal(true);
        setPreferredSize(new java.awt.Dimension(500, 300));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        difficultyGroup.add(easyRadio);
        easyRadio.setSelected(true);
        easyRadio.setText("Easy");
        easyRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                easyRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(easyRadio, gridBagConstraints);

        gameGroup.add(singleplayerRadio);
        singleplayerRadio.setSelected(true);
        singleplayerRadio.setText("Singleplayer");
        singleplayerRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleplayerRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(singleplayerRadio, gridBagConstraints);

        difficultyGroup.add(mediumRadio);
        mediumRadio.setText("Medium");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(mediumRadio, gridBagConstraints);

        gameGroup.add(multiplayerRadio);
        multiplayerRadio.setText("Multiplayer");
        multiplayerRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiplayerRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(multiplayerRadio, gridBagConstraints);

        difficultyGroup.add(hardRadio);
        hardRadio.setText("Hard");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(hardRadio, gridBagConstraints);

        quitBtn.setText("Quit");
        quitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(40, 0, 0, 0);
        getContentPane().add(quitBtn, gridBagConstraints);

        playersSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        playersSpinner.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        playersSpinner.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(playersSpinner, gridBagConstraints);

        playersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        playersLabel.setText("Players");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(playersLabel, gridBagConstraints);

        startBtn.setText("Start Game");
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(40, 0, 0, 0);
        getContentPane().add(startBtn, gridBagConstraints);

        gameItems.setText("Game");
        gameItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameItemsActionPerformed(evt);
            }
        });

        startItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        startItem.setText("Start");
        startItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startItemActionPerformed(evt);
            }
        });
        gameItems.add(startItem);

        scoreItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK));
        scoreItem.setText("High Score");
        scoreItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreItemActionPerformed(evt);
            }
        });
        gameItems.add(scoreItem);

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        exitItem.setText("Quit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        gameItems.add(exitItem);

        gameMenu.add(gameItems);

        playersItems.setText("Players");

        editPlayerItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        editPlayerItem.setText("Edit Names");
        editPlayerItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPlayerItemActionPerformed(evt);
            }
        });
        playersItems.add(editPlayerItem);

        gameMenu.add(playersItems);

        setJMenuBar(gameMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void easyRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_easyRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_easyRadioActionPerformed

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        System.exit(0); //quit
    }//GEN-LAST:event_exitItemActionPerformed

    private void multiplayerRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiplayerRadioActionPerformed
        if (!playersSpinner.isEnabled()) {
            playersSpinner.setValue(new Integer(2)); //default value for multipl.
            playersSpinner.setEnabled(true);
        }
    }//GEN-LAST:event_multiplayerRadioActionPerformed

    private void singleplayerRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleplayerRadioActionPerformed
        if (playersSpinner.isEnabled()) {
            playersSpinner.setValue(new Integer(1)); // default value for singlepl.
            playersSpinner.setEnabled(false);
        }
    }//GEN-LAST:event_singleplayerRadioActionPerformed

    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        startGame();
    }//GEN-LAST:event_startBtnActionPerformed

    private void gameItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameItemsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gameItemsActionPerformed

    private void startItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startItemActionPerformed
        startGame();
    }//GEN-LAST:event_startItemActionPerformed

    private void editPlayerItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPlayerItemActionPerformed
        gameCreator.editPlayers();
    }//GEN-LAST:event_editPlayerItemActionPerformed

    private void quitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitBtnActionPerformed
       System.exit(0); //quit
    }//GEN-LAST:event_quitBtnActionPerformed

    private void scoreItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreItemActionPerformed
        gameCreator.showHighScore(null);
    }//GEN-LAST:event_scoreItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup difficultyGroup;
    private javax.swing.JRadioButton easyRadio;
    private javax.swing.JMenuItem editPlayerItem;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.ButtonGroup gameGroup;
    private javax.swing.JMenu gameItems;
    private javax.swing.JMenuBar gameMenu;
    private javax.swing.JRadioButton hardRadio;
    private javax.swing.JRadioButton mediumRadio;
    private javax.swing.JRadioButton multiplayerRadio;
    private javax.swing.JMenu playersItems;
    private javax.swing.JLabel playersLabel;
    private javax.swing.JSpinner playersSpinner;
    private javax.swing.JButton quitBtn;
    private javax.swing.JMenuItem scoreItem;
    private javax.swing.JRadioButton singleplayerRadio;
    private javax.swing.JButton startBtn;
    private javax.swing.JMenuItem startItem;
    // End of variables declaration//GEN-END:variables

    private void startGame() {
        boolean singleplayer = true; //singleplayer is default
        int difficulty = Game.EASY; //easy is default
        int noPlayers = ((Integer)playersSpinner.getValue()).intValue(); // deafult for singleplayer
        
        // set player mode
        if (multiplayerRadio.isSelected()) {
            singleplayer = false;
            if (noPlayers < 2) {
                noPlayers = 2; //make sure min 2 players for multiplayer
            }
        } else {
            noPlayers = 1; //make sure max 1 player for singleplayer
        }
        
        // set difficulty
        if (mediumRadio.isSelected()) {
            difficulty = Game.MEDIUM;
        } else if (hardRadio.isSelected()) {
            difficulty = Game.HARD;
        }
        // let the game creator know it can start the game
        gameCreator.startGame(singleplayer, noPlayers, difficulty);
    }
}
// Credits: Roman Velic