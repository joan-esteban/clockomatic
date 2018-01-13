package org.jesteban.clockomatic.fragments.registerpage;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterPagePresenter implements RegisterPageContract.Presenter, SelectedDayProviderContract.Listener {
    private static final Logger LOGGER = Logger.getLogger(RegisterPagePresenter.class.getName());
    private PresenterBase  parent = null;
    private SelectedDayProviderContract selectedDay = null;
    private EntriesProviderContract entries = null;
    private RegisterPageContract.View view = null;
    private Date lastForceSetDay = new Date();
    private String currentFilterBelongingForDay = "";
    private final int minimumTimeToForceDayInSeconds = 10;
    // https://stackoverflow.com/questions/1877417/how-to-set-a-timer-in-android


    public RegisterPagePresenter(@NonNull RegisterPageContract.View view){
        this.view = view;
    }
    @Override
    public List<Provider> getBindings() {
        List<Provider> list = parent.getBindings();
        return list;
    }
    // This is fill with DependencyInjectorBinding
    public void setSelectedDayProvider(SelectedDayProviderContract i) {
        selectedDay = i;
        selectedDay.subscribe(this);
    }
    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {

    }

    @Override
    public void startUi() {

    }

    @Override
    public boolean register(Entry entry) {
        return entries.register(entry);
    }

    @Override
    public void selected(Calendar date) {
        selectedDay.setSelecteDay(date);
    }

    @Override
    public boolean remove(Entry entry) {
        return false;
    }

    @Override
    public void onResume() {
        LOGGER.log(Level.INFO, "onResume setting current date");
        Date now = new Date();
        if ( (now.getTime() - lastForceSetDay.getTime()) > minimumTimeToForceDayInSeconds ) {
            LOGGER.log(Level.INFO, "onResume due a timeout I'm setting current date");
            Calendar cal = Calendar.getInstance();
            selectedDay.setSelecteDay(cal);
            view.showDate(cal);
        }
    }

    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProviderContract i){
        entries = i;
        //entries.subscribe(this);
    }


    @Override
    public void onChangeSelectedDay() {
        if (currentFilterBelongingForDay.equals(selectedDay.getFilterBelongingForDay())) return;
        currentFilterBelongingForDay = selectedDay.getFilterBelongingForDay();
        view.showDate(selectedDay.getSelectedDay());
    }
}
