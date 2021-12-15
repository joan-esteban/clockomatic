package org.jesteban.clockomatic.fragments.infodayvieew;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.Minutes2String;
import org.jesteban.clockomatic.helpers.PresenterBasic;

import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.views.MyCalendarDayViewContract;
import org.jesteban.clockomatic.views.MyPairedEntryViewContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.BLUE;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.COLOR_FRIDAY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.COLOR_MONDAY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.COLOR_THURSDAY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.COLOR_TUESDAY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.COLOR_WEDNESDAY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.GREY;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.ColorStyle.RED;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.SizeStyle.BIG;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.SizeStyle.SMALL;

public class InfoDayViewPresenterNoProviderLink extends PresenterBasic<InfoDayViewContract.View>
        implements InfoDayViewContract.Presenter {



    public InfoDayViewPresenterNoProviderLink(@NonNull InfoDayViewContract.View view, Context current) {
        super(view);
        this.context = current;
    }

    private MyCalendarDayViewContract.CalendarDayViewVisualData getVisualDataForDay(Entry.BelongingDay belongingDay, int numEntries){
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
        } else{
            switch (calDay.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY: res.colorStyle = COLOR_MONDAY; break;
                case Calendar.TUESDAY: res.colorStyle = COLOR_TUESDAY; break;
                case Calendar.WEDNESDAY: res.colorStyle = COLOR_WEDNESDAY; break;
                case Calendar.THURSDAY: res.colorStyle = COLOR_THURSDAY; break;
                case Calendar.FRIDAY: res.colorStyle = COLOR_FRIDAY; break;

            }
        }

        String[] months = context.getResources().getStringArray(R.array.name_months_short_array);
        res.textBottom = months[calDay.get(Calendar.MONTH)];

        res.sizeStyle = SMALL;
        if (numEntries>2) res.sizeStyle = BIG;
        if (numEntries==0) {
            res.colorStyle = GREY;
            res.sizeStyle = SMALL;
        }
        if (alwaysBig) res.sizeStyle = BIG;
        return res;
    }

    private MyPairedEntryViewContract.PairedEntryVisualData getVisualDataFor(InfoDayEntry.PairedEntry data){
        //if (!data.countHr()) return null;
        int color = Color.GRAY;
        String startingDistance = "";
        try {
            startingDistance = data.starting.getDayOffsetBetweenBelongingDayAndRegisterAsString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String starting = sdfHourMinute.format(data.starting.getRegisterDate().getTime());

        String ending = null;
        String endDistance = "";
        if (data.finish!=null) {
            try {
                endDistance = data.finish.getDayOffsetBetweenBelongingDayAndRegisterAsString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ending = sdfHourMinute.format(data.finish.getRegisterDate().getTime());
        }

        if (data.countHr()){
            color = Color.BLACK;
        }

        return new MyPairedEntryViewContract.PairedEntryVisualData(starting,ending,startingDistance,endDistance,color,0);
    }

    private List<MyPairedEntryViewContract.PairedEntryVisualData> getVisualDataFor(List<InfoDayEntry.PairedEntry> pairedEntries){
        ArrayList<MyPairedEntryViewContract.PairedEntryVisualData> res = new ArrayList<>();
        for (InfoDayEntry.PairedEntry pairedEntry :  pairedEntries){
            MyPairedEntryViewContract.PairedEntryVisualData visualData = getVisualDataFor(pairedEntry);
            if (visualData!=null) res.add(visualData);
        }
        return res;
    }


    protected InfoDayViewContract.View.InfoDayBriefData getVisualDataForBrief(InfoDayEntry infoDay){
        InfoDayViewContract.View.InfoDayBriefData res = new InfoDayViewContract.View.InfoDayBriefData();
        int indexForRHInfo=1;
        int indexForFreeInfo=0;
        int infoDayIndx = infoDays.indexOf(infoDay);
        InfoDayEntry infoDayFree = infoDays.get(0);
        InfoDayEntry infoDayWorkSchedule = null;
        if (infoDayIndexForWorkSchedule>=0) infoDayWorkSchedule = infoDays.get(infoDayIndexForWorkSchedule);
        if (infoDayIndx == infoDayIndexForWorkSchedule){
            indexForFreeInfo=1;
            indexForRHInfo=0;
        }
        res.title[indexForFreeInfo]=context.getResources().getString(R.string.view_info_full_time_title).toString();
        res.text[indexForFreeInfo] = Minutes2String.convert(infoDayFree.getTotalMinuteOfWork());
        res.color[indexForFreeInfo] = Color.GRAY;
        if (infoDayWorkSchedule!=null) {
            res.title[indexForRHInfo] = context.getResources().getString(R.string.view_info_hr_time_title).toString();
            res.text[indexForRHInfo] = Minutes2String.convert(infoDayWorkSchedule.getTotalMinuteOfWorkForHR());
            res.color[indexForRHInfo] = Color.RED;
            if (infoDayWorkSchedule.isExpectedWorkingTimeAchieved()) res.color[indexForRHInfo] = Color.GREEN;
        } else{
            res.title[indexForRHInfo] = "";
            res.text[indexForRHInfo] = "";
        }


        return res;
    }

    public String getExtraLine(InfoDayEntry infoDay){
        Entry bestLeavingWork = infoDay.getBestEntryLeavingWork();
        if (bestLeavingWork!=null){
            return bestLeavingWork.toShortString();
        }
        return null;
    }

    public InfoDayViewContract.View.InfoDayVisualData getVisualData(InfoDayEntry infoDay){
        Entry.BelongingDay belongingDay = null;

        belongingDay = new Entry.BelongingDay(infoDay.getBelongingDay());


        InfoDayViewContract.View.InfoDayVisualData data = new InfoDayViewContract.View.InfoDayVisualData();
        data.dayData = getVisualDataForDay(belongingDay, infoDay.getPairsInfo().size() );
        data.entriesData = getVisualDataFor(infoDay.getPairsInfo());
        data.briefData = getVisualDataForBrief(infoDay);
        data.belongingDay = belongingDay;
        data.textExtraLine = getExtraLine(infoDay);
        WorkScheduleContract ws = infoDay.getGeneratedWorkSchedule();
        if (ws!=null){
            data.workingScheduleName = ws.getName();
        }
        return data;
    }

    public void showInfoDay(InfoDayEntry infoDay) {
        view.showData(getVisualData(infoDay));
    }

    @Override
    public void showInfoDay(List<InfoDayEntry> infoDays, int infoDayIndexForWorkSchedule){
        this.infoDays = infoDays;

        if (infoDays.size()>0){
            showingInfoDayIndex = 0;
            this.infoDayIndexForWorkSchedule = infoDayIndexForWorkSchedule;
        } else this.infoDayIndexForWorkSchedule = -1;
        showInfoDay(infoDays.get(showingInfoDayIndex));
    }
    @Override
    public void clickOnCard() {
        if ( (infoDays!=null) && (infoDays.size()>0)){
            showingInfoDayIndex = (showingInfoDayIndex+1)% infoDays.size();
            showInfoDay(infoDays.get(showingInfoDayIndex));
        }
        if (onClickOnCardAction!=null) onClickOnCardAction.execute();
    }

    @Override
    public void clickOnEntryItem(int position) {
        if (onClicknEntryItem!=null) onClicknEntryItem.execute();
    }

    @Override
    public void clickOnCalendar() {
        if (onClicknOnCalendar!=null) onClicknOnCalendar.execute();
    }

    @Override
    public void setOnClickOnCardAction(OnAction action) {
        onClickOnCardAction = action;
    }

    @Override
    public void setOnClickOnEntryItem(OnAction action) {
        onClicknEntryItem = action;
    }

    @Override
    public void setOnClickOnCalendar(OnAction action) {
        onClicknOnCalendar = action;
    }

    private SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    private SimpleDateFormat sdfHourMinute = new SimpleDateFormat("HH:mm");
    protected Context context = null;
    private boolean alwaysBig = true;
    private OnAction onClickOnCardAction = null;
    private OnAction onClicknEntryItem = null;
    private OnAction onClicknOnCalendar = null;
    private List<InfoDayEntry> infoDays;
    int showingInfoDayIndex = -1;
    int infoDayIndexForWorkSchedule = -1;

}
