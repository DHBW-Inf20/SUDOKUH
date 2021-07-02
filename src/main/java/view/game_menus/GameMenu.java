package view.game_menus;

import view.CellLabel;
import view.CustomButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public abstract class GameMenu extends JFrame {

    private ArrayList<ArrayList<CellLabel>> labels;
    private CellLabel clicked;

    // Color for field-background
    private final Color backgroundColor;
    // Color for field-background when clicked
    private final Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    private final Color markedColor;
    // Color for field-background when field is predefined
    private final Color predefinedColor;
    // Color for field-borders
    private final Color borderColor;

    private Container pane;
    private JLabel guiText;
    private boolean textSet;
    private JLabel invalid;

    public GameMenu(int size, ActionListener buttonListener, String title) {
        super(title);

        backgroundColor = Color.white;
        clickedColor = Color.decode("#dcedc9");
        markedColor = Color.decode("#f2ffe3");
        predefinedColor = Color.lightGray;
        borderColor = Color.darkGray;
        textSet = false;

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        // Game overlay
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(size, size));
        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        labels = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            ArrayList<CellLabel> temp = new ArrayList<>();
            for (int j = 0; j < size * size; j++) {
                CellLabel field = new CellLabel(" ", i, j);
                field.setOpaque(true);
                field.setBackground(backgroundColor);
                field.setBorder(new LineBorder(borderColor, 1));
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setVerticalAlignment(SwingConstants.CENTER);
                field.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
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
                        int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                        int rowUpperBound = rowLowerBound + size - 1;
                        int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                        int columnUpperBound = columnLowerBound + size - 1;
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
                        clicked = field;
                        // Marking of possible conflicting cells
                        clickedRow = clicked.getRow();
                        clickedCol = clicked.getCol();
                        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                            labels.get(clickedRow).get(k).setBackground(markedColor);
                            if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedColor);
                        }
                        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                            labels.get(k).get(clickedCol).setBackground(markedColor);
                            if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedColor);
                        }
                        rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                        rowUpperBound = rowLowerBound + size - 1;
                        columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                        columnUpperBound = columnLowerBound + size - 1;
                        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                                labels.get(k).get(l).setBackground(markedColor);
                                if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedColor);
                            }
                        }
                        field.setBackground(clickedColor);

                        // Reset the value of an invalid cell
                        if(invalid != null) invalid.setText("");
                    }
                });
                temp.add(field);
            }
            labels.add(temp);
        }

        // Creating matrix of panels ('panels') -> Creation in advance in order to get the right coordinates
        ArrayList<ArrayList<JPanel>> panels = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<JPanel> temp = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(size, size));
                panel.setBorder(new LineBorder(borderColor, 2));
                temp.add(panel);
            }
            panels.add(temp);
        }

        // Filling the panels-matrix with values from the labels-matrix
        for (int row = 0; row < size * size; row++) {
            for (int col = 0; col < size * size; col++) {
                CellLabel field = labels.get(row).get(col);
                panels.get(row / size).get(col / size).add(field);
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
        buttonsPanel.setLayout(new GridLayout(size + 1, size));
        for (int i = 1; i <= size * size; i++) {
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

    // Definition of pre-defined elements -> cannot be changed
    public void setPredefined(int row, int col, int value) {
        // TODO: "Einlesen" aus vorgegebenem/generierten Sudoku -> Methodenaufruf aus Backend (nur für Spiel-Modus relevant)
        labels.get(row).get(col).setText(String.valueOf(value));
        labels.get(row).get(col).setPredefined(true);
        labels.get(row).get(col).setBackground(predefinedColor);
    }

    public CellLabel getClicked() { return clicked; }

    // Set a value to a cell from backend
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(String.valueOf(value));
        labels.get(row).get(col).setForeground(Color.black);
    }

    // Set a valid input by user
    public void validInput(String input) {
        clicked.setText(input);
        clicked.setForeground(Color.black);
        invalid = null;
    }

    // Set a invalid input by user (only frontend)
    public void invalidInput(String input) {
        clicked.setText(input);
        clicked.setForeground(Color.red);
        invalid = clicked;
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
        if(textSet) {
            pane.remove(guiText);
            pane.revalidate();
            textSet = false;
        }
    }
}

// TODO MVP für Main-GameMenu implementieren (Button-Klick-Events auslagern in Presenter)
// TODO Aufteilen des GameMenu in Spiel-Menü (ggf. nochmal Unterteilung in Normal, Killer & Str8ts) & Lösen-Menü -> Erben von GameMenu
// TODO Implementieren von Tipp-Funktion in Lösen
// TODO Implementieren von Blocken in Str8ts
// TODO Implementieren von Generieren, Stift-, Tipp- & Überprüfen-Funktion in Spielen
// TODO Implementieren von Gruppen in Killer