package org.jesteban.clockomatic.mainactivity;

import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.EntriesProvider;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.providers.SelectedDayProvider;
import org.jesteban.clockomatic.providers.SelectedMonthProviderContract;
import org.jesteban.clockomatic.providers.SelectedMonthProvider;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.helpers.DependencyInjectorBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivityPresenter implements MainActivityContract.Presenter, SelectedDayProviderContract.Listener {
    private StateController stateController = new StateController();
    private EntriesProviderContract entriesProvider = new EntriesProvider(stateController);
    private SelectedDayProviderContract selectedDay = new SelectedDayProvider();
    private SelectedMonthProviderContract selectedMonth = new SelectedMonthProvider();
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
        //view.showRegisterPage();
    }
}
