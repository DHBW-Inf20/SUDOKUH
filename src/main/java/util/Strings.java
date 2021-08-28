package util;

import java.util.Locale;
import java.util.Objects;

public final class Strings {

    public static final boolean isGerman = Objects.equals(Locale.getDefault().getLanguage(), "de");


    @SuppressWarnings("SpellCheckingInspection")
    public static final String SUDOKUH = "SUDOKUH";

    public static final String PLAY = isGerman ? "Spielen" : "Play";

    public static final String YES = isGerman ? "JA" : "YES";

    public static final String NO = isGerman ? "NEIN" : "NO";

    public static final String DELETE = isGerman ? "L\u00f6schen" : "delete";

    public static final String SEARCH_SOLUTION = isGerman ? "L\u00f6sung suchen" : "search solution";

    public static final String SHOW_TIP = isGerman ? "Tipp anzeigen" : "show tip";

    public static final String VERIFY_SOLUTION = isGerman ? "L\u00f6sung \u00fcberpr\u00fcfen" : "verify solution";

    public static final String NOTE_MODE = isGerman ? "Notiz-Modus" : "note mode";

    public static final String EDIT_GROUP = isGerman ? "Gruppe bearbeiten" : "edit group";

    public static final String CHANGE_COLOR = isGerman ? "Farbe wechseln" : "change color";

    public static final String CHOOSE_GROUP = isGerman ? "Gruppe ausw\u00e4hlen" : "choose group";

    public static final String DELETE_GROUP = isGerman ? "Gruppe l\u00f6schen" : "delete group";

    public static final String CORRECT_SOLUTION = isGerman ? "Korrekte L\u00f6sung!" : "Correct solution!";

    public static final String LOGICAL_WRONG_INPUT = isGerman ? "Logisch falscher Input!" : "Logical wrong input!";

    public static final String THIS_SOLUTION_IS_WRONG_OR_INCOMPLETE = isGerman ? "Diese L\u00f6sung ist falsch oder unvollst\u00e4ndig!" : "This solution is wrong or incomplete!";

    public static final String HOUR = isGerman ? "Stunde" : "hour";

    public static final String HOURS = isGerman ? "Stunden" : "hours";

    public static final String MINUTE = isGerman ? "Minute" : "minute";

    public static final String MINUTES = isGerman ? "Minuten" : "minutes";

    public static final String SECOND = isGerman ? "Sekunde" : "second";

    public static final String SECONDS = isGerman ? "Sekunden" : "seconds";

    public static final String PLAY_TIME = isGerman ? "Spielzeit" : "Play time";

    public static final String THIS_PUZZLE_CANNOT_BE_SOLVED = isGerman ? "Dieses R\u00e4tsel kann nicht gel\u00f6st werden!" : "This puzzle cannot be solved!";
    public static final String THIS_SUDOKU_CANNOT_BE_SOLVED = isGerman ? "Dieses Sudoku kann nicht gel\u00f6st werden!" : "This Sudoku cannot be solved!";

    public static final String THIS_PUZZLE_CANNOT_BE_SOLVED_YET = isGerman ? "Dieses R\u00e4tsel kann noch nicht gel\u00f6st werden!" : "This puzzle cannot be solved yet!";
    public static final String THIS_SUDOKU_CANNOT_BE_SOLVED_YET = isGerman ? "Dieses Sudoku kann noch nicht gel\u00f6st werden!" : "This Sudoku cannot be solved yet!";

    public static final String THE_PUZZLE_WAS_SOLVED_SUCCESSFULLY = isGerman ? "Das R\u00e4tsel wurde erfolgreich gel\u00f6st!" : "The puzzle was solved successfully!";
    public static final String THE_SUDOKU_WAS_SOLVED_SUCCESSFULLY = isGerman ? "Das Sudoku wurde erfolgreich gel\u00f6st!" : "The Sudoku was solved successfully!";

    public static final String BUT_THERE_WAS_MORE_THAN_ONE_POSSIBILITY = isGerman ? "Es gab allerdings mehr als eine M\u00f6glichkeit." : "But there was more than one possibility.";

    public static final String THE_SELECT_MODE_CANNOT_BE_ACTIVATED_WHILE_THE_EDIT_MODE_IS_ACTIVATED = CENTER(isGerman ? "W\u00e4hrend der Bearbeitungsmodus aktiviert ist, kann der<br>Auswahlmodus nicht aktiviert werden." : "The select mode cannot be activated<br>while the edit mode is activated.");

