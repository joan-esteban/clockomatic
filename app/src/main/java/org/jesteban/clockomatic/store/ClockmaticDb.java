package org.jesteban.clockomatic.store;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import org.jesteban.clockomatic.model.Company;

import java.io.File;
import java.util.logging.Logger;

// How to write ClockmaticDb on external:
// https://stackoverflow.com/questions/5332328/sqliteopenhelper-problem-with-fully-qualified-db-path-name/9168969#9168969
// A projects using database:
// https://github.com/fetlife/android/blob/master/FetLife/fetlife/src/main/java/com/bitlove/fetlife/model/db/FetLifeDatabase.java
// https://github.com/tvportal/android
//
// handle errors:
// https://stackoverflow.com/questions/21619618/sqlexception-error-handling-in-android
//
// DATABASE_VERSION 7            -> Default WorkSchedule per company
// DATABASE_VERSION 8 (20191012) -> Keep starting company on Db
public class ClockmaticDb extends SQLiteOpenHelper {
    public static final String ROWID = "rowid";
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "clockmatic.db";
    private static final Logger LOGGER = Logger.getLogger(ClockmaticDb.class.getName());

    // https://es.wikipedia.org/wiki/Anexo:Historial_de_versiones_de_Android
    // Api 21 -22 -> Lollipop 5.0 - 5.1
    public ClockmaticDb(final Context context) {
        //this(context, DATABASE_NAME, android.os.Build.VERSION.SDK_INT<22);
        this(context, DATABASE_NAME, true);
    }

