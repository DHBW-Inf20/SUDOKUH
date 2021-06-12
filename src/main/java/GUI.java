import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUI extends JFrame {
    private ArrayList<ArrayList<CellLabel>> labels;
    private CellLabel clicked;
    private Color backgroundColor;
    private Color clickedColor;
    private Color borderColor;

    public GUI(int size) {
        super("Sudoku");

        backgroundColor = Color.white;
        clickedColor = Color.decode("#dcedc9");
        borderColor = Color.darkGray;

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        // Game overlay
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout((int)Math.sqrt(size),(int)Math.sqrt(size)));
        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        labels = new ArrayList<>();
        for (int i = 0; i < size+100; i++) {
            ArrayList<CellLabel> temp = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                CellLabel field = new CellLabel(" ",i,j);
                field.setOpaque(true);
                field.setBackground(backgroundColor);
                field.setBorder(new LineBorder(borderColor, 1));
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setVerticalAlignment(SwingConstants.CENTER);
                field.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(clicked != null) clicked.setBackground(backgroundColor);
                        clicked = field;
                        field.setBackground(clickedColor);
                    }
                });
                temp.add(field);
            }
            labels.add(temp);
        }

        // Creating matrix of panels ('panels') -> Creation in advance in order to get the right coordinates
        ArrayList<ArrayList<JPanel>> panels = new ArrayList<>();
        for (int i = 0; i < Math.sqrt(size); i++) {
            ArrayList<JPanel> temp = new ArrayList<>();
            for (int j = 0; j < Math.sqrt(size); j++) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout((int) Math.sqrt(size), (int) Math.sqrt(size)));
                panel.setBorder(new LineBorder(borderColor, 2));
                temp.add(panel);
            }
            panels.add(temp);
        }

        // Filling the panels-matrix with values from the labels-matrix
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CellLabel field = labels.get(row).get(col);
                panels.get(row/(int)Math.sqrt(size)).get(col/(int)Math.sqrt(size)).add(field);
            }
        }

        // Filling the outerPanel-matrix (game overlay) with values from the panels-matrix
        for (ArrayList<JPanel> panel : panels) {
            for (JPanel jPanel : panel) {
                outerPanel.add(jPanel);
            }
        }

        // Buttons for input
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4,3));
        JButton button1 = new JButton("1");
        JButton button2 = new JButton("2");
        JButton button3 = new JButton("3");
        JButton button4 = new JButton("4");
        JButton button5 = new JButton("5");
        JButton button6 = new JButton("6");
        JButton button7 = new JButton("7");
        JButton button8 = new JButton("8");
        JButton button9 = new JButton("9");
        JButton buttonDelete = new JButton("L\u00f6schen");
        buttonsPanel.add(button9);
        buttonsPanel.add(button8);
        buttonsPanel.add(button7);
        buttonsPanel.add(button6);
        buttonsPanel.add(button5);
        buttonsPanel.add(button4);
        buttonsPanel.add(button3);
        buttonsPanel.add(button2);
        buttonsPanel.add(button1);
        buttonsPanel.add(buttonDelete);
        button1.addActionListener(new ButtonListener());
        button2.addActionListener(new ButtonListener());
        button3.addActionListener(new ButtonListener());
        button4.addActionListener(new ButtonListener());
        button5.addActionListener(new ButtonListener());
        button6.addActionListener(new ButtonListener());
        button7.addActionListener(new ButtonListener());
        button8.addActionListener(new ButtonListener());
        button9.addActionListener(new ButtonListener());
        buttonDelete.addActionListener(new ButtonListener());

        // Adding game overlay and buttons to the panel
        pane.add(outerPanel, BorderLayout.CENTER);
        pane.add(buttonsPanel, BorderLayout.EAST);

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = labels.get(0).get(0);
        clicked.setBackground(clickedColor);
    }

    // Definition of pre-defined elements -> cannot be changed
    public void definePredfined(int row, int col, int value) {
        // TODO: "Einlesen" aus vorgegebenem/generierten Sudoku -> Methodenaufruf aus Backend
        labels.get(row).get(col).setText(value+"");
        labels.get(row).get(col).setPredefined(true);
    }

    // Button-Listener for numbers-buttons to provide a correct input
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = e.getActionCommand();
            if(clicked.isPredefined()) return;
            if(input.equals("L\u00f6schen")) {
                clicked.setText("");
            } else {
                clicked.setText(input);
            }
        }
    }


}
