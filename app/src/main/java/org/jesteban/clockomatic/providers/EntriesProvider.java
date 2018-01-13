package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.State;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;


public class EntriesProvider implements EntriesProviderContract,Observer {
    private StateController state = null;
    private ObservableDispatcher<Listener> observable = new ObservableDispatcher<>();

    public EntriesProvider(StateController parent){
        state = parent;
        state.addObserver(this);
    }
    @Override
    public Boolean register(Entry date) {
        if (state.register(date)) {
            return true;
        } else return false;
    }

    @Override
    public Boolean remove(Entry date) {
        if (state.remove(date)){
            return true;
        }else return false;
    }

    @Override
    public EntrySet getEntries() {
        State st = state.getState();
        if (st!=null) return st.getEntries();
        return null;
    }

    @Override
    public EntrySet getEntriesBelongingDay(Calendar day) {
        DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_DAY);
        String filter = df.format(day.getTime());
        return getEntries().getEntriesBelongingDayStartWith(filter);
    }

    @Override
    public EntrySet getEntriesBelongingMonth(Calendar day) {
        DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
        String filter = df.format(day.getTime());
        return getEntries().getEntriesBelongingDayStartWith(filter);
    }

    @Override
    public void subscribe(Listener listener) {
        observable.add(listener);
    }


    @Override
    public void update(Observable o, Object arg) {

        observable.notify("onChangeEntries");
    }

    @Override
    public String getName() {
        return EntriesProviderContract.KEY_PROVIDER;
    }
}
