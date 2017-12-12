package org.jesteban.clockomatic.fragments.reportpage.showdaydetail;

import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.bindings.SelectedDayProvider;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.Minutes2String;
import org.jesteban.clockomatic.helpers.PresenterBasicProviderEntriesReady;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DayDetailPresenter extends PresenterBasicProviderEntriesReady<DayDetailContract.View>
        implements DayDetailContract.Presenter, SelectedDayProvider.Listener {
    private static final Logger LOGGER = Logger.getLogger(DayDetailPresenter.class.getName());

    private SelectedDayProvider selectedDay = null;
    public DayDetailPresenter(@NonNull DayDetailContract.View view) {
        super(view);
    }
    // TODO this must be a resource to be localized
    private static String[] days = {"?", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    @Override
    public List<Provider> getBindings() {
        return super.getBindings();
    }




    private StringBuilder getTextForEntries(InfoDayEntry infoDay){
        StringBuilder sb = new StringBuilder();
        List<InfoDayEntry.EntryPairs> pairs = infoDay.getPairsInfo();
        if (pairs==null) return sb;
        Entry2Html aux = new Entry2Html();
        for (InfoDayEntry.EntryPairs pair : pairs) {
            sb.append("<li>" + aux.getJustHours(pair.starting) + " --> " + aux.getJustHours(pair.finish) + "\n");
        };
        return sb;
    }

    @Override
    public void onChangeEntries() {
        LOGGER.log(Level.INFO, "onChangeEntries");
        InfoDayEntry infoDayEntry = new InfoDayEntry(this.entries.getEntries().getEntriesBelongingDayStartWith(selectedDay.getFilterBelongingForDay())
                , selectedDay.getFilterBelongingForDay());
        StringBuilder txt = getTextForEntries(infoDayEntry);
        view.showEntries(txt.toString());
        long totalMinutesWork = infoDayEntry.getTotalMinuteOfWork();
        view.showInfo( Minutes2String.convert(totalMinutesWork));
    }

    @Override
    public void onChangeSelectedDay() {

        LOGGER.log(Level.INFO, "onChangeSelectedDay");
        Calendar date = selectedDay.getSelectedDay();

        view.showDayName( days[date.get(Calendar.DAY_OF_WEEK)] );
        view.showDayNumber(Integer.toString(date.get(Calendar.DAY_OF_MONTH)));
        onChangeEntries();
    }

    @Override
    public void startUi() {
        super.startUi();
        onChangeSelectedDay();

    }

    public void setSelectedDayProvider(@NonNull SelectedDayProvider p){
        LOGGER.log(Level.INFO, "setSelectedDayProvider");
        selectedDay = p;
        p.subscribe(this);
    }
}
