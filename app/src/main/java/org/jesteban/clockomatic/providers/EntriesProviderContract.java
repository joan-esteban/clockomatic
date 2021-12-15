package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.UndoAction;

import java.text.ParseException;
import java.util.Calendar;

public interface EntriesProviderContract extends  Provider{
    static final String  KEY_PROVIDER="entries";

    UndoAction register(Entry date);
    UndoAction remove(Entry date) throws ParseException;
    UndoAction wipeStore() throws ParseException;

    EntrySet getEntriesBelongingDay(Calendar day) throws ParseException;
    EntrySet getEntriesBelongingMonth(Calendar day) throws ParseException;
    public EntrySet getEntriesBelongingDayStartWith(String prefixBelongingDay) throws ParseException;
    void setCompanyId(int companyId);

    void subscribe(Listener listner);

    public interface Listener {
        void onChangeEntries();
    }
}
