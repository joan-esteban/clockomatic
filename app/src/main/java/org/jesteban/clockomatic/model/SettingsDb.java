package org.jesteban.clockomatic.model;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jesteban.clockomatic.store.ClockmaticDb;

import java.util.logging.Logger;


public class SettingsDb implements SettingsContract {
    private static final Logger LOGGER = Logger.getLogger(SettingsDb.class.getName());
    private ClockmaticDb clockmaticDb;


    public SettingsDb(ClockmaticDb db){
        clockmaticDb = db;
    }

    @Override
    public String getSetting(String key) {
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { key };
        Cursor cursor = db.query(ClockmaticDb.TableSettings.TABLE_NAME,
                null,
                ClockmaticDb.TableSettings.FIELD_KEY_ID+ " = ?",
                selectionArgs,
                null, null, null);
        if (!cursor.moveToFirst()) return null;
        return cursor.getString(cursor.getColumnIndexOrThrow(ClockmaticDb.TableSettings.FIELD_VALUE));

    }


    @Override
    public UndoAction setSetting(String key, String value) {
        if (existsSetting(key)) {
            return updateSetting(key,value);
        } else {
            return addSetting(key,value);
        }
    }

    @Override
    public boolean existsSetting(String key) {
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { key };
        Cursor cursor = db.query(ClockmaticDb.TableSettings.TABLE_NAME,
                null,
                ClockmaticDb.TableSettings.FIELD_KEY_ID+ " = ?",
                selectionArgs,
                null, null, null);
        return cursor.getCount()>0;
    }

    @Override
    public UndoAction removeSetting(String key){
        String oldValue = getSetting(key);
        LOGGER.info(String.format("removeSetting %s, old_value= %s", key, oldValue));
        String whereClause = ClockmaticDb.TableSettings.FIELD_KEY_ID + "=?";
        String[] whereArgs = { key };
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        int rowAffected = db.delete(ClockmaticDb.TableSettings.TABLE_NAME,
                whereClause ,
                whereArgs);
        if (rowAffected>0){
            UndoAction undo = new UndoAction();
            undo.actions.add(new UndoModifySetting(this,key, oldValue));
            return undo;
        }
        return null;
    }

    private UndoAction addSetting(String key, String value) {
        LOGGER.info(String.format("addSetting %s=  %s", key, value));


        ContentValues values = new ContentValues();
        values.put(ClockmaticDb.TableSettings.FIELD_KEY_ID, key);
        values.put(ClockmaticDb.TableSettings.FIELD_VALUE, value);

        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        db.insertOrThrow(ClockmaticDb.TableSettings.TABLE_NAME, null, values);
        UndoAction undo = new UndoAction();
        undo.actions.add(new UndoAddSetting(this,key));
        return undo;
    }

    private UndoAction updateSetting(String key, String value) {
        String oldValue = getSetting(key);
        LOGGER.info("update updateSetting " + key + " = " + value + "   old_value= " + oldValue);
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClockmaticDb.TableSettings.FIELD_VALUE, value);
        String [] selectionArgs = { key };
        db.update(ClockmaticDb.TableSettings.TABLE_NAME,
                values,
                ClockmaticDb.TableSettings.FIELD_KEY_ID + " = ? ",
                selectionArgs
        );
        //notifyChange(true);
        UndoAction undo = new UndoAction();
        undo.actions.add(new UndoModifySetting(this,key, oldValue));
        return undo;
    }


    // Undo Helpers ////////////////////////////////////////////////////////////////////////////////
    public class UndoModifySetting implements UndoAction.UndoActionStepContract {
        SettingsDb settingDb;
        String key;
        String oldValue;

        public UndoModifySetting(SettingsDb settingDb, String key, String oldValue) {
            this.settingDb = settingDb;
            this.key = key;
            this.oldValue = oldValue;
        }
        @Override
        public void executeUndo() {
            settingDb.setSetting(key, oldValue);

        }
    }

    public class UndoAddSetting implements UndoAction.UndoActionStepContract {
        SettingsDb settingDb;
        String key;


        public UndoAddSetting(SettingsDb settingDb, String key) {
            this.settingDb = settingDb;
            this.key = key;
        }
        @Override
        public void executeUndo() {
            settingDb.removeSetting(key);

        }
    }

}


