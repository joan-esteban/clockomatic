package org.jesteban.clockomatic.helpers;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarHelper {
    public static Calendar createCalendarAtMinutesOfDay(Calendar referenceDay, int minutes){
        Calendar result = (Calendar) referenceDay.clone();
        result.set(Calendar.HOUR_OF_DAY,minutes/60);
        result.set(Calendar.MINUTE, minutes%60);
        result.set(Calendar.SECOND,0);
        result.set(Calendar.MILLISECOND,0);
        return result;
    }

    public static Calendar createCalendarAddMinutes(Calendar referenceDate, int minutes){
        Calendar result = (Calendar) referenceDate.clone();
        result.add(Calendar.HOUR_OF_DAY,minutes/60);
        result.add(Calendar.MINUTE, minutes%60);
        result.set(Calendar.SECOND,0);
        result.set(Calendar.MILLISECOND,0);
        return result;
    }

    public static String toString(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:s z");
        return sdf.format(date.getTime());
    }
}
