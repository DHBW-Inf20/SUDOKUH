package view.main_menu;


import util.Mode;
import util.Themes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class MainMenu extends JFrame {

    CardLayout cl = new CardLayout();
    JPanel cardsPanel;

    JPanel mainMenuPanel = new JPanel();
    JPanel settingsPanel = new JPanel();
    JPanel gameSettingsPanel = new JPanel();
    JLabel tipText = new JLabel("Tipp-Limit:");

    Mode mode;

    String theme = "default";

    JButton playSudokuButton, solveSudokuButton, solveKillerButton, solveStraitsButton, settingsButton, startButton, backButtonGameSettings, backButtonSettings;

    JToggleButton darkModeSwitch, autoStepForwardSwitch, highlightSwitch;

    boolean darkMode = false, autoStepForward = true, highlighting = true;

    SizeChooseSlider slider;
    JSlider tipSlider;

    Themes t = new Themes(theme);

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
        mainMenuPanel.setBackground(t.getPrimaryBackgroundColor());
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
        slider = new SizeChooseSlider();
        slider.setBackground(t.getPrimaryBackgroundColor());
        gameSettingsPanel.add(backButtonSettings);
        gameSettingsPanel.setLayout(null);
        gameSettingsPanel.add(slider);
        gameSettingsPanel.setBackground(t.getPrimaryBackgroundColor());
        startButton = new JButton("Start");
        startButton.addActionListener(new MainMenu.ButtonListener());
        startButton.setBounds(100, 450, 150, 50);
        gameSettingsPanel.add(startButton);


        //Settings Panel
        settingsPanel.setLayout(null);
        settingsPanel.setBackground(t.getPrimaryBackgroundColor());
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
        tipSlider = new JSlider();
        tipSlider.setMinimum(0);
        tipSlider.setMaximum(4);
        tipSlider.setValue(1);
        tipSlider.setPaintTicks(true);
        tipSlider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("0"));
        labels.put(1, new JLabel("3"));
        labels.put(2, new JLabel("5"));
        labels.put(3, new JLabel("10"));
        labels.put(4, new JLabel("20"));
        tipSlider.setLabelTable(labels);
        tipSlider.setBounds(100, 500, 150, 50);
        tipSlider.setFocusable(false);
        tipSlider.setBackground(t.getPrimaryBackgroundColor());
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
                if (darkMode) {
                    theme = "dark";
                } else {
                    theme = "default";
                }
                updateBackgrounds();
            }
            if (e.getSource() == backButtonSettings) {
                setTitle("SUDOKUH Hauptmen\u00fc");
                cl.first(cardsPanel);
            }
            if (e.getSource() == startButton) {
                startGame(slider.getValue());
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
        t = new Themes(theme);
        mainMenuPanel.setBackground(t.getPrimaryBackgroundColor());
        settingsPanel.setBackground(t.getPrimaryBackgroundColor());
        tipSlider.setBackground(t.getPrimaryBackgroundColor());
        tipText.setForeground(t.getPrimaryTextColor());
        gameSettingsPanel.setBackground(t.getPrimaryBackgroundColor());
        slider.setBackground(t.getPrimaryBackgroundColor());
    }
}
