package view.game_menus;

import util.Themes;
import view.CellLabel;
import view.CustomButton;
import view.LabelPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static util.Type.*;

public abstract class GameMenu extends JFrame {

    /**
     * Reference to all {@link LabelPanel fields} of the sudoku game.
     */
    protected ArrayList<ArrayList<LabelPanel>> labels;

    /**
     * Reference to the actual clicked {@link LabelPanel field} of the sudoku game.
     */
    protected LabelPanel clicked;

    /**
     * Various {@link Color colors} for the game menu.
     */
    // Color for field-background
    protected Color primaryBackgroundColor;
    // Color for field-background (secondary)
    protected Color secondaryBackgroundColor;
    // Color for field-background when clicked
    protected Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    protected Color markedColor;
    // Color for field-background when field is predefined
    protected Color predefinedColor;
    // Color for field-background when field is predefined and possible conflicting to field
    protected Color predefinedMarkedColor;
    // Color for field-borders
    protected Color borderColor;
    // Color for text
    protected Color primaryTextColor;
    // Color for text (secondary)
    protected Color secondaryTextColor;
    // Color for text if there is an error
    protected Color errorTextColor;

    /**
     * The whole {@link Container menu}.
     */
    protected Container pane;
    /**
     * A text that is shown in the menu when various events are triggered.
     */
    protected JLabel guiText;
    protected boolean textSet;
    /**
     * Reference to an invalid {@link CellLabel cell} if there is one.
     */
    protected CellLabel invalid;
    /**
     * List of conflicting cells.
     */
    protected Set<model.AbstractPuzzle.Cell> conflicts;

    /**
     * The size of the grid.
     */
    protected static int size;

