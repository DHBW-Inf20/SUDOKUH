package view.ingame;

import util.Strings;

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
        // Icons from https://www.freepik.com
        NUMBER(null, null),
        DELETE("/delete.png", Strings.DELETE),
        SOLVE("/solve.png", Strings.SEARCH_SOLUTION),
        TIP("/tip.png", Strings.SHOW_TIP),
        VERIFY("/verify.png", Strings.VERIFY_SOLUTION),
        NOTE("/pen.png", Strings.NOTE_MODE),
        CHANGE_COLOR("/color.png", Strings.CHANGE_COLOR),
        CHOOSE_GROUP("/choosegroup.png", Strings.CHOOSE_GROUP),
        REMOVE_GROUP("/removegroup.png", Strings.DELETE_GROUP),
        EDIT_GROUP("/pen.png", Strings.EDIT_GROUP);

        private final String icon;
        private final String toolTip;

        Type(String icon, String toolTip) {
            this.icon = icon;
            this.toolTip = toolTip;
        }
    }


    private final int value;
    private final Type type;


    public CustomButton(Type type) {
        this(type, -1);
    }

    public CustomButton(Type type, int value) {
        this.value = value;
        this.type = type;

        if (type == Type.NUMBER) {
            setText(Integer.toString(value));
            setToolTipText(Strings.isGerman ? value + " setzen" : "set " + value);
        } else {
            try {
                Image icon = ImageIO.read(requireNonNull(getClass().getResource(type.icon)));
                setIcon(new ImageIcon(icon));
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
            setToolTipText(type.toolTip);
        }
        setFocusable(false);
        setMargin(new Insets(0, 0, 0, 0));
        setFont(new Font(getFont().getName(), Font.BOLD, 25));
    }


    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }
}
