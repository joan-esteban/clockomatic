package org.jesteban.clockomatic.bindings;

import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.helpers.ObservableDispatcher;

import java.util.Calendar;


public class SelectedMonthProviderImpl implements SelectedMonthProvider {
    private ObservableDispatcher<Listener> observable = new ObservableDispatcher<>();
    private Calendar selectedMonth = Calendar.getInstance();
    @Override
    public boolean setSelectedMonth(Calendar date) {
        if (selectedMonth==date) return false;
        if (selectedMonth.equals(date)) return false;
        selectedMonth = date;
        observable.notify("onChangeSelectedMonth");
        return true;
    }

    @Override
    public Calendar getSelectedMonth() {
        return selectedMonth;
    }

    @Override
    public void subscribe(Listener listener) {
        observable.add(listener);
    }

    @Override
    public String getName() {
        return SelectedMonthProvider.KEY_PROVIDER;
    }
}
