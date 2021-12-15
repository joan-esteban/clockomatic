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
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InfoDayEntryTest {
    EntrySet entries;
    String belongingDay = "01/01/2017";
    Entry entry0900;
    Entry entry1000;
    Entry entry1015;
    Entry entry1030;
    Entry entry1045;
    Entry entry1100;
    Entry entry1130;
    Entry entry1200;
    Entry entry1215;
    Entry entry1315;
    Entry entry1325;
    Entry entry1400;
    Entry entry1430;
    Entry entry1530;

    @Before
    public void setUp() throws Exception {
        // 13h - 15h must be 1h
        entries = new EntrySet();
        entry0900 = new Entry("01/01/2017 09:00:00", belongingDay);
        entry1000= new Entry("01/01/2017 10:00:00", belongingDay);
        entry1015= new Entry("01/01/2017 10:15:00", belongingDay);
        entry1030= new Entry("01/01/2017 10:30:00", belongingDay);
        entry1045= new Entry("01/01/2017 10:45:00", belongingDay);
        entry1100 = new Entry("01/01/2017 11:00:00", belongingDay);
        entry1130 = new Entry("01/01/2017 11:30:00", belongingDay);
        entry1200 = new Entry("01/01/2017 12:00:00", belongingDay);
        entry1215 = new Entry("01/01/2017 12:15:00", belongingDay);
        entry1315 = new Entry("01/01/2017 13:15:00", belongingDay);
        entry1325 = new Entry("01/01/2017 13:25:00", belongingDay);
        entry1400 = new Entry("01/01/2017 14:00:00", belongingDay);
        entry1430 = new Entry("01/01/2017 14:30:00", belongingDay);
        entry1530 = new Entry("01/01/2017 15:30:00", belongingDay);

    }


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

    @Test(expected = IllegalArgumentException.class)
    public void test_create_InfoDayEntry_with_entries_not_belonging_same_day_throw_exception() throws ParseException {
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2018 09:00:00", "01/01/2018");
        entries.addEntry( entry1 );
        entries.addEntry( entry2 );
        InfoDayEntry infoDayEntry = new InfoDayEntry(entries, belongingDay);
    }


    @Test
    public void test_1entry_is_unfinished() throws ParseException {
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        entries.addEntry( getEntry("1/1/2017 09:00:00", belongingDay));
        InfoDayEntry infoDayEntry= null;

        infoDayEntry = new InfoDayEntry(entries, belongingDay);

        assertTrue(infoDayEntry.isUnfinishDay());
    }

    @Test
    public void test_2_times_same_entry_object_is_unfinished() throws ParseException {
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry = getEntry("1/1/2017 09:00:00", belongingDay);
        entries.addEntry( entry );
        entries.addEntry( entry );
        InfoDayEntry infoDayEntry= null;

        infoDayEntry = new InfoDayEntry(entries, belongingDay);

        assertTrue(infoDayEntry.isUnfinishDay());
    }

    @Test
    public void test_2_times_same_entry_distint_object_is_unfinished() throws ParseException {
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 09:00:00", belongingDay);
        entries.addEntry( entry1 );
        entries.addEntry( entry2 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        assertTrue(entry1.equals(entry2));
        assertTrue(infoDayEntry.isUnfinishDay());
    }

    @Test
    public void test_get_pairs_for_1_entries_are_1_pair() throws ParseException {
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        entries.addEntry( entry1 );
        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> pairs= infoDayEntry.getPairsInfo();
        assertEquals(1, pairs.size() );
    }

    @Test
    public void test_get_pairs_for_2_entries_are_1_pair() throws ParseException {
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
    public void test_get_pairs_for_2_entries_have_right_values() throws ParseException {
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

    @Test
    public void test_split_partial(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, null);
        List<InfoDayEntry.PairedEntry> lst = InfoDayEntry.PairedEntry.split(pair, entry1.getRegisterDate());
        assertEquals("it must return same entry because is not complete", lst.get(0).toString(), pair.toString());
    }

    @Test
    public void test_split_no_need_to_split_beacause_is_in_past(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 10:00:00", belongingDay);
        Entry entrySplitPoint = getEntry("1/1/2017 11:00:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);

        List<InfoDayEntry.PairedEntry> lst = InfoDayEntry.PairedEntry.split(pair, entrySplitPoint.getRegisterDate());

        assertEquals("it must return same entry because is not complete", lst.get(0).toString(), pair.toString());
    }

    @Test
    public void test_split_no_need_to_split_beacause_is_in_future(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("1/1/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("1/1/2017 10:00:00", belongingDay);
        Entry entrySplitPoint = getEntry("1/1/2017 08:00:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        List<InfoDayEntry.PairedEntry> lst = InfoDayEntry.PairedEntry.split(pair, entrySplitPoint.getRegisterDate());
        assertEquals("it must return same entry because is not complete", lst.get(0).toString(), pair.toString());
    }

    @Test
    public void test_split_need_to_split(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = getEntry("01/01/2017 10:30:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        List<InfoDayEntry.PairedEntry> lst = InfoDayEntry.PairedEntry.split(pair, entrySplitPoint.getRegisterDate());
        assertEquals("it must return 2 paired because is splitted", lst.size(), 2);
        assertEquals("01/01/2017 09:00:00", lst.get(0).starting.getRegisterDateAsString());
        assertEquals("01/01/2017 10:30:00", lst.get(0).finish.getRegisterDateAsString());

        assertEquals("01/01/2017 10:30:00", lst.get(1).starting.getRegisterDateAsString());
        assertEquals("01/01/2017 11:00:00", lst.get(1).finish.getRegisterDateAsString());
    }

    @Test
    public void test_split_need_to_split_on_bondaries(){
        EntrySet entries = new EntrySet();
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = entry2;
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        List<InfoDayEntry.PairedEntry> lst = InfoDayEntry.PairedEntry.split(pair, entrySplitPoint.getRegisterDate());
        assertEquals("it must return 1 paired because is splitted", lst.size(), 1);
        assertEquals("01/01/2017 09:00:00", lst.get(0).starting.getRegisterDateAsString());
        assertEquals("01/01/2017 11:00:00", lst.get(0).finish.getRegisterDateAsString());

    }

    @Test
    public void test_in_contained(){
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = getEntry("01/01/2017 10:30:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        assertTrue(pair.isContained(entrySplitPoint.getRegisterDate()));
    }

    @Test
    public void test_in_not_contained_because_is_on_bondaries(){
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = entry2;
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        assertFalse(pair.isContained(entrySplitPoint.getRegisterDate()));
    }

    @Test
    public void test_is_in_future_because_is_on_finish_bondaries(){
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = getEntry("01/01/2017 10:30:00", belongingDay);
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        assertTrue(pair.isInFuture(entrySplitPoint.getRegisterDate()));
        assertTrue(pair.isInPast(entrySplitPoint.getRegisterDate()));
    }

    @Test
    public void test_is_in_past_because_is_on_starting_bondaries(){
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = entry1;
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        assertTrue(pair.isInPast(entrySplitPoint.getRegisterDate()));
        assertFalse(pair.isInFuture(entrySplitPoint.getRegisterDate()));

    }

    @Test
    public void test_is_in_both_because_is_contained(){
        String belongingDay = "01/01/2017";
        Entry entry1 = getEntry("01/01/2017 09:00:00", belongingDay);
        Entry entry2 = getEntry("01/01/2017 11:00:00", belongingDay);
        Entry entrySplitPoint = entry1;
        InfoDayEntry.PairedEntry pair = new InfoDayEntry.PairedEntry(entry1, entry2);
        assertTrue(pair.isInPast(entrySplitPoint.getRegisterDate()));
        assertFalse(pair.isInFuture(entrySplitPoint.getRegisterDate()));

    }


    @Test
    //   |-----=======-------|.... (==== is segment returned)
    //  9h    10h   10:30    11h
    public void test_segment_subset_pair() throws ParseException {
        Entry entrySegmentStart= entry1000;
        Entry entrySegmentFinish= entry1030;
        entries.addEntry(entry0900);
        entries.addEntry(entry1100);

        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> result = infoDayEntry.getTimeSegment(entrySegmentStart.getRegisterDate(),entrySegmentFinish.getRegisterDate() ).getPairsInfo();
        assertEquals(1, result.size());
        assertEquals(entrySegmentStart.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(entrySegmentFinish.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());

    }

    @Test
    // |---====|.... (==== is segment returned)
    public void test_segment_is_a_upper_part_of_1_pair() throws ParseException {
        Entry entrySegmentStart= entry1030;
        Entry entrySegmentFinish= entry1100;
        entries.addEntry(entry0900);
        entries.addEntry(this.entry1045);
        entries.addEntry(this.entry1100);
        entries.addEntry(this.entry1325);

        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> result = infoDayEntry.getTimeSegment(entrySegmentStart.getRegisterDate(),entrySegmentFinish.getRegisterDate() ).getPairsInfo();
        assertEquals(1, result.size());
        assertEquals(entrySegmentStart.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(entry1030.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(entry1045.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());

    }

    @Test
    // ....|========________| (==== is segment returned)
    // 9   10:30   10:45
    public void test_segment_is_a_lower_part_of_1_pair() throws ParseException {
        Entry entrySegmentStart= entry0900;
        Entry entrySegmentFinish= entry1045;
        entries.addEntry(entry1030);
        entries.addEntry(this.entry1100);


        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> result = infoDayEntry.getTimeSegment(entrySegmentStart.getRegisterDate(),entrySegmentFinish.getRegisterDate() ).getPairsInfo();
        assertEquals(1, result.size());
        assertEquals(entry1030.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(entry1045.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());

    }

    @Test
    // ....|========|..... (==== is segment returned)
    // 9   10:30   10:45
    public void test_segment_superset_1_pair() throws ParseException {
        Entry entrySegmentStart= entry0900;
        Entry entrySegmentFinish= entry1200;
        entries.addEntry(entry1030);
        entries.addEntry(this.entry1100);


        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> result = infoDayEntry.getTimeSegment(entrySegmentStart.getRegisterDate(),entrySegmentFinish.getRegisterDate() ).getPairsInfo();
        assertEquals(1, result.size());
        assertEquals(entry1030.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(entry1100.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());

    }

    @Test
    // ....|========|..... (==== is segment returned)
    // 9   10:30   10:45
    public void test_segment_no_intersection() throws ParseException {
        Entry entrySegmentStart= entry0900;
        Entry entrySegmentFinish= this.entry1000;
        entries.addEntry(entry1030);
        entries.addEntry(this.entry1100);


        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> result = infoDayEntry.getTimeSegment(entrySegmentStart.getRegisterDate(),entrySegmentFinish.getRegisterDate() ).getPairsInfo();
        assertEquals(0, result.size());

    }

    @Test
    // ....|========|..... (==== is segment returned)
    // 9   10:30   10:45
    public void test_segment_multi_pair() throws ParseException {
        Entry entrySegmentStart= entry1015;
        Entry entrySegmentFinish= this.entry1400;
        entries.addEntry(entry1000);         entries.addEntry(this.entry1030);
        entries.addEntry(this.entry1045);    entries.addEntry(this.entry1200);
        entries.addEntry(this.entry1315);    entries.addEntry(this.entry1325);

        InfoDayEntry infoDayEntry= new InfoDayEntry(entries, belongingDay);
        List<InfoDayEntry.PairedEntry> result = infoDayEntry.getTimeSegment(entrySegmentStart.getRegisterDate(),entrySegmentFinish.getRegisterDate() ).getPairsInfo();
        assertEquals(3, result.size());

        assertEquals(entry1015.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(entry1030.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());

        assertEquals(entry1045.getRegisterDateAsString(), result.get(1).starting.getRegisterDateAsString());
        assertEquals(entry1200.getRegisterDateAsString(), result.get(1).finish.getRegisterDateAsString());
    }



}
