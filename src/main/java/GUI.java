//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//
//public class GUI extends JFrame {
//    private ArrayList<ArrayList<CellLabel>> boxes;
//    private CellLabel clicked;
//    private Color backgroundColor;
//    private Color clickedColor;
//    private Color borderColor;
//
//    public GUI(int size) {
//        super("Sudoku");
//
//        boxes = new ArrayList<>();
//        backgroundColor = Color.white;
//        clickedColor = Color.decode("#dcedc9");
//        borderColor = Color.darkGray;
//
//        Container pane = getContentPane();
//        pane.setLayout(new BorderLayout());
//
//        // TODO Entweder bei oberer Lösung Linien hinzufügen oder bei unterer Lösung Koordinaten fixen
//        // Game overlay
//        JPanel outerPanel = new JPanel();
//        outerPanel.setLayout(new GridLayout(size,size));
//        for (int i = 0; i < size; i++) {
//            ArrayList<CellLabel> fields = new ArrayList<>();
//            for (int j = 0; j < size; j++) {
//                CellLabel field = new CellLabel(i+" "+j,i,j);
//                field.setOpaque(true);
//                field.setBackground(backgroundColor);
//                field.setBorder(new LineBorder(borderColor, 1));
//                field.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        if(clicked != null) clicked.setBackground(backgroundColor);
//                        clicked = field;
//                        field.setBackground(clickedColor);
//                        // TODO Logische Prüfung der Eingabe (Backend)
//                    }
//
//                });
//                fields.add(field);
//                outerPanel.add(field);
//            }
//            boxes.add(fields);
//        }
//        // TODO: "Einlesen" aus vorgegebenem/generierten Sudoku
//        boxes.get(1).get(1).setText("PRE");
//        boxes.get(1).get(1).setPredefined(true);
//
//
//        // Buttons for input
//        JPanel buttonsPanel = new JPanel();
//        buttonsPanel.setLayout(new GridLayout(4,3));
//        JButton button1 = new JButton("1");
//        JButton button2 = new JButton("2");
//        JButton button3 = new JButton("3");
//        JButton button4 = new JButton("4");
//        JButton button5 = new JButton("5");
//        JButton button6 = new JButton("6");
//        JButton button7 = new JButton("7");
//        JButton button8 = new JButton("8");
//        JButton button9 = new JButton("9");
//        JButton buttonDelete = new JButton("L\u00f6schen");
//        buttonsPanel.add(button9);
//        buttonsPanel.add(button8);
//        buttonsPanel.add(button7);
//        buttonsPanel.add(button6);
//        buttonsPanel.add(button5);
//        buttonsPanel.add(button4);
//        buttonsPanel.add(button3);
//        buttonsPanel.add(button2);
//        buttonsPanel.add(button1);
//        buttonsPanel.add(buttonDelete);
//        button1.addActionListener(new ButtonListener());
//        button2.addActionListener(new ButtonListener());
//        button3.addActionListener(new ButtonListener());
//        button4.addActionListener(new ButtonListener());
//        button5.addActionListener(new ButtonListener());
//        button6.addActionListener(new ButtonListener());
//        button7.addActionListener(new ButtonListener());
//        button8.addActionListener(new ButtonListener());
//        button9.addActionListener(new ButtonListener());
//        buttonDelete.addActionListener(new ButtonListener());
//
//        pane.add(outerPanel, BorderLayout.CENTER);
//        pane.add(buttonsPanel, BorderLayout.EAST);
//    }
//
//    private class ButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String input = e.getActionCommand();
//            if(clicked.isPredefined()) return;
//            if(input.equals("L\u00f6schen")) {
//                clicked.setText("");
//            } else {
//                clicked.setText(input);
//            }
//        }
//    }
//
//
//}
//
import javax.swing.*;
        import javax.swing.border.LineBorder;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.MouseAdapter;
        import java.awt.event.MouseEvent;
        import java.util.ArrayList;

public class GUI extends JFrame {
    private ArrayList<ArrayList<CellLabel>> boxes;
    private CellLabel clicked;
    private Color backgroundColor;
    private Color clickedColor;
    private Color borderColor;

    public GUI(int size) {
        super("Sudoku");

        boxes = new ArrayList<>();
        backgroundColor = Color.white;
        clickedColor = Color.decode("#dcedc9");
        borderColor = Color.darkGray;

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        // Game overlay
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout((int)Math.sqrt(size),(int)Math.sqrt(size)));
        for (int i = 0; i < size; i++) {
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new GridLayout((int)Math.sqrt(size),(int)Math.sqrt(size)));
            innerPanel.setBorder(new LineBorder(borderColor, 2));
            ArrayList<CellLabel> fields = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                CellLabel field = new CellLabel(i+" "+j,i,j);
                field.setOpaque(true);
                field.setBackground(backgroundColor);
                field.setBorder(new LineBorder(borderColor, 1));
                field.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(clicked != null) clicked.setBackground(backgroundColor);
                        clicked = field;
                        field.setBackground(clickedColor);
                        // TODO Prüfung der Eingabe (Backend)
                    }

                });
                fields.add(field);
                innerPanel.add(field);
//                if(colCounter >= Math.sqrt(size)-1) {
//                    colCounter = 0;
//                    if(rowCounter >= Math.sqrt(size)-1) {
//                        rowCounter = 0;
//                        fieldCounter++;
//                    } else {
//                        rowCounter++;
//                    }
//                } else {
//                    colCounter++;
//                }
            }
            boxes.add(fields);
            outerPanel.add(innerPanel);
        }
        // TODO: "Einlesen" aus vorgegebenem/generierten Sudoku
        boxes.get(1).get(1).setText("PRE");
        boxes.get(1).get(1).setPredefined(true);


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

        pane.add(outerPanel, BorderLayout.CENTER);
        pane.add(buttonsPanel, BorderLayout.EAST);
    }

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
