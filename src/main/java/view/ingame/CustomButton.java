package view.ingame;

import util.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public class CustomButton extends JButton {

    private final int value;
    private final Type type;
    Image delete, solve, tip, verify, pen, color, choosegroup, removegroup;

    public CustomButton(Type type) {
        this(-1, type);
    }

    public CustomButton(int value, Type type) {
        try {
            //Icons from https://www.freepik.com
            delete = ImageIO.read(getClass().getResource("/delete.png"));
            solve = ImageIO.read(getClass().getResource("/solve.png"));
            tip = ImageIO.read(getClass().getResource("/tip.png"));
            verify = ImageIO.read(getClass().getResource("/verify.png"));
            pen = ImageIO.read(getClass().getResource("/pen.png"));
            color = ImageIO.read(getClass().getResource("/color.png"));
            choosegroup = ImageIO.read(getClass().getResource("/choosegroup.png"));
            removegroup = ImageIO.read(getClass().getResource("/removegroup.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.value = value;
        this.type = type;
        switch (type) {
            case NUMBER -> {
                setText(Integer.toString(value));
                setToolTipText(value + " setzen");
            }
            case DELETE -> {
                setIcon(new ImageIcon(delete));
                setToolTipText("L\u00f6schen");
            }
            case SOLVE -> {
                setIcon(new ImageIcon(solve));
                setToolTipText("L\u00f6sung suchen");
            }
            case TIP -> {
                setIcon(new ImageIcon(tip));
                setToolTipText("Tipp anzeigen");
            }
            case VERIFY -> {
                setIcon(new ImageIcon(verify));
                setToolTipText("L\u00f6sung \u00fcberpr\u00fcfen");
            }
            case PEN, EDITGROUP -> {
                setIcon(new ImageIcon(pen));
                if (type == Type.PEN) setToolTipText("Stift-Funktion");
                if (type == Type.EDITGROUP) setToolTipText("Gruppe bearbeiten");
            }
            case CHANGECOLOR -> {
                setIcon(new ImageIcon(color));
                setToolTipText("Farbe wechseln");
            }
            case CHOOSEGROUP -> {
                setIcon(new ImageIcon(choosegroup));
                setToolTipText("Gruppe ausw\u00e4hlen");
            }
            case REMOVEGROUP -> {
                setIcon(new ImageIcon(removegroup));
                setToolTipText("Gruppe l\u00f6schen");
            }
        }
        setFocusable(false);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setFont(new Font(getFont().getName(), Font.BOLD, 40));
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

}
