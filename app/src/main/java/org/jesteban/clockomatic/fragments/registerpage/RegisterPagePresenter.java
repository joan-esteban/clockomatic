package org.jesteban.clockomatic.fragments.registerpage;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.bindings.SelectedDayProvider;
import org.jesteban.clockomatic.bindings.SelectedDayProviderImpl;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;
import java.util.List;

public class RegisterPagePresenter implements RegisterPageContract.Presenter {
    private PresenterBase  parent = null;
    private SelectedDayProvider selectedDay = new SelectedDayProviderImpl();
    private EntriesProvider entries = null;
    private RegisterPageContract.View view = null;

    public RegisterPagePresenter(@NonNull RegisterPageContract.View view){
        this.view = view;
    }
    @Override
    public List<Provider> getBindings() {
        List<Provider> list = parent.getBindings();
        list.add(selectedDay);
        return list;
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

    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProvider i){
        entries = i;
        //entries.subscribe(this);
    }

}
