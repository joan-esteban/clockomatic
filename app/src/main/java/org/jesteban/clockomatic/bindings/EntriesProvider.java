package org.jesteban.clockomatic.bindings;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

public interface EntriesProvider extends  Provider{
    static final String  KEY_PROVIDER="entries";

    Boolean register(Entry date);
    Boolean remove(Entry date);
    EntrySet getEntries();

    void subscribe(Listener listner);

    public interface Listener {
        void onChangeEntries();
    }
}
