package org.jesteban.clockomatic.controllers;


import org.jesteban.clockomatic.bindings.Provider;

import java.util.List;


public interface PresenterBase {
    List<Provider> getBindings();

    /**
     * It just say who is my parent, no action need (keep Object if you need)
     * @param parent
     */
    void setParent(PresenterBase parent);

    /**
     * Parent inject dependencies to child
     * @param child
     */
    void onAttachChild(PresenterBase child);

    /**
     * UI have been started and need instructions!
     */
    void startUi();

}
