package org.jesteban.clockomatic.fragments.showdaydetail;

import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.bindings.SelectedDayProvider;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.Minutes2String;
import org.jesteban.clockomatic.helpers.PresenterBasicProviderEntriesReady;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DayDetailPresenter extends PresenterBasicProviderEntriesReady<DayDetailContract.View>
        implements DayDetailContract.Presenter, SelectedDayProvider.Listener {
    private static final Logger LOGGER = Logger.getLogger(DayDetailPresenter.class.getName());
    private Context context = null;
    private SelectedDayProvider selectedDay = null;

    public DayDetailPresenter(@NonNull DayDetailContract.View view, Context current) {
        super(view);
        context = current;
    }


    private StringBuilder getTextForEntries(InfoDayEntry infoDay) {
        StringBuilder sb = new StringBuilder();
        List<InfoDayEntry.PairedEntry> pairs = infoDay.getPairsInfo();
        if (pairs == null) return sb;
        Entry2Html aux = new Entry2Html();
        for (InfoDayEntry.PairedEntry pair : pairs) {
            sb.append("<li>" + aux.getJustHours(pair.starting) + " --> " + aux.getJustHours(pair.finish) + "</li><br>\n");
        }
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
        view.showInfo(Minutes2String.convert(totalMinutesWork));
    }

    @Override
    public void onChangeSelectedDay() {

        LOGGER.log(Level.INFO, "onChangeSelectedDay");
        Calendar date = selectedDay.getSelectedDay();
        String[] days = context.getResources().getStringArray(R.array.name_days_short_array);
        view.showDayName(days[date.get(Calendar.DAY_OF_WEEK)]);
        view.showDayNumber(Integer.toString(date.get(Calendar.DAY_OF_MONTH)));
        onChangeEntries();
    }

    @Override
    public void startUi() {
        super.startUi();
        onChangeSelectedDay();

    }

    public void setSelectedDayProvider(@NonNull SelectedDayProvider p) {
        LOGGER.log(Level.INFO, "setSelectedDayProvider");
        selectedDay = p;
        p.subscribe(this);
    }
}
