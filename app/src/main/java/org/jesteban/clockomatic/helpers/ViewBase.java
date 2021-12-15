package org.jesteban.clockomatic.helpers;


import org.jesteban.clockomatic.helpers.PresenterBase;

public interface ViewBase<T extends PresenterBase> {
    void setPresenter(T presenter);
    T getPresenter();
}
