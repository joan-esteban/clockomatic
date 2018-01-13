package org.jesteban.clockomatic.providers;

import java.util.Calendar;

public interface SelectedMonthProviderContract extends Provider{
    public static final String KEY_PROVIDER ="selectedMonth";

    public boolean setSelectedMonth(Calendar date);
    public Calendar getSelectedMonth();

    void subscribe(Listener listner);

    public interface Listener {
        void onChangeSelectedMonth();
    }
}
