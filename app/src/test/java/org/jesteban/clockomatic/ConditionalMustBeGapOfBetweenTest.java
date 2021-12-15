package org.jesteban.clockomatic;

import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalMustBeGapOfBetween;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ConditionalMustBeGapOfBetweenTest {
    EntrySet entries;
    ConditionalMustBeGapOfBetween cmb;
    String belongingDay = "2017/01/01";
    Entry entry0900;
    Entry entry1000;
    Entry entry1015;
    Entry entry1030;
    Entry entry1045;
    Entry entry1100;
    Entry entry1130;
    Entry entry1200;
    Entry entry1215;
    Entry entry1300;
    Entry entry1315;
    Entry entry1325;
    Entry entry1330;
    Entry entry1400;
    Entry entry1415;
    Entry entry1430;
    Entry entry1530;
    Map<Integer, Entry> entryData;

    @Before
    public void setUp() throws Exception {
        // 13h - 15h must be 1h
        cmb = new ConditionalMustBeGapOfBetween( (13*60), 15*60, 1*60);
        entries = new EntrySet();
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

    @Test
    public void already_exists_gap_nothing_to_do() throws ParseException {
        entries.addEntry( entry0900 );
        entries.addEntry( entry1215 );
        entries.addEntry( entry1430 );
        entries.addEntry( entry1530 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        InfoDayEntry result = cmb.apply(infoDayEntry);
        assertEquals(infoDayEntry, result);

    }

    @Test
    // 9:00 - 15:30 Need gap, must break into 9:00-13:00 [13:00-14:00] 14:00-15:30
    public void there_is_no_gap_so_must_break_a_segment() throws ParseException {
        entries.addEntry( entry0900 );
        entries.addEntry( entry1530 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        InfoDayEntry result = cmb.apply(infoDayEntry);
        assertEquals(3,result.getPairsInfo().size());

        assertEquals(entry0900.getRegisterDateAsString(), result.getPairsInfo().get(0).starting.getRegisterDateAsString());
        assertEquals(entry1300.getRegisterDateAsString(), result.getPairsInfo().get(0).finish.getRegisterDateAsString());
        assertEquals(true, result.getPairsInfo().get(0).countHr());

        assertEquals(entry1300.getRegisterDateAsString(), result.getPairsInfo().get(1).starting.getRegisterDateAsString());
        assertEquals(entry1400.getRegisterDateAsString(), result.getPairsInfo().get(1).finish.getRegisterDateAsString());
        assertEquals(false, result.getPairsInfo().get(1).countHr());

        assertEquals(entry1400.getRegisterDateAsString(), result.getPairsInfo().get(2).starting.getRegisterDateAsString());
        assertEquals(entry1530.getRegisterDateAsString(), result.getPairsInfo().get(2).finish.getRegisterDateAsString());
        assertEquals(true, result.getPairsInfo().get(2).countHr());

    }

    @Test
    // 9:00-13:15  13:30-15:30 Gap is small, need to be bigger [13:30-14:15] - 14:15-15:30
    public void there_is_small_gap_must_be_bigger() throws ParseException {
        entries.addEntry( entry0900 );
        entries.addEntry( entry1315 );
        entries.addEntry( entry1330 );
        entries.addEntry( entry1530 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        InfoDayEntry result = cmb.apply(infoDayEntry);
        assertEquals(3,result.getPairsInfo().size());

        assertEquals(entry0900.getRegisterDateAsString(), result.getPairsInfo().get(0).starting.getRegisterDateAsString());
        assertEquals(entry1315.getRegisterDateAsString(), result.getPairsInfo().get(0).finish.getRegisterDateAsString());
        assertEquals(true, result.getPairsInfo().get(0).countHr());

        assertEquals(entry1330.getRegisterDateAsString(), result.getPairsInfo().get(1).starting.getRegisterDateAsString());
        assertEquals(entry1415.getRegisterDateAsString(), result.getPairsInfo().get(1).finish.getRegisterDateAsString());
        assertEquals(false, result.getPairsInfo().get(1).countHr());

        assertEquals(entry1415.getRegisterDateAsString(), result.getPairsInfo().get(2).starting.getRegisterDateAsString());
        assertEquals(entry1530.getRegisterDateAsString(), result.getPairsInfo().get(2).finish.getRegisterDateAsString());
        assertEquals(true, result.getPairsInfo().get(2).countHr());

    }

    @Test
    // 9:00-13:15  13:30-15:30 Gap is small, need to be bigger [13:30-14:15] - 14:15-15:30
    public void there_not_finish_pair() throws ParseException {
        entries.addEntry( entry0900 );
        entries.addEntry( entry1315 );
        entries.addEntry( entry1330 );
        //entries.addEntry( entry1530 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        InfoDayEntry result = cmb.apply(infoDayEntry);
        assertEquals(2,result.getPairsInfo().size());

        assertEquals(entry0900.getRegisterDateAsString(), result.getPairsInfo().get(0).starting.getRegisterDateAsString());
        assertEquals(entry1315.getRegisterDateAsString(), result.getPairsInfo().get(0).finish.getRegisterDateAsString());
        assertEquals(true, result.getPairsInfo().get(0).countHr());

        assertEquals(entry1330.getRegisterDateAsString(), result.getPairsInfo().get(1).starting.getRegisterDateAsString());
        assertEquals(null, result.getPairsInfo().get(1).finish);
        assertEquals(true, result.getPairsInfo().get(1).countHr());


    }
}
