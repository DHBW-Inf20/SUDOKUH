package view.main_menu;

import presenter.GUIPresenter;
import view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    CardLayout cl = new CardLayout();
    JPanel cardsPanel;

    Mode mode;

    JButton solveModeButton, playModeButton, settingsButton, startButton, backButton;

    SizeChooseSlider slider;

    public MainMenu(){
        super("Sudoku Hauptmen\u00fc");

        setSize(new Dimension(350,700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        //Main Menu Panel
        JPanel mainMenuPanel = new JPanel();
        setBackground(Color.RED);
        mainMenuPanel.setLayout(null);
        solveModeButton = new JButton("Sudoku Löser");
        solveModeButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(solveModeButton);
        solveModeButton.setBounds(75,200,200,50);

        playModeButton = new JButton("Sudoku Spiel");
        playModeButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(playModeButton);
        playModeButton.setBounds(75,300,200,50);

//        settingsButton = new JButton("Einstellungen");
//        settingsButton.addActionListener(new MainMenu.ButtonListener());
//        mainMenuPanel.add(settingsButton);
//        settingsButton.setBounds(75,400,200,50);

        //Back Button
        backButton = new JButton("Zurück");
        backButton.addActionListener(new MainMenu.ButtonListener());
        mainMenuPanel.add(backButton);
        backButton.setBounds(75, 100, 200,50);

        //Solve and Play Mode Panel
        JPanel modesPanel = new JPanel();
        modesPanel.setLayout(null);
        slider = new SizeChooseSlider();
        modesPanel.add(slider, BorderLayout.CENTER);
        startButton = new JButton("Start");
        startButton.addActionListener(new MainMenu.ButtonListener());
        startButton.setBounds(100, 450, 150,50);
        modesPanel.add(startButton, BorderLayout.SOUTH);
        modesPanel.add(backButton, BorderLayout.PAGE_START);

//        //Settings Panel
//        JPanel settingsPanel = new JPanel();
//        settingsPanel.setLayout(new BorderLayout());
//        settingsPanel.add(backButton, BorderLayout.PAGE_START);

        cardsPanel = new JPanel(cl);
        cardsPanel.add(mainMenuPanel, "mainMenu");
        cardsPanel.add(modesPanel, "modesPanel");
//        cardsPanel.add(settingsPanel, "settingsPanel");
        cl.first(cardsPanel);
        add(cardsPanel,BorderLayout.CENTER);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == solveModeButton){
                setTitle("Neuer Sudoku Löser");
                mode = Mode.SOLVE_MODE;
                cl.show(cardsPanel, "modesPanel");
            }
            if(e.getSource() == playModeButton){
                setTitle("Neues Sudoku Spiel");
                mode = Mode.PLAY_MODE;
                cl.show(cardsPanel, "modesPanel");
            }
//            if(e.getSource() == settingsButton){
//                setTitle("Einstellungen");
//                cl.show(cardsPanel, "settingsPanel");
//            }
            if(e.getSource() == backButton){
                setTitle("Sudoku Hauptmen\u00fc");
                cl.first(cardsPanel);
            }
            if(e.getSource() == startButton){
                if(mode == Mode.SOLVE_MODE){
                    startSolveMode(slider.getValue());
                } else{
                    // START PLAY MODE
                }
            }
        }
    }

    public void startSolveMode(int size){
        new GUIPresenter(size);
        dispose();
    }
}

enum Mode{
    SOLVE_MODE, PLAY_MODE
}
