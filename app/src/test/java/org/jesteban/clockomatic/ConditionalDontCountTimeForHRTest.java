package org.jesteban.clockomatic;


import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR.Condition.GREAT;
import static org.jesteban.clockomatic.model.work_schedule.conditionals.ConditionalDontCountTimeForHR.Condition.LESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConditionalDontCountTimeForHRTest {
    EntryDataForTest data;
    ConditionalDontCountTimeForHR condLower;
    ConditionalDontCountTimeForHR condUpper;

    @Before
    public void setUp() throws Exception {
        data = new EntryDataForTest();
        condLower = new ConditionalDontCountTimeForHR(LESS,(8*60)+30 );
        condLower = new ConditionalDontCountTimeForHR(LESS,(8*60)+30 );
        condUpper = new ConditionalDontCountTimeForHR(GREAT,(10*60)+00 );
    }

    @Test
    public void cut_lower_all_outisde_period() throws ParseException {
        InfoDayEntry.PairedEntry paired = new InfoDayEntry.PairedEntry(data.entry0100,data.entry0200 );


        List<InfoDayEntry.PairedEntry> result = condLower.apply(paired);
        assertEquals(1,result.size());

        assertEquals(data.entry0100.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(data.entry0200.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());
        assertEquals(false, result.get(0).countHr());



    }

    @Test
    public void apply_both_all_outisde_period() throws ParseException {
        InfoDayEntry.PairedEntry paired = new InfoDayEntry.PairedEntry(data.entry0100,data.entry0200 );


        List<InfoDayEntry.PairedEntry> result = condLower.apply(paired);
        result = condUpper.apply(result.get(0));
        assertEquals(1,result.size());

        assertEquals(data.entry0100.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(data.entry0200.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());
        assertEquals(false, result.get(0).countHr());



    }

    @Test
    public void cut_lower_dont_count_period() throws ParseException {
        InfoDayEntry.PairedEntry paired = new InfoDayEntry.PairedEntry(data.entry0700,data.entry0900 );


        List<InfoDayEntry.PairedEntry> result = condLower.apply(paired);
        assertEquals(2,result.size());

        assertEquals(data.entry0700.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(data.entry0830.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());
        assertEquals(false, result.get(0).countHr());

        assertEquals(data.entry0830.getRegisterDateAsString(), result.get(1).starting.getRegisterDateAsString());
        assertEquals(data.entry0900.getRegisterDateAsString(), result.get(1).finish.getRegisterDateAsString());
        assertEquals(true, result.get(1).countHr());


    }

    @Test
    public void cut_upper_dont_count_period() throws ParseException {
        InfoDayEntry.PairedEntry paired = new InfoDayEntry.PairedEntry(data.entry0700,data.entry1200 );


        List<InfoDayEntry.PairedEntry> result = condUpper.apply(paired);
        assertEquals(2,result.size());

        assertEquals(data.entry0700.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(data.entry1000.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());
        assertEquals(true, result.get(0).countHr());

        assertEquals(data.entry1000.getRegisterDateAsString(), result.get(1).starting.getRegisterDateAsString());
        assertEquals(data.entry1200.getRegisterDateAsString(), result.get(1).finish.getRegisterDateAsString());
        assertEquals(false, result.get(1).countHr());


    }

    @Test
    public void cut_both_dont_count_period() throws ParseException {
        InfoDayEntry.PairedEntry paired = new InfoDayEntry.PairedEntry(data.entry0700,data.entry1200 );


        List<InfoDayEntry.PairedEntry> lower = condLower.apply(paired);
        List<InfoDayEntry.PairedEntry> result = new ArrayList<>();
        for (InfoDayEntry.PairedEntry pair : lower){
            result.addAll(condUpper.apply(pair));
        }
        assertEquals(3,result.size());

        assertEquals(data.entry0700.getRegisterDateAsString(), result.get(0).starting.getRegisterDateAsString());
        assertEquals(data.entry0830.getRegisterDateAsString(), result.get(0).finish.getRegisterDateAsString());
        assertEquals(false, result.get(0).countHr());

        assertEquals(data.entry0830.getRegisterDateAsString(), result.get(1).starting.getRegisterDateAsString());
        assertEquals(data.entry1000.getRegisterDateAsString(), result.get(1).finish.getRegisterDateAsString());
        assertEquals(true, result.get(1).countHr());

        assertEquals(data.entry1000.getRegisterDateAsString(), result.get(2).starting.getRegisterDateAsString());
        assertEquals(data.entry1200.getRegisterDateAsString(), result.get(2).finish.getRegisterDateAsString());
        assertEquals(false, result.get(2).countHr());


    }

}

