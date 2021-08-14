package view.main_menu;


import util.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class MainMenu extends JFrame {

    CardLayout cl = new CardLayout();
    JPanel cardsPanel;

    Mode mode;

    String theme = "default";

    JButton playSudokuButton, solveSudokuButton, solveKillerButton, solveStraitsButton, settingsButton, startButton, backButton;

    JToggleButton darkModeSwitch, autoStepForwardSwitch;

    boolean darkMode = false, autoStepForward = true;

    SizeChooseSlider slider;
    JSlider tipSlider;

    int tipLimit = 3;

    public MainMenu() {
        super("Sudoku Hauptmen\u00fc");

        setSize(new Dimension(350, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        //Main Menu Panel
        JPanel mainMenuPanel = new JPanel();
        setBackground(Color.RED);
        mainMenuPanel.setLayout(null);

        playSudokuButton = new JButton("Sudoku Spiel");
        playSudokuButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(playSudokuButton);
        playSudokuButton.setBounds(75, 100, 200, 50);

        solveSudokuButton = new JButton("Sudoku Löser");
        solveSudokuButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveSudokuButton);
        solveSudokuButton.setBounds(75, 200, 200, 50);

        solveKillerButton = new JButton("Killer Löser");
        solveKillerButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveKillerButton);
        solveKillerButton.setBounds(75, 300, 200, 50);

        solveStraitsButton = new JButton("Str8ts Löser");
        solveStraitsButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveStraitsButton);
        solveStraitsButton.setBounds(75, 400, 200, 50);

        settingsButton = new JButton("Einstellungen");
        settingsButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(settingsButton);
        settingsButton.setBounds(75,500,200,50);

        //Back Button
        backButton = new JButton("Zurück");
        backButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(backButton);
        backButton.setBounds(75, 100, 200, 50);

        //Game Settings Panel
        JPanel gameSettingsPanel = new JPanel();
        gameSettingsPanel.setLayout(null);
        slider = new SizeChooseSlider();
        gameSettingsPanel.add(slider, BorderLayout.CENTER);
        startButton = new JButton("Start");
        startButton.addActionListener(new MainMenu.ButtonListener());
        startButton.setBounds(100, 450, 150, 50);
        gameSettingsPanel.add(startButton, BorderLayout.SOUTH);
        gameSettingsPanel.add(backButton, BorderLayout.PAGE_START);

        //Settings Panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(null);
        settingsPanel.add(backButton, BorderLayout.PAGE_START);
        darkModeSwitch = new JToggleButton("DarkMode", darkMode);
        darkModeSwitch.addActionListener(new MainMenu.ButtonListener());
        darkModeSwitch.setBounds(100, 300, 150, 50);
        darkModeSwitch.setFocusable(false);
        settingsPanel.add(darkModeSwitch);
        autoStepForwardSwitch = new JToggleButton("Auto Step", autoStepForward);
        autoStepForwardSwitch.addActionListener(new MainMenu.ButtonListener());
        autoStepForwardSwitch.setBounds(100, 400, 150, 50);
        autoStepForwardSwitch.setFocusable(false);
        settingsPanel.add(autoStepForwardSwitch);
        JLabel tipText = new JLabel("Tipp-Limit:");
        tipText.setBounds(100,450,150,50);
        settingsPanel.add(tipText);
        tipSlider = new JSlider();
        tipSlider.setMinimum(0);
        tipSlider.setMaximum(4);
        tipSlider.setValue(5);
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
        settingsPanel.add(tipSlider);

        cardsPanel = new JPanel(cl);
        cardsPanel.add(mainMenuPanel, "mainMenu");
        cardsPanel.add(gameSettingsPanel, "gameSettingsPanel");
        cardsPanel.add(settingsPanel, "settingsPanel");
        cl.first(cardsPanel);
        add(cardsPanel, BorderLayout.CENTER);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == solveSudokuButton) {
                setTitle("Neuer Sudoku Löser");
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
            if(e.getSource() == settingsButton){
                setTitle("Einstellungen");
                cl.show(cardsPanel, "settingsPanel");
            }
            if (e.getSource() == autoStepForwardSwitch) {
                autoStepForward = !autoStepForward;
            }
            if (e.getSource() == darkModeSwitch) {
                darkMode = !darkMode;
                if(darkMode) {
                    theme = "dark";
                } else {
                    theme = "default";
                }
            }
            if (e.getSource() == backButton) {
                switch(tipSlider.getValue()) {
                    case 0: tipLimit = 0; break;
                    case 1: tipLimit = 3; break;
                    case 2: tipLimit = 5; break;
                    case 3: tipLimit = 10; break;
                    case 4: tipLimit = 20; break;
                }
                setTitle("Sudoku Hauptmen\u00fc");
                cl.first(cardsPanel);
            }
            if (e.getSource() == startButton) {
                startGame(slider.getValue());
            }
        }

        private void startGame(int size) {
            try {
                UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            } catch(Exception e) {}
            switch (mode) {
                case SUDOKU_SOLVE -> new presenter.SolveSudokuPresenter(size, theme, autoStepForward);
                case SUDOKU_PLAY -> new presenter.PlayPresenter(size, theme, autoStepForward, tipLimit);
                case KILLER_SOLVE -> new presenter.SolveKillerPresenter(size, theme, autoStepForward);
                case STRAITS_SOLVE -> new presenter.SolveStr8tsPresenter(size, theme, autoStepForward);
            }
            dispose();
        }
    }
}
