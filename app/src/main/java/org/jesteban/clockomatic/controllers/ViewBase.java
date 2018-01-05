package org.jesteban.clockomatic.controllers;



public interface ViewBase<T extends PresenterBase> {
    void setPresenter(T presenter);
    T getPresenter();
}
