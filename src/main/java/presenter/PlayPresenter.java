package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetCellResult;
import model.Sudoku;
import model.SudokuGenerator;
import util.Mode;
import view.Theme;
import view.ingame.CustomButton;
import view.ingame.LabelPanel;
import view.ingame.InGameViewScaffold;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Philipp Kremling
 */
public class PlayPresenter implements Presenter {

    protected final AbstractPuzzle sudoku;
    protected final AbstractPuzzle solution;
    protected final view.ingame.InGameViewScaffold inGameViewScaffold;

    protected int tipLimit;
    protected int tipsUsed;

    protected boolean autoStepForward;

    protected Timer timer;
    protected long startTime;
    protected long lastUpdateTime;
    protected long pausedTime = 0;

    protected boolean solved;

    /**
     * Specifies whether the node mode is active or not
     */
    private boolean noteMode;

    public PlayPresenter(int size, Theme theme, boolean autoStepForward, boolean highlighting, int tipLimit) {
        noteMode = false;

        SudokuGenerator.SudokuAndSolution sudokuAndSolution = SudokuGenerator.generateSudokuAndSolution(size);
        sudoku = sudokuAndSolution.sudoku();
        solution = sudokuAndSolution.solution();

        inGameViewScaffold = new InGameViewScaffold(size, this::handleButtonListenerEvent, "Spielen", theme, highlighting, autoStepForward, tipLimit, Mode.SUDOKU_PLAY, this);

        inGameViewScaffold.addKeyListener(new KeyInputListener(this, autoStepForward));

        this.setPredefinedCells();

        this.tipLimit = tipLimit;
        this.tipsUsed = 0;
        this.autoStepForward = autoStepForward;
        inGameViewScaffold.setRemainingTips(tipLimit - tipsUsed);
        if (tipLimit - tipsUsed == 0) inGameViewScaffold.reachedMaxTips();

        startTime = ZonedDateTime.now().toInstant().toEpochMilli();
        lastUpdateTime = 0;

        timer = new Timer(0, e -> setTimer());
        timer.setInitialDelay(1000);
        timer.start();

        solved = false;
    }

    /**
     * Getting all predefined cells and setting it to the gui
     */
    private void setPredefinedCells() {
        for (int i = 0; i < sudoku.getGridSize(); i++) {
            for (int j = 0; j < sudoku.getGridSize(); j++) {
                int value = sudoku.getCell(i, j);
                if (value != Sudoku.EMPTY_CELL) {
                    inGameViewScaffold.setPredefined(i, j, value);
                }
            }
        }
    }

    /**
     * If sudoku is solved right, print a text to the gui
     */
    private boolean verifySolution() {
        if (sudoku.equals(solution)) {
            timer.stop();
            long actualTime = ZonedDateTime.now().toInstant().toEpochMilli();
            long timeDif = actualTime - startTime;
            inGameViewScaffold.setGUIText(("<html><body><center>Korrekte L\u00f6sung!<br>" + this.getTimerText(timeDif) + "</center></body></html>"), Color.green);
            solved = true;
            return true;
        }
        return false;
    }

    /**
     * Action listener for buttons
     */
    public void handleButtonListenerEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        handleButton(button);
    }

    /**
     * Handles the button events and triggers actions based on the clicked button
     */
    @Override
    public void handleButton(CustomButton button) {
        if (solved) return;
        LabelPanel clickedCell = inGameViewScaffold.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                int number = button.getValue();
                if (!clickedCell.isPredefined()) {
                    if (noteMode) {
                        if (clickedCell.getLabelValue() != null) {
                            sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                            clickedCell.setText("");
                        }
                        inGameViewScaffold.setNote(number);
                    } else {
                        final SetCellResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                        if (result == SetCellResult.SUCCESS) {
                            inGameViewScaffold.validInput(String.valueOf(number));
                            this.verifySolution();
                        } else {
                            final Set<Cell> conflicts = result.conflictingCells();
                            for (Cell c : conflicts) {
                                inGameViewScaffold.highlightConflicts(c);
                            }
                            inGameViewScaffold.invalidInput(String.valueOf(number));
                            inGameViewScaffold.setGUIText("Logisch falscher Input!", Color.red);
                            // Pause time display for 1 seconds
                            lastUpdateTime += 1000;
                        }
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                    clickedCell.setText("");
                }
            }
            case TIP -> {
                if (!clickedCell.isPredefined()) {
                    tipsUsed++;
                    if (tipLimit >= tipsUsed) {
                        inGameViewScaffold.validInput(String.valueOf(solution.getCell(clickedCell.getRow(), clickedCell.getCol())), (tipLimit - tipsUsed));
                        inGameViewScaffold.setPredefined(clickedCell.getRow(), clickedCell.getCol(), solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                        final SetCellResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                        if (!result.isSuccess()) {
                            final Set<Cell> conflicts = result.conflictingCells();
                            if (!conflicts.isEmpty()) {
                                // can be any conflict (no order guaranteed)
                                for (Cell c : conflicts) {
                                    inGameViewScaffold.invalidInput(c.row(), c.column());
                                }
                            }
                        }
                        this.verifySolution();
                        if (tipsUsed == tipLimit) inGameViewScaffold.reachedMaxTips();
                    } else {
                        inGameViewScaffold.reachedMaxTips();
                    }
                }
            }
            case VERIFY -> {
                if (!this.verifySolution()) {
                    inGameViewScaffold.setGUIText("Diese L\u00f6sung ist falsch oder unvollst\u00e4ndig!", Color.red);
                    // Pause time display for 5 seconds
                    lastUpdateTime += 5000;
                }
            }
            case PEN -> {
                noteMode = !noteMode;
                if (noteMode) {
                    inGameViewScaffold.setNoteMode();
                } else {
                    inGameViewScaffold.setNormalMode();
                }
            }
        }
    }

    /**
     * Sets the playing time timer to the actual value
     */
    protected void setTimer() {
        long actualTime = ZonedDateTime.now().toInstant().toEpochMilli();
        // Only set the time if there is a difference of one second
        if (actualTime >= lastUpdateTime + 1000) {
            long timeDif = actualTime - startTime + pausedTime;
            inGameViewScaffold.setGUIText(this.getTimerText(timeDif));
            lastUpdateTime = actualTime;
        }
    }

    /**
     * Returns a formatted String of time
     *
     * @param timeDif the time difference
     * @return a String to be set in the gui
     */
    private String getTimerText(long timeDif) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeDif);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDif) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDif));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDif) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDif));
        String hoursText = hours == 1 ? " Stunde, " : " Stunden, ";
        String minutesText = minutes == 1 ? " Minute, " : " Minuten, ";
        String secondsText = seconds == 1 ? " Sekunde" : " Sekunden";
        String text = "Spielzeit: ";
        if (hours != 0) text += hours + hoursText;
        if (minutes != 0) text += minutes + minutesText;
        return (text + seconds + secondsText);
    }

    /**
     * Saves current time to add later when the timer is resumed
     */
    public void pauseTimer() {
        pausedTime = pausedTime + lastUpdateTime - startTime;
        timer.stop();
    }

    public void resumeTimer() {
        timer.start();
        startTime = ZonedDateTime.now().toInstant().toEpochMilli();
    }

    /**
     * @return the gui of the program
     */
    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }

    @Override
    public boolean getNoteMode() {
        return noteMode;
    }
}
