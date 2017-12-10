package org.jesteban.clockomatic.bindings;


import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.State;

import java.util.Observable;
import java.util.Observer;


public class EntriesProviderImpl  implements EntriesProvider,Observer {
    private StateController state = null;
    private ObservableDispatcher<Listener> observable = new ObservableDispatcher<>();

    public EntriesProviderImpl(StateController parent){
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
    public void subscribe(Listener listener) {
        observable.add(listener);
    }


    @Override
    public void update(Observable o, Object arg) {

        observable.notify("onChangeEntries");
    }

    @Override
    public String getName() {
        return EntriesProvider.KEY_PROVIDER;
    }
}
