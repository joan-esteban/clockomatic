package org.jesteban.clockomatic;

import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InfoDayEntryTest {

    private Entry getEntry(String date, String belongingDay){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        Calendar cal = Calendar.getInstance();

        try {
            return new Entry(date, belongingDay);
        } catch (Exception e){

        }
        return new Entry(cal,belongingDay);
    }

    @Test
    public void test_1entry_is_unfinished(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        entries.addEntry( getEntry("1/1/2017 09:00:00", belongingDay));
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        assertTrue(infoDayEntry.isUnfinishDay());
    }

    @Test
    public void test_2_times_same_entry_object_is_unfinished(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry = getEntry("1/1/2017 09:00:00", belongingDay);
        entries.addEntry( entry );
        entries.addEntry( entry );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        assertTrue(infoDayEntry.isUnfinishDay());
    }

    @Test
    public void test_2_times_same_entry_distint_object_is_unfinished(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 09:00:00", belongingDay);
        entries.addEntry( entry1 );
        entries.addEntry( entry2 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        assertTrue(infoDayEntry.isUnfinishDay());
    }

    @Test
    public void test_get_pairs_for_1_entries_are_1_pair(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        entries.addEntry( entry1 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> pairs= infoDayEntry.getPairsInfo();
        assertEquals(1, pairs.size() );
    }

    @Test
    public void test_get_pairs_for_2_entries_are_1_pair(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 10:00:00", belongingDay);
        entries.addEntry( entry1 );
        entries.addEntry( entry2 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> pairs= infoDayEntry.getPairsInfo();
        assertEquals(1, pairs.size() );
    }

    @Test
    public void test_get_pairs_for_2_entries_have_right_values(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 10:00:00", belongingDay);
        entries.addEntry( entry1 );
        entries.addEntry( entry2 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> pairs= infoDayEntry.getPairsInfo();
        assertEquals(entry1, pairs.get(0).starting );
        assertEquals(entry2, pairs.get(0).finish);
    }

    @Test
    public void test_get_right_working_time_for_a_full_pair(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 10:00:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        assertEquals(60, pair.getMinutesWorkingTime() );
    }





}
