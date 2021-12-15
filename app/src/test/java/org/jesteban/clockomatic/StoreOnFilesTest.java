package org.jesteban.clockomatic;

import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.StoreEntriesOnFiles;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class StoreOnFilesTest {
    @Ignore("Filesystem issues")
    @Test
    public void inst() throws Exception {
        StoreEntriesOnFiles sof = new StoreEntriesOnFiles();
        EntrySet entries = new EntrySet();
        sof.write(entries);
    }
}
