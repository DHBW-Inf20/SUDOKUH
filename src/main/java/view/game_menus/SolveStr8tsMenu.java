package view.game_menus;

import view.CustomButton;
import view.LabelPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import static util.Type.*;

public class SolveStr8tsMenu extends SolveMenu {

    private ArrayList<ArrayList<model.Str8ts.Color>> colors;

    public SolveStr8tsMenu(int size, ActionListener buttonListener, String title) {
        super(size, buttonListener, title);
        colors = new ArrayList<>();
        for (int i = 0; i < size*size; i++) {
            ArrayList<model.Str8ts.Color> temp = new ArrayList<>();
            for (int j = 0; j < size*size; j++) {
                temp.add(model.Str8ts.Color.WHITE);
            }
            colors.add(temp);
        }
    }

    @Override
    // Set buttons other than the standard ones
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        CustomButton buttonSolve = new CustomButton(SOLVE);
        buttonsPanel.add(buttonSolve);
        buttonSolve.addActionListener(buttonListener);
        CustomButton buttonChangeColor = new CustomButton(CHANGECOLOR);
        buttonsPanel.add(buttonChangeColor);
        buttonChangeColor.addActionListener(buttonListener);
    }

    // Set pre-defined colored elements
    public void changeColor() {
        int row = clicked.getRow();
        int col = clicked.getCol();
        model.Str8ts.Color color = colors.get(row).get(col);
        switch(color) {
            case WHITE -> {
                labels.get(row).get(col).setBackground(Color.black);
                labels.get(row).get(col).getLabel().setForeground(Color.white);
                colors.get(row).set(col, model.Str8ts.Color.BLACK);
            }
            case BLACK -> {
                labels.get(row).get(col).setBackground(Color.white);
                labels.get(row).get(col).getLabel().setForeground(Color.black);
                colors.get(row).set(col, model.Str8ts.Color.WHITE);
            }
        }
    }

    @Override
    protected void handleClickEvent(int gridSize, LabelPanel labelPanel) {
        // Unmarking of possible conflicting cells
        int clickedRow = clicked.getRow();
        int clickedCol = clicked.getCol();
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(clickedRow).get(k).setBackground(backgroundColor);
            if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedColor);
            if(colors.get(clickedRow).get(k) == model.Str8ts.Color.BLACK) {
                labels.get(clickedRow).get(k).setBackground(Color.BLACK);
                labels.get(clickedRow).get(k).getLabel().setForeground(Color.WHITE);
            }
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(backgroundColor);
            if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedColor);
            if(colors.get(k).get(clickedCol) == model.Str8ts.Color.BLACK) {
                labels.get(k).get(clickedCol).setBackground(Color.BLACK);
                labels.get(k).get(clickedCol).getLabel().setForeground(Color.WHITE);
            }
        }
        int rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        int rowUpperBound = rowLowerBound + gridSize - 1;
        int columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        int columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(backgroundColor);
                if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedColor);
                if(colors.get(k).get(l) == model.Str8ts.Color.BLACK) {
                    labels.get(k).get(l).setBackground(Color.BLACK);
                    labels.get(k).get(l).getLabel().setForeground(Color.WHITE);
                }
            }
        }
        if (clicked != null) {
            if (colors.get(clickedRow).get(clickedCol) == model.Str8ts.Color.BLACK) {
                clicked.setBackground(Color.BLACK);
                clicked.getLabel().setForeground(Color.WHITE);
            } else if (inputs.contains(clicked)) {
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
            if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
            if(colors.get(clickedRow).get(k) == model.Str8ts.Color.BLACK) {
                labels.get(clickedRow).get(k).setBackground(Color.BLACK);
                labels.get(clickedRow).get(k).getLabel().setForeground(Color.WHITE);
            }
        }
        for (int k = 0; k < labels.get(clickedRow).size(); k++) {
            labels.get(k).get(clickedCol).setBackground(markedColor);
            if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
            if(colors.get(k).get(clickedCol) == model.Str8ts.Color.BLACK) {
                labels.get(k).get(clickedCol).setBackground(Color.BLACK);
                labels.get(k).get(clickedCol).getLabel().setForeground(Color.WHITE);
            }
        }
        rowLowerBound = clicked.getRow() - (clicked.getRow() % gridSize);
        rowUpperBound = rowLowerBound + gridSize - 1;
        columnLowerBound = clicked.getCol() - (clicked.getCol() % gridSize);
        columnUpperBound = columnLowerBound + gridSize - 1;
        for (int k = rowLowerBound; k <= rowUpperBound; k++) {
            for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                labels.get(k).get(l).setBackground(markedColor);
                if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                if(colors.get(k).get(l) == model.Str8ts.Color.BLACK) {
                    labels.get(k).get(l).setBackground(Color.BLACK);
                    labels.get(k).get(l).getLabel().setForeground(Color.WHITE);
                }
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

    // Set a value to a cell from backend
    @Override
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        if(colors.get(row).get(col) == model.Str8ts.Color.BLACK) {
            labels.get(row).get(col).setForeground(Color.white);
        } else {
            labels.get(row).get(col).setForeground(Color.black);
            if(inputs.contains(labels.get(row).get(col))) labels.get(row).get(col).setBackground(predefinedColor);
        }
    }

    @Override
    public void validInput(String input) {
        clicked.setText(input);
        if(colors.get(clicked.getRow()).get(clicked.getCol()) == model.Str8ts.Color.BLACK) {
            clicked.setForeground(Color.white);
            clicked.getLabel().setForeground(Color.white);
        } else {
            clicked.setForeground(Color.black);
            clicked.getLabel().setForeground(Color.black);
        }
        inputs.add(clicked);
        invalid = null;
    }
}
