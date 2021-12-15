package org.jesteban.clockomatic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;


import org.jesteban.clockomatic.model.SettingsDb;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.store.ClockmaticDb;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SettingsDbTest {

    SettingsDb settingsDb;
    ClockmaticDb db;
    static String KEY = "key";
    static String VALUE1= "value1";
    static String VALUE2= "value2";

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new ClockmaticDb(appContext,null,false);
        settingsDb = new SettingsDb(db);

    }

    @Test
    public void getSettingsNoPreviousValueReturnNullTest() throws Exception {
        assertEquals(null, settingsDb.getSetting(KEY));
    }

    @Test
    public void existsSettingsNoPreviousValueReturnFalseTest() throws Exception {
        assertEquals(false, settingsDb.existsSetting(KEY));
    }

    @Test
    public void setValueNoPreviousValueTest() throws Exception {
        settingsDb.setSetting(KEY, VALUE1);
        assertEquals(VALUE1, settingsDb.getSetting(KEY));
        assertEquals(true, settingsDb.existsSetting(KEY));
    }

    @Test
    public void setValueThatHaveAPreviousValueTest() throws Exception {
        settingsDb.setSetting(KEY, VALUE1);
        settingsDb.setSetting(KEY, VALUE2);
        assertEquals(VALUE2, settingsDb.getSetting(KEY));
        assertEquals(true, settingsDb.existsSetting(KEY));
    }

    @Test
    public void undoSetValueNoPreviousValueTest() throws Exception {
        UndoAction undo = settingsDb.setSetting(KEY, VALUE1);

        undo.execute();

        assertEquals(false, settingsDb.existsSetting(KEY));
    }

    @Test
    public void undoSetValueThatHaveAPreviousValueTest() throws Exception {
        settingsDb.setSetting(KEY, VALUE1);
        UndoAction undo = settingsDb.setSetting(KEY, VALUE2);

        undo.execute();

        assertEquals(VALUE1, settingsDb.getSetting(KEY));
        assertEquals(true, settingsDb.existsSetting(KEY));
    }

    @Test
    public void removeKeyThatExistsTest() throws Exception {
        settingsDb.setSetting(KEY, VALUE1);
        settingsDb.removeSetting(KEY);
        assertEquals(false, settingsDb.existsSetting(KEY));
    }

    @Test
    public void removeKeyThatNotExistsTest() throws Exception {
        UndoAction undo = settingsDb.removeSetting(KEY);
        assertEquals(null, undo);
    }

    @Test
    public void undoRemoveKeyThatExistsTest() throws Exception {
        settingsDb.setSetting(KEY, VALUE1);
        UndoAction undo = settingsDb.removeSetting(KEY);

        undo.execute();

        assertEquals(VALUE1, settingsDb.getSetting(KEY));
        assertEquals(true, settingsDb.existsSetting(KEY));
    }

}
