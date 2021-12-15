package org.jesteban.clockomatic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jesteban.clockomatic.store.ClockmaticDb;
import org.junit.Test;
import org.junit.runner.RunWith;

// How to create db on memory : https://stackoverflow.com/questions/37035046/sqliteopenhelper-multiple-in-memory-databases

@RunWith(AndroidJUnit4.class)
public class ClockmaticDbTest  {

    @Test
    public void onCreateDb() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        ClockmaticDb db = new ClockmaticDb(appContext,null,false);

    }

}