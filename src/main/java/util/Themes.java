package util;

import java.awt.*;

public class Themes {
    private final Color primaryBackgroundColor;
    // Color for field-background (secondary)
    private final Color secondaryBackgroundColor;
    // Color for field-background when clicked
    private final Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    private final Color markedColor;
    // Color for field-background when field is predefined
    private final Color predefinedColor;
    // Color for field-background when field is predefined and possible conflicting to field
    private final Color predefinedMarkedColor;
    // Color for field-borders
    private final Color borderColor;
    // Color for text
    private final Color primaryTextColor;
    // Color for text (secondary)
    private final Color secondaryTextColor;
    // Color for text if there is an error
    private final Color errorTextColor;
    // Color for panel background
    private final Color panelBackgroundColor;
    // Color for menu background
    private final Color menuBackgroundColor;

    public Themes(String theme) {
        switch (theme) {
            case "dark": {
                primaryBackgroundColor = Color.decode("#686868");
                secondaryBackgroundColor = Color.decode("#FFFFFF");
                clickedColor = Color.decode("#5ba122");
                markedColor = Color.decode("#8bc34a");
                predefinedColor = Color.decode("#989898");
                predefinedMarkedColor = Color.decode("#78b53a");
                primaryTextColor = Color.decode("#FFFFFF");
                secondaryTextColor = Color.decode("#686868");
                errorTextColor = Color.red;
                borderColor = Color.decode("#C0C0C0");
                panelBackgroundColor = Color.darkGray.brighter();
                menuBackgroundColor = Color.darkGray;
                break;
            }
            default: {
                primaryBackgroundColor = Color.white;
                secondaryBackgroundColor = Color.black;
                clickedColor = Color.decode("#c5e1a5");
                markedColor = Color.decode("#f2ffe3");
                predefinedColor = Color.lightGray;
                predefinedMarkedColor = Color.decode("#dcedc9");
                primaryTextColor = Color.black;
                secondaryTextColor = Color.white;
                errorTextColor = Color.red;
                borderColor = Color.darkGray;
                panelBackgroundColor = Color.lightGray.brighter();
                menuBackgroundColor = Color.lightGray;
            }
        }
    }

    public Color getPrimaryBackgroundColor() {
        return primaryBackgroundColor;
    }

    public Color getSecondaryBackgroundColor() {
        return secondaryBackgroundColor;
    }

    public Color getClickedColor() {
        return clickedColor;
    }

    public Color getMarkedColor() {
        return markedColor;
    }

    public Color getPredefinedColor() {
        return predefinedColor;
    }

    public Color getPredefinedMarkedColor() {
        return predefinedMarkedColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getPrimaryTextColor() {
        return primaryTextColor;
    }

    public Color getSecondaryTextColor() {
        return secondaryTextColor;
    }

    public Color getErrorTextColor() {
        return errorTextColor;
    }

    public Color getPanelBackgroundColor() {
        return panelBackgroundColor;
    }

    public Color getMenuBackgroundColor() {
        return menuBackgroundColor;
    }
}
