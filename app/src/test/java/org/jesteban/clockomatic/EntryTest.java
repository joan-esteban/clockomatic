package org.jesteban.clockomatic;

import org.jesteban.clockomatic.model.Entry;
import org.junit.Test;

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
}