    public static final String THE_EDIT_MODE_CANNOT_BE_ACTIVATED_WHILE_THE_SELECT_MODE_IS_ACTIVATED = CENTER(isGerman ? "W\u00e4hrend der Auswahlmodus aktiviert ist, kann der<br>Bearbeitungsmodus nicht aktiviert werden." : "The edit mode cannot be activated<br>while the select mode is activated.");

    public static final String FAULTY_SUM = isGerman ? "Fehlerhafte Summe!" : "Faulty sum!";

    public static final String LOGICAL_INCORRECT_SUM = isGerman ? "Logisch inkorrekte Summe!" : "Logical incorrect sum!";

    public static final String GROUPS_WITH_DUPLICATE_VALUES_ARE_NOT_ALLOWED = isGerman ? "Gruppen mit doppelt vorkommenden Werten sind nicht erlaubt!" : "Groups with duplicate values are not allowed!";

    public static final String THE_SUM_IS_INVALID = isGerman ? "Die Summe ist ung\u00fcltig!" : "The sum is invalid!";

    public static final String EMPTY_GROUPS_ARE_NOT_ALLOWED = isGerman ? "Es k\u00f6nnen keine leeren Gruppen hinzugef\u00fcgt werden!" : "Empty groups are not allowed!";

    public static String GROUPS_CAN_AT_MOST_HAVE_N_CELLS(final int n) {
        return isGerman ? "Gruppen k\u00f6nnen maximal " + n + " Zellen haben!" : "Groups can at most have " + n + " cells!";
    }

    public static final String THIS_WOULD_LEAD_TO_GROUPS_WITH_UNCONNECTED_CELLS = CENTER(isGerman ? "Dies w\u00fcrde zu Gruppen mit<br>nicht zusammenh\u00e4ngenden Zellen f\u00fchren!" : "This would lead to groups<br>with unconnected cells!");

    public static final String SOLVE_STR8TS = isGerman ? "Str8ts l\u00f6sen" : "Solve Str8ts";

    public static final String SOLVE_KILLER = isGerman ? "Killer l\u00f6sen" : "Solve Killer";

    public static final String SOLVE_SUDOKU = isGerman ? "Sudoku l\u00f6sen" : "Solve Sudoku";

    public static final String RESET = isGerman ? "Spielfeld zur\u00fccksetzen" : "reset";

    public static final String RESET_ALL_PROGRESS_WILL_BE_LOST = isGerman ? "Spielfeld zur\u00fccksetzen? Der Fortschritt geht verloren!" : "Reset? All progress will be lost!";

    public static final String GO_TO_MAIN_MENU = isGerman ? "Zum Hauptmen\u00fc" : "go to main menu";

    public static final String GO_TO_MAIN_MENU_ALL_PROGRESS_WILL_BE_LOST = isGerman ? "Zur\u00fcck zum Men\u00fc? Der Fortschritt geht verloren!" : "Go to main menu? All progress will be lost!";

    public static final String WHICH_SUM_DOES_THIS_GROUP_HAVE = isGerman ? "Welche Summe hat diese Gruppe?" : "Which sum does this group have?";

    public static final String TIP_LIMIT = isGerman ? "Tipp-Limit:" : "Tip limit:";

    public static final String MAIN_MENU = isGerman ? "Hauptmen\u00fc" : "main menu";

    public static final String SUDOKU_GAME = isGerman ? "Sudoku Spiel" : "Sudoku game";

    public static final String NEW_SUDOKU_GAME = isGerman ? "Neues Sudoku Spiel" : "New Sudoku game";

    public static final String SUDOKU_SOLVER = isGerman ? "Sudoku L\u00f6ser" : "Sudoku solver";

    public static final String NEW_SUDOKU_SOLVER = isGerman ? "Neuer Sudoku L\u00f6ser" : "New Sudoku solver";

    public static final String KILLER_SOLVER = isGerman ? "Killer L\u00f6ser" : "Killer solver";

    public static final String STR8TS_SOLVER = isGerman ? "Str8ts L\u00f6ser" : "Str8ts solver";

    public static final String SETTINGS = isGerman ? "Einstellungen" : "Settings";

    public static final String BACK = isGerman ? "Zur\u00fcck" : "Back";

    public static final String START = "Start";

    public static final String DARK_MODE = isGerman ? "DarkMode" : "Dark mode";

    public static final String AUTO_STEP = "Auto Step";

    public static final String HIGHLIGHTING = isGerman ? "Hervorhebungen" : "Highlighting";


    public static final String BR = "<br>";

    public static String CENTER(final String string) {
        return "<html><body><center>" + string + "</center></body></html>";
    }

    private Strings() {}
}
