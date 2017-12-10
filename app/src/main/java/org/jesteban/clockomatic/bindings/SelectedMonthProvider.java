package org.jesteban.clockomatic.bindings;

import java.util.Calendar;

public interface SelectedMonthProvider extends Provider{
    public static final String KEY_PROVIDER ="selectedMonth";

    public boolean setSelectedMonth(Calendar date);
    public Calendar getSelectedMonth();

    void subscribe(Listener listner);

    public interface Listener {
        void onChangeSelectedMonth();
    }
}
