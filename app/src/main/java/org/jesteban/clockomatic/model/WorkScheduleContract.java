package org.jesteban.clockomatic.model;

import org.jesteban.clockomatic.helpers.InfoDayEntry;

import java.util.Calendar;

/**
 * This is schedule for each day
 * Example:
 *  PART_TIME :
 *      Enter hour= 8:30 - 9:00
 *      Midday= 13:00 - 15:00
 *      Minimum midday = 1h
 *      Working time = 8h:14m
 */

public interface WorkScheduleContract {
    public static int THERE_ARE_NOT_CONTROL_OVER_WORK_TIME = -1;
    public static int EXPECTED_NO_WORK = 0; // Example on Sunday

    public enum WorkScheduleType{
        FREESTYLE,  // No restrictions
        SPLIT,      // Have a break at midday
        CONTINOUS   // There is no break (ex: intensive day)
    }
    public enum WorkScheduleTimesOfDay{
        ENTER,
        MIDDAY,
        EXIT
    }
     String getName();
     int getId();
    WorkScheduleType getTypeId();

    InfoDayEntry getInfoDay(InfoDayEntry infoDay);
    InfoDayEntry getInfoDay(InfoDayEntry infoDay, Calendar now);
    // THERE_ARE_NOT_CONTROL_OVER_WORK_TIME -> Means that there aren't control over worked time
    // EXPECTED_NO_WORK -> Is a holiday day
    // >0 -> Minutes
    int getExpectedWorkingTimeInMinutes();

}
