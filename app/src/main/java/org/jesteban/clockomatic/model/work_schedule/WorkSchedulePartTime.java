package org.jesteban.clockomatic.model.work_schedule;


import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalContract;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalMustBeGapOfBetween;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.jesteban.clockomatic.model.WorkScheduleContract.WorkScheduleTimesOfDay.ENTER;

public class WorkSchedulePartTime implements WorkScheduleContract {
    List<ConditionalContract> condPerPaired = new ArrayList<>();
    ConditionalMustBeGapOfBetween condMiddayGap = new ConditionalMustBeGapOfBetween(13*60,15*60,1*60);
    int id=0;
    String name = null;
    int expectedWorkingTimeInMinutes = (8*60) + 16;
    int earlyEnterMinutes = (8*60)+30;
    int laterLeaveMinutes = (19*60)+00;
    public WorkSchedulePartTime(int id, String name){
        this.id = id;
        this.name = name;
        condPerPaired.add(new ConditionalDontCountTimeForHR( ConditionalDontCountTimeForHR.Condition.LESS, earlyEnterMinutes));
        condPerPaired.add(new ConditionalDontCountTimeForHR( ConditionalDontCountTimeForHR.Condition.GREAT, laterLeaveMinutes));

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public WorkScheduleType getTypeId() {
        return WorkScheduleType.SPLIT;
    }



    private List<InfoDayEntry.PairedEntry> applyAllCond(InfoDayEntry.PairedEntry initialPair){
        List<InfoDayEntry.PairedEntry> result = new ArrayList<>();
        result.add(initialPair);
        for (ConditionalContract cond : condPerPaired) {
            List<InfoDayEntry.PairedEntry> step = new ArrayList<>();
            for (InfoDayEntry.PairedEntry pair : result){
                step.addAll(cond.apply(pair));
            }
            result = step;
        }
        return result;
    }

    @Override
    public InfoDayEntry getInfoDay(InfoDayEntry infoDay) {
        return getInfoDay(infoDay, Calendar.getInstance());
    }
    @Override
    public InfoDayEntry getInfoDay(InfoDayEntry infoDay, Calendar now) {
        InfoDayEntry result = new InfoDayEntry(infoDay.getBelongingDay());
        List<InfoDayEntry.PairedEntry> paireds = infoDay.getPairsInfo();
        for (InfoDayEntry.PairedEntry paired: paireds ){
            List<InfoDayEntry.PairedEntry> cal = null;
            result.getPairsInfo().addAll(applyAllCond(paired));
        }
        result.setBestEntryLeavingWork(calculateBestLeaveWork(infoDay, result, now));
        //return result;
        return condMiddayGap.apply(result);
    }

    @Override
    public int getExpectedWorkingTimeInMinutes() {
        return expectedWorkingTimeInMinutes;
    }

    protected Entry calculateBestLeaveWork(InfoDayEntry infoDayRaw, InfoDayEntry infoDayScheduler, Calendar now){
        long minutesWorkedHR = infoDayScheduler.getTotalMinuteOfWorkForHR();
        int numPairs = infoDayRaw.getPairsInfo().size();
        Entry entry = new Entry(now,
                infoDayRaw.getBelongingDay().getDay(), Entry.Kind.WORKING, "best fit");
        if (numPairs == 0){
            entry.setMinutesOfDay( earlyEnterMinutes + (int) getExpectedWorkingTimeInMinutes() + (int)condMiddayGap.expectedMinimumExpectedGapMinutes() );
        } else if (numPairs >= 1){
            InfoDayEntry.PairedEntry pairedEntry = infoDayRaw.getPairsInfo().get(0);
            int minutesFirstEntry = (int)pairedEntry.starting.getMinutesOfDay();
            if (minutesFirstEntry>=earlyEnterMinutes){
                entry.setMinutesOfDay( minutesFirstEntry + (int) getExpectedWorkingTimeInMinutes() + (int)condMiddayGap.expectedMinimumExpectedGapMinutes() );
            } else {
                entry.setMinutesOfDay( earlyEnterMinutes + (int) getExpectedWorkingTimeInMinutes() + (int)condMiddayGap.expectedMinimumExpectedGapMinutes() );
            }
        }
        return entry;
    }
}
