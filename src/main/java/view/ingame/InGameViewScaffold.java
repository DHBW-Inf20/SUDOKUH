package view.ingame;

import util.Themes;
import view.game_menus.GameMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InGameViewScaffold extends JFrame {

    Color backgroundColor;
    Color panelBackgroundColor;

    TopInfoPanel topInfoPanel;
    SudokuFieldPanel sudokuFieldPanel;
    RightControlsPanel rightControlsPanel;

    public InGameViewScaffold(int gridSize, ActionListener buttonListener, String title, String theme){
        //General Window Options
        super("SUDOKUH - "+title);
        this.setSize(1195, 980);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setLayout(null);

        //JFrame Container Settings
        Themes t = new Themes(theme);
        backgroundColor = t.getMenuBackgroundColor();;
        panelBackgroundColor = t.getPanelBackgroundColor();
        Container mainContainer = this.getContentPane();
        mainContainer.setBackground(backgroundColor);
        mainContainer.setLayout(null);

        //Top information panel
        topInfoPanel = new TopInfoPanel(panelBackgroundColor);
        mainContainer.add(topInfoPanel);

        //Sudoku field panel
        sudokuFieldPanel = new SudokuFieldPanel();
        mainContainer.add(sudokuFieldPanel);

        //Right controls panel
        rightControlsPanel = new RightControlsPanel();
        mainContainer.add(rightControlsPanel);

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
}


