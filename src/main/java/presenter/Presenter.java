package presenter;

import view.ingame.CustomButton;
import view.ingame.InGameViewScaffold;

import java.awt.event.ActionEvent;

/**
 * @author Fabian Heinl
 */
public interface Presenter {

    void handleButtonListenerEvent(ActionEvent e);

    void handleButton(CustomButton button);

    InGameViewScaffold getInGameViewScaffold();

    boolean isNoteModeActivated();
}
