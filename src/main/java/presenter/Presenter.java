package presenter;

import view.CustomButton;

import java.awt.event.ActionEvent;

public interface Presenter {

    void handleButtonListenerEvent(ActionEvent e);
    void handleButton(CustomButton button);
}
