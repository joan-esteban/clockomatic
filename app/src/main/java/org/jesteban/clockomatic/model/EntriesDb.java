package org.jesteban.clockomatic.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jesteban.clockomatic.store.ClockmaticDb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class EntriesDb extends Observable {
    private static final Logger LOGGER = Logger.getLogger(EntriesDb.class.getName());
    private static final String FORMAT_DATE = "yyyy/MM/dd HH:mm z";
    private ClockmaticDb clockmaticDb = null;
    private SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);

    public EntriesDb(ClockmaticDb db){
        clockmaticDb = db;
    }

    private String getRegisterDateString(Calendar date){
        return sdf.format(date.getTime());
    }

    public UndoAction register(int companyId, Entry entry){
        LOGGER.info(String.format("register %d, %s", companyId, entry));
        if (entry.isEmpty()) {
            LOGGER.warning("Cant add a empty date");
            return null;
        }
        EntrySet entriesAdd = new EntrySet();
        entriesAdd.addEntry(entry);
        UndoAction undoAction = new UndoAction();
        undoAction.actions.add(new UndoAdd(this,entriesAdd,companyId));

        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        ContentValues values = new ContentValues();

        String registerDateText = getRegisterDateString(entry.getRegisterDate());
        values.put(ClockmaticDb.TableEntries.FIELD_COMPANY_ID, companyId);
        values.put(ClockmaticDb.TableEntries.FIELD_REGISTER_DATE, registerDateText);
        values.put(ClockmaticDb.TableEntries.FIELD_BELONGING_DAY, entry.getBelongingDay());
        values.put(ClockmaticDb.TableEntries.FIELD_DESCRIPTION, entry.getDescription());
        values.put(ClockmaticDb.TableEntries.FIELD_KIND, entry.getKind().ordinal());
        long newRowId = db.insertOrThrow(ClockmaticDb.TableEntries.TABLE_NAME, null, values);
        LOGGER.info("Insert ok, sending update");
        notifyChange(newRowId>=0);
        return undoAction;
    }

    public UndoAction wipeStore(int companyId) throws ParseException {
        List<UndoAction.UndoActionStepContract> undo = new ArrayList<>();
        EntrySet deletedEntries = getEntriesForCompany(companyId);
        undo.add(new UndoRemove(this,deletedEntries,companyId));
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        String[] whereArgs = { Integer.toString(companyId) };
        db.delete(ClockmaticDb.TableEntries.TABLE_NAME,
                ClockmaticDb.TableEntries.FIELD_COMPANY_ID + "=?", whereArgs);
        LOGGER.info("WipeStore ok, sending update");
        notifyChange();
        return new UndoAction("delete company data",undo);

    }

    private Entry getEntryFromCursor(Cursor cursor) throws ParseException {
        String registerDate = cursor.getString(cursor.getColumnIndexOrThrow(ClockmaticDb.TableEntries.FIELD_REGISTER_DATE));
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(registerDate));
        String belonging = cursor.getString(cursor.getColumnIndexOrThrow(ClockmaticDb.TableEntries.FIELD_BELONGING_DAY));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(ClockmaticDb.TableEntries.FIELD_DESCRIPTION));
        int kindOrdinal = cursor.getInt(cursor.getColumnIndexOrThrow(ClockmaticDb.TableEntries.FIELD_KIND));
        return new Entry(cal, belonging, Entry.Kind.values()[kindOrdinal],description);
    }

    private EntrySet getEntriesWhere(String selection, String [] selectionArgs) throws ParseException{
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        Cursor cursor = db.query(ClockmaticDb.TableEntries.TABLE_NAME,
                ClockmaticDb.TableEntries.allColumns,
                selection,
                selectionArgs,
                null,
                null,
                ClockmaticDb.TableEntries.FIELD_BELONGING_DAY + " ASC"
                        + ", " + ClockmaticDb.TableEntries.FIELD_REGISTER_DATE + " ASC");
        EntrySet result = new EntrySet();
        if (!cursor.moveToFirst()) return result;
        do {
            Entry entry  = getEntryFromCursor(cursor);
            result.addEntry(entry);
        } while (cursor.moveToNext());
        return result;
    }
    private EntrySet getEntriesBelongingDayLike(int companyId,String belongingDay) throws ParseException{
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(Integer.toString(companyId));
        String selection = ClockmaticDb.TableEntries.FIELD_COMPANY_ID + " = ? ";
        if (belongingDay!=null){
            selectionArgs.add(belongingDay);
            selection += " AND  " + ClockmaticDb.TableEntries.FIELD_BELONGING_DAY + " LIKE ? ";
        }
        return getEntriesWhere(selection, selectionArgs.toArray(new String[0]));
    }
    public EntrySet getEntriesForCompany(int companyId) throws ParseException {
        return getEntriesBelongingDayLike(companyId, null);
    }

    public EntrySet getEntriesBelongingDayStartWith(int companyId, String prefixBelongingDay) throws ParseException {
        return getEntriesBelongingDayLike(companyId, prefixBelongingDay + "%");
    }
    public EntrySet getEntriesBelongingDay(int companyId,Calendar day) throws ParseException{
        DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_DAY);
        String filter = df.format(day.getTime());
        return getEntriesBelongingDayLike(companyId, filter);
    }





    public EntrySet getEntriesBelongingMonth(int companyId,Calendar day) throws ParseException{
        DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
        String filter = df.format(day.getTime());
        return getEntriesBelongingDayLike(companyId, filter + "%");
    }
    private void notifyChange(){notifyChange(true);}
    private void notifyChange(Boolean condition){
        if (condition) {
            setChanged();
            notifyObservers();
        }
    }

    public UndoAction remove(int companyId, Entry date) throws ParseException {
        LOGGER.info(String.format("remove %d, %s", companyId, date));
        String whereClause = ClockmaticDb.TableEntries.FIELD_COMPANY_ID + "=?"
                + " AND " + ClockmaticDb.TableEntries.FIELD_REGISTER_DATE + "=? "
                + " AND " + ClockmaticDb.TableEntries.FIELD_BELONGING_DAY + "=? ";
        String registerDateText = getRegisterDateString(date.getRegisterDate());
        String[] whereArgs = { Integer.toString(companyId),
                registerDateText,
                date.getBelongingDay()};
        EntrySet removedEntries = getEntriesWhere(whereClause, whereArgs);
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        int rowAffected = db.delete(ClockmaticDb.TableEntries.TABLE_NAME,
                whereClause ,
                whereArgs);
        notifyChange(rowAffected>0);
        if (rowAffected>0){
            List<UndoAction.UndoActionStepContract> undo = new ArrayList<>();
            undo.add(new UndoRemove(this,removedEntries, companyId));
            return new UndoAction("removed entry", undo);
        }
        return null;
    }
    ////////////////// UNDO SUPPORT ////////////////////////////////////////////////////////////////
    public class UndoRemove implements UndoAction.UndoActionStepContract {
        EntriesDb entriesDb = null;
        EntrySet data = null;
        int companyId = -1;
        public UndoRemove(EntriesDb entriesDb, EntrySet data, int companyId){
            this.entriesDb = entriesDb;
            this.data = data;
            this.companyId = companyId;
        }

        @Override
        public void executeUndo() {
            for (Entry entry: data.getEntries()){
                entriesDb.register(companyId,entry);
            }
        }
    }
    ////////////////// UNDO SUPPORT ////////////////////////////////////////////////////////////////
    public class UndoAdd implements UndoAction.UndoActionStepContract {
        EntriesDb entriesDb = null;
        EntrySet data = null;
        int companyId = -1;
        public UndoAdd(EntriesDb entriesDb, EntrySet data, int companyId){
            this.entriesDb = entriesDb;
            this.data = data;
            this.companyId = companyId;
        }
        @Override
        public void executeUndo() throws ParseException {
            for (Entry entry: data.getEntries()){
                entriesDb.remove(companyId,entry);
            }
        }
    }


}
