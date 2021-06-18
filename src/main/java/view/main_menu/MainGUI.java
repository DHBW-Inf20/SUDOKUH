package view.main_menu;

import view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class MainGUI extends JFrame {
    private GUI gameGui;
    private JSlider sizeChooseSlider;

    public MainGUI() {
        super("Sudoku Hauptmen\u00fc");

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        sizeChooseSlider = new JSlider( 2, 5, 3 );
        sizeChooseSlider.setPaintTicks(true);
        sizeChooseSlider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(2, new JLabel(4+""));
        labels.put(3, new JLabel(9+""));
        labels.put(4, new JLabel(16+""));
        labels.put(5, new JLabel(25+""));
        sizeChooseSlider.setLabelTable(labels);
        pane.add(sizeChooseSlider, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ButtonListener());
        pane.add(startButton, BorderLayout.SOUTH);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startGame(sizeChooseSlider.getValue()*sizeChooseSlider.getValue());
        }
    }

    public void startGame(int size) {
        gameGui = new GUI(size);
        gameGui.setSize(800,600);
        gameGui.setVisible(true);
        gameGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(false);
        // DEBUG - Aufruf erfolgt aus Backend
        gameGui.setPredefined(0,0,4);
        gameGui.setPredefined(2,3,9);
    }

    public GUI getGameGui() {
        return gameGui;
    }
}
