package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.Calendar;

public interface EntriesProviderContract extends  Provider{
    static final String  KEY_PROVIDER="entries";

    Boolean register(Entry date);
    Boolean remove(Entry date);
    EntrySet getEntries();
    EntrySet getEntriesBelongingDay(Calendar day);
    EntrySet getEntriesBelongingMonth(Calendar day);


    void subscribe(Listener listner);

    public interface Listener {
        void onChangeEntries();
    }
}
