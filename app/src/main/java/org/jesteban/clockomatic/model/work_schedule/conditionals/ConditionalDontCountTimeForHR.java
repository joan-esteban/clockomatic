package org.jesteban.clockomatic.model.work_schedule.conditionals;

import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.work_schedule.WorkSchedulePartTime;

import java.util.Calendar;
import java.util.List;

import static org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR.Condition.GREAT;
import static org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR.Condition.LESS;

/**
 * This is a Scheduler condition,
 * Ex:
 * Count time at ENTER must be  LATER(GREAT) than 8:30
 * ConditionalDontCountTimeForHR(ENTER, GREAT, 8:30 (in minutes)
 *  So a set like : 8:15 - 13:00 is converted to:
 *              COUNTING:     8:30-13:00 and
 *              NOT_COUNTING: 8:15-8:30
 * Another example: 8:00 - 8:25 is converted to:
 *              COUNTING:       ----- none
 *              NOT_COUNTING:  8:00 - 8:25
 */

public class ConditionalDontCountTimeForHR implements  ConditionalContract{

    private Condition condition;
    private Integer minutes;
    public enum Condition{
        GREAT,
        LESS
    }
    public ConditionalDontCountTimeForHR( Condition condition, Integer minutes){

        this.condition = condition;
        this.minutes = minutes;
    }



    private Calendar getSplitDate(Entry entry){
        Calendar splitDate = entry.getBelongingDayObject().getBelongingDayDate();
        splitDate.set(Calendar.HOUR_OF_DAY,minutes/60);
        splitDate.set(Calendar.MINUTE, minutes%60);
        splitDate.set(Calendar.SECOND,0);
        splitDate.set(Calendar.MILLISECOND,0);
        return splitDate;
    }

    public List<InfoDayEntry.PairedEntry> apply(InfoDayEntry.PairedEntry paired){
        Calendar splitDate = getSplitDate(paired.getStarting());
        List<InfoDayEntry.PairedEntry> splitted = InfoDayEntry.PairedEntry.split(paired,splitDate);


        for (InfoDayEntry.PairedEntry pairedSplitted : splitted){
            if (pairedSplitted.isContained(splitDate) || pairedSplitted.isInFuture(splitDate) ){
                if (condition==LESS) pairedSplitted.setCountHr(false);
            } else  if (condition==GREAT) pairedSplitted.setCountHr(false);
        }
        return splitted;

    }

    public void apply(InfoDayEntry entriesPaired){
        List<InfoDayEntry.PairedEntry> paireds = entriesPaired.getPairsInfo();
        for (InfoDayEntry.PairedEntry paired: paireds ){

        }
    }
}
