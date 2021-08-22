package view.ingame;

import util.Themes;
import view.CustomButton;
import view.LabelPanel;
import view.PopUpWindow;
import presenter.PlayPresenter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class InGameViewScaffold extends JFrame implements ActionListener{

    Color backgroundColor;
    Color panelBackgroundColor;

    String theme;

    TopInfoPanel topInfoPanel;
    SudokuFieldPanel sudokuFieldPanel;
    RightControlsPanel rightControlsPanel;

    JButton againButton = new JButton();
    JButton homeButton = new JButton();

    private PlayPresenter playPresenter;

    public InGameViewScaffold(int gridSize, ActionListener buttonListener, String title, String theme, boolean highlighting, util.Mode gamemode){
        //General Window Options
        super("SUDOKUH - "+title);
        this.setResizable(false);
        this.setSize(1195, 980);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        //JFrame Container Settings
        Themes t = new Themes(theme);
        backgroundColor = t.getMenuBackgroundColor();
        panelBackgroundColor = t.getPanelBackgroundColor();
        Container mainContainer = this.getContentPane();
        mainContainer.setBackground(backgroundColor);
        mainContainer.setLayout(null);

        //Top information panel
        topInfoPanel = new TopInfoPanel(theme, gamemode);
        mainContainer.add(topInfoPanel);

        //Sudoku field panel
        sudokuFieldPanel = new SudokuFieldPanel(gridSize, theme, highlighting, gamemode);
        mainContainer.add(sudokuFieldPanel);

        //Right controls panel
        rightControlsPanel = new RightControlsPanel(gridSize, buttonListener, theme, gamemode);
        mainContainer.add(rightControlsPanel);

        //Control Buttons
        againButton.setBackground(backgroundColor);
        againButton.setBounds(980, 20,80,80);
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/again_icon.png")));
            againButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        againButton.setFocusable(false);
        againButton.setBorder(null);
        againButton.addActionListener(this);
        mainContainer.add(againButton);

        homeButton.setBackground(backgroundColor);
        homeButton.setBounds(1080, 20,80,80);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("/logo_80.png"));
            homeButton.setIcon(new ImageIcon(img));
            //Sets Icon of Frame
            this.setIconImage(img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        homeButton.setFocusable(false);
        homeButton.setBorder(null);
        homeButton.addActionListener(this);
        mainContainer.add(homeButton);

        this.setVisible(true);

        this.theme = theme;
    }

    public InGameViewScaffold(int gridSize, ActionListener buttonListener, String title, String theme,boolean highlighting, util.Mode gamemode, PlayPresenter playPresenter){
        this(gridSize, buttonListener, title, theme, highlighting,  gamemode);
        this.playPresenter = playPresenter;
    }


    /**
     * @return the actual clicked cell
     */
    public LabelPanel getClicked() { return sudokuFieldPanel.getClicked(); }

    /**
     * Set the clicked cell to specified coordinates
     *
     * @param row the cell-row
     * @param column the cell-column
     */
    public void setClicked(int row, int column){
        sudokuFieldPanel.setClicked(row, column);
    }

    /**
     * Sets a value to a cell of specified coordinates
     *
     * @param row the cell-row
     * @param col the cell-column
     * @param value the value to be set
     */
    public void setValue(int row, int col, int value) {
        sudokuFieldPanel.setValue(row, col, value);
    }

    /**
     * Deletes the value of the actual clicked cell
     */
    public void resetCell() {
        sudokuFieldPanel.resetCell();
    }

    /**
     * Sets an input to the clicked cell and set the text color correctly
     *
     * @param input the input value from type String
     */
    public void validInput(String input) {
        sudokuFieldPanel.validInput(input);
    }

    /**
     * Sets an invalid input (color: {@code errorTextColor}) to the clicked cell
     *
     * @param input the input value from type String
     */
    public void invalidInput(String input) {
        sudokuFieldPanel.invalidInput(input);
    }

    /**
     * Sets an invalid input to a specified cell when a conflict is created by using the tip function
     *
     * @param row the cell-row
     * @param col the cell-column
     */
    public void invalidInput(int row, int col) {
        sudokuFieldPanel.invalidInput(row, col);
    }

    /**
     * Prints a text to the top of the gui
     *
     * @param text text to be printed
     * @param color color in which the text should be printed
     */
    public void setGUIText(String text, Color color) {
        topInfoPanel.setGUIText(text, color);
    }

    /**
     * Prints a text to the top of the gui in the primary text color
     *
     * @param text text to be printed
     */
    public void setGUIText(String text) {
        topInfoPanel.setGUIText(text);
    }

    /**
     * If there is a text at the top of the gui this method will remove it
     */
    public void resetGUIText() {
        topInfoPanel.resetGUIText();
    }

    /**
     * Sets the color of a conflicting cell to red and adds it to the conflicts-list
     *
     * @param c the invalid cell
     */
    public void highlightConflicts(model.AbstractPuzzle.Cell c) {
        sudokuFieldPanel.highlightConflicts(c);
    }

    /**
     * @return the grid size
     */
    public int getGridSize() {
        return sudokuFieldPanel.getGridSize();
    }

    /**
     * Marks a specific cell as predifined in order to cannot be changed and has another color
     */
    public void setPredefined(int row, int col, int value) {
        sudokuFieldPanel.setPredefined(row, col, value);
    }

    /**
     * Changes the node mode
     */
    public void setNoteMode() {
        rightControlsPanel.setNoteMode();
    }

    /**
     * Changes the node mode
     */
    public void setNormalMode() {
        rightControlsPanel.setNormalMode();
    }

    /**
     * Sets a valid input to cell with changing the tip-button to the remaining tips
     *
     * @param input the input value from type integer
     * @param tipsRemaining the number how many tips are remaining
     */
    public void validInput(String input, int tipsRemaining) {
        sudokuFieldPanel.validInput(input);
        rightControlsPanel.setTipButtonText("Tipp anzeigen ("+ tipsRemaining +")");
    }

    /**
     * Sets a note to the clicked cell
     */
    public void setNote(int value) {
        sudokuFieldPanel.setNote(value);
    }

    /**
     * Changing the tip-button to the remaining tips
     *
     * @param tipsRemaining the number how many tips are remaining
     */
    public void setRemainingTips(int tipsRemaining) {
        rightControlsPanel.setRemainingTips(tipsRemaining);
    }

    /**
     * Changing the color of the tip-button when there are no more remaining tips
     */
    public void reachedMaxTips() {
        rightControlsPanel.reachedMaxTips();
    }

    /**
     * Change the color of the actual clicked cell
     */
    public void changeColor() {
        sudokuFieldPanel.changeColor();
    }

    /**
     * @return the playPresenter for PopUpWindow
     */
    public PlayPresenter getPlayPresenter(){
        return playPresenter;
    }

    /**
     * Handles control button events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == homeButton){
            if(playPresenter!=null){
                playPresenter.pauseTimer();
            }
            new PopUpWindow(this);
        }
    }
}


// TODO Killer Frontend
// TODO Warnings entfernen