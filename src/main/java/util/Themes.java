package util;

import java.awt.*;

public class Themes {
    public static Color primaryBackgroundColor;
    // Color for field-background (secondary)
    public static Color secondaryBackgroundColor;
    // Color for field-background when clicked
    public static Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    public static Color markedColor;
    // Color for field-background when field is predefined
    public static Color predefinedColor;
    // Color for field-background when field is predefined and possible conflicting to field
    public static Color predefinedMarkedColor;
    // Color for field-borders
    public static Color borderColor;
    // Color for text
    public static Color primaryTextColor;
    // Color for text (secondary)
    public static Color secondaryTextColor;
    // Color for text if there is an error
    public static Color errorTextColor;

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
            }
        }
    }
}
