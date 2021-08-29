package view.ingame;

import util.GameMode;
import view.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static util.GameMode.KILLER_SOLVE;
import static util.GameMode.STR8TS_SOLVE;
import static view.ingame.CustomButton.Type.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public final class RightControlsPanel extends JPanel {

    private final Theme theme;

    private final CustomButton tipButton;
    private final CustomButton noteButton;
    private final CustomButton chooseGroupButton;
    private final CustomButton editGroupButton;

    public RightControlsPanel(int gridSize, ActionListener buttonListener, Theme theme, GameMode gamemode) {
        this.theme = theme;

        setBounds(840, 120, 320, 600);
        setBackground(theme.menuBackgroundColor);

        // Layout for buttons
        GridLayout layout = new GridLayout(gridSize + 1, gridSize);
        layout.setHgap(12);
        layout.setVgap(12);
        setLayout(layout);

        // Buttons for input
        for (int i = 1; i <= gridSize * gridSize; i++) {
            createAndAddNumberButton(i, theme, buttonListener);
        }

        createAndAddCustomButton(DELETE, theme, buttonListener);

        if (gamemode == KILLER_SOLVE) {
            chooseGroupButton = createAndAddCustomButton(CHOOSE_GROUP, theme, buttonListener);
            createAndAddCustomButton(REMOVE_GROUP, theme, buttonListener);
            editGroupButton = createAndAddCustomButton(EDIT_GROUP, theme, buttonListener);
        } else {
            chooseGroupButton = editGroupButton = null;
        }

        if (gamemode == STR8TS_SOLVE) {
            createAndAddCustomButton(CHANGE_COLOR, theme, buttonListener);
        }

        switch (gamemode) {
            case SUDOKU_PLAY -> {
                tipButton = createAndAddCustomButton(TIP, theme, buttonListener);
                noteButton = createAndAddCustomButton(NOTE, theme, buttonListener);
                createAndAddCustomButton(VERIFY, theme, buttonListener);
            }
            case SUDOKU_SOLVE, STR8TS_SOLVE, KILLER_SOLVE -> {
                tipButton = noteButton = null;
                createAndAddCustomButton(SOLVE, theme, buttonListener);
            }
            default -> throw new IllegalStateException("Unknown enum constant GameMode." + gamemode.name());
        }
    }

    private void createAndAddNumberButton(int value, Theme theme, ActionListener buttonListener) {
        createAndAddCustomButton(NUMBER, value, theme, buttonListener);
    }

    private CustomButton createAndAddCustomButton(CustomButton.Type type, Theme theme, ActionListener buttonListener) {
        return createAndAddCustomButton(type, -1, theme, buttonListener);
    }

    private CustomButton createAndAddCustomButton(CustomButton.Type type, int value, Theme theme,
                                                  ActionListener buttonListener) {
        CustomButton button = new CustomButton(type, value);
        button.setForeground(theme.primaryTextColor);
        button.setBackground(theme.normalCellColor);
        button.setOpaque(true);
        button.addActionListener(buttonListener);
        add(button);
        return button;
    }


    /**
     * Sets the color of the note button depending on {@code activated}.
     */
    public void setNoteMode(boolean activated) {
        setNullableButtonActivated(noteButton, activated);
    }

    /**
     * Sets the color of the choose-group button depending on {@code activated}.
     */
    public void setChooseGroupMode(boolean activated) {
        setNullableButtonActivated(chooseGroupButton, activated);
    }

    /**
     * Sets the color of the edit group button depending on {@code activated}.
     */
    public void setEditGroupMode(boolean activated) {
        setNullableButtonActivated(editGroupButton, activated);
    }

    private void setNullableButtonActivated(CustomButton button, boolean activated) {
        if (button != null) {
            button.setBackground(activated ? theme.markedCellColor : theme.normalCellColor);
        }
    }

    /**
     * Changing the tip-button to the remaining tips
     *
     * @param tipsRemaining the number how many tips are remaining
     */
    public void setRemainingTips(int tipsRemaining) {
        if (tipButton != null) {
            tipButton.setText(Integer.toString(Math.max(tipsRemaining, 0)));
            if (tipsRemaining <= 0) tipButton.setBackground(theme.errorTextColor);
        }
    }
}
