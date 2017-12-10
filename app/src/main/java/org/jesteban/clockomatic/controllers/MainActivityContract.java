package org.jesteban.clockomatic.controllers;

import org.jesteban.clockomatic.StateController;


public class MainActivityContract {
    public interface View extends ViewBase {

    }

    public interface Presenter extends PresenterBase {
        StateController getStateController();
        void wipeStore();
    }
}
