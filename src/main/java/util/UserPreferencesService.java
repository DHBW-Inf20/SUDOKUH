package util;

import view.Theme;

import java.util.prefs.Preferences;

import static java.util.Arrays.stream;

/**
 * @author Fabian Heinl
 */
public class UserPreferencesService {

    /**
     * Reads {@link UserPreferences} and returns them.
     */
    public static UserPreferences readUserPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(UserPreferencesService.class);
        String theme = prefs.get("theme", Theme.LIGHT.name());
        return new UserPreferences(
                stream(Theme.values()).anyMatch(it -> it.name().equals(theme)) ? Theme.valueOf(theme) : Theme.LIGHT,
                prefs.getBoolean("autoStep", true),
                prefs.getBoolean("highlightingEnabled", true),
                prefs.getInt("tipLimit", 3)
        );
    }

    /**
     * Saves user settings.
     *
     * @param theme                  theme to save
     * @param autoStepForwardEnabled autoStep setting to save
     * @param highlightingEnabled    autoStep setting to save
     * @param tipLimit               amount of tipLimit to save
     */
    public static void writeUserPreferences(Theme theme, boolean autoStepForwardEnabled, boolean highlightingEnabled, int tipLimit) {
        Preferences prefs = Preferences.userNodeForPackage(UserPreferencesService.class);
        prefs.put("theme", theme.name());
        prefs.putBoolean("autoStep", autoStepForwardEnabled);
        prefs.putBoolean("highlightingEnabled", highlightingEnabled);
        prefs.putInt("tipLimit", tipLimit);
    }
}
