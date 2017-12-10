package org.jesteban.clockomatic.helpers;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;

import java.util.logging.Logger;

public class SewingBox {
    private static final Logger LOGGER = Logger.getLogger(SewingBox.class.getName());

    public static void sewPresenters(PresenterBase newPresenter, PresenterBase parent){

        DependencyInjectorBinding dib =new DependencyInjectorBinding();
        dib.inject(newPresenter, parent.getBindings());
        parent.onAttachChild(newPresenter);
        newPresenter.setParent(parent);
    }
    public static void sewPresentersView(PresenterBase newPresenter, PresenterBase parent, ViewBase view){
        if (newPresenter==null) throw new RuntimeException("newPresenter is null");
        if (parent==null) throw new RuntimeException("parent is null");
        if  (view==null) throw new RuntimeException("view is null");
        LOGGER.info("sewPresentersView parent = " + parent);
        sewPresenters(newPresenter,parent);
        view.setPresenter(newPresenter);
    }
}
