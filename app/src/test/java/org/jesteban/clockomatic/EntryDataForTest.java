package org.jesteban.clockomatic;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EntryDataForTest {
    public String belongingDay = "2017/01/01";
    public Entry entry0100;
    public Entry entry0200;
    public Entry entry0700;
    public Entry entry0800;
    public Entry entry0815;
    public Entry entry0830;
    public Entry entry0845;
    public Entry entry0900;
    public Entry entry1000;
    public Entry entry1015;
    public Entry entry1030;
    public Entry entry1045;
    public Entry entry1100;
    public Entry entry1130;
    public Entry entry1200;
    public Entry entry1215;
    public Entry entry1300;
    public Entry entry1315;
    public Entry entry1325;
    public Entry entry1330;
    public Entry entry1400;
    public Entry entry1415;
    public Entry entry1430;
    public Entry entry1530;
    EntrySet entries;

    public EntryDataForTest(){
        entries = new EntrySet();
        entry0100 = getEntry("01/01/2017 01:00:00", belongingDay);
        entry0200 = getEntry("01/01/2017 02:00:00", belongingDay);
        entry0700 = getEntry("01/01/2017 07:00:00", belongingDay);
        entry0800 = getEntry("01/01/2017 08:00:00", belongingDay);
        entry0815 = getEntry("01/01/2017 08:15:00", belongingDay);
        entry0830 = getEntry("01/01/2017 08:30:00", belongingDay);
        entry0845 = getEntry("01/01/2017 08:45:00", belongingDay);

        entry0900 = getEntry("01/01/2017 09:00:00", belongingDay);
        entry1000= getEntry("01/01/2017 10:00:00", belongingDay);
        entry1015= getEntry("01/01/2017 10:15:00", belongingDay);
        entry1030= getEntry("01/01/2017 10:30:00", belongingDay);
        entry1045= getEntry("01/01/2017 10:45:00", belongingDay);
        entry1100 = getEntry("01/01/2017 11:00:00", belongingDay);
        entry1130 = getEntry("01/01/2017 11:30:00", belongingDay);
        entry1200 = getEntry("01/01/2017 12:00:00", belongingDay);
        entry1215 = getEntry("01/01/2017 12:15:00", belongingDay);
        entry1300 = getEntry("01/01/2017 13:00:00", belongingDay);
        entry1315 = getEntry("01/01/2017 13:15:00", belongingDay);
        entry1325 = getEntry("01/01/2017 13:25:00", belongingDay);
        entry1330 = getEntry("01/01/2017 13:30:00", belongingDay);
        entry1400 = getEntry("01/01/2017 14:00:00", belongingDay);
        entry1415 = getEntry("01/01/2017 14:15:00", belongingDay);
        entry1430 = getEntry("01/01/2017 14:30:00", belongingDay);
        entry1530 = getEntry("01/01/2017 15:30:00", belongingDay);
    }

    private Entry getEntry(String date, String belongingDay){
        date=date+ " CET";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
        sdf.setTimeZone(TimeZone.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        Calendar cal = Calendar.getInstance();

        try {
            Date parsedDate = sdf.parse(date);
            cal.setTime(parsedDate);
            return new Entry(cal, belongingDay);
        } catch (Exception e){

        }
        return new Entry(cal,belongingDay);
    }

}
