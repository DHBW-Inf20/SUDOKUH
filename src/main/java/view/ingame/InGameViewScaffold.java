package view.ingame;

import model.AbstractPuzzle.Cell;
import model.Str8ts;
import presenter.PlayPresenter;
import util.GameMode;
import view.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;
import static util.Strings.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 * Scaffold for inGame UI and different gamemodes
 */
public final class InGameViewScaffold extends JFrame {

    /**
     * Panel for information like current playtime or warnings
     */
    private final TopInfoPanel topInfoPanel;

    /**
     * Panel for SudokuField
     */
    private final GridPanel gridPanel;

    /**
     * Panel for input buttons
     */
    private final RightControlsPanel rightControlsPanel;

    private final PlayPresenter playPresenter;


    public InGameViewScaffold(int gridSize, ActionListener buttonListener, String title, Theme theme,
                              boolean highlighting, boolean autoStepForward, GameMode gamemode) {
        this(gridSize, buttonListener, title, theme, highlighting, autoStepForward, gamemode, 0, null);
    }

    public InGameViewScaffold(int gridSize, ActionListener buttonListener, String title, Theme theme,
                              boolean highlighting, boolean autoStepForward, GameMode gamemode, int tipLimit,
                              PlayPresenter playPresenter) {
        super(SUDOKUH + "-" + title);

        // different game settings
        this.playPresenter = playPresenter;

        // General Window Options
        setResizable(false);
        setSize(1195, 980);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // JFrame Container Settings
        Container mainContainer = this.getContentPane();
        mainContainer.setBackground(theme.menuBackgroundColor);
        mainContainer.setLayout(null);

        // Top information panel
        topInfoPanel = new TopInfoPanel(theme);
        mainContainer.add(topInfoPanel);

        // Sudoku grid panel
        gridPanel = new GridPanel(gridSize, theme, highlighting, gamemode);
        mainContainer.add(gridPanel);

        // Right controls panel
        rightControlsPanel = new RightControlsPanel(gridSize, buttonListener, theme, gamemode);
        mainContainer.add(rightControlsPanel);

        // Control Buttons
        JButton againButton = new JButton();
        againButton.setBackground(theme.menuBackgroundColor);
        againButton.setBounds(980, 20, 80, 80);
        try {
            Image img = ImageIO.read(requireNonNull(getClass().getResourceAsStream("/again.png")));
            againButton.setIcon(new ImageIcon(img));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        againButton.setFocusable(false);
        againButton.setBorder(null);
        againButton.addActionListener((e) -> {
            if (playPresenter != null) {
                playPresenter.pauseTimer();
            }
            new AgainPopUpWindow(this, gamemode, gridSize, theme, autoStepForward, highlighting, tipLimit);
        });
        againButton.setToolTipText(RESET);
        mainContainer.add(againButton);

        JButton homeButton = new JButton();
        homeButton.setBackground(theme.menuBackgroundColor);
        homeButton.setBounds(1080, 20, 80, 80);
        try {
            Image img = ImageIO.read(requireNonNull(getClass().getResourceAsStream("/logo_80.png")));
            homeButton.setIcon(new ImageIcon(img));
            // Sets Icon of Frame
            setIconImage(img);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        homeButton.setFocusable(false);
        homeButton.setBorder(null);
        homeButton.addActionListener((e) -> {
            if (playPresenter != null) {
                playPresenter.pauseTimer();
            }
            new PopUpWindow(this);
        });
        homeButton.setToolTipText(GO_TO_MAIN_MENU);
        mainContainer.add(homeButton);

        setVisible(true);
    }


    /**
     * @return the currently clicked cell
     */
    public CellPanel getClicked() {
        return gridPanel.getClicked();
    }

    /**
     * Set the clicked cell to specified coordinates
     *
     * @param row    the cell-row
     * @param column the cell-column
     */
    public void setClicked(int row, int column) {
        gridPanel.setClicked(row, column);
    }

    /**
     * Sets a value to a cell of specified coordinates
     *
     * @param row   the cell-row
     * @param col   the cell-column
     * @param value the value to be set
     */
    public void setValue(int row, int col, int value) {
        gridPanel.setValue(row, col, value);
    }

    /**
     * Deletes the value of the currently clicked cell
     */
    public void resetCell() {
        gridPanel.resetCell();
    }

    /**
     * Sets an input to the clicked cell and set the text color correctly
     *
     * @param input the input value from type String
     */
    public void validInput(String input) {
        gridPanel.validInput(input);
    }

    /**
     * Sets an invalid input (color: {@code errorTextColor}) to the clicked cell
     *
     * @param input the input value from type String
     */
    public void invalidInput(String input) {
        gridPanel.invalidInput(input);
    }

    /**
     * Sets an invalid input to a specified cell when a conflict is created by using the tip function
     *
     * @param row the cell-row
     * @param col the cell-column
     */
    public void invalidInput(int row, int col) {
        gridPanel.invalidInput(row, col);
    }

    /**
     * Prints a text to the top of the gui
     *
     * @param text  text to be printed
     * @param color color in which the text should be printed
     */
    public void setInfoText(String text, Color color) {
        topInfoPanel.setInfoText(text, color);
    }

    /**
     * Prints a text to the top of the gui in the primary text color
     *
     * @param text text to be printed
     */
    public void setInfoText(String text) {
        topInfoPanel.setInfoText(text);
    }

    /**
     * If there is a text at the top of the gui this method will remove it
     */
    public void resetInfoText() {
        topInfoPanel.resetInfoText();
    }

    /**
     * Sets the color of a conflicting cell to red and adds it to the conflicts-list
     *
     * @param cell the invalid cell
     */
    public void highlightConflicts(Cell cell) {
        gridPanel.highlightConflicts(cell);
    }

    /**
     * @return the grid size
     */
    public int getGridSize() {
        return gridPanel.getGridSize();
    }

    /**
     * Marks a specific cell as predefined in order to be unchangeable and have another color
     */
    public void setPredefined(int row, int col, int value) {
        gridPanel.setPredefined(row, col, value);
    }

    /**
     * Changes the node mode
     */
    public void setNoteMode(boolean activated) {
        rightControlsPanel.setNoteMode(activated);
    }

    /**
     * Sets a valid input to cell with changing the tip-button to the remaining tips
     *
     * @param input         the input value from type integer
     * @param tipsRemaining the number how many tips are remaining
     */
    public void validInput(String input, int tipsRemaining) {
        gridPanel.validInput(input);
        setRemainingTips(tipsRemaining);
    }

    /**
     * Sets a note to the clicked cell
     */
    public void setNote(int value) {
        gridPanel.setNote(value);
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
     * Change the color of the currently clicked cell
     */
    public Str8ts.Color changeAndGetColor() {
        return gridPanel.changeAndGetColor();
    }

    /**
     * @return the playPresenter for PopUpWindow
     */
    public PlayPresenter getPlayPresenter() {
        return playPresenter;
    }

    /**
     * Sets choose mode to true
     */
    public void startChooseGroupMode() {
        rightControlsPanel.setChooseGroupMode(true);
        gridPanel.startChooseGroupMode();
    }

    /**
     * Sets choose mode to false
     *
     * @return the currently chosen group
     */
    public ArrayList<CellPanel> endChooseGroupModeAndGetGroup() {
        rightControlsPanel.setChooseGroupMode(false);
        return gridPanel.endChooseGroupModeAndGetGroup();
    }

    /**
     * Sets edit mode to true
     */
    public void startEditGroupMode(ArrayList<CellPanel> group) {
        rightControlsPanel.setEditGroupMode(true);
        gridPanel.startEditGroupMode(group);
    }

    /**
     * Sets edit mode to false
     *
     * @return the currently chosen group
     */
    public ArrayList<CellPanel> endEditGroupModeAndGetGroup() {
        rightControlsPanel.setEditGroupMode(false);
        return gridPanel.endEditGroupModeAndGetGroup();
    }

    /**
     * Adds a group of cells to a group
     *
     * @param labels an arraylist of the {@link CellPanel cells}
     * @param sum    the sum of the group
     */
    public void addGroup(ArrayList<CellPanel> labels, int sum) {
        gridPanel.addGroup(labels, sum);
    }

    /**
     * Removes a group completely
     *
     * @param cell the group of this cell will be deleted
     */
    public ArrayList<CellPanel> removeGroup(CellPanel cell) {
        return gridPanel.removeGroup(cell);
    }
}
