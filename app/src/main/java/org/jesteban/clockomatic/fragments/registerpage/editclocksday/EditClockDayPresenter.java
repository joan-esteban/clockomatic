package org.jesteban.clockomatic.fragments.registerpage.editclocksday;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.bindings.SelectedDayProvider;
import org.jesteban.clockomatic.bindings.SelectedDayProviderImpl;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPageContract;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.List;

public class EditClockDayPresenter implements EditClockDayContract.Presenter,
    SelectedDayProvider.Listener,
        EntriesProvider.Listener{
    private PresenterBase  parent = null;
    private SelectedDayProvider selectedDay = null;
    private EntriesProvider entries = null;
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

    }

    @Override
    public boolean remove(Entry entry) {
        return false;
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
    public void setEntriesProvider(@NonNull EntriesProvider i){
        entries = i;
        entries.subscribe(this);
    }

    public void setSelectedDayProvider(@NonNull SelectedDayProvider i){
        selectedDay = i;
        selectedDay.subscribe(this);
    }
}
