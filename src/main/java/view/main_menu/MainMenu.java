package view.main_menu;


import util.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    CardLayout cl = new CardLayout();
    JPanel cardsPanel;

    Mode mode;

    String theme = "dark";

    JButton playSudokuButton, solveSudokuButton, solveKillerButton, solveStraitsButton, settingsButton, startButton, backButton;

    SizeChooseSlider slider;

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

//        settingsButton = new JButton("Einstellungen");
//        settingsButton.addActionListener(new MainMenu.ButtonListener());
//        mainMenuPanel.add(settingsButton);
//        settingsButton.setBounds(75,400,200,50);

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

//        //Settings Panel
//        JPanel settingsPanel = new JPanel();
//        settingsPanel.setLayout(new BorderLayout());
//        settingsPanel.add(backButton, BorderLayout.PAGE_START);

        cardsPanel = new JPanel(cl);
        cardsPanel.add(mainMenuPanel, "mainMenu");
        cardsPanel.add(gameSettingsPanel, "gameSettingsPanel");
//        cardsPanel.add(settingsPanel, "settingsPanel");
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
                setTitle("Neuer Killer Löser");
                mode = Mode.KILLER_SOLVE;
                cl.show(cardsPanel, "gameSettingsPanel");
            }
            if (e.getSource() == solveStraitsButton) {
                setTitle("Neuer Str8ts Löser");
                mode = Mode.STRAITS_SOLVE;
                cl.show(cardsPanel, "gameSettingsPanel");
            }
//            if(e.getSource() == settingsButton){
//                setTitle("Einstellungen");
//                cl.show(cardsPanel, "settingsPanel");
//            }
            if (e.getSource() == backButton) {
                setTitle("Sudoku Hauptmen\u00fc");
                cl.first(cardsPanel);
            }
            if (e.getSource() == startButton) {
                startGame(slider.getValue());
            }
        }

        private void startGame(int size) {
            switch (mode) {
                case SUDOKU_SOLVE:
                    new presenter.SolveSudokuPresenter(size, theme);
                    break;
                case SUDOKU_PLAY:
                    new presenter.PlayPresenter(size, theme);
                    break;
                case KILLER_SOLVE:
                    new presenter.SolveKillerPresenter(size, theme);
                    break;
                case STRAITS_SOLVE:
                    new presenter.SolveStr8tsPresenter(size, theme);
                    break;
            }
            dispose();
        }
    }
}
