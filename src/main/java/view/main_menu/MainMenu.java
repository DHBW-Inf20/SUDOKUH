package view.main_menu;

import util.Mode;
import view.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static view.Theme.DARK;
import static view.Theme.LIGHT;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public class MainMenu extends JFrame {

    Theme theme = LIGHT;

    CardLayout cl = new CardLayout();
    JPanel cardsPanel;

    JPanel mainMenuPanel = new JPanel();
    JPanel settingsPanel = new JPanel();
    JPanel gameSettingsPanel = new JPanel();
    JLabel tipText = new JLabel("Tipp-Limit:");

    Mode mode;

    JButton playSudokuButton, solveSudokuButton, solveKillerButton, solveStraitsButton, settingsButton, startButton, backButtonGameSettings, backButtonSettings;

    JToggleButton darkModeSwitch, autoStepForwardSwitch, highlightSwitch;

    boolean darkMode = false, autoStepForward = true, highlighting = true;

    SizeChooseSlider sizeSlider;
    TipChooseSlider tipSlider;

    int tipLimit = 3;

    public MainMenu() {
        super("SUDOKUH Hauptmen\u00fc");

        setSize(new Dimension(350, 660));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.white);
        this.setLocationRelativeTo(null);
        setResizable(false);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("/logo_200.png"));
            this.setIconImage(img);
        } catch (Exception ex) {
            System.out.println(ex);
        }


        //Main Menu Panel
        mainMenuPanel.setBackground(theme.normalCellColor);
        mainMenuPanel.setLayout(null);
        add(mainMenuPanel);

        try {
            BufferedImage logoImg = ImageIO.read(getClass().getResourceAsStream("/logo_200.png"));
            JLabel logo = new JLabel(new ImageIcon(logoImg));
            mainMenuPanel.add(logo);
            logo.setBounds(75, 25, 200, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }


        playSudokuButton = new JButton("Sudoku Spiel");
        playSudokuButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(playSudokuButton);
        playSudokuButton.setBounds(75, 250, 200, 50);

        solveSudokuButton = new JButton("Sudoku L\u00f6ser");
        solveSudokuButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveSudokuButton);
        solveSudokuButton.setBounds(75, 325, 200, 50);

        solveKillerButton = new JButton("Killer L\u00f6ser");
        solveKillerButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveKillerButton);
        solveKillerButton.setBounds(75, 400, 200, 50);

        solveStraitsButton = new JButton("Str8ts L\u00f6ser");
        solveStraitsButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveStraitsButton);
        solveStraitsButton.setBounds(75, 475, 200, 50);

        settingsButton = new JButton("Einstellungen");
        settingsButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(settingsButton);
        settingsButton.setBounds(75, 550, 200, 50);

        //Back Button Settings
        backButtonSettings = new JButton("Zur\u00fcck");
        backButtonSettings.addActionListener(new MainMenu.ButtonListener());
        backButtonSettings.setBounds(100, 25, 150, 50);

        //Back Button Game Settings
        backButtonGameSettings = new JButton("Zur\u00fcck");
        backButtonGameSettings.addActionListener(new MainMenu.ButtonListener());
        backButtonGameSettings.setBounds(100, 25, 150, 50);

        //Game Settings Panel
        sizeSlider = new SizeChooseSlider();
        sizeSlider.setBackground(theme.normalCellColor);
        sizeSlider.setLabelColors(theme.primaryTextColor);
        gameSettingsPanel.add(backButtonSettings);
        gameSettingsPanel.setLayout(null);
        gameSettingsPanel.add(sizeSlider);
        gameSettingsPanel.setBackground(theme.normalCellColor);
        startButton = new JButton("Start");
        startButton.addActionListener(new MainMenu.ButtonListener());
        startButton.setBounds(100, 450, 150, 50);
        gameSettingsPanel.add(startButton);


        //Settings Panel
        settingsPanel.setLayout(null);
        settingsPanel.setBackground(theme.normalCellColor);
        settingsPanel.add(backButtonGameSettings);
        darkModeSwitch = new JToggleButton("DarkMode", darkMode);
        darkModeSwitch.addActionListener(new MainMenu.ButtonListener());
        darkModeSwitch.setBounds(100, 200, 150, 50);
        darkModeSwitch.setFocusable(false);
        settingsPanel.add(darkModeSwitch);
        autoStepForwardSwitch = new JToggleButton("Auto Step", autoStepForward);
        autoStepForwardSwitch.addActionListener(new MainMenu.ButtonListener());
        autoStepForwardSwitch.setBounds(100, 300, 150, 50);
        autoStepForwardSwitch.setFocusable(false);
        settingsPanel.add(autoStepForwardSwitch);
        highlightSwitch = new JToggleButton("Hervorhebungen", highlighting);
        highlightSwitch.addActionListener(new MainMenu.ButtonListener());
        highlightSwitch.setBounds(100, 400, 150, 50);
        highlightSwitch.setFocusable(false);
        settingsPanel.add(highlightSwitch);
        tipText.setBounds(100, 450, 150, 50);
        settingsPanel.add(tipText);
        tipSlider = new TipChooseSlider();
        tipSlider.setFocusable(false);
        tipSlider.setBackground(theme.normalCellColor);
        settingsPanel.add(tipSlider);

        cardsPanel = new JPanel(cl);
        cardsPanel.add(mainMenuPanel, "mainMenu");
        cardsPanel.add(gameSettingsPanel, "gameSettingsPanel");
        cardsPanel.add(settingsPanel, "settingsPanel");
        cl.first(cardsPanel);
        add(cardsPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == solveSudokuButton) {
                setTitle("Neuer Sudoku L\u00f6ser");
                mode = Mode.SUDOKU_SOLVE;
                cl.show(cardsPanel, "gameSettingsPanel");
            }
            if (e.getSource() == playSudokuButton) {
                setTitle("Neues Sudoku Spiel");
                mode = Mode.SUDOKU_PLAY;
                cl.show(cardsPanel, "gameSettingsPanel");
            }
            if (e.getSource() == solveKillerButton) {
                mode = Mode.KILLER_SOLVE;
                startGame(3);
            }
            if (e.getSource() == solveStraitsButton) {
                mode = Mode.STRAITS_SOLVE;
                startGame(3);
            }
            if (e.getSource() == settingsButton) {
                setTitle("Einstellungen");
                cl.show(cardsPanel, "settingsPanel");
            }
            if (e.getSource() == autoStepForwardSwitch) {
                autoStepForward = !autoStepForward;
            }
            if (e.getSource() == highlightSwitch) {
                highlighting = !highlighting;
            }
            if (e.getSource() == darkModeSwitch) {
                darkMode = !darkMode;
                theme = darkMode ? DARK : LIGHT;
                updateBackgrounds();
            }
            if (e.getSource() == backButtonSettings) {
                setTitle("SUDOKUH Hauptmen\u00fc");
                cl.first(cardsPanel);
            }
            if (e.getSource() == startButton) {
                startGame(sizeSlider.getValue());
            }
            if (e.getSource() == backButtonGameSettings) {
                setTitle("SUDOKUH Hauptmen\u00fc");
                switch (tipSlider.getValue()) {
                    case 0 -> tipLimit = 0;
                    case 1 -> tipLimit = 3;
                    case 2 -> tipLimit = 5;
                    case 3 -> tipLimit = 10;
                    case 4 -> tipLimit = 20;
                }
                cl.first(cardsPanel);
            }
        }

        private void startGame(int size) {
            try {
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            switch (mode) {
                case SUDOKU_SOLVE -> new presenter.SolveSudokuPresenter(size, theme, autoStepForward, highlighting);
                case SUDOKU_PLAY -> new presenter.PlayPresenter(size, theme, autoStepForward, highlighting, tipLimit);
                case KILLER_SOLVE -> new presenter.SolveKillerPresenter(size, theme, autoStepForward, false);
                case STRAITS_SOLVE -> new presenter.SolveStr8tsPresenter(size, theme, autoStepForward, highlighting);
            }
            dispose();
        }
    }

    private void updateBackgrounds() {
        mainMenuPanel.setBackground(theme.normalCellColor);
        settingsPanel.setBackground(theme.normalCellColor);
        tipSlider.setBackground(theme.normalCellColor);
        tipSlider.setLabelColors(theme.primaryTextColor);
        tipText.setForeground(theme.primaryTextColor);
        gameSettingsPanel.setBackground(theme.normalCellColor);
        sizeSlider.setBackground(theme.normalCellColor);
        sizeSlider.setLabelColors(theme.primaryTextColor);
    }
}
