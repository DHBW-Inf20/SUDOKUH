package util;


import view.Theme;
import view.main_menu.MainMenu;

import java.util.prefs.Preferences;

public class PreferencesService {

    /**
     * Reads UserPreferences and saves them in the given menu
     * @param menu menu to save preferences to
     */
    public void readUserPreferences(MainMenu menu) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        menu.theme = Theme.valueOf(prefs.get("theme", "LIGHT"));
        menu.autoStepForwardEnabled = prefs.getBoolean("autoStep", true);
        menu.highlightingEnabled = prefs.getBoolean("highlightingEnabled",true);
        menu.tipLimit = prefs.getInt("tipLimit", 3);
    }

    /**
     * Saves user settings
     * @param themeEnum theme to save
     * @param autoStepForwardEnabled autoStep setting to save
     * @param highlightingEnabled autoStep setting to save
     * @param tipLimit amount of tipLimit to save
     */
    public void writeUserPreferences(Theme themeEnum, boolean autoStepForwardEnabled, boolean highlightingEnabled, int tipLimit) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put("theme", themeEnum.toString());
        prefs.putBoolean("autoStep", autoStepForwardEnabled);
        prefs.putBoolean("highlightingEnabled", highlightingEnabled);
        prefs.putInt("tipLimit", tipLimit);
    }
}
