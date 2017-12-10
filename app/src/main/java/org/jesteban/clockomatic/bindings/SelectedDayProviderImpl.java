package org.jesteban.clockomatic.bindings;

import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;



public class SelectedDayProviderImpl implements  SelectedDayProvider{
    private ObservableDispatcher<SelectedDayProvider.Listener> observable = new ObservableDispatcher<>();
    private Calendar selectedDay = Calendar.getInstance();
    @Override
    public boolean setSelecteDay(Calendar date) {
        selectedDay = date;
        observable.notify("onChangeSelectedDay");
        return true;
    }

    @Override
    public Calendar getSelectedDay() {
        return selectedDay;
    }

    @Override
    public Entry getFloatingEntry() {
        return new Entry(selectedDay);
    }

    @Override
    public void subscribe(SelectedDayProvider.Listener listener)  {
        observable.add(listener);
    }

    @Override
    public String getName() {
        return SelectedDayProvider.KEY_PROVIDER;
    }
}
