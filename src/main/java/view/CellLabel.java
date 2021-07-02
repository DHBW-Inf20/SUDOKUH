package view;

import javax.swing.*;
import java.awt.*;

public class CellLabel extends JLabel {
    public CellLabel(String text) {
        super(text);
        setFont(new Font(getFont().getName(), Font.BOLD, 25));
    }
}
