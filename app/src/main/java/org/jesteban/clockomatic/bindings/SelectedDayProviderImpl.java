package org.jesteban.clockomatic.bindings;

import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class SelectedDayProviderImpl implements  SelectedDayProvider{
    private ObservableDispatcher<SelectedDayProvider.Listener> observable = new ObservableDispatcher<>();
    private Calendar selectedDay = Calendar.getInstance();

    private String showBelongingDayPrefix = null;
    private String showBelongingMonthPrefix = null;
    @Override
    public boolean setSelecteDay(Calendar date) {
        selectedDay = date;
        showBelongingMonthPrefix = null;
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
    public String getFilterBelongingForDay() {
        if (showBelongingDayPrefix==null) {
            DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_DAY);
            showBelongingDayPrefix = df.format(selectedDay.getTime());
        };
        return showBelongingDayPrefix;
    }

    @Override
    public String getFilterBelongingForMonth() {
        if (showBelongingMonthPrefix==null) {
            DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
            showBelongingMonthPrefix = df.format(selectedDay.getTime());
        };
        return showBelongingMonthPrefix;
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
