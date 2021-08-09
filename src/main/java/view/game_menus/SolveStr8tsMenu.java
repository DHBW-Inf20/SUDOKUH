package view.game_menus;

import model.AbstractPuzzle;
import model.Str8ts;
import view.CustomButton;
import view.LabelPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import static util.Type.*;

public class SolveStr8tsMenu extends SolveMenu {

    private final ArrayList<ArrayList<model.Str8ts.Color>> colors;

    public SolveStr8tsMenu(int size, ActionListener buttonListener, String title, String theme) {
        super(size, buttonListener, title, theme);
        colors = new ArrayList<>();
        for (int i = 0; i < size*size; i++) {
            ArrayList<model.Str8ts.Color> temp = new ArrayList<>();
            for (int j = 0; j < size*size; j++) {
                temp.add(model.Str8ts.Color.WHITE);
            }
            colors.add(temp);
        }
    }

    /**
     * Add buttons other than the standard ones to the menu
     *
     * @param buttonsPanel The panel where the buttons should be added to
     * @param buttonListener The listener the buttons should be attached to
     */
    @Override
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        CustomButton buttonSolve = new CustomButton(SOLVE);
        buttonSolve.setForeground(primaryTextColor);
        buttonSolve.setBackground(primaryBackgroundColor);
        buttonSolve.setOpaque(true);
        buttonsPanel.add(buttonSolve);
        buttonSolve.addActionListener(buttonListener);
        CustomButton buttonChangeColor = new CustomButton(CHANGECOLOR);
        buttonChangeColor.setForeground(primaryTextColor);
        buttonChangeColor.setBackground(primaryBackgroundColor);
        buttonChangeColor.setOpaque(true);
        buttonsPanel.add(buttonChangeColor);
        buttonChangeColor.addActionListener(buttonListener);
    }

    /**
     * Change the color of the actual clicked cell
     */
    public void changeColor() {
        int row = clicked.getRow();
        int col = clicked.getCol();
        model.Str8ts.Color color = colors.get(row).get(col);
        switch(color) {
            case WHITE -> {
                labels.get(row).get(col).setBackground(secondaryBackgroundColor);
                labels.get(row).get(col).getLabel().setForeground(secondaryTextColor);
                colors.get(row).set(col, model.Str8ts.Color.BLACK);
            }
            case BLACK -> {
                labels.get(row).get(col).setBackground(primaryBackgroundColor);
                labels.get(row).get(col).getLabel().setForeground(primaryTextColor);
                colors.get(row).set(col, model.Str8ts.Color.WHITE);
            }
        }
    }

    /**
     * Handling of clicking on a cell
     *
     * @param labelPanel Panel of cells
     */
    @Override
    protected void handleClickEvent(LabelPanel labelPanel) {
        // Unmarking of possible conflicting cells
        int clickedRow = clicked.getRow();
        int clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(primaryBackgroundColor);
            if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedColor);
            if(colors.get(clickedRow).get(k) == Str8ts.Color.BLACK) {
                labels.get(clickedRow).get(k).setBackground(secondaryBackgroundColor);
                labels.get(clickedRow).get(k).getLabel().setForeground(secondaryTextColor);
            }
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(primaryBackgroundColor);
            if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedColor);
            if(colors.get(k).get(clickedCol) == Str8ts.Color.BLACK) {
                labels.get(k).get(clickedCol).setBackground(secondaryBackgroundColor);
                labels.get(k).get(clickedCol).getLabel().setForeground(secondaryTextColor);
            }
        }
        int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
        int rowUpperBound = rowLowerBound + size - 1;
        int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
        int columnUpperBound = columnLowerBound + size - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(primaryBackgroundColor);
                if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedColor);
                if(colors.get(k).get(l) == Str8ts.Color.BLACK) {
                    labels.get(k).get(l).setBackground(secondaryBackgroundColor);
                    labels.get(k).get(l).getLabel().setForeground(secondaryTextColor);
                }
            }
        }
        if (clicked != null) {
            if (colors.get(clickedRow).get(clickedCol) == Str8ts.Color.BLACK) {
                clicked.setBackground(secondaryBackgroundColor);
                clicked.getLabel().setForeground(secondaryTextColor);
            } else if (inputs.contains(clicked)) {
                clicked.setBackground(predefinedColor);
            } else {
                clicked.setBackground(primaryBackgroundColor);
            }
        }
        clicked = labelPanel;
        // Marking of possible conflicting cells
        clickedRow = clicked.getRow();
        clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(markedColor);
            if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
            if(colors.get(clickedRow).get(k) == Str8ts.Color.BLACK) {
                labels.get(clickedRow).get(k).setBackground(secondaryBackgroundColor);
                labels.get(clickedRow).get(k).getLabel().setForeground(secondaryTextColor);
            }
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(markedColor);
            if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
            if(colors.get(k).get(clickedCol) == Str8ts.Color.BLACK) {
                labels.get(k).get(clickedCol).setBackground(secondaryBackgroundColor);
                labels.get(k).get(clickedCol).getLabel().setForeground(secondaryTextColor);
            }
        }
        rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
        rowUpperBound = rowLowerBound + size - 1;
        columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
        columnUpperBound = columnLowerBound + size - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(markedColor);
                if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                if(colors.get(k).get(l) == Str8ts.Color.BLACK) {
                    labels.get(k).get(l).setBackground(secondaryBackgroundColor);
                    labels.get(k).get(l).getLabel().setForeground(secondaryTextColor);
                }
            }
        }
        clicked.setBackground(clickedColor);

        // Reset the value of an invalid cell
        if (invalid != null) invalid.setText("");
        if(! conflicts.isEmpty()) {
            for(AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
            }
        }
    }

    // Set a value to a cell from backend
    @Override
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        if(colors.get(row).get(col) == model.Str8ts.Color.BLACK) {
            labels.get(row).get(col).setForeground(secondaryTextColor);
        } else {
            labels.get(row).get(col).setForeground(primaryTextColor);
            if(inputs.contains(labels.get(row).get(col))) labels.get(row).get(col).setBackground(predefinedColor);
        }
    }

    @Override
    public void validInput(String input) {
        clicked.setText(input);
        if(colors.get(clicked.getRow()).get(clicked.getCol()) == model.Str8ts.Color.BLACK) {
            clicked.setForeground(secondaryTextColor);
            clicked.getLabel().setForeground(secondaryTextColor);
        } else {
            clicked.setForeground(primaryTextColor);
            clicked.getLabel().setForeground(primaryTextColor);
        }
        inputs.add(clicked);
        invalid = null;
    }
}
