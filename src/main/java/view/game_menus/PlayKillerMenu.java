package view.game_menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayKillerMenu extends PlayMenu{
    private final List<List<view.LabelPanel>> groups = new ArrayList<>();
    private final List<Color> colors = new ArrayList<>();
    private final List<Integer> sum = new ArrayList<>();

    public PlayKillerMenu(int size, ActionListener buttonListener, String title) {
        super(size, buttonListener, title);
        noteMode = false;

        // TODO get groups
        // groups = ...;

        // Add group-indication to pane
        JPanel groupsPanel = new JPanel();
        groupsPanel.setLayout(new GridLayout(groups.size(), 1));
        for (int i = 0; i <= groups.size(); i++) {
            //int sum = groups.get(i).sum();
            JLabel gr = new JLabel("Summe: "+sum);
            //panelLabels.add(gr);
            groupsPanel.add(gr);
        }
        this.addToPanel(groupsPanel, BorderLayout.EAST);
    }

    public void addCellToGroup(int row, int col, int groupIndex) {
        if(groups.get(groupIndex).isEmpty()) {
            ArrayList temp = new ArrayList();
            groups.set(groupIndex, temp);
            switch(groupIndex) {
                case 0: break;
            }
            //colors.set();
        }
        groups.get(groupIndex).add(labels.get(row).get(col));
    }
}
