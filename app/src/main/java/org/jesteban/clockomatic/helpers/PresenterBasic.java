package org.jesteban.clockomatic.helpers;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.providers.Provider;

import java.util.List;

public abstract class PresenterBasic<VIEW_CLASS> implements PresenterBase {
    protected PresenterBase parent;
    protected VIEW_CLASS view = null;

    public PresenterBasic(@NonNull VIEW_CLASS view){
        this.view = view;
    }
    @Override
    public List<Provider> getBindings(){
        return parent.getBindings();
    }
    public List<Provider> getBindings(Provider...extraProdivers) {
        List<Provider> list = parent.getBindings();
        for (Provider p : extraProdivers)
            list.add(p);
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        // Nothing
    }

    @Override
    public void startUi() {

    }



}
