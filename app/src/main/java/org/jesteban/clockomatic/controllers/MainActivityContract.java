package org.jesteban.clockomatic.controllers;

import org.jesteban.clockomatic.StateController;


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
