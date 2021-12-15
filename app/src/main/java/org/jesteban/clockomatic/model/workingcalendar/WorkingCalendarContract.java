package org.jesteban.clockomatic.model.workingcalendar;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public interface WorkingCalendarContract {
    // EnumSet info: http://eddmann.com/posts/using-bit-flags-and-enumsets-in-java/
    public enum WorkingDayType{
        WORKING_DAY,                // Dia laborable normal
        SPECIAL_WORKING_HOURS,      // Te un horari especial
        FREE_STYLE_DAY,             // Pots treballar les hores que vulguis
        OPTIONAL_HOLIDAYS,          // Pot ser elegit de vacances
        FORCED_HOLIDAYS;            // Es de vacances obligatories

        public static final EnumSet<WorkingDayType> ALL_OPTS = EnumSet.allOf(WorkingDayType.class);
    }

    public enum ValuesMode{
        OPTIONAL,
        MUST,
        FORBIDDEN,
        DONT_CARE
    }

    /**
     * Return what kind of day is
     * @param belongingDay (can be NULL and return most common value)
     * @return
     */
    //ValuesMode getMiddayBreakMode(Entry.BelongingDay belongingDay);
    long expectedWorkingMinutesPerDay(Entry.BelongingDay belongingDay);
    long countWorkingMinutesFor(Entry.BelongingDay belongingDay);
    /**
     * Returns all availables modes for this Calendar
     */
    //List<WorkingDayType> getModesAvailables();

    //void getModesAvailableForDay(Date askDate);

    /**
     * Return within period that can be applied
     * If returns a EMPTY means that is forever
     */
    //public Period getWhatPeriodIsAvailableThisCalendar();



    /**
     * Return what kind of day is
     * @param askDate
     * @return
     */
    EnumSet<WorkingDayType> getWorkingDayType(Date askDate);

    /**
     * Return what to witch day is belonging a date
     * For example: - a common office work is same day
     *              - for a rotating shifts may be is not same day
     * @param askDate
     * @return
     */
     Date getWorkingDay(Date askDate);

    /**
     * Return what is legal period working for a day
     * That means time that HR count:
     * Example: I have flexible starting hour from:
     *                  8:30 till 9:00 - 13:00 to 14:00
     *                  14:00 till 15:00 - to 17:45 to 18:45
     * @param askDate
     * @return
     */
    //SchedulerDay getLegalHumanResourcesWorkingSchedulerForDay(Date askDate);

    /**
     * Returns start hour for a day
     * example: for office is 00:00
     *          for rotating shift can be 6:00
     * @param askDate
     * @return
     */
    //Date getStartingHour(Date askDate);
}
