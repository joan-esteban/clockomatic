package org.jesteban.clockomatic.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import org.jesteban.clockomatic.model.work_schedule.WorkScheduleContinuos;
import org.jesteban.clockomatic.model.work_schedule.WorkSchedulePartTime;
import org.jesteban.clockomatic.store.ClockmaticDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Logger;

public class WorkScheduleDb extends Observable {
    private static final Logger LOGGER = Logger.getLogger(WorkScheduleDb.class.getName());
    private ClockmaticDb clockmaticDb = null;
    private static final int DEFAULT_WORK_SCHEDULE_ID=0;
    private Map<Integer,WorkScheduleContract> availableWorkSchedule = new HashMap<>();

    public WorkScheduleDb(ClockmaticDb db){
        clockmaticDb = db;
        availableWorkSchedule.put(0, new WorkSchedulePartTime(0, "SPLIT SHIFT (8:16)"));
        availableWorkSchedule.put(1, new WorkScheduleContinuos(1, "INTENSIVE (7h)"));
    }

    public int getWorkScheduleIdForDay(int companyId, String day, int defaultValue){
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { Integer.toString(companyId), day};
        Cursor cursor = db.query(ClockmaticDb.TableBelongingDay.TABLE_NAME,
                null,
                ClockmaticDb.TableBelongingDay.FIELD_COMPANY_ID + " = ? "
                        + " AND " + ClockmaticDb.TableBelongingDay.FIELD_BELONGING_DAY + "  = ? ",
                selectionArgs,
                null, null, null);
        if (!cursor.moveToFirst()) return defaultValue;
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ClockmaticDb.TableBelongingDay.FIELD_WORK_SCHEDULE_ID));
        return id;
    }

    public WorkScheduleContract getWorkSchedule(int id){
        return availableWorkSchedule.get(id);
    }


    public WorkScheduleContract getWorkScheduleForDay(int companyId, Entry.BelongingDay day){
        int id = getWorkScheduleIdForDay(companyId, day.getDay(),-1);
        if (id<0){
            LOGGER.warning(String.format("Dont found workScheduler for company=%d day=%s return null", companyId, day.getDay()));
            //id = DEFAULT_WORK_SCHEDULE_ID;
            return null;
        }
        return getWorkSchedule(id);
    }

    public static class WorkScheduleData{
        final int id;
        final String name;
        public WorkScheduleData(int id, String name){
            this.id = id;
            this.name = name;
        }
        public int getId(){return id;}
        public String getName(){return name;}
    }

    public List<WorkScheduleData> getAvailableWorkSchedule(int companyId){
        List<WorkScheduleData> result = new ArrayList<>();
        for (Map.Entry<Integer,WorkScheduleContract> entry : availableWorkSchedule.entrySet()){
            result.add( new WorkScheduleData(entry.getValue().getId(), entry.getValue().getName()) );
        }
        return result;
    }

    private ContentValues getContentValues(int companyId, Entry.BelongingDay day, int id){
        ContentValues values = new ContentValues();
        values.put(ClockmaticDb.TableBelongingDay.FIELD_COMPANY_ID,companyId);
        values.put(ClockmaticDb.TableBelongingDay.FIELD_BELONGING_DAY,day.getDay());
        values.put(ClockmaticDb.TableBelongingDay.FIELD_WORK_SCHEDULE_ID, id );
        return values;
    }

    public  UndoAction insertWorkScheduleForDay(int companyId, Entry.BelongingDay day,int wsId){
        UndoAction undoAction = new UndoAction();
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        ContentValues values = getContentValues(companyId, day, wsId);
        db.insertOrThrow(ClockmaticDb.TableBelongingDay.TABLE_NAME, null, values);
        undoAction.actions.add(new UndoInsertWorkSchedule(this,companyId,day));
        return undoAction;
    }

    public  UndoAction updateWorkScheduleForDay(int companyId, Entry.BelongingDay day,int wsId){
        WorkScheduleContract oldWs = getWorkScheduleForDay(companyId,day);
        if  ( (oldWs!=null) && oldWs.getId()==wsId){
            // Nothing to do
            return null;
        }
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        ContentValues values = getContentValues(companyId, day, wsId);;
        String [] selectionArgs = { Integer.toString(companyId ), day.getDay()};
        db.update(ClockmaticDb.TableBelongingDay.TABLE_NAME,
                values,
                ClockmaticDb.TableBelongingDay.FIELD_COMPANY_ID + " = ? "
                            + " AND " + ClockmaticDb.TableBelongingDay.FIELD_BELONGING_DAY + " = ? ",
                selectionArgs
        );
        UndoAction undoAction = new UndoAction();
        undoAction.actions.add(new UndoUpdateWorkSchedule(this,companyId,day,oldWs));
        return undoAction;
    }

    public  UndoAction deleteWorkScheduleForDay(int companyId, Entry.BelongingDay day){
        UndoAction undoAction = new UndoAction();
        WorkScheduleContract ws = getWorkScheduleForDay(companyId,day);
        if (ws==null){
            LOGGER.warning(String.format("Nothing to remove for company=%d day=%s", companyId, day.getDay()));
            return null;
        }
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        String [] whereArgs = { Integer.toString(companyId ), day.getDay()};
        db.delete(ClockmaticDb.TableBelongingDay.TABLE_NAME,
                ClockmaticDb.TableBelongingDay.FIELD_COMPANY_ID + " = ? "
                        + " AND " + ClockmaticDb.TableBelongingDay.FIELD_BELONGING_DAY + " = ? ",
                whereArgs);
        undoAction.actions.add(new UndoDeleteWorkSchedule(this,companyId,day,ws));
        return undoAction;
    }

    public  UndoAction setWorkScheduleForDay(int companyId, Entry.BelongingDay day,WorkScheduleContract ws){
        if (ws==null){
            LOGGER.warning(String.format("Nothing to set for company=%d day=%s", companyId, day.getDay()));
            return null;
        }
        if (!availableWorkSchedule.containsKey(ws.getId())){
            availableWorkSchedule.put(ws.getId(),ws);
        }
        return setWorkScheduleForDay(companyId, day, ws.getId());
    }

    public  UndoAction setWorkScheduleForDay(int companyId, Entry.BelongingDay day,int wsId){
        try {
            return insertWorkScheduleForDay(companyId, day, wsId);
        } catch (SQLiteConstraintException e){
            // Already exist try update
            return updateWorkScheduleForDay(companyId, day, wsId);
        }
    }

    // Undo Helpers ////////////////////////////////////////////////////////////////////////////////
    public class UndoInsertWorkSchedule implements UndoAction.UndoActionStepContract {
        WorkScheduleDb workScheduleDb = null;
        int companyId;
        Entry.BelongingDay day;

        public UndoInsertWorkSchedule(WorkScheduleDb workScheduleDb, int companyId, Entry.BelongingDay day) {
            this.workScheduleDb = workScheduleDb;
            this.companyId = companyId;
            this.day = new Entry.BelongingDay(day);
        }
        @Override
        public void executeUndo() {
            workScheduleDb.deleteWorkScheduleForDay(companyId, day);
        }
    }
    // Undo Helpers ////////////////////////////////////////////////////////////////////////////////
    public class UndoDeleteWorkSchedule implements UndoAction.UndoActionStepContract {
        WorkScheduleDb workScheduleDb = null;
        int companyId;
        Entry.BelongingDay day;
        WorkScheduleContract ws;

        public UndoDeleteWorkSchedule(WorkScheduleDb workScheduleDb, int companyId, Entry.BelongingDay day,WorkScheduleContract ws) {
            this.workScheduleDb = workScheduleDb;
            this.companyId = companyId;
            this.day = new Entry.BelongingDay(day);
            this.ws = ws;
        }
        @Override
        public void executeUndo() {
            workScheduleDb.insertWorkScheduleForDay(companyId, day,ws.getId());
        }
    }

    // Undo Helpers ////////////////////////////////////////////////////////////////////////////////
    public class UndoUpdateWorkSchedule implements UndoAction.UndoActionStepContract {
        WorkScheduleDb workScheduleDb = null;
        int companyId;
        Entry.BelongingDay day;
        WorkScheduleContract ws;

        public UndoUpdateWorkSchedule(WorkScheduleDb workScheduleDb, int companyId, Entry.BelongingDay day,WorkScheduleContract ws) {
            this.workScheduleDb = workScheduleDb;
            this.companyId = companyId;
            this.day = new Entry.BelongingDay(day);
            this.ws = ws;
        }
        @Override
        public void executeUndo() {
            workScheduleDb.updateWorkScheduleForDay(companyId, day,ws.getId());
        }
    }
}
