package org.jesteban.clockomatic;

import org.jesteban.clockomatic.model.Entry;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EntryTest {
    @Test
    public void emptiness_a_created_entry_is_empty() throws Exception {
        Entry entry = new Entry();
        assertTrue(entry.isEmpty());
    }
    @Test
    public void emptiness_a_created_entry_initialized_is_not_empty() throws Exception {
        Entry entry = new Entry(Calendar.getInstance());
        assertFalse(entry.isEmpty());
    }

    @Test
    public void emptiness_a_created_entry_initialized_with_0_milis_is_not_empty() throws Exception {
        Calendar date =  Calendar.getInstance();
        date.setTimeInMillis(0);
        Entry entry = new Entry(date);
        assertFalse(entry.isEmpty());
    }

    @Test
    public void equals_same_entry_is_equal(){
        Entry entry = new Entry();
        assertEquals(entry, entry);
    }

    @Test
    public void equals_are_not_the_same_a_empty_entry_and_no_empty(){
        Entry entryEmpty = new Entry();
        Entry entry = new Entry( Calendar.getInstance());
        assertFalse(entryEmpty.equals(entry));
    }

    @Test
    public void equals_are_same_entry_if_have_same_value(){
        Calendar cal = Calendar.getInstance();
        Entry entry1 = new Entry(cal);
        Entry entry2 = new Entry(cal);
        assertTrue(entry1.equals(entry2));
    }

    @Test
    public void equals_are_different_entry_if_have_same_value_and_different_belonging_day(){
        Calendar cal = Calendar.getInstance();
        Entry entry1 = new Entry(cal, "day1");
        Entry entry2 = new Entry(cal, "day2");
        assertFalse(entry1.equals(entry2));
    }

    @Test
    public void equals_are_different_entry_if_have_same_value_and_different_kind(){
        Calendar cal = Calendar.getInstance();
        Entry entry1 = new Entry(cal, "day1", Entry.Kind.WORKING,"");
        Entry entry2 = new Entry(cal, "day1",Entry.Kind.OTHER,"");
        assertFalse(entry1.equals(entry2));
    }

    @Test
    public void equals_are_different_entry_if_have_same_value_and_different_comment(){
        Calendar cal = Calendar.getInstance();
        Entry entry1 = new Entry(cal, "day1", Entry.Kind.WORKING,"comment A");
        Entry entry2 = new Entry(cal, "day1",Entry.Kind.WORKING,"comment B");
        assertFalse(entry1.equals(entry2));
    }

    @Test
    public void create_a_entry_with_null_is_like_create_a_empty() throws Exception {
        Entry entryNull = new Entry((Calendar)null);
        Entry entryEmpty = new Entry();
        assertTrue(entryNull.equals(entryEmpty));
    }

    @Test
    public void toString_a_empty_return_empty_string(){
        Entry entryEmpty = new Entry();
        assertTrue(entryEmpty.toString().equals(Entry.EMPTY_STRING));
    }
    @Test
    public void toString_a_non_empty_entry_dont_return_empty_string(){
        Entry entryEmpty = new Entry(Calendar.getInstance());
        assertFalse(entryEmpty.toString().equals(Entry.EMPTY_STRING));
    }

    @Test
    public void OffsetDays_same_belonging_day_offset_is_0(){
        Entry entryEmpty = new Entry(Calendar.getInstance());
        try {
            assertEquals(entryEmpty.getDayOffsetBetweenBelongingDayAndRegister(),  0);
        } catch (ParseException e) {
            assertTrue("throw a exception!!", false);
        }
    }
    private Calendar createCalendar(int year, int month, int day, int hour, int minute){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        return cal;
    }
    @Test
    public void OffsetDays_belonging_day_is_previous_offset_is_plus1(){
        Calendar cal = createCalendar(2018,1,2,23,59);
        Calendar calBd = createCalendar(2018,1,1,01,59);
        try {
            Entry.BelongingDay bd = new Entry.BelongingDay(calBd);
            Entry entryEmpty = new Entry(cal, bd.getDay());
            assertEquals(entryEmpty.getDayOffsetBetweenBelongingDayAndRegister(),  1);
        } catch (ParseException e) {
            assertTrue("throw a exception!!", false);
        }
    }

    @Test
    public void OffsetDays_belonging_day_is_previous_offset_is_minus1(){
        Calendar cal = createCalendar(2018,1,1,23,59);
        Calendar calBd = createCalendar(2018,1,2,01,59);
        try {
            Entry.BelongingDay bd = new Entry.BelongingDay(calBd);
            Entry entryEmpty = new Entry(cal, bd.getDay());
            assertEquals(entryEmpty.getDayOffsetBetweenBelongingDayAndRegister(),  -1);
        } catch (ParseException e) {
            assertTrue("throw a exception!!", false);
        }
    }
}
