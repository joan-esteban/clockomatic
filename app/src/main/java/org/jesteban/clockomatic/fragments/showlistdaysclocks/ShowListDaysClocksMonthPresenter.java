package org.jesteban.clockomatic.fragments.showlistdaysclocks;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.PresenterBasicProviderEntriesReady;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class ShowListDaysClocksMonthPresenter extends PresenterBasicProviderEntriesReady<ShowListDaysClocksContract.View>
        implements ShowListDaysClocksContract.Presenter,
        SelectedMonthProvider.Listener {
    private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksMonthPresenter.class.getName());
    private SelectedMonthProvider selectedMonth = null;

    public ShowListDaysClocksMonthPresenter(@NonNull ShowListDaysClocksContract.View view) {
        super(view);
    }

    @Override
    public void onChangeSelectedMonth() {
        LOGGER.info("onChangeSelectedMonth");
        onChangeEntries();
    }

    @Override
    public void onChangeEntries() {
        EntrySet entries = this.entries.getEntriesBelongingMonth(selectedMonth.getSelectedMonth());
        List<DayDataToShow> showData = new ArrayList<>();
        Set<String> days = entries.getDistintBelongingDays();
        for (String day : days) {
            InfoDayEntry infoDayEntry = new InfoDayEntry(entries.getEntriesBelongingDayStartWith(day), day);
            DayDataToShow dayData = new DayDataToShow();
            dayData.dateName = day;
            dayData.entryText = new String[infoDayEntry.getPairsInfo().size()];
            Entry2Html aux = new Entry2Html();
            int idx = 0;
            for (InfoDayEntry.PairedEntry pair : infoDayEntry.getPairsInfo()) {
                dayData.entryText[idx] = aux.getJustHours(pair.starting) + " --> " + aux.getJustHours(pair.finish);
                idx++;
            }

            showData.add(dayData);
        }

        view.showEntries(showData);
    }

    // This is fill with DependencyInjectorBinding
    public void setSelectedMonthProvider(SelectedMonthProvider i) {
        selectedMonth = i;
        selectedMonth.subscribe(this);
    }


    @Override
    public void startUi() {
        onChangeEntries();
    }
}
