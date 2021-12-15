package org.jesteban.clockomatic;

import android.support.test.runner.AndroidJUnit4;

import org.jesteban.clockomatic.helpers.CalendarHelper;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.WorkScheduleDb;
import org.jesteban.clockomatic.model.work_schedule.WorkScheduleContinuos;
import org.jesteban.clockomatic.model.work_schedule.WorkSchedulePartTime;
import org.junit.runner.RunWith;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.store.ClockmaticDb;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)

public class WorkScheduleDbTest {
    ClockmaticDb db = null;
    WorkScheduleDb workScheduleDb = null;
    Calendar date = Calendar.getInstance();
    Entry.BelongingDay belongingDay = new Entry.BelongingDay(date);
    int companyId = 1;
    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new ClockmaticDb(appContext,null,false);
        workScheduleDb = new WorkScheduleDb(db);
    }

    @Test
    public void get_not_exists_record_must_return_null() throws Exception {
        Calendar date = Calendar.getInstance();
        WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( null,ws);
    }

    @Test
    public void set_not_exists_record() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        WorkScheduleContract wsReturn = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( (int)2,wsReturn.getId());
    }
    @Test
    public void undo_set_not_exists_record() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        UndoAction undo = workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        WorkScheduleContract wsReturn = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( (int)2,wsReturn.getId());
        undo.execute();
        wsReturn = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( null, wsReturn);
    }

    @Test
    public void undo_set_exists_record() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        WorkScheduleContract ws2 = new WorkScheduleContinuos(3,"3");
        workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        UndoAction undo = workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws2);
        WorkScheduleContract wsReturn = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( (int)3,wsReturn.getId());

        undo.execute();

        wsReturn = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( (int)2,wsReturn.getId());
    }


    @Test
    public void set_existing_record() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        WorkScheduleContract wsReturn = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( (int)2,wsReturn.getId());
    }

    @Test
    public void delete_not_exists_record_must_return_no_undo() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        UndoAction undo = workScheduleDb.deleteWorkScheduleForDay(companyId, belongingDay);
        assertEquals( null,undo);
    }

    @Test
    public void delete_exists_record() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        UndoAction undo = workScheduleDb.deleteWorkScheduleForDay(companyId, belongingDay);
        ws = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertEquals( null,ws);
    }

    @Test
    public void undo_delete_exists_record() throws Exception {
        //WorkScheduleContract ws = workScheduleDb.getWorkScheduleForDay(companyId,belongingDay);
        WorkScheduleContract ws = new WorkScheduleContinuos(2,"2");
        workScheduleDb.setWorkScheduleForDay(companyId, belongingDay, ws);
        UndoAction undo = workScheduleDb.deleteWorkScheduleForDay(companyId, belongingDay);
        undo.execute();
        ws = workScheduleDb.getWorkScheduleForDay(companyId, belongingDay);
        assertNotEquals( null,ws);
    }

}
