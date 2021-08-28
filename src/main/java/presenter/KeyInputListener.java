package presenter;

import view.ingame.CustomButton;
import view.ingame.CellPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static view.ingame.CustomButton.Type.*;

/**
 * @author Fabian Heinl
 */
public final class KeyInputListener implements KeyListener {

    private final Presenter presenter;

    private final boolean autoStep;

    public KeyInputListener(Presenter presenter, boolean autoStepForward) {
        this.presenter = presenter;
        autoStep = autoStepForward;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        //NUMBERS ROW
        if(e.getKeyCode()>=49 && e.getKeyCode()<=57){
            presenter.handleButton(new CustomButton(NUMBER, e.getKeyCode()-48));
            if(autoStep)autoStepForward();
            return;
        }
        //NUMBERS PAD
        if(e.getKeyCode()>=97 && e.getKeyCode()<=104){
            presenter.handleButton(new CustomButton(NUMBER, e.getKeyCode()-96));
            if(autoStep)autoStepForward();
            return;
        }
        switch (e.getKeyCode()) {
            case 8, 127 -> presenter.handleButton(new CustomButton(DELETE));
            case 84 -> presenter.handleButton(new CustomButton(TIP));
            case 78 -> presenter.handleButton(new CustomButton(NOTE));
            case 70 -> presenter.handleButton(new CustomButton(CHANGE_COLOR));
            case 69 -> presenter.handleButton(new CustomButton(VERIFY));
            case 10 -> presenter.handleButton(new CustomButton(SOLVE));
            case 71 -> presenter.handleButton(new CustomButton(CHOOSE_GROUP));
            case 76 -> presenter.handleButton(new CustomButton(REMOVE_GROUP));
            case 66 -> presenter.handleButton(new CustomButton(EDIT_GROUP));
        }

        if(e.getKeyCode()>=37 && e.getKeyCode()<=40){
           CellPanel lastClicked = presenter.getInGameViewScaffold().getClicked();
            switch (e.getKeyCode()){
                //LEFT ARROW BUTTON
                case 37 ->{
                    if(lastClicked.getCol()-1!=-1){
                        presenter.getInGameViewScaffold().setClicked(lastClicked.getRow(),lastClicked.getCol()-1);
                    }else{
                        presenter.getInGameViewScaffold().setClicked(lastClicked.getRow(),presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()-1);
                    }
                }
                //UP ARROW BUTTON
                case 38 ->{
                    if(lastClicked.getRow()-1!=-1){
                        presenter.getInGameViewScaffold().setClicked(lastClicked.getRow()-1,lastClicked.getCol());
                    }else{
                        presenter.getInGameViewScaffold().setClicked(presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()-1, lastClicked.getCol());
                    }
                }
                //RIGHT ARROW BUTTON
                case 39 ->{
                    if(lastClicked.getCol()+1!=presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()){
                        presenter.getInGameViewScaffold().setClicked(lastClicked.getRow(),lastClicked.getCol()+1);
                    }else{
                        presenter.getInGameViewScaffold().setClicked(lastClicked.getRow(),0);
                    }
                }
                //DOWN ARROW BUTTON
                case 40 ->{
                    if(lastClicked.getRow()+1!=presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()){
                        presenter.getInGameViewScaffold().setClicked(lastClicked.getRow()+1,lastClicked.getCol());
                    }else{
                        presenter.getInGameViewScaffold().setClicked(0,lastClicked.getCol());
                    }
                }
            }
        }
    }

    private void autoStepForward() {
        if(!presenter.getNoteMode()){
            CellPanel lastClicked = presenter.getInGameViewScaffold().getClicked();
            if(lastClicked.getCol()+1 != presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()){
                presenter.getInGameViewScaffold().setClicked(lastClicked.getRow(),lastClicked.getCol()+1);
            }else if(lastClicked.getRow()+1 < presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()){
                presenter.getInGameViewScaffold().setClicked(lastClicked.getRow()+1,0);
            }
        }
    }

    /**
        Not used KeyListener Methods
     */

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
