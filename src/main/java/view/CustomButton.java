package view;

import util.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {

    private final int value;
    private final Type type;
    Image delete, solve, tip, verify, pen, color;

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
        } catch (Exception ex) {
            System.out.println(ex);
        }
        this.value = value;
        this.type = type;
        switch (type) {
            case NUMBER -> setText(Integer.toString(value));
            case DELETE -> setIcon(new ImageIcon(delete));
            case SOLVE -> setIcon(new ImageIcon(solve));
            case TIP -> setIcon(new ImageIcon(tip));
            case VERIFY -> setIcon(new ImageIcon(verify));
            case PEN -> setIcon(new ImageIcon(pen));
            case CHANGECOLOR -> setIcon(new ImageIcon(color));
        }
        setFocusable(false);
        this.setMargin(new Insets(0,0,0,0));
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

}
