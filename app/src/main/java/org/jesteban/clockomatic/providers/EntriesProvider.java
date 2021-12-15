package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.EntriesDb;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.UndoAction;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;


public class EntriesProvider implements EntriesProviderContract,Observer {
    private static final Logger LOGGER = Logger.getLogger(EntriesProvider.class.getName());

    private EntriesDb entriesDb = null;
    private ObservableDispatcher<Listener> observable = new ObservableDispatcher<>();
    int companyId=0;
    public EntriesProvider(){

    }
    public EntriesProvider(EntriesDb entriesDb){
        this.entriesDb=entriesDb;
        entriesDb.addObserver(this);
    }



    @Override
    public UndoAction register(Entry date) {
        return entriesDb.register(companyId,date);
    }

    @Override
    public UndoAction remove(Entry date) throws ParseException {
        return entriesDb.remove(companyId,date);
    }

    @Override
    public UndoAction wipeStore() throws ParseException {
        return entriesDb.wipeStore(companyId);
    }


    @Override
    public EntrySet getEntriesBelongingDay(Calendar day) throws ParseException {
        return entriesDb.getEntriesBelongingDay(companyId,day);
    }

    @Override
    public EntrySet getEntriesBelongingMonth(Calendar day) throws ParseException {
        return entriesDb.getEntriesBelongingMonth(companyId,day);
    }
    @Override
    public EntrySet getEntriesBelongingDayStartWith(String prefixBelongingDay) throws ParseException{
        return entriesDb.getEntriesBelongingDayStartWith(companyId,prefixBelongingDay);
    }

    @Override
    public void setCompanyId(int companyId) {
        if (this.companyId == companyId) return;
        this.companyId = companyId;
        notifyChange();
    }

    @Override
    public void subscribe(Listener listener) {
        observable.add(listener);
    }


    @Override
    public void update(Observable o, Object arg) {
        LOGGER.info("EntriesDb send a update!");
        notifyChange();
    }

    @Override
    public String getName() {
        return EntriesProviderContract.KEY_PROVIDER;
    }

    private void notifyChange(){
        LOGGER.info("EntriesProvider send a update!");
        observable.notify("onChangeEntries");}

}
