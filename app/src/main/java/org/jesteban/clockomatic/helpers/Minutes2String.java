package org.jesteban.clockomatic.helpers;

public class Minutes2String {
    public static String convert(long totalMinutes){
        long hour = totalMinutes / 60;
        long minutes = totalMinutes - (hour *60);
        return String.format("%02d:%02d", hour, minutes);

    }
}
