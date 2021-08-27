package util;

import presenter.Presenter;
import view.CustomButton;
import view.LabelPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputListener implements KeyListener {

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
            presenter.handleButton(new CustomButton(e.getKeyCode()-48, Type.NUMBER));
            if(autoStep)autoStepForward();
            return;
        }
        //NUMBERS PAD
        if(e.getKeyCode()>=97 && e.getKeyCode()<=104){
            presenter.handleButton(new CustomButton(e.getKeyCode()-96, Type.NUMBER));
            if(autoStep)autoStepForward();
            return;
        }
        switch (e.getKeyCode()) {
            case 8, 127 -> presenter.handleButton(new CustomButton(Type.DELETE));
            case 84 -> presenter.handleButton(new CustomButton(Type.TIP));
            case 78 -> presenter.handleButton(new CustomButton(Type.PEN));
            case 70 -> presenter.handleButton(new CustomButton(Type.CHANGECOLOR));
            case 69 -> presenter.handleButton(new CustomButton(Type.VERIFY));
            case 10 -> presenter.handleButton(new CustomButton(Type.SOLVE));
            case 71 -> presenter.handleButton(new CustomButton(Type.CHOOSEGROUP));
            case 76 -> presenter.handleButton(new CustomButton(Type.REMOVEGROUP));
            case 66 -> presenter.handleButton(new CustomButton(Type.EDITGROUP));
        }

        if(e.getKeyCode()>=37 && e.getKeyCode()<=40){
           LabelPanel lastClicked = presenter.getInGameViewScaffold().getClicked();
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
            LabelPanel lastClicked = presenter.getInGameViewScaffold().getClicked();
            if(lastClicked.getCol()+1 != presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()){
                System.out.println("a");
                presenter.getInGameViewScaffold().setClicked(lastClicked.getRow(),lastClicked.getCol()+1);
            }else if(lastClicked.getRow()+1 < presenter.getInGameViewScaffold().getGridSize()*presenter.getInGameViewScaffold().getGridSize()){
                System.out.println("b");
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
