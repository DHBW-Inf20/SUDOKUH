package util;

import view.Theme;

/**
 * @author Luca Kellermann
 */
public record UserPreferences(Theme theme, boolean autoStepForwardEnabled, boolean highlightingEnabled, int tipLimit) {}
