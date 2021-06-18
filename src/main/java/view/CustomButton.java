package view;

import javax.swing.*;

public class CustomButton extends JButton {

    private final int value;
    private final Type type;

    public CustomButton(Type type) {
        this(-1, type);
    }

    public CustomButton(int value, Type type) {
        super(String.valueOf(value));
        this.value = value;
        this.type = type;
        switch (type) {
            case DELETE -> setText("Löschen");
            case SOLVE -> setText("Lösen");
        }
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        NUMBER, DELETE, SOLVE
    }
}
