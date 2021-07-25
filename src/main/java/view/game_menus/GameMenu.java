package view.game_menus;

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

public abstract class GameMenu extends JFrame {

    protected ArrayList<ArrayList<LabelPanel>> labels;
    protected LabelPanel clicked;

    // Color for field-background
    protected final Color backgroundColor;
    // Color for field-background when clicked
    protected final Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    protected final Color markedColor;
    // Color for field-background when field is predefined
    protected final Color predefinedColor;
    // Color for field-background when field is predefined and possible conflicting to field
    protected final Color predefinedMarkedColor;
    // Color for field-borders
    protected final Color borderColor;

    protected Container pane;
    protected JLabel guiText;
    protected boolean textSet;
    protected CellLabel invalid;
    protected Set<model.AbstractPuzzle.Cell> conflicts;
    protected ArrayList<ArrayList<JPanel>> panels;

    protected static int size;

    public GameMenu(int gridSize, ActionListener buttonListener, String title) {
        super(title);

        backgroundColor = Color.white;
        clickedColor = Color.decode("#c5e1a5");
        markedColor = Color.decode("#f2ffe3");
        predefinedColor = Color.lightGray;
        predefinedMarkedColor = Color.decode("#dcedc9");
        borderColor = Color.darkGray;
        textSet = false;
        conflicts = new HashSet<>();

        size = gridSize;
        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        // Game overlay
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(gridSize, gridSize));
        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        labels = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            ArrayList<LabelPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize * gridSize; j++) {
                CellLabel field = new CellLabel(" ");
                LabelPanel labelPanel = new LabelPanel(field,i,j,gridSize);
                labelPanel.setOpaque(true);
                field.setBackground(backgroundColor);
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
        panels = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            ArrayList<JPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize; j++) {
                JPanel panel = new JPanel();
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
        for (int i = 1; i <= gridSize * gridSize; i++) {
            CustomButton button = new CustomButton(i, CustomButton.Type.NUMBER);
            buttonsPanel.add(button);
            button.addActionListener(buttonListener);
        }
        CustomButton buttonDelete = new CustomButton(CustomButton.Type.DELETE);
        buttonsPanel.add(buttonDelete);
        buttonDelete.addActionListener(buttonListener);
        setCustomButtons(buttonsPanel, buttonListener);

        // Adding game overlay and buttons to the panel
        pane.add(outerPanel, BorderLayout.CENTER);
        pane.add(buttonsPanel, BorderLayout.EAST);

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = labels.get(0).get(0);
        clicked.setBackground(clickedColor);

        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Set buttons other than the standard ones
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        CustomButton buttonSolve = new CustomButton(CustomButton.Type.SOLVE);
        buttonsPanel.add(buttonSolve);
        buttonSolve.addActionListener(buttonListener);
    }

    protected void handleClickEvent(int gridSize, LabelPanel labelPanel) {
        // Unmarking of possible conflicting cells
        int clickedRow = clicked.getRow();
        int clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(backgroundColor);
            if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedColor);
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(backgroundColor);
            if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedColor);
        }
        int rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        int rowUpperBound = rowLowerBound + gridSize - 1;
        int columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        int columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(backgroundColor);
                if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedColor);
            }
        }
        if (clicked != null) {
            if (clicked.isPredefined()) {
                clicked.setBackground(predefinedColor);
            } else {
                clicked.setBackground(backgroundColor);
            }
        }
        clicked = labelPanel;
        // Marking of possible conflicting cells
        clickedRow = clicked.getRow();
        clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(markedColor);
            if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(markedColor);
            if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
        }
        rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        rowUpperBound = rowLowerBound + gridSize - 1;
        columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(markedColor);
                if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedMarkedColor);
            }
        }
        clicked.setBackground(clickedColor);

        // Reset the value of an invalid cell
        if (invalid != null) invalid.setText("");
        if(! conflicts.isEmpty()) {
            for(model.AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(Color.black);
            }
        }
    }

    public LabelPanel getClicked() { return clicked; }

    // Set a value to a cell from backend
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setForeground(Color.black);
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
        clicked.setForeground(Color.black);
        clicked.getLabel().setForeground(Color.black);
        invalid = null;
        if(! conflicts.isEmpty()) {
            for(model.AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(Color.black);
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
        clicked.setForeground(Color.red);
        clicked.getLabel().setForeground(Color.red);
        invalid = clicked.getLabel();
    }

    // Set a invalid input by backend (tip function)
    public void invalidInput(int row, int col) {
        labels.get(row).get(col).setForeground(Color.red);
        labels.get(row).get(col).getLabel().setForeground(Color.red);
        invalid = labels.get(row).get(col).getLabel();
    }

    // Print a text to the top of the gui
    public void setGUIText(String text, Color color) {
        guiText = new JLabel(text);
        guiText.setOpaque(true);
        guiText.setBackground(backgroundColor);
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

    public abstract void changeColor();
}

// TODO (Fabian) Merge von Tastatureingaben (vsl. einige Konflikte)
// TODO (Fabian) Main-Menü: Jeweils Auswahl von Str8ts, normal & Killer
// TODO (Philipp) Implementieren von Blocken in Str8ts (Spielen + Lösen)
// TODO (Philipp+Luca) Verbinden von Frontend Str8ts mit Backend Str8ts
// TODO (Luca/(ggf. Fabian)) Backend Killer
// TODO (Philipp/(ggf. Fabian)) Frontend: Implementieren von Gruppen in Killer
// TODO (wer Zeit hat) GUIs schöner gestalten, ggf. auch von User anpassbar
// (TODO Zeitmessung mit Option zu Pausieren)
// (TODO von Untermenüs zurück in Mainmenu)
// (TODO Rückgängig-Funktion)
