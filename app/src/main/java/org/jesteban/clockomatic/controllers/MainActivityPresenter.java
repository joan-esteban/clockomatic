package org.jesteban.clockomatic.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.EntriesProviderImpl;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.bindings.SelectedDayProvider;
import org.jesteban.clockomatic.bindings.SelectedDayProviderImpl;
import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.bindings.SelectedMonthProviderImpl;
import org.jesteban.clockomatic.helpers.DependencyInjectorBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivityPresenter implements MainActivityContract.Presenter, SelectedDayProvider.Listener {
    private StateController stateController = new StateController();
    private EntriesProvider entriesProvider = new EntriesProviderImpl(stateController);
    private SelectedDayProvider selectedDay = new SelectedDayProviderImpl();
    private SelectedMonthProvider selectedMonth = new SelectedMonthProviderImpl();
    private MainActivityContract.View view = null;
    private Context context = null;
    public MainActivityPresenter(@NonNull MainActivityContract.View view, Context context){
        this.view = view;
        this.context = context;
        selectedDay.subscribe(this);
    }

    @Override
    public List<Provider> getBindings() {
        List<Provider> list = new ArrayList<>();
        list.add(entriesProvider);
        list.add(selectedDay);
        list.add(selectedMonth);
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

    @Override
    public void onChangeSelectedDay() {
        view.showMessage("onChangeSelectedDay " + selectedDay.getFilterBelongingForDay());
        view.showRegisterPage();
    }
}
