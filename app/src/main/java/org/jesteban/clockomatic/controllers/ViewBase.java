package org.jesteban.clockomatic.controllers;

/**
 * Created by zodiac on 10/12/2017.
 */

public interface ViewBase<T extends PresenterBase> {
    void setPresenter(T presenter);
}
