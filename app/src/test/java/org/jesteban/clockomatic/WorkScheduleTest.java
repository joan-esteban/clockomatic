package org.jesteban.clockomatic;

import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.work_schedule.WorkSchedule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class WorkScheduleTest {
    WorkScheduleContract wsc;
    String wscName = "wscName";
    int id = 1234;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        wsc = new WorkSchedule(id, wscName);
    }

    @Test
    public void test_getName(){
        assertEquals(wscName, wsc.getName());
    }


    @Test
    public void test_getInfoDay_without_pairs_returns_no_paired_entries(){
        Calendar cal = Calendar.getInstance();
        InfoDayEntry res = wsc.getInfoDay(new InfoDayEntry(new Entry.BelongingDay(cal)) );
        assertEquals(0, res.getPairsInfo().size());
    }

    //@Test(expected = IllegalArgumentException.class)
    @Test
    public void test_getInfoDay_empty_returns_it_self(){
        Calendar cal = Calendar.getInstance();
        InfoDayEntry infoDay = new InfoDayEntry();
        InfoDayEntry res = wsc.getInfoDay( infoDay );
        assertEquals(infoDay, res);
    }

    @Test
    public void test_WorkSchedule_empty_is_type_FREESTYLE(){
        WorkSchedule wsc_empty = new WorkSchedule(id, wscName);
        assertEquals(WorkScheduleContract.WorkScheduleType.FREESTYLE, wsc_empty.getTypeId());
    }

    @Test
    public void test_WorkSchedule_with_expecting_working_time_is_type_CONTINOUS(){
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName).setExpectedWorkingTimeInMinutes(100);
        assertEquals(WorkScheduleContract.WorkScheduleType.CONTINOUS, wsc_with_expecting_time.getTypeId());
    }

    @Test
    public void test_WorkSchedule_with_expecting_working_time_return_this_time(){
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName).setExpectedWorkingTimeInMinutes(100);
        assertEquals(100, wsc_with_expecting_time.getExpectedWorkingTimeInMinutes());
    }

    @Test
    public void test_WorkSchedule_with_expecting_working_time_negative_is_type_FREESTYLE(){
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName).setExpectedWorkingTimeInMinutes(-100);
        assertEquals(WorkScheduleContract.WorkScheduleType.FREESTYLE, wsc_with_expecting_time.getTypeId());
    }

    @Test
    public void test_WorkSchedule_with_expecting_working_time_negative_return_UNDEFINED(){
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName).setExpectedWorkingTimeInMinutes(-100);
        assertEquals(WorkSchedule.WORKING_TIME_UNDEFINED, wsc_with_expecting_time.getExpectedWorkingTimeInMinutes());
    }

    @Test
    public void test_WorkSchedule_isExpectedWorkingTimeAchieved_returns_false_if_havent_work_enough() throws ParseException {

        Entry entryEnter = new Entry(createCalendar(2019,10,22,8,00));
        Entry entryLeave = new Entry(createCalendar(2019,10,22,9,00));
        EntrySet entrySet = new EntrySet();
        entrySet.addEntry(entryEnter);
        entrySet.addEntry(entryLeave);
        InfoDayEntry infoDay = new InfoDayEntry(entrySet, entryEnter.getBelongingDay());
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName).setExpectedWorkingTimeInMinutes( (7*60) + 1);
        InfoDayEntry res = wsc_with_expecting_time.getInfoDay(infoDay);

        assertEquals(1*60, res.getTotalMinuteOfWorkForHR());
        assertEquals(false, res.isExpectedWorkingTimeAchieved());
    }

    @Test
    public void test_WorkSchedule_isExpectedWorkingTimeAchieved_returns_true_if_have_work_exactly_expected_time() throws ParseException {

        Entry entryEnter = new Entry(createCalendar(2019,10,22,8,00));
        Entry entryLeave = new Entry(createCalendar(2019,10,22,9,00));
        EntrySet entrySet = new EntrySet();
        entrySet.addEntry(entryEnter);
        entrySet.addEntry(entryLeave);
        InfoDayEntry infoDay = new InfoDayEntry(entrySet, entryEnter.getBelongingDay());
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName).setExpectedWorkingTimeInMinutes( (1*60) );
        InfoDayEntry res = wsc_with_expecting_time.getInfoDay(infoDay);

        assertEquals(1*60, res.getTotalMinuteOfWorkForHR());
        assertEquals(true, res.isExpectedWorkingTimeAchieved());
    }

    @Test
    public void test_WorkSchedule_isExpectedWorkingTimeAchieved_with_earlyEnter_is_calculated_time_right() throws ParseException {

        Entry entryEnter = new Entry(createCalendar(2019,10,22,8,00));
        Entry entryLeave = new Entry(createCalendar(2019,10,22,9,00));
        EntrySet entrySet = new EntrySet();
        entrySet.addEntry(entryEnter);
        entrySet.addEntry(entryLeave);
        InfoDayEntry infoDay = new InfoDayEntry(entrySet, entryEnter.getBelongingDay());
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName)
                        .setExpectedWorkingTimeInMinutes( (1*60) )
                        .setEarlyEnterTimeInMinutes(8*60 + 30); // 8:30

        InfoDayEntry res = wsc_with_expecting_time.getInfoDay(infoDay);

        // 8:00 - 9:00 but early is 8:30 so only count 30 minutes
        assertEquals(30, res.getTotalMinuteOfWorkForHR());
        assertEquals(false, res.isExpectedWorkingTimeAchieved());
    }

    @Test
    public void test_WorkSchedule_get_best_leave_entry_for_empty_day() throws ParseException {
        Calendar now = createCalendar(2019,10,22,8,00);
        Entry entryEnter = new Entry(createCalendar(2019,10,22,8,00));
        InfoDayEntry infoDay = new InfoDayEntry(new EntrySet(), entryEnter.getBelongingDay());

        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName)
                .setExpectedWorkingTimeInMinutes( (1*60) );
        //        .setEarlyEnterTimeInMinutes(8*60 + 30); // 8:30

        InfoDayEntry res = wsc_with_expecting_time.getInfoDay(infoDay, now);
        Entry bestLeave = res.getBestEntryLeavingWork();

        // 8:00 - 9:00 but early is 8:30 so only count 30 minutes
        assertEquals("WORKING 2019/11/22 / 2019/11/22 09:00:0 CET desc:[best fit", bestLeave.toString());
    }

    @Test
    public void test_WorkSchedule_isExpectedWorkingTimeAchieved_with_earlyEnter_calculate_best_leave_entry() throws ParseException {

        Entry entryEnter = new Entry(createCalendar(2019,10,22,8,00));
        Entry entryLeave = new Entry(createCalendar(2019,10,22,9,00));
        EntrySet entrySet = new EntrySet();
        entrySet.addEntry(entryEnter);
        entrySet.addEntry(entryLeave);
        InfoDayEntry infoDay = new InfoDayEntry(entrySet, entryEnter.getBelongingDay());
        WorkSchedule wsc_with_expecting_time = new WorkSchedule(id, wscName)
                .setExpectedWorkingTimeInMinutes( (1*60) )
                .setEarlyEnterTimeInMinutes(8*60 + 30); // 8:30

        InfoDayEntry res = wsc_with_expecting_time.getInfoDay(infoDay);
        Entry bestLeave = res.getBestEntryLeavingWork();

        // 8:00 - 9:00 but early is 8:30 so only count 30 minutes
        assertEquals("abc", bestLeave.toString());
    }



    private Calendar createCalendar(int year, int month, int day, int hour, int minute){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);
        return cal;
    }
}
