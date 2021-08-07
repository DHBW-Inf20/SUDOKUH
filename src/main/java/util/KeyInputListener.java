package util;

import presenter.Presenter;
import view.CustomButton;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputListener implements KeyListener {

    private final Presenter presenter;

    public KeyInputListener(Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        //NUMBERS ROW
        if(e.getKeyCode()>=48 && e.getKeyCode()<=57){
            presenter.handleButton(new CustomButton(e.getKeyCode()-48, Type.NUMBER));
            return;
        }
        //NUMBERS PAD
        if(e.getKeyCode()>=96 && e.getKeyCode()<=105){
            presenter.handleButton(new CustomButton(e.getKeyCode()-96, Type.NUMBER));
            return;
        }
        switch (e.getKeyCode()) {
            case 8, 127 -> presenter.handleButton(new CustomButton(Type.DELETE));
            case 84 -> presenter.handleButton(new CustomButton(Type.TIP));
            case 78 -> presenter.handleButton(new CustomButton(Type.PEN));
            case 69 -> presenter.handleButton(new CustomButton(Type.VERIFY));
            case 10 -> presenter.handleButton(new CustomButton(Type.SOLVE));
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
