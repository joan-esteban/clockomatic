package org.jesteban.clockomatic.views;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.PresenterBasic;

import org.jesteban.clockomatic.model.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.BLUE;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.GREY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.RED;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.SizeStyle.BIG;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.SizeStyle.SMALL;

public class InfoDayViewPresenter extends PresenterBasic<InfoDayViewContract.View>
        implements InfoDayViewContract.Presenter {

    private SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    private SimpleDateFormat sdfHourMinute = new SimpleDateFormat("HH:mm");
    private Context context = null;

    public InfoDayViewPresenter(@NonNull InfoDayViewContract.View view, Context current) {
        super(view);
        this.context = current;
    }

    private MyCalendarDayViewContract.CalendarDayViewVisualData getVisualDataFor(Entry.BelongingDay belongingDay){

        MyCalendarDayViewContract.CalendarDayViewVisualData res = new MyCalendarDayViewContract.CalendarDayViewVisualData();
        if (belongingDay==null){
            return res;
        }


        Calendar calDay = belongingDay.getBelongingDayDate();
        res.textMiddle = sdfDay.format(calDay.getTime());
        String[] days = context.getResources().getStringArray(R.array.name_days_short_array);
        res.textUpper = days[calDay.get(Calendar.DAY_OF_WEEK)];
        if (calDay.get(Calendar.DAY_OF_WEEK)==1 || calDay.get(Calendar.DAY_OF_WEEK)==7){
            res.colorStyle = RED;
        } else  res.colorStyle = BLUE;
        String[] months = context.getResources().getStringArray(R.array.name_months_short_array);
        res.textBottom = months[calDay.get(Calendar.MONTH)];
        res.sizeStyle = BIG;
        return res;
    }

    private MyPairedEntryViewContract.PairedEntryVisualData getVisualDataFor(InfoDayEntry.PairedEntry data){
        String starting = sdfHourMinute.format(data.starting.getRegisterDate().getTime());
        String ending = sdfHourMinute.format(data.finish.getRegisterDate().getTime());
        return new MyPairedEntryViewContract.PairedEntryVisualData(starting,ending);
    }

    private List<MyPairedEntryViewContract.PairedEntryVisualData> getVisualDataFor(List<InfoDayEntry.PairedEntry> pairedEntries){
        ArrayList<MyPairedEntryViewContract.PairedEntryVisualData> res = new ArrayList<>();
        for (InfoDayEntry.PairedEntry pairedEntry :  pairedEntries){
            MyPairedEntryViewContract.PairedEntryVisualData visualData = getVisualDataFor(pairedEntry);
            res.add(visualData);
        }
        return res;
    }

    @Override
    public void showInfoDay(InfoDayEntry infoDay) {
        Entry.BelongingDay belongingDay = null;
        try {
            belongingDay = new Entry.BelongingDay(infoDay.getBelongingDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.showCalendarDay(getVisualDataFor(belongingDay));
        view.showPairedEntries(getVisualDataFor(infoDay.getPairsInfo()));
    }
}
