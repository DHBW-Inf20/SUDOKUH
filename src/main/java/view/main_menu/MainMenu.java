package view.main_menu;

import presenter.PlayPresenter;
import presenter.SolveKillerPresenter;
import presenter.SolveStr8tsPresenter;
import presenter.SolveSudokuPresenter;
import util.GameMode;
import view.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.awt.BorderLayout.CENTER;
import static java.util.Objects.requireNonNull;
import static util.GameMode.*;
import static util.Strings.*;
import static view.Theme.DARK;
import static view.Theme.LIGHT;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 * @author Luca Kellermann
 */
public final class MainMenu extends JFrame {

    /**
     * CardLayout to switch between different JPanels
     */
    final CardLayout cardLayout = new CardLayout();

    /**
     * Different JPanels to represent the separate menus
     */
    final JPanel cardsPanel = new JPanel(cardLayout);
    final JPanel mainMenuPanel = new JPanel();
    final JPanel settingsPanel = new JPanel();
    final JPanel gameSettingsPanel = new JPanel();

    /**
     * Text for the tipLabel
     */
    final JLabel tipText = new JLabel(TIP_LIMIT);

    /**
     * Sliders to setup game settings
     */
    final TipChooseSlider tipSlider = new TipChooseSlider();
    final SizeChooseSlider sizeSlider = new SizeChooseSlider();

    /**
     * Theme of menu
     */
    Theme theme = LIGHT;
    boolean darkModeEnabled = false;

    /**
     * Saves gameMode to open correct menu after clicking Button
     */
    GameMode gameMode = SUDOKU_SOLVE;

    /**
     * Gamesettings
     */
    boolean autoStepForwardEnabled = true;
    boolean highlightingEnabled = true;
    int tipLimit = 3;

    public MainMenu() {
        super(SUDOKUH + " " + MAIN_MENU);

        setSize(new Dimension(350, 660));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(theme.normalCellColor);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            setIconImage(ImageIO.read(requireNonNull(getClass().getResourceAsStream("/logo_200.png"))));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }


        // Main Menu Panel
        mainMenuPanel.setLayout(null);
        mainMenuPanel.setBackground(theme.normalCellColor);
        add(mainMenuPanel);
        try {
            BufferedImage logoImg = ImageIO.read(requireNonNull(getClass().getResourceAsStream("/logo_200.png")));
            JLabel logo = new JLabel(new ImageIcon(logoImg));
            mainMenuPanel.add(logo);
            logo.setBounds(75, 25, 200, 200);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        createButtonAndAddToPanel(mainMenuPanel, SUDOKU_GAME, 75, 250, 200, () -> {
            gameMode = SUDOKU_PLAY;
            setTitle(NEW_SUDOKU_GAME);
            cardLayout.show(cardsPanel, "gameSettingsPanel");
        });
        createButtonAndAddToPanel(mainMenuPanel, SUDOKU_SOLVER, 75, 325, 200, () -> {
            gameMode = SUDOKU_SOLVE;
            setTitle(NEW_SUDOKU_SOLVER);
            cardLayout.show(cardsPanel, "gameSettingsPanel");
        });
        createButtonAndAddToPanel(mainMenuPanel, KILLER_SOLVER, 75, 400, 200, () -> {
            gameMode = KILLER_SOLVE;
            startGame(3);
        });
        createButtonAndAddToPanel(mainMenuPanel, STR8TS_SOLVER, 75, 475, 200, () -> {
            gameMode = STR8TS_SOLVE;
            startGame(3);
        });
        createButtonAndAddToPanel(mainMenuPanel, SETTINGS, 75, 550, 200, () -> {
            setTitle(SETTINGS);
            cardLayout.show(cardsPanel, "settingsPanel");
        });


        // Game Settings Panel
        gameSettingsPanel.setLayout(null);
        gameSettingsPanel.setBackground(theme.normalCellColor);
        createButtonAndAddToPanel(gameSettingsPanel, BACK, 100, 25, 150, () -> {
            setTitle(SUDOKUH + " " + MAIN_MENU);
            cardLayout.first(cardsPanel);
        });
        sizeSlider.setBackground(theme.normalCellColor);
        sizeSlider.setLabelColors(theme.primaryTextColor);
        gameSettingsPanel.add(sizeSlider);
        createButtonAndAddToPanel(gameSettingsPanel, START, 100, 450, 150,
                () -> startGame(sizeSlider.getValue())
        );


        // Settings Panel
        settingsPanel.setLayout(null);
        settingsPanel.setBackground(theme.normalCellColor);
        createButtonAndAddToPanel(settingsPanel, BACK, 100, 25, 150, () -> {
            setTitle(SUDOKUH + " " + MAIN_MENU);
            switch (tipSlider.getValue()) {
                case 0 -> tipLimit = 0;
                case 1 -> tipLimit = 3;
                case 2 -> tipLimit = 5;
                case 3 -> tipLimit = 10;
                case 4 -> tipLimit = 20;
            }
            cardLayout.first(cardsPanel);
        });
        createToggleButtonAndAddToSettingsPanel(DARK_MODE, darkModeEnabled, 200, () -> {
            darkModeEnabled = !darkModeEnabled;
            theme = darkModeEnabled ? DARK : LIGHT;
            updateTheme();
        });
        createToggleButtonAndAddToSettingsPanel(AUTO_STEP, autoStepForwardEnabled, 300,
                () -> autoStepForwardEnabled = !autoStepForwardEnabled
        );
        createToggleButtonAndAddToSettingsPanel(HIGHLIGHTING, highlightingEnabled, 400,
                () -> highlightingEnabled = !highlightingEnabled
        );
        tipText.setBounds(100, 450, 150, 50);
        settingsPanel.add(tipText);
        tipSlider.setFocusable(false);
        tipSlider.setBackground(theme.normalCellColor);
        settingsPanel.add(tipSlider);

        cardsPanel.add(mainMenuPanel, "mainMenu");
        cardsPanel.add(gameSettingsPanel, "gameSettingsPanel");
        cardsPanel.add(settingsPanel, "settingsPanel");
        cardLayout.first(cardsPanel);
        add(cardsPanel, CENTER);

        setVisible(true);
    }

