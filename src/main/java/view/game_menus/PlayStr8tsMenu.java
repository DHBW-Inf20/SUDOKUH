package view.game_menus;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayStr8tsMenu extends PlayMenu {

    private ArrayList<ArrayList<model.Str8ts.Color>> colors;

    public PlayStr8tsMenu(int size, ActionListener buttonListener, String title) {
        super(size, buttonListener, title);
        noteMode = false;
        colors = new ArrayList<>();
        for (int i = 0; i < size*size; i++) {
            ArrayList<model.Str8ts.Color> temp = new ArrayList<>();
            colors.add(temp);
        }
    }

    @Override
    // Definition of pre-defined colored elements -> cannot be changed
    public void setColor(int row, int col, model.Str8ts.Color color) {
        labels.get(row).get(col).setPredefined(true);
        colors.get(row).add(col,color);
        switch(color) {
            case BLACK -> {
                labels.get(row).get(col).setBackground(Color.black);
                labels.get(row).get(col).setForeground(Color.white);
            }
            case WHITE -> {
                labels.get(row).get(col).setBackground(Color.white);
            }
        }
    }
}
