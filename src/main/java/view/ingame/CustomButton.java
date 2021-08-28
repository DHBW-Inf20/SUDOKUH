package view.ingame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public final class CustomButton extends JButton {

    public enum Type {
        NUMBER, DELETE, SOLVE, TIP, VERIFY, PEN, CHANGE_COLOR, CHOOSE_GROUP, REMOVE_GROUP, EDIT_GROUP
    }

    private final int value;
    private final Type type;
    private Image delete, solve, tip, verify, pen, color, chooseGroup, removeGroup;

    public CustomButton(Type type) {
        this(type, -1);
    }

    public CustomButton(Type type, int value) {
        try {
            // Icons from https://www.freepik.com
            delete = ImageIO.read(requireNonNull(getClass().getResource("/delete.png")));
            solve = ImageIO.read(requireNonNull(getClass().getResource("/solve.png")));
            tip = ImageIO.read(requireNonNull(getClass().getResource("/tip.png")));
            verify = ImageIO.read(requireNonNull(getClass().getResource("/verify.png")));
            pen = ImageIO.read(requireNonNull(getClass().getResource("/pen.png")));
            color = ImageIO.read(requireNonNull(getClass().getResource("/color.png")));
            chooseGroup = ImageIO.read(requireNonNull(getClass().getResource("/choosegroup.png")));
            removeGroup = ImageIO.read(requireNonNull(getClass().getResource("/removegroup.png")));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
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
            case PEN, EDIT_GROUP -> {
                setIcon(new ImageIcon(pen));
                if (type == Type.PEN) setToolTipText("Stift-Funktion");
                if (type == Type.EDIT_GROUP) setToolTipText("Gruppe bearbeiten");
            }
            case CHANGE_COLOR -> {
                setIcon(new ImageIcon(color));
                setToolTipText("Farbe wechseln");
            }
            case CHOOSE_GROUP -> {
                setIcon(new ImageIcon(chooseGroup));
                setToolTipText("Gruppe ausw\u00e4hlen");
            }
            case REMOVE_GROUP -> {
                setIcon(new ImageIcon(removeGroup));
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
