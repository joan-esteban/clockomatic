package org.jesteban.clockomatic.model.work_schedule;

import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalContract;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalMustBeGapOfBetween;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WorkScheduleContinuos implements WorkScheduleContract {
    List<ConditionalContract> condPerPaired = new ArrayList<>();
    //ConditionalMustBeGapOfBetween condMiddayGap = new ConditionalMustBeGapOfBetween(13*60,15*60,1*60);
    int id=0;
    String name = null;
    int expectedWorkingTimeInMinutes = 7*60;

    public WorkScheduleContinuos(int id, String name){
        this.id = id;
        this.name = name;
        condPerPaired.add(new ConditionalDontCountTimeForHR( ConditionalDontCountTimeForHR.Condition.LESS, (8*60)+0));
        condPerPaired.add(new ConditionalDontCountTimeForHR( ConditionalDontCountTimeForHR.Condition.GREAT, (16*60)+00));

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
        return WorkScheduleType.CONTINOUS;
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
        InfoDayEntry result = new InfoDayEntry(infoDay.getBelongingDay());
        List<InfoDayEntry.PairedEntry> paireds = infoDay.getPairsInfo();
        for (InfoDayEntry.PairedEntry paired: paireds ){
            result.getPairsInfo().addAll(applyAllCond(paired));
        }
        return result;
    }

    @Override
    public InfoDayEntry getInfoDay(InfoDayEntry infoDay, Calendar now) {
        return getInfoDay(infoDay);
    }

    @Override
    public int getExpectedWorkingTimeInMinutes() {
        return expectedWorkingTimeInMinutes;
    }
}