    public GameMenu(int gridSize, ActionListener buttonListener, String title, String theme) {
        super(title);

        Themes t = new Themes(theme);
        primaryBackgroundColor = t.getPrimaryBackgroundColor();
        secondaryBackgroundColor = t.getSecondaryBackgroundColor();
        clickedColor = t.getClickedColor();
        markedColor = t.getMarkedColor();
        predefinedColor = t.getPredefinedColor();
        predefinedMarkedColor = t.getPredefinedMarkedColor();
        primaryTextColor = t.getPrimaryTextColor();
        secondaryTextColor = t.getSecondaryTextColor();
        errorTextColor = t.getErrorTextColor();
        borderColor = t.getBorderColor();

        textSet = false;
        conflicts = new HashSet<>();

        size = gridSize;
        pane = getContentPane();
        pane.setLayout(new BorderLayout());
        pane.setBackground(primaryBackgroundColor);
        pane.setForeground(primaryTextColor);

        // Game overlay
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(gridSize, gridSize));
        outerPanel.setBackground(primaryBackgroundColor);
        outerPanel.setForeground(primaryTextColor);
        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        labels = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            ArrayList<LabelPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize * gridSize; j++) {
                CellLabel field = new CellLabel(" ");
                LabelPanel labelPanel = new LabelPanel(field,i,j,gridSize, primaryTextColor);
                labelPanel.setOpaque(true);
                labelPanel.setBackground(primaryBackgroundColor);
                labelPanel.setForeground(primaryTextColor);
                field.setBackground(primaryBackgroundColor);
                field.setForeground(primaryTextColor);
                labelPanel.setBorder(new LineBorder(borderColor, 1));
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setVerticalAlignment(SwingConstants.CENTER);
                labelPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleClickEvent(gridSize, labelPanel);
                    }
                });
                temp.add(labelPanel);
            }
            labels.add(temp);
        }

        // Initializing matrix of panels ('panels') -> Creation in advance in order to get the right coordinates
        ArrayList<ArrayList<JPanel>> panels = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            ArrayList<JPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize; j++) {
                JPanel panel = new JPanel();
                panel.setBackground(primaryBackgroundColor);
                panel.setForeground(primaryTextColor);
                panel.setLayout(new GridLayout(gridSize, gridSize));
                panel.setBorder(new LineBorder(borderColor, 2));
                temp.add(panel);
            }
            panels.add(temp);
        }

        // Filling the panels-matrix with values from the labels-matrix
        for (int row = 0; row < gridSize * gridSize; row++) {
            for (int col = 0; col < gridSize * gridSize; col++) {
                LabelPanel field = labels.get(row).get(col);
                panels.get(row / gridSize).get(col / gridSize).add(field);
            }
        }

        // Filling the outerPanel-matrix (game overlay) with values from the panels-matrix
        for (ArrayList<JPanel> panel : panels) {
            for (JPanel jPanel : panel) {
                outerPanel.add(jPanel);
            }
        }

        // Buttons for input
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(gridSize + 1, gridSize));
        buttonsPanel.setBackground(primaryBackgroundColor);
        buttonsPanel.setForeground(primaryTextColor);
        for (int i = 1; i <= gridSize * gridSize; i++) {
            CustomButton button = new CustomButton(i, NUMBER);
            button.setForeground(primaryTextColor);
            button.setBackground(primaryBackgroundColor);
            button.setOpaque(true);
            buttonsPanel.add(button);
            button.addActionListener(buttonListener);
        }
        CustomButton buttonDelete = new CustomButton(DELETE);
        buttonDelete.setForeground(primaryTextColor);
        buttonDelete.setBackground(primaryBackgroundColor);
        buttonDelete.setOpaque(true);
        buttonsPanel.add(buttonDelete);
        buttonDelete.addActionListener(buttonListener);
        setCustomButtons(buttonsPanel, buttonListener);

        // Adding game overlay and buttons to the panel
        this.addToPanel(outerPanel, BorderLayout.CENTER);
        this.addToPanel(buttonsPanel, BorderLayout.EAST);

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = labels.get(0).get(0);
        clicked.setBackground(clickedColor);

        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Adds a {@link JLabel panel} to the whole menu
     *
     * @param panel The panel that should be added to the menu
     * @param layout The position where panel should be added
     */
    protected void addToPanel(JPanel panel, String layout) {
        pane.add(panel, layout);
    }

    /**
     * Add buttons other than the standard ones to the menu
     *
     * @param buttonsPanel The panel where the buttons should be added to
     * @param buttonListener The listener the buttons should be attached to
     */
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        CustomButton buttonSolve = new CustomButton(SOLVE);
        buttonSolve.setForeground(primaryTextColor);
        buttonSolve.setBackground(primaryBackgroundColor);
        buttonSolve.setOpaque(true);
        buttonsPanel.add(buttonSolve);
        buttonSolve.addActionListener(buttonListener);
    }

    protected void handleClickEvent(int gridSize, LabelPanel labelPanel) {
        // Unmarking of possible conflicting cells
        int clickedRow = clicked.getRow();
        int clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(primaryBackgroundColor);
            labels.get(clickedRow).get(k).setForeground(primaryTextColor);
            if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedColor);
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(primaryBackgroundColor);
            labels.get(k).get(clickedCol).setForeground(primaryTextColor);
            if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedColor);
        }
        int rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        int rowUpperBound = rowLowerBound + gridSize - 1;
        int columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        int columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(primaryBackgroundColor);
                labels.get(k).get(l).setForeground(primaryTextColor);
                if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedColor);
            }
        }
        if (clicked != null) {
            if (clicked.isPredefined()) {
                clicked.setBackground(predefinedColor);
            } else {
                clicked.setBackground(primaryBackgroundColor);
            }
            clicked.setForeground(primaryTextColor);
        }
        clicked = labelPanel;
        // Marking of possible conflicting cells
        clickedRow = clicked.getRow();
        clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(markedColor);
            if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
            labels.get(clickedRow).get(k).setForeground(primaryTextColor);
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(markedColor);
            if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
            labels.get(k).get(clickedCol).setForeground(primaryTextColor);
        }
        rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        rowUpperBound = rowLowerBound + gridSize - 1;
        columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(markedColor);
                if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                labels.get(k).get(l).setForeground(primaryTextColor);
            }
        }
        clicked.setBackground(clickedColor);
        clicked.setForeground(primaryTextColor);

        // Reset the value of an invalid cell
        if (invalid != null) invalid.setText("");
        if(! conflicts.isEmpty()) {
            for(model.AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
            }
        }
    }

    public LabelPanel getClicked() { return clicked; }

    // Method for keyboard input
    public void setClicked(int row, int column){
        LabelPanel newClicked = labels.get(row).get(column);
        handleClickEvent(size,newClicked);
    }

    // Set a value to a cell from backend
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setForeground(primaryTextColor);
    }

    // Delete a value from a cell
    public void resetCell() {
        clicked.getLabel().setText("");
    }

    public void validInput(int input) {
        validInput(Integer.toString(input));
    }

    // Set a valid input by user
    public void validInput(String input) {
        clicked.setText(input);
        clicked.setForeground(primaryTextColor);
        clicked.getLabel().setForeground(primaryTextColor);
        invalid = null;
        if(! conflicts.isEmpty()) {
            for(model.AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
            }
        }
        conflicts = new HashSet<>();
    }

    public void invalidInput(int input) {
        invalidInput(Integer.toString(input));
    }

    // Set a invalid input by user (only frontend)
    public void invalidInput(String input) {
        clicked.setText(input);
        clicked.setForeground(errorTextColor);
        clicked.getLabel().setForeground(errorTextColor);
        invalid = clicked.getLabel();
    }

    // Set a invalid input by backend (tip function)
    public void invalidInput(int row, int col) {
        labels.get(row).get(col).setForeground(errorTextColor);
        labels.get(row).get(col).getLabel().setForeground(errorTextColor);
        invalid = labels.get(row).get(col).getLabel();
    }

    // Print a text to the top of the gui
    public void setGUIText(String text, Color color) {
        guiText = new JLabel(text);
        guiText.setOpaque(true);
        guiText.setBackground(primaryBackgroundColor);
        guiText.setForeground(color);
        guiText.setBorder(new LineBorder(borderColor, 1));
        guiText.setFont(new Font(getFont().getName(), Font.BOLD, 25));
        guiText.setHorizontalAlignment(SwingConstants.CENTER);
        guiText.setVerticalAlignment(SwingConstants.CENTER);

        pane.add(guiText, BorderLayout.NORTH);
        pane.revalidate();
        textSet = true;
    }

    // Removes the text from the top of the gui
    public void resetGUIText() {
        if (textSet) {
            pane.remove(guiText);
            pane.revalidate();
            textSet = false;
        }
    }

    public void highlightConflicts(model.AbstractPuzzle.Cell c) {
        labels.get(c.row()).get(c.column()).getLabel().setForeground(Color.red);
        conflicts.add(c);
    }

    public int getGridSize() {
        return size;
    }
}

// TODO (Fabian) Tastatureingaben
// TODO (Fabian) Mit Tastatureingaben: Nach Eingabe einer Zahl gleich ein Feld weiter gehen
// TODO (Fabian) Frontend: Implementieren von Gruppen in Killer
// TODO (Fabian+Luca) Verbinden von Backend+Frontend Killer
// TODO (Fabian) Theme-Wechsel in Settings unterbringen, ggf. auch weitere Themes hinzufügen
// TODO (Alle) Code aufräumen & Dokumentation
// (TODO Zeitmessung mit Option zu Pausieren)
// (TODO Tipp-Limit)
// (TODO von Untermenüs zurück in Mainmenu)
// (TODO Rückgängig-Funktion)
