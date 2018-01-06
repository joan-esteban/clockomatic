package org.jesteban.clockomatic.fragments.showlistdaysclocks;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.SelectedDayProvider;
import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.PresenterBasicProviderEntriesReady;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.views.InfoDayViewContract;
import org.jesteban.clockomatic.views.InfoDayViewPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class ShowListDaysClocksMonthPresenter extends PresenterBasicProviderEntriesReady<ShowListDaysClocksContract.View>
        implements ShowListDaysClocksContract.Presenter,
        SelectedMonthProvider.Listener {
    private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksMonthPresenter.class.getName());
    private SelectedMonthProvider selectedMonth = null;
    private SelectedDayProvider selectedDay = null;
    private InfoDayViewPresenter infoDayViewPresenter = null;

    public ShowListDaysClocksMonthPresenter(@NonNull ShowListDaysClocksContract.View view, Context context) {
        super(view);
        infoDayViewPresenter = new InfoDayViewPresenter(null, context);
    }

    @Override
    public void onChangeSelectedMonth() {
        LOGGER.info("onChangeSelectedMonth");
        onChangeEntries();
    }

    @Override
    public void onChangeEntries() {
        EntrySet entries = this.entries.getEntriesBelongingMonth(selectedMonth.getSelectedMonth());
        List<InfoDayViewContract.View.InfoDayVisualData> showData = new ArrayList<>();
        Set<String> days = entries.getDistintBelongingDays();
        for (String day : days) {
            InfoDayEntry infoDayEntry = new InfoDayEntry(entries.getEntriesBelongingDayStartWith(day), day);

            InfoDayViewContract.View.InfoDayVisualData dayData = this.infoDayViewPresenter.getVisualData(infoDayEntry);
            showData.add(dayData);
        }

        view.showData(showData);
    }

    // This is fill with DependencyInjectorBinding
    public void setSelectedMonthProvider(SelectedMonthProvider i) {
        selectedMonth = i;
        selectedMonth.subscribe(this);
    }
    // This is fill with DependencyInjectorBinding
    public void setSelectedDayProvider(SelectedDayProvider i) {
        selectedDay = i;
        //selectedDay.subscribe(this);
    }



    @Override
    public void startUi() {
        onChangeEntries();
    }

    @Override
    public void clickOnDay(Entry.BelongingDay day) {
        this.selectedDay.setSelecteDay(day.getBelongingDayDate());
    }
}
