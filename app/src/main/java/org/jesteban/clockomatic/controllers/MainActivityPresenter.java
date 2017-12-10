package org.jesteban.clockomatic.controllers;

import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.EntriesProviderImpl;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.helpers.DependencyInjectorBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivityPresenter implements MainActivityContract.Presenter {
    private StateController stateController = new StateController();
    private EntriesProvider entriesProvider = new EntriesProviderImpl(stateController);
    @Override
    public List<Provider> getBindings() {
        List<Provider> list = new ArrayList<>();
        list.add(entriesProvider);
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {

    }

    @Override
    public void onAttachChild(PresenterBase child) {
        child.setParent(this);
        DependencyInjectorBinding dib =new DependencyInjectorBinding();
        dib.inject(child, getBindings());
    }

    @Override
    public void startUi() {

    }


    @Override
    public StateController getStateController(){
        return stateController;
    }

    @Override
    public void wipeStore() {
        stateController.wipeStore();
    }
}
