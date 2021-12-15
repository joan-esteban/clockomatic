package org.jesteban.clockomatic.fragments.infoperiodview;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;
import org.jesteban.clockomatic.fragments.showlistdaysclocks.ShowListDaysClocksMonthPresenter;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.Minutes2String;
import org.jesteban.clockomatic.helpers.PresenterBasic;

import java.util.Map;

public class InfoPeriodViewPresenterNoProviderLink extends PresenterBasic<InfoPeriodViewContract.View>
    implements InfoPeriodViewContract.Presenter{
    public InfoPeriodViewPresenterNoProviderLink(@NonNull InfoPeriodViewContract.View view) {
        super(view);
    }

    @Override
    public void showData(Map<Integer, ShowListDaysClocksMonthPresenter.DataPerDay> showingInfoDays) {
        view.showData(convertData(showingInfoDays));
    }

    @Override
    public InfoPeriodViewContract.View.InfoPeriodVisualData convertData(Map<Integer, ShowListDaysClocksMonthPresenter.DataPerDay> showingInfoDays) {
        InfoPeriodViewContract.View.InfoPeriodVisualData visualData = new InfoPeriodViewContract.View.InfoPeriodVisualData();
        // Convert data to visual
        long totalMinutes = 0;
        long totalMinutesHr = 0;
        long totalDaysWithScheduler = 0;
        long totalExpectedHr = 0;
        for (Map.Entry<Integer,ShowListDaysClocksMonthPresenter.DataPerDay> entry: showingInfoDays.entrySet()){
            ShowListDaysClocksMonthPresenter.DataPerDay day = entry.getValue();
            InfoDayEntry infoDayWorkScheduler = day.getInfoDayWorkScheduler();
            if (infoDayWorkScheduler!=null){
                totalMinutesHr += infoDayWorkScheduler.getTotalMinuteOfWorkForHR();
                totalExpectedHr += infoDayWorkScheduler.getGeneratedWorkSchedule().getExpectedWorkingTimeInMinutes();
                totalDaysWithScheduler++;
            }
            InfoDayEntry infoDayAny = day.infoDays.get(0);
            totalMinutes += infoDayAny.getTotalMinuteOfWork();
        }
        visualData.numWorkingDays = String.format("%d with scheduler:%d", showingInfoDays.size(), totalDaysWithScheduler);
        visualData.hrWorkingTime = String.format("%s expected:%s (diff:%d)", Minutes2String.convert(totalMinutesHr), Minutes2String.convert(totalExpectedHr), totalExpectedHr-totalMinutesHr);
        visualData.totalWorkingTime= String.format("%s", Minutes2String.convert(totalMinutes) );
        return visualData;
    }
}
