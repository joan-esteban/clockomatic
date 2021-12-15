package org.jesteban.clockomatic.fragments.infodayvieew;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

//import org.jesteban.clockomatic.fragments.showdaydetail.DayDetailContract;
import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.Minutes2String;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.work_schedule.WorkSchedulePartTime;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.providers.WorkScheduleProviderContract;

import java.util.ArrayList;
import java.util.List;

public class InfoDayViewPresenter extends  InfoDayViewPresenterNoProviderLink
                                  implements EntriesProviderContract.Listener, SelectedDayProviderContract.Listener,
        WorkScheduleProviderContract.Listener{




    public InfoDayViewPresenter(@NonNull InfoDayViewContract.View view, Context current) {
        super(view, current);
    }

    @Override
    public void onChangeEntries() {
        try {
            List<InfoDayEntry> infoDays = new ArrayList<>();
            infoDayFree = new InfoDayEntry(this.entries.getEntriesBelongingDayStartWith(selectedDay.getFilterBelongingForDay())
                    , selectedDay.getFilterBelongingForDay());

            if (currentWorkSchedule!=null){
                infoDayWorkSchedule = currentWorkSchedule.getInfoDay(infoDayFree);
                infoDayWorkSchedule.setGeneratedWorkSchedule(currentWorkSchedule);
                infoDays.add(infoDayWorkSchedule);
                infoDays.add(infoDayFree);

                showInfoDay(infoDays,0);
            } else{
                infoDayWorkSchedule = null;
                infoDays.add(infoDayFree);
                showInfoDay(infoDays,-1);
            }


        } catch (Exception e){
            infoDayFree = new InfoDayEntry();
            showInfoDay(infoDayFree);
        }
    }


    @Override
    public void showInfoDay(InfoDayEntry infoDay) {
        infoDayShowing = infoDay;
        super.showInfoDay(infoDay);
    }

    @Override
    public void onChangeSelectedDay() {
        onChangeEntries();
    }

    @Override
    public void onChangeWorkScheduler() {
        currentWorkSchedule = workScheduleProvider.getWorkSchedule();
        onChangeEntries();
    }

    public void setSelectedDayProvider(@NonNull SelectedDayProviderContract p) {
        selectedDay = p;
        selectedDay.subscribe(this);
    }

    public void setEntriesProvider(@NonNull EntriesProviderContract i){
        entries = i;
        entries.subscribe(this);
    }

    public void setWorkScheduleProvider(@NonNull WorkScheduleProviderContract i){
        workScheduleProvider = i;
        workScheduleProvider.subscribe(this);
    }



    protected SelectedDayProviderContract selectedDay = null;
    protected EntriesProviderContract entries = null;
    protected WorkScheduleProviderContract workScheduleProvider = null;
    protected InfoDayEntry infoDayFree = null;
    protected InfoDayEntry infoDayWorkSchedule = null;
    protected InfoDayEntry infoDayShowing = null;
    protected WorkScheduleContract currentWorkSchedule = null;


}