    public ClockmaticDb(final Context context, String databaseName, Boolean onExternalStorage) {
        super(new DatabaseContext(context, onExternalStorage), databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableCompanies.createSql());
        db.execSQL(TableCompanies.populateSql());
        db.execSQL(TableEntries.createSql());
        db.execSQL(TableBelongingDay.createSql());
        db.execSQL(TableSettings.createSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: In the future I will need a better upgrade to avoid losing data!!!
        if (oldVersion==7){
            // Add settings
            LOGGER.warning(String.format("upgrading from %s to %s - Add table TableSettings", oldVersion,newVersion));
            //db.execSQL("DROP TABLE " + TableSettings.TABLE_NAME + ";");
            db.execSQL(TableSettings.createSql());
        } else
        if (oldVersion==6){
            // Need to add a field on companies table to set default company
            db.execSQL(TableCompanies.addDefaultWorkScheduleFieldSql());
        } else
        if (oldVersion==5){
            LOGGER.warning(String.format("upgrading from %s to %s - Add table belonging", oldVersion,newVersion));
            db.execSQL(TableBelongingDay.createSql());
        } else {
            LOGGER.warning("upgrading from " + Integer.toString(oldVersion) + " to " +
                    Integer.toString(newVersion) + " actually is a drop and a create!");
            db.execSQL("DROP TABLE " + TableEntries.TABLE_NAME + ";");
            db.execSQL("DROP TABLE " + TableCompanies.TABLE_NAME + ";");
            db.execSQL("DROP TABLE " + TableBelongingDay.TABLE_NAME + ";");
            onCreate(db);
        }
    }

    public void test() {
        getReadableDatabase().execSQL("sELECT * FROM " + TableEntries.TABLE_NAME + ";");
        LOGGER.warning("DATABSE PATH:" + getReadableDatabase().getPath());
    }

    public static class TableCompanies {
        public static final String TABLE_NAME = "cm_companies";
        public static final String FIELD_COMPANY_ID = "company_id";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_DESC = "description";
        public static final String FIELD_COLOR = "color";
        public static final String FIELD_STATE = "state";
        public static final String FIELD_DEFAULT_WORK_SCHEDULE = "default_work_schedule";

        public static final int STATE_ENABLE = 0;
        public static final int STATE_DELETED = 1;

        public static final int DEFAULT_WORK_SCHEDULE_NONE=-1;

        public static final String[] ALL_FIELDS = {
                TableCompanies.FIELD_COMPANY_ID,
                TableCompanies.FIELD_NAME,
                TableCompanies.FIELD_DESC,
                TableCompanies.FIELD_COLOR,
                TableCompanies.FIELD_STATE,
                TableCompanies.FIELD_DEFAULT_WORK_SCHEDULE
        };

        private TableCompanies() {
            // Can't be instantiated
        }

        public static String createSql() {
            return "CREATE TABLE " + TABLE_NAME + "("
                    + FIELD_COMPANY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + FIELD_NAME + " text NOT NULL , "
                    + FIELD_DESC + " text, "
                    + FIELD_COLOR + " integer default " + Integer.toString(Company.COLOR_NOT_SET) + ", "
                    + FIELD_STATE + " integer default " + Integer.toString(STATE_ENABLE) + ", "
                    + FIELD_DEFAULT_WORK_SCHEDULE + " integer default " + Integer.toString(DEFAULT_WORK_SCHEDULE_NONE) + ", "
                    + " UNIQUE (" + FIELD_NAME + ") "
                    + ");";
        }

        public static String addDefaultWorkScheduleFieldSql(){
            return "ALTER TABLE " + TABLE_NAME
                    + " ADD " + FIELD_DEFAULT_WORK_SCHEDULE + " integer default " + Integer.toString(DEFAULT_WORK_SCHEDULE_NONE)
                    + ";";
        }

        public static String populateSql() {
            return "INSERT INTO " + TABLE_NAME + "("
                    + FIELD_NAME + ", "
                    + FIELD_DESC + ", "
                    + FIELD_COLOR + ") "
                    + " VALUES('Main','Initial Company', " + Integer.toString(Color.YELLOW) + ");";
        }
    }

    public static class TableEntries {
        public static final String TABLE_NAME = "cm_entries";
        public static final String FIELD_COMPANY_ID = "company_id";
        public static final String FIELD_REGISTER_DATE = "register_date";
        public static final String FIELD_BELONGING_DAY = "belonging_day";
        public static final String FIELD_DESCRIPTION = "description";
        public static final String FIELD_KIND = "kind";

        public static final String[] allColumns = {
                FIELD_COMPANY_ID,
                FIELD_REGISTER_DATE,
                FIELD_BELONGING_DAY,
                FIELD_DESCRIPTION,
                FIELD_KIND
        };

        private TableEntries() {
            // Can't be instantiated
        }

        public static String createSql() {
            return "create table " + TABLE_NAME + "("
                    + FIELD_COMPANY_ID + " integer not null, "
                    + FIELD_REGISTER_DATE + " text,"
                    + FIELD_BELONGING_DAY + " text,"
                    + FIELD_DESCRIPTION + " text,"
                    + FIELD_KIND + " integer, "
                    + "FOREIGN KEY (" + FIELD_COMPANY_ID + ") REFERENCES " + TableCompanies.TABLE_NAME + "(" + TableCompanies.FIELD_COMPANY_ID + ") ON DELETE RESTRICT"
                    + " UNIQUE (" + FIELD_COMPANY_ID + "," + FIELD_REGISTER_DATE + " ) "
                    + ");";

        }

    }

    public static class TableBelongingDay {
        public static final String TABLE_NAME = "cm_belonging_day";
        public static final String FIELD_COMPANY_ID = "company_id";
        public static final String FIELD_BELONGING_DAY = "belonging_day";
        public static final String FIELD_WORK_SCHEDULE_ID = "work_schedule_id";

        public static final String[] allColumns = {
                FIELD_COMPANY_ID,
                FIELD_BELONGING_DAY,
                FIELD_WORK_SCHEDULE_ID
        };
        private TableBelongingDay(){
            // Can't be instantiated
        }

        public static String createSql() {
            return "create table " + TABLE_NAME + "("
                    + FIELD_COMPANY_ID + " integer not null, "
                    + FIELD_BELONGING_DAY + " text,"
                    + FIELD_WORK_SCHEDULE_ID + " text,"
                    + "FOREIGN KEY (" + FIELD_COMPANY_ID + ") REFERENCES " + TableCompanies.TABLE_NAME + "(" + TableCompanies.FIELD_COMPANY_ID + ") ON DELETE RESTRICT"
                    + " UNIQUE (" + FIELD_COMPANY_ID + "," + FIELD_BELONGING_DAY + " ) "
                    + ");";

        }

    }

    public static class TableSettings {
        public static final String TABLE_NAME = "cm_settings";
        public static final String FIELD_KEY_ID = "key_id";
        public static final String FIELD_VALUE = "value";

        public static final String[] allColumns = {
                FIELD_KEY_ID,
                FIELD_VALUE
        };
        private TableSettings(){
            // Can't be instantiated
        }

        public static String createSql() {
            return "create table " + TABLE_NAME + "("
                    + FIELD_KEY_ID + " text not null, "
                    + FIELD_VALUE + " text,  "
                    + " UNIQUE (" + FIELD_KEY_ID + " ) "
                    + ");";

        }

    }

    // Not in use!!!!!!!
    public static class TableWorkSchedule {
        public static final String TABLE_NAME = "cm_work_schedule";
        public static final String FIELD_WORK_SCHEDULE_ID = "work_schedule_id";
        public static final String FIELD_WORK_SCHEDULE_TYPE = "work_schedule_type_id";
        public static final String FIELD_NAME = "work_schedule_name";
        public static final String FIELD_PARAMS = "work_schedule_params";

        public static final String[] allColumns = {
                FIELD_WORK_SCHEDULE_ID,
                FIELD_WORK_SCHEDULE_TYPE,
                FIELD_NAME,
                FIELD_PARAMS
        };
        private TableWorkSchedule(){
            // Can't be instantiated
        }

        public static String createSql() {
            return "create table " + TABLE_NAME + "("
                    + FIELD_WORK_SCHEDULE_ID + " integer not null, "
                    + FIELD_WORK_SCHEDULE_TYPE + " integer,"
                    + FIELD_NAME + " text,"
                    + FIELD_PARAMS + " text,"
                    + " UNIQUE (" + FIELD_WORK_SCHEDULE_ID +" ) "
                    + ");";

        }

    }



    // This class is extracted from https://stackoverflow.com/questions/5332328/sqliteopenhelper-problem-with-fully-qualified-db-path-name/9168969#9168969
    // author: k3b (https://stackoverflow.com/users/519334/k3b)
    static class DatabaseContext extends ContextWrapper {

        private static final String DEBUG_CONTEXT = "DatabaseContext";
        Boolean onExternalStorage = true;

        public DatabaseContext(Context base, Boolean onExternalStorage) {
            super(base);
            this.onExternalStorage = onExternalStorage;
        }

        @Override
        public File getDatabasePath(String name) {
            if (!onExternalStorage) {
                Log.w(DEBUG_CONTEXT, "calling super to get database because is not in external");
                return super.getDatabasePath(name);
            }
            File sdcard = Environment.getExternalStorageDirectory();
            sdcard = this.getBaseContext().getExternalFilesDir(null);
            //sdcard = Environment.getExternalFilesDir();
            String dbfile = sdcard.getAbsolutePath() + File.separator + "clockomatic" + File.separator + name;
            if (!dbfile.endsWith(".db")) {
                dbfile += ".db";
            }

            File result = new File(dbfile);

            if (!result.getParentFile().exists()) {
                result.getParentFile().mkdirs();
            }

            if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
                Log.w(DEBUG_CONTEXT, "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
            }

            return result;
        }

        /* this version is called for android devices >= api-11. thank to @damccull for fixing this. */
        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
            return openOrCreateDatabase(name, mode, factory);
        }

        /* this version is called for android devices < api-11 */
        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
            SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
            if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
                Log.w(DEBUG_CONTEXT, "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
            }
            return result;
        }
    }
}
