package view.ingame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class InGameViewScaffold extends JFrame {

    Color backgroundColor;
    Color panelBackgroundColor;

    public InGameViewScaffold(){

        //General Window Options
        super("SUDOKUH");
        this.setSize(1195, 980);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setLayout(null);

        //JFrame Container Settings
        setLightMode();
        Container mainContainer = this.getContentPane();
        mainContainer.setBackground(backgroundColor);
        mainContainer.setLayout(null);

        //Top information panel
        mainContainer.add(new TopInfoPanel(panelBackgroundColor));

        //Sudoku field panel
        mainContainer.add(new SudokuFieldPanel());

        //Right controls panel
        mainContainer.add(new RightControlsPanel());

        //Control Buttons
        JButton againButton = new JButton();
        againButton.setBackground(backgroundColor);
        againButton.setBounds(980, 20,80,80);
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
        homeButton.setBounds(1080, 20,80,80);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("/logo_80.png"));
            homeButton.setIcon(new ImageIcon(img));
            //Sets Icon of Frame
            this.setIconImage(img);
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


