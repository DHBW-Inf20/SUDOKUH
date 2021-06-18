package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUI extends JFrame {

    private ArrayList<ArrayList<CellLabel>> labels;
    private CellLabel clicked;

    // Color for field-background
    private final Color backgroundColor;
    // Color for field-background when clicked
    private final Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    private final Color markedColor; // TODO implementation of marking
    // Color for field-background when field is predefined
    private final Color predefinedColor;
    // Color for field-borders
    private final Color borderColor;

    public GUI(int size, ActionListener buttonListener) {
        super("Sudoku");

        backgroundColor = Color.white;
        clickedColor = Color.decode("#dcedc9");
        markedColor = Color.decode("#f2ffe3");
        predefinedColor = Color.lightGray;
        borderColor = Color.darkGray;

        Container pane = getContentPane();
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
                        if (clicked != null) {
                            if (clicked.isPredefined()) {
                                clicked.setBackground(predefinedColor);
                            } else {
                                clicked.setBackground(backgroundColor);
                            }
                        }
                        clicked = field;
                        field.setBackground(clickedColor);
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

        // TODO Eigene Buttons mit value (int) zum Abfragen im Presenter (statt Stringvergleich)
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
        CustomButton buttonSolve = new CustomButton(CustomButton.Type.SOLVE);
        buttonsPanel.add(buttonSolve);
        buttonSolve.addActionListener(buttonListener);

        // Adding game overlay and buttons to the panel
        pane.add(outerPanel, BorderLayout.CENTER);
        pane.add(buttonsPanel, BorderLayout.EAST);

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = labels.get(0).get(0);
        clicked.setBackground(clickedColor);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Definition of pre-defined elements -> cannot be changed
    public void setPredefined(int row, int col, int value) {
        // TODO: "Einlesen" aus vorgegebenem/generierten Sudoku -> Methodenaufruf aus Backend
        labels.get(row).get(col).setText(value + "");
        labels.get(row).get(col).setPredefined(true);
        labels.get(row).get(col).setBackground(predefinedColor);
    }

    public CellLabel getClicked() { return clicked; }

    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(String.valueOf(value));
    }
}

// TODO MVC implementieren (Klassen umbennenen, Button-Klick-Events auslagern in Controller, GUI aktualisieren von Model)
