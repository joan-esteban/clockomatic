package org.jesteban.clockomatic;

import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.State;
import org.jesteban.clockomatic.store.StoreOnFiles;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.logging.Logger;


public class StateController extends Observable {
    public interface InyectableStateController {
        public void setStateController(StateController stateController);
    }

    public interface EventsListener{
        void onChangeEntries(StateController owner);
    }

    private static Logger LOGGER = Logger.getLogger(StateController.class.getName());

    private State state = new State();
    private StoreOnFiles store = new StoreOnFiles();

    public Boolean save() {
        LOGGER.info("save ");
        if ((store == null) || (state == null)) return false;
        EntrySet entries = state.getEntries();
        try {
            if (entries != null) store.write(entries);
        } catch (Exception e) {
            LOGGER.warning("Error writing data! " + e);
            state.getStatus().setError("Error writting data " + e);
            return false;
        }
        return true;
    }

    public Boolean register(Entry date) {
        LOGGER.info("register " + date);
        Boolean result = state.addEntry(date);
        if (result) {
            result = save();
        }

        setChanged();
        notifyObservers();
        return result;
    }

    public Boolean remove(Entry date) {
        Boolean result = state.removeEntry(date);
        if (result) {
            result = save();
        }

        setChanged();
        notifyObservers();
        return result;
    }

    public void setFloatingEntry(Entry date) {
        LOGGER.info("setFloatingEntry " + date);
        state.setFloatingEntry(date);
        setChanged();
        notifyObservers();

    }

    public State getState() {
        LOGGER.info("getState ");
        if (state.getEntries() == null) {
            try {
                LOGGER.info("read ");
                EntrySet entries = store.read();
                state.setEntries(entries);
                setChanged();
                notifyObservers();
            } catch (FileNotFoundException e) {
                LOGGER.warning("Error getting entries from store : " + e);
                state.setEntries(new EntrySet());
                return state;
            } catch (Exception e) {
                LOGGER.warning("Error getting entries from store : " + e + " setting to invalid state");
                state.getStatus().setError("Loading data from store");
                state.setEntries(null);
                return null;
            }
        }
        return state;
    }

    public void wipeStore() {
        LOGGER.warning("Deleting all data! (WipeStore)");
        store.wipe();
        state.setEntries(null);
        setChanged();
        notifyObservers();
    }
}
