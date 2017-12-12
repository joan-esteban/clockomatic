package org.jesteban.clockomatic.bindings;

import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;


public interface SelectedDayProvider extends Provider{
    public static final String KEY_PROVIDER ="selectedDay";

    public boolean setSelecteDay(Calendar date);
    public Calendar getSelectedDay();
    public Entry getFloatingEntry();

    public String getFilterBelongingForDay();
    public String getFilterBelongingForMonth();


    void subscribe(SelectedDayProvider.Listener listner);

    public interface Listener {
        void onChangeSelectedDay();
    }
}
