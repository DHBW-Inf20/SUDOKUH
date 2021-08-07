package view;

import util.Type;

import javax.swing.*;

public class CustomButton extends JButton {

    private final int value;
    private final Type type;

    public CustomButton(Type type) {
        this(-1, type);
    }

    public CustomButton(int value, Type type) {
        this.value = value;
        this.type = type;
        switch (type) {
            case NUMBER -> setText(Integer.toString(value));
            case DELETE -> setText("Löschen");
            case SOLVE -> setText("Lösen");
            case TIP -> setText("Tipp anzeigen");
            case VERIFY -> setText("Ergebnis überprüfen");
            case PEN -> setText("Notiz");
            case CHANGECOLOR -> setText("Farbe wechseln");
        }
        setFocusable(false);
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

}

