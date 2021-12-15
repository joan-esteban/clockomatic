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

// This is a container of restrictions:
// add EntryWorkTime(int early, int later)
// add ExpectedWorkTime(int minTimeMinutes);
// add MiddayGap(int early, int later, int minTimeMinutes);
// add LeaveWorkTime(int early, int later)
//
// Each one returns:
//      - Warning or error
//      - Modify entries to skip not counting time
//
// Best leave work entry:
//      - Combination of all
//

public class WorkSchedule implements WorkScheduleContract {
    public WorkSchedule(int id, String name){
        this.id = id;
        this.name = name;
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
        return this.typeWorkSchedule;
    }

    @Override
    public InfoDayEntry getInfoDay(InfoDayEntry infoDay){
        return getInfoDay(infoDay, null);
    }

    @Override
    public InfoDayEntry getInfoDay(InfoDayEntry infoDay, Calendar now) {
        //if (now == null) now = Calendar.getInstance();
        if (infoDay==null || infoDay.getBelongingDay()==null){
            //throw new IllegalArgumentException("getInfoDay(InfoDayEntry infoDay) infoDay or infoDay.getBelongingDay() can't be null!");
            return infoDay;
        }
        InfoDayEntry result = new InfoDayEntry(infoDay.getBelongingDay());
        result.setGeneratedWorkSchedule(this);
        List<InfoDayEntry.PairedEntry> paireds = infoDay.getPairsInfo();
        for (InfoDayEntry.PairedEntry paired: paireds ){
            List<InfoDayEntry.PairedEntry> cal = null;
            result.getPairsInfo().addAll(applyAllCond(paired));
        }
        result.setBestEntryLeavingWork(calculateBestLeaveWork(infoDay, result, now));

        if (condMiddayGap!=null) {
            return condMiddayGap.apply(result);
        } else return result;
    }

    @Override
    public int getExpectedWorkingTimeInMinutes() {
        return expectedWorkingTimeInMinutes;
    }

    public WorkSchedule setExpectedWorkingTimeInMinutes(int minutes) {
        if (minutes<0){
            this.expectedWorkingTimeInMinutes = WORKING_TIME_UNDEFINED;
            this.typeWorkSchedule = WorkScheduleType.FREESTYLE;
        } else {
            this.expectedWorkingTimeInMinutes = minutes;
            this.typeWorkSchedule = WorkScheduleType.CONTINOUS;
        }
        //TODO: What happens if it's 0... ?? a holidays day?
        return this;
    }

    public WorkSchedule setEarlyEnterTimeInMinutes(int earlyEnterMinutes){
        condPerPaired.add(new ConditionalDontCountTimeForHR( ConditionalDontCountTimeForHR.Condition.LESS, earlyEnterMinutes));
        return this;
    }

    public static final int WORKING_TIME_UNDEFINED = -1;

    WorkScheduleType typeWorkSchedule =  WorkScheduleType.FREESTYLE;
    int expectedWorkingTimeInMinutes = WORKING_TIME_UNDEFINED;
    ConditionalMustBeGapOfBetween condMiddayGap;
    List<ConditionalContract> condPerPaired = new ArrayList<>();

    int id=0;
    String name = null;

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

    private Entry getLastEntry(InfoDayEntry id){
        List<InfoDayEntry.PairedEntry> pairs = id.getPairsInfo();
        if (pairs==null || pairs.size()==0) return null;
        InfoDayEntry.PairedEntry lastPairs =  pairs.get(pairs.size()-1);
        if (lastPairs.finish!=null) return lastPairs.finish;
        return lastPairs.starting;
    }

    protected Entry calculateBestLeaveWork(InfoDayEntry infoDayRaw, InfoDayEntry infoDayScheduler, Calendar now){
        long minutesWorkedHR = infoDayScheduler.getTotalMinuteOfWorkForHR();
        Entry last = getLastEntry(infoDayScheduler);
        if (last==null){
            // (int) infoDayScheduler.remainWorkingTime()
            now.add(Calendar.MINUTE, (int) infoDayScheduler.remainWorkingTime());
            return new Entry(now,
                    infoDayRaw.getBelongingDay().getDay(), Entry.Kind.WORKING, "best fit");
        }
        /*long minutesWorkedHR = infoDayScheduler.getTotalMinuteOfWorkForHR();
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

         */
        return null;
    }
}
