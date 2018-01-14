package org.jesteban.clockomatic.fragments.editclocksday;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.List;

public class EditClockDayPresenter implements EditClockDayContract.Presenter,
    SelectedDayProviderContract.Listener,
        EntriesProviderContract.Listener{
    private PresenterBase  parent = null;
    private SelectedDayProviderContract selectedDay = null;
    private EntriesProviderContract entries = null;
    private EditClockDayContract.View view = null;

    public EditClockDayPresenter(EditClockDayContract.View view){
        this.view = view;
    }
    @Override
    public List<Provider> getBindings() {
        return parent.getBindings();
    }

    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        // Nothing
    }

    @Override
    public void startUi() {
        // Nothing to do
    }

    @Override
    public boolean selectedEntry(Entry entry) {
        return entries.remove(entry);
    }

    @Override
    public void onChangeSelectedDay() {
        syncWithView();
    }

    @Override
    public void onChangeEntries() {
        syncWithView();
    }

    private void syncWithView(){
        Entry floatingEntry = selectedDay.getFloatingEntry();
        EntrySet entriesDay = entries.getEntries().getEntriesBelongingDayStartWith(floatingEntry.getBelongingDay());
        view.showEntries(entriesDay);
    }

    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProviderContract i){
        entries = i;
        entries.subscribe(this);
    }

    public void setSelectedDayProvider(@NonNull SelectedDayProviderContract i){
        selectedDay = i;
        selectedDay.subscribe(this);
    }
}
