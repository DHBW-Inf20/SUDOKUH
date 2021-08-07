package view.game_menus;

import model.AbstractPuzzle;
import view.LabelPanel;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SolveMenu extends GameMenu {
    protected ArrayList<view.LabelPanel> inputs;

    public SolveMenu(int size, ActionListener buttonListener, String title, String theme) {
        super(size, buttonListener, title, theme);
        inputs = new ArrayList<>();
    }

    @Override
    public void validInput(String input) {
        clicked.setText(input);
        clicked.setForeground(primaryTextColor);
        clicked.getLabel().setForeground(primaryTextColor);
        inputs.add(clicked);
        invalid = null;
    }

    // Set a value to a cell from backend
    @Override
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setForeground(primaryTextColor);
        if(inputs.contains(labels.get(row).get(col))) labels.get(row).get(col).setBackground(predefinedColor);
    }

    @Override
    // Delete a value from a cell
    public void resetCell() {
        clicked.setText("");
        inputs.remove(clicked);
    }

    @Override
    protected void handleClickEvent(int gridSize, LabelPanel labelPanel) {
        // Unmarking of possible conflicting cells
        int clickedRow = clicked.getRow();
        int clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(primaryBackgroundColor);
            if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedColor);
            labels.get(clickedRow).get(k).setForeground(primaryTextColor);
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(primaryBackgroundColor);
            if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedColor);
            labels.get(k).get(clickedCol).setForeground(primaryTextColor);
        }
        int rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        int rowUpperBound = rowLowerBound + gridSize - 1;
        int columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        int columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(primaryBackgroundColor);
                if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedColor);
                labels.get(k).get(l).setForeground(primaryTextColor);
            }
        }
        if (clicked != null) {
            if(inputs.contains(clicked)) {
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
            if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
            labels.get(clickedRow).get(k).setForeground(primaryTextColor);
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(markedColor);
            if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
            labels.get(k).get(clickedCol).setForeground(primaryTextColor);
        }
        rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        rowUpperBound = rowLowerBound + gridSize - 1;
        columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(markedColor);
                if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                labels.get(k).get(l).setForeground(primaryTextColor);
            }
        }
        clicked.setBackground(clickedColor);
        clicked.setForeground(primaryTextColor);

        // Reset the value of an invalid cell
        if (invalid != null) invalid.setText("");
        if(! conflicts.isEmpty()) {
            for(AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
            }
        }
    }
}
