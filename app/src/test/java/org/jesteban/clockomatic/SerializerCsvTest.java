package org.jesteban.clockomatic;

import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.Serializer;

import org.jesteban.clockomatic.store.serializers.SerializerCsv;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SerializerCsvTest {
    private Entry getEntry(String date, String belongingDay){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (Exception e){
            return new Entry(null, belongingDay);
        }
        return new Entry(cal,belongingDay);
    }
    private EntrySet getTestDataMultiDay() {
        EntrySet entries = new EntrySet();


        entries.addEntry(getEntry("1/1/2017 12:00:00", "20170101"));
        entries.addEntry(getEntry("1/1/2017 12:10:00", "20170101"));

        entries.addEntry(getEntry("2/1/2017 13:10:00", "20170102"));
        entries.addEntry(getEntry("2/1/2017 14:10:00", "20170102"));
        return entries;
    }


    @Test
    public void serialize_entry() throws Exception {
        EntrySet entries = new EntrySet();
        entries.addEntry(getEntry("1/1/2017 12:00:00", "20170101"));
        Serializer serializer = new SerializerCsv();
        String data = serializer.serialize(entries);
        System.out.println(data);
        assertEquals("A;20170101;1483268400000;Sun Jan 01 12:00:00 CET 2017;\n", data);
    }

    @Test
    public void deserialize_entry() throws Exception {
        String data = "A;20170101;1483268400000;Sun Jan 01 12:00:00 CET 2017;\n";
        EntrySet entries = new EntrySet();
        entries.addEntry(getEntry("1/1/2017 12:00:00", "20170101"));
        Serializer serializer = new SerializerCsv();
        EntrySet desearilizedEntries = serializer.deserialize(data);
        System.out.println(data);
        System.out.println("original = " + entries);
        System.out.println("desarilzed = " + desearilizedEntries);
        assertTrue(entries.equals(desearilizedEntries));
        //assertEquals( entries,desearilizedEntries);
    }

    @Test
    public void deserialize_2entry() throws Exception {

        EntrySet entries = new EntrySet();
        entries.addEntry(getEntry("1/1/2017 12:00:00", "20170101"));
        entries.addEntry(getEntry("1/1/2017 12:10:00", "20170102"));
        Serializer serializer = new SerializerCsv();
        String data = serializer.serialize(entries);
        EntrySet desearilizedEntries = serializer.deserialize(data);
        System.out.println(data);
        System.out.println("original = " + entries);
        System.out.println("desarilzed = " + desearilizedEntries);
        assertTrue(entries.equals(desearilizedEntries));
        //assertEquals( entries,desearilizedEntries);
    }
}
