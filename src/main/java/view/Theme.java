package view;

import java.awt.*;

/**
 * @author Philipp Kremling
 */
public enum Theme {
    DARK(
            Color.decode("#686868"), // normalCellColor
            Color.white, // otherCellColor
            Color.decode("#5ba122"), // clickedCellColor
            Color.decode("#8bc34a"), // markedCellColor
            Color.decode("#989898"), // predefinedCellColor
            Color.decode("#78b53a"), // predefinedCellMarkedColor
            Color.decode("#C0C0C0"), // cellBorderColor
            Color.white, // primaryTextColor
            Color.decode("#686868"), // secondaryTextColor
            Color.red, // errorTextColor
            Color.darkGray.brighter(), // panelBackgroundColor
            Color.darkGray // menuBackgroundColor
    ),
    LIGHT(
            Color.white, // normalCellColor
            Color.black, // otherCellColor
            Color.decode("#c5e1a5"), // clickedCellColor
            Color.decode("#f2ffe3"), // markedCellColor
            Color.lightGray, // predefinedCellColor
            Color.decode("#dcedc9"), // predefinedCellMarkedColor
            Color.darkGray, // cellBorderColor
            Color.black, // primaryTextColor
            Color.white, // secondaryTextColor
            Color.red, // errorTextColor
            Color.lightGray.brighter(), // panelBackgroundColor
            Color.lightGray // menuBackgroundColor
    );

    /**
     * The normal {@link Color} for cell background.
     */
    public final Color normalCellColor;

    /**
     * The other {@link Color} for cell background.
     */
    public final Color otherCellColor;

    /**
     * The {@link Color} for cell background when clicked.
     */
    public final Color clickedCellColor;

    /**
     * The {@link Color} for cell background when there mustn't be a duplicate to clicked cell.
     */
    public final Color markedCellColor;

    /**
     * The {@link Color} for cell background when cell is predefined.
     */
    public final Color predefinedCellColor;

    /**
     * The {@link Color} for cell background when cell is predefined and possible conflicting to field.
     */
    public final Color predefinedCellMarkedColor;

    /**
     * The {@link Color} for cell borders.
     */
    public final Color cellBorderColor;

    /**
     * The primary {@link Color} for text.
     */
    public final Color primaryTextColor;

    /**
     * The secondary {@link Color} for text.
     */
    public final Color secondaryTextColor;

    /**
     * The {@link Color} for error text.
     */
    public final Color errorTextColor;

    /**
     * The {@link Color} for panel background.
     */
    public final Color panelBackgroundColor;

    /**
     * The {@link Color} for menu background.
     */
    public final Color menuBackgroundColor;


    Theme(Color normalCellColor, Color otherCellColor, Color clickedCellColor, Color markedCellColor,
          Color predefinedCellColor, Color predefinedCellMarkedColor, Color cellBorderColor, Color primaryTextColor,
          Color secondaryTextColor, Color errorTextColor, Color panelBackgroundColor, Color menuBackgroundColor) {
        this.normalCellColor = normalCellColor;
        this.otherCellColor = otherCellColor;
        this.clickedCellColor = clickedCellColor;
        this.markedCellColor = markedCellColor;
        this.predefinedCellColor = predefinedCellColor;
        this.predefinedCellMarkedColor = predefinedCellMarkedColor;
        this.cellBorderColor = cellBorderColor;
        this.primaryTextColor = primaryTextColor;
        this.secondaryTextColor = secondaryTextColor;
        this.errorTextColor = errorTextColor;
        this.panelBackgroundColor = panelBackgroundColor;
        this.menuBackgroundColor = menuBackgroundColor;
    }
}
