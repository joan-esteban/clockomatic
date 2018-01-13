package org.jesteban.clockomatic.mainactivity;

import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;


public class MainActivityContract {
    public interface View extends ViewBase {
        void showMessage(String text);
        void showRegisterPage();
    }

    public interface Presenter extends PresenterBase {
        StateController getStateController();
        void wipeStore();
    }
}
