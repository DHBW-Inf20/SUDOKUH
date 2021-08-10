package view.ingame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class InGameViewScaffold extends JFrame {

    Color backgroundColor;
    Color panelBackgroundColor;

    public InGameViewScaffold(){

        //General Window Options
        super("<Sudoku>");
        this.setSize(1400, 1120);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setLayout(null);

        //JFrame Container Settings
        setLightMode();
        Container mainContainer = this.getContentPane();
        mainContainer.setBackground(backgroundColor);
        mainContainer.setLayout(null);

        //Margin to outside if necessary
        //this.getRootPane().setBorder(BorderFactory.createMatteBorder(10,10,10,10,backgroundColor));

        //TODO: Die einzelnen Panels als extra Klasse -> Übersichtlicher

        //Top information panel
        JPanel topInfoPanel = new JPanel();
        topInfoPanel.setBackground(panelBackgroundColor);
        topInfoPanel.setBounds(20,20, 900,120);
        mainContainer.add(topInfoPanel);

        //Sudoku field panel
        JPanel sudokuFieldPanel = new JPanel();
        sudokuFieldPanel.setBackground(panelBackgroundColor);
        sudokuFieldPanel.setBounds(20, 160,900,900);
        mainContainer.add(sudokuFieldPanel);

        //Right controls panel
        JPanel rightControlsPanel = new JPanel();
        rightControlsPanel.setBackground(panelBackgroundColor);
        rightControlsPanel.setBounds(940, 160,420,900);
        mainContainer.add(rightControlsPanel);

        //Control Buttons
        JButton againButton = new JButton();
        againButton.setBackground(backgroundColor);
        againButton.setBounds(1180, 40,80,80);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("/again_icon.png"));
            againButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        againButton.setFocusable(false);
        againButton.setBorder(null);
        mainContainer.add(againButton);

        JButton homeButton = new JButton();
        homeButton.setBackground(backgroundColor);
        homeButton.setBounds(1280, 40,80,80);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("/home_icon.png"));
            homeButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        homeButton.setFocusable(false);
        homeButton.setBorder(null);
        mainContainer.add(homeButton);

        this.setVisible(true);
    }

    void setDarkMode(){
         backgroundColor = Color.darkGray;
         panelBackgroundColor = Color.darkGray.brighter();
    }

    void setLightMode(){
         backgroundColor = Color.lightGray;
         panelBackgroundColor = Color.lightGray.brighter();
    }
}