    /**
     * Opens new Game
     * @param size of the new game or solver
     */
    private void startGame(int size) {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        switch (gameMode) {
            case SUDOKU_SOLVE -> new SolveSudokuPresenter(size, theme, autoStepForwardEnabled, highlightingEnabled);
            case SUDOKU_PLAY -> new PlayPresenter(size, theme, autoStepForwardEnabled, highlightingEnabled, tipLimit);
            case KILLER_SOLVE -> new SolveKillerPresenter(size, theme, autoStepForwardEnabled, false);
            case STR8TS_SOLVE -> new SolveStr8tsPresenter(size, theme, autoStepForwardEnabled, highlightingEnabled);
        }
        dispose();
    }

    /**
     * changes Theme
     */
    private void updateTheme() {
        mainMenuPanel.setBackground(theme.normalCellColor);
        settingsPanel.setBackground(theme.normalCellColor);
        gameSettingsPanel.setBackground(theme.normalCellColor);

        tipText.setForeground(theme.primaryTextColor);
        tipSlider.setBackground(theme.normalCellColor);
        tipSlider.setLabelColors(theme.primaryTextColor);

        sizeSlider.setBackground(theme.normalCellColor);
        sizeSlider.setLabelColors(theme.primaryTextColor);
    }

    /**
     * adds button to the Panel
     * @param panel
     * @param text
     * @param x coordinates
     * @param y coordinates
     * @param width
     * @param listener for inputs
     */
    private void createButtonAndAddToPanel(JPanel panel, String text, int x, int y, int width, Runnable listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, 50);
        button.addActionListener(e -> listener.run());
        panel.add(button);
    }

    /**
     * adds toggle button to settings menu
     * @param text
     * @param toggled
     * @param y coordinates
     * @param listener for inputs
     */
    private void createToggleButtonAndAddToSettingsPanel(String text, boolean toggled, int y, Runnable listener) {
        JToggleButton button = new JToggleButton(text, toggled);
        button.setBounds(100, y, 150, 50);
        button.setFocusable(false);
        button.addActionListener(e -> listener.run());
        settingsPanel.add(button);
    }
}
