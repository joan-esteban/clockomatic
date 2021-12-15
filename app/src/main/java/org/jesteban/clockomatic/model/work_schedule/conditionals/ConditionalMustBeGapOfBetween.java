package org.jesteban.clockomatic.model.work_schedule.conditionals;


import org.jesteban.clockomatic.helpers.CalendarHelper;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.TimeFrame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class ConditionalMustBeGapOfBetween  {
    private static final Logger LOGGER = Logger.getLogger(ConditionalMustBeGapOfBetween.class.getName());
    int minutesStart;
    int minutesEnd;
    int minutesGap;

    public ConditionalMustBeGapOfBetween(int minutesStart, int minutesEnd, int minutesGap){
        this.minutesStart = minutesStart;
        this.minutesEnd = minutesEnd;
        this.minutesGap = minutesGap;
    }

    public Calendar getStart(Entry.BelongingDay belongingDay){
        return CalendarHelper.createCalendarAtMinutesOfDay(belongingDay.getBelongingDayDate(), minutesStart);
    }

    public Calendar getEnd(Entry.BelongingDay belongingDay){
        return CalendarHelper.createCalendarAtMinutesOfDay(belongingDay.getBelongingDayDate(), minutesEnd);
    }


    public TimeFrame getGapTimeFrameAt(Calendar gapStart){
        return new TimeFrame(gapStart,CalendarHelper.createCalendarAddMinutes(gapStart,minutesGap) );
    }

    public Boolean isGapEnough(InfoDayEntry segment,TimeFrame where){
        long t = segment.getTotalMinutesGaps(where);
        return (segment.getTotalMinutesGaps(where)>=minutesGap);
    }

    List< InfoDayEntry.PairedEntry > cutSegmentGap(InfoDayEntry.PairedEntry pair, TimeFrame where){
        List< InfoDayEntry.PairedEntry > entryPairs = new ArrayList<>();
        for (InfoDayEntry.PairedEntry  splitStart : InfoDayEntry.PairedEntry.split(pair, where.getStartTime())){
            if (splitStart.isContained(where.getFinishTime())){
                for (InfoDayEntry.PairedEntry  splitFinish : InfoDayEntry.PairedEntry.split(splitStart, where.getFinishTime())){
                    if (splitFinish.isInFuture(where.getFinishTime())) splitFinish.setCountHr(false);
                    entryPairs.add(splitFinish);
                }
            } else entryPairs.add(splitStart);
        }
        return entryPairs;
    }
    TimeFrame getBestFitGap(InfoDayEntry segment,TimeFrame where){
        TimeFrame result = new TimeFrame();
        // This is later that must start gap
        Calendar gapMaximumStart = CalendarHelper.createCalendarAddMinutes(where.getFinishTime(),-minutesGap);
        for (InfoDayEntry.PairedEntry pair : segment.getPairsInfo()){
            if (pair.isContained(gapMaximumStart)){
                return getGapTimeFrameAt(gapMaximumStart);
            } else{
                return getGapTimeFrameAt(pair.getFinish().getRegisterDate());
            }
        }
        return getGapTimeFrameAt(gapMaximumStart);
    }
    public long expectedMinimumExpectedGapMinutes(){
        return minutesGap;
    }
    public InfoDayEntry apply(InfoDayEntry infoDay){
        // A: 8:00 <-> 13:15 - 13:16 <->17:00             (need extends 13:15 - 14:15)
        // B: 13:15 - 13:16  13:28-13:29 (extends and absorb!)
        // C: 12:15 - 16:00              (create gap 13:00 - 14:00)
        // D: 16:00 - 17:00              already exists gap nothing to do
        // E:
        //LOGGER.info("apply infoDay " + CalendarHelper.toString(infoDay.getBelongingDay().getBelongingDayDate()));

        TimeFrame whereMustBeGap = new TimeFrame(getStart(infoDay.getBelongingDay()), getEnd(infoDay.getBelongingDay()));

        InfoDayEntry segment = infoDay.getTimeSegment(whereMustBeGap);
        if (isGapEnough(segment,whereMustBeGap))  return infoDay;
        // I must create expected Gap
        List< InfoDayEntry.PairedEntry > entryPairs = new ArrayList<>();
        if (segment.getPairsInfo().size()==1){
            TimeFrame gapTimeFrame = getGapTimeFrameAt(whereMustBeGap.getStartTime());
            // There are a solid segment, so is no gap. I have to force a gap [start - start+gap]
            // Also assume that Exists a Pair that start+gap is contained
            for (InfoDayEntry.PairedEntry pair : infoDay.getPairsInfo()){
                if (pair.isContained(gapTimeFrame.getStartTime())){
                    // Found pair to break: it can be
                    // A: |----XXXXX----| Need a cut and generate 3 segment
                    entryPairs.addAll(cutSegmentGap(pair,gapTimeFrame));

                } else entryPairs.add(pair);
            }
        } else{
            // There are multiples segments, so I have to break 1 and set that doesnt count HR
            // It can fit
            TimeFrame gapTimeFrame = getBestFitGap(segment,whereMustBeGap);
            for (InfoDayEntry.PairedEntry pair : infoDay.getPairsInfo()){
                if (pair.isContained(gapTimeFrame.getStartTime())){
                    // Found pair to break: it can be
                    // A: |----XXXXX----| Need a cut and generate 3 segment
                    entryPairs.addAll(cutSegmentGap(pair,gapTimeFrame));

                } else if (pair.isContained(gapTimeFrame.getFinishTime())){
                    entryPairs.addAll(cutSegmentGap(pair,gapTimeFrame));
                } else entryPairs.add(pair);
            }
        }
        return new InfoDayEntry(entryPairs,infoDay.getBelongingDay());
    }
}
