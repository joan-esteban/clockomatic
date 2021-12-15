package org.jesteban.clockomatic.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.jesteban.clockomatic.store.ClockmaticDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class CompaniesDb extends Observable implements CompaniesContract {
    private static final Logger LOGGER = Logger.getLogger(CompaniesDb.class.getName());
    private ClockmaticDb clockmaticDb = null;
    private Company activeCompany = null;
    private SettingsContract settingsDb;

    public static final String KEY_ACTIVE_COMPANY = "ACTIVE_COMPANY";



    public CompaniesDb(ClockmaticDb db, SettingsContract settingsDb){
        this.clockmaticDb = db;
        this.settingsDb = settingsDb;
    }

    private void notifyChange(Boolean condition){
        if (condition) {
            setChanged();
            notifyObservers();
        }
    }
    private Company getCompany(Cursor cursor){
        Company c = new Company();
        c.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClockmaticDb.TableCompanies.FIELD_COMPANY_ID )));
        c.setName(cursor.getString(cursor.getColumnIndexOrThrow(ClockmaticDb.TableCompanies.FIELD_NAME)));
        c.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(ClockmaticDb.TableCompanies.FIELD_DESC)));
        c.setColor(cursor.getInt(cursor.getColumnIndexOrThrow(ClockmaticDb.TableCompanies.FIELD_COLOR)));
        int state = cursor.getInt(cursor.getColumnIndexOrThrow(ClockmaticDb.TableCompanies.FIELD_STATE ));
        c.setIsEnabled(state==ClockmaticDb.TableCompanies.STATE_ENABLE);
        c.setDefaultWorkSchedule(cursor.getInt(cursor.getColumnIndexOrThrow(ClockmaticDb.TableCompanies.FIELD_DEFAULT_WORK_SCHEDULE)));
        return c;
    }

    @Override
    public long getCountEntriesForCompany(int companyId){
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { Integer.toString(companyId)};
        Cursor cursor = db.query(ClockmaticDb.TableEntries.TABLE_NAME,
                null,
                ClockmaticDb.TableEntries.FIELD_COMPANY_ID + " = ?",
                selectionArgs,
                null, null, null);
        return cursor.getCount();
    }

    public Company getCompany(int companyId) {
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { Integer.toString(companyId)};
        Cursor cursor = db.query(ClockmaticDb.TableCompanies.TABLE_NAME,
                ClockmaticDb.TableCompanies.ALL_FIELDS,
                ClockmaticDb.TableCompanies.FIELD_COMPANY_ID + " = ?",
                selectionArgs,
                null,
                null,
                null
        );
        if (!cursor.moveToFirst()) return null;
        return getCompany(cursor);
    }

    private Company getCompanyByRowId(long rowid) {
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { Long.toString(rowid)};
        Cursor cursor = db.query(ClockmaticDb.TableCompanies.TABLE_NAME,
                ClockmaticDb.TableCompanies.ALL_FIELDS,
                ClockmaticDb.ROWID + " = ?",
                selectionArgs,
                null,
                null,
                null
        );
        if (!cursor.moveToFirst()) return null;
        return getCompany(cursor);
    }




    @Override
    public List<Company> getAllCompaniesEnabled() {
        return getCompanies(true,ClockmaticDb.TableCompanies.STATE_ENABLE);
    }

    public List<Company> getAllCompaniesIncludingDisabled() {
        return getCompanies(false,0);
    }

    public List<Company> getCompanies(Boolean filterByState, int state) {
        List<Company> result = new ArrayList<>();
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { Integer.toString(state)};
        String selection = ClockmaticDb.TableCompanies.FIELD_STATE + " = ? ";
        if (!filterByState) {
            selection = null;
            selectionArgs = null;
        }
        Cursor cursor = db.query(ClockmaticDb.TableCompanies.TABLE_NAME,
                ClockmaticDb.TableCompanies.ALL_FIELDS,
                selection,
                selectionArgs,
                null,
                null,
                ClockmaticDb.TableCompanies.FIELD_STATE + " ASC,"
                       + ClockmaticDb.TableCompanies.FIELD_COMPANY_ID + " ASC"
                );
        if (!cursor.moveToFirst()) return result;
        do{
            Company c = getCompany(cursor);
            result.add(c);
        } while (cursor.moveToNext());
        return result;
    }

    public int howManyActivesCompaniesThereAre(){
        SQLiteDatabase db = clockmaticDb.getReadableDatabase();
        String [] selectionArgs = { Integer.toString(ClockmaticDb.TableCompanies.STATE_ENABLE)};
        Cursor cursor = db.query(ClockmaticDb.TableCompanies.TABLE_NAME,
                null,
                ClockmaticDb.TableCompanies.FIELD_STATE + " = ? ",
                selectionArgs,
                null,
                null,
                null
        );
        return cursor.getCount();
    }

    private Company getFirstAvailableCompany(){
        List<Company> companies = getAllCompaniesEnabled();
        return companies.get(0);
    }

    @Override
    public Company getActiveCompany() {
        if (activeCompany==null){
            String company_id = settingsDb.getSetting(this.KEY_ACTIVE_COMPANY);
            if (company_id==null){
                activeCompany = getFirstAvailableCompany();
            } else {
                activeCompany = getCompany(Integer.parseInt(company_id));
                if (!activeCompany.isEnabled()) {
                    LOGGER.info("No company active on Db (SettingsTable)");
                    activeCompany = getFirstAvailableCompany();
                }
            }
        }
        activeCompany = getCompany(activeCompany.getId());
        if (!activeCompany.isEnabled()){
            activeCompany = null;
            return getActiveCompany();
        }
        return activeCompany;
    }




    protected ContentValues getContentValues(Company c, Boolean forceId){
        ContentValues values = new ContentValues();
        if (forceId) values.put(ClockmaticDb.TableCompanies.FIELD_COMPANY_ID, c.getId());
        values.put(ClockmaticDb.TableCompanies.FIELD_NAME, c.getName());
        values.put(ClockmaticDb.TableCompanies.FIELD_DESC, c.getDescription());
        values.put(ClockmaticDb.TableCompanies.FIELD_COLOR, c.getColor());
        values.put(ClockmaticDb.TableCompanies.FIELD_STATE, getDbStateForDisableState(c.isEnabled()));
        return values;
    }

    protected UndoAction addCompany(Company c, Boolean forceId) throws SQLException {
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        ContentValues values = getContentValues(c,forceId);
        LOGGER.info("Inserting new Company name: " + c.getName());
        long newRowId = db.insertOrThrow(ClockmaticDb.TableCompanies.TABLE_NAME, null, values);
        LOGGER.info("Inserting new Company name: " + c.getName()+ " OK rowid=" + Long.toString(newRowId));
        Company companyRetrieve = getCompanyByRowId(newRowId);
        LOGGER.info("Retrieve company from db = " + companyRetrieve.toString() );
        c.setId(companyRetrieve.getId());
        c.setIsEnabled(companyRetrieve.isEnabled());
        notifyChange(true);
        UndoAction undo = new UndoAction();
        undo.actions.add(new UndoAddCompany(this,companyRetrieve.getId()));
        return undo;
    }

    @Override
    public UndoAction  addCompany(Company c) throws SQLException {
        return addCompany(c,false);
    }

    private int getDbStateForDisableState(boolean isEnabled){
        if (!isEnabled) {
            return ClockmaticDb.TableCompanies.STATE_DELETED;
        } else return ClockmaticDb.TableCompanies.STATE_ENABLE;
    }

    public UndoAction updateCompany(Company c){
        LOGGER.info("update Company " + c.toString());
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        Company companyOnDb = getCompany(c.getId());
        ContentValues values = getContentValues(c,false);
        String [] selectionArgs = { Integer.toString(c.getId() )};
        db.update(ClockmaticDb.TableCompanies.TABLE_NAME,
                values,
                ClockmaticDb.TableCompanies.FIELD_COMPANY_ID + " = ? ",
                selectionArgs
        );
        notifyChange(true);
        UndoAction undo = new UndoAction();
        undo.actions.add(new UndoModifyCompany(this,companyOnDb));
        return undo;
    }



    public UndoAction setStateCompany(int companyId, Boolean active){
        Company c = getCompany(companyId);
        c.setIsEnabled(active);
        return updateCompany(c);
    }

    @Override
    public UndoAction removeCompany(int companyId) {
        Company c = getCompany(companyId);
        LOGGER.info("removeCompany " + Integer.toString(companyId));
        if (c.isEnabled() && (howManyActivesCompaniesThereAre() <=1)  ){
            // Always must be 1 company
            LOGGER.info("removeCompany " + Integer.toString(companyId) + " ERROR: is last active company");
            throw new UnsupportedOperationException("Can't delete last company");
        }
        if (getCountEntriesForCompany(companyId)>0){
            LOGGER.info("removeCompany " + Integer.toString(companyId) + " ERROR: there are associate data");
            throw new UnsupportedOperationException("Can't delete company with data");
        }
        SQLiteDatabase db = clockmaticDb.getWritableDatabase();
        String[] whereArgs = { Integer.toString(companyId) };
        db.delete(ClockmaticDb.TableCompanies.TABLE_NAME,
                ClockmaticDb.TableCompanies.FIELD_COMPANY_ID + "=?", whereArgs);
        notifyChange(true);

        UndoAction undo = new UndoAction();
        undo.actions.add(new UndoRemoveCompany(this,c));
        return undo;

    }


    @Override
    public Boolean setActiveCompany(int index) {
        Company newActiveCompany = getCompany(index);
        if (activeCompany.getId() == newActiveCompany.getId()) return false;
        LOGGER.info("setting active company from " + activeCompany.toString() + " to " + newActiveCompany.toString() );
        activeCompany = newActiveCompany;
        this.settingsDb.setSetting(this.KEY_ACTIVE_COMPANY, String.valueOf(activeCompany.getId()) );
        notifyChange(true);
        return (activeCompany != null);
    }







    // Undo Helpers ////////////////////////////////////////////////////////////////////////////////
    public class UndoRemoveCompany implements UndoAction.UndoActionStepContract {
        CompaniesDb companiesDb = null;
        Company company = null;

        public UndoRemoveCompany(CompaniesDb companiesDb,  Company company) {
            this.companiesDb = companiesDb;
            this.company = company;
        }
        @Override
        public void executeUndo() {
            companiesDb.addCompany(company, true);

        }
    }

    public class UndoAddCompany implements UndoAction.UndoActionStepContract {
        CompaniesDb companiesDb = null;
        int companyId = -1;
        UndoAddCompany(CompaniesDb companiesDb, int companyId){
            this.companiesDb = companiesDb;
            this.companyId = companyId;
        }
        @Override
        public void executeUndo() {
            companiesDb.removeCompany(companyId);
        }
    }

    public class UndoModifyCompany implements UndoAction.UndoActionStepContract {
        CompaniesDb companiesDb = null;
        Company company = null;

        public UndoModifyCompany(CompaniesDb companiesDb,  Company company) {
            this.companiesDb = companiesDb;
            this.company = company;
        }
        @Override
        public void executeUndo() {
            companiesDb.updateCompany(company);

        }

    }
}
