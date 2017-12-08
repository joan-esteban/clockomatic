package org.jesteban.clockomatic;

import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.StoreOnFiles;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class StoreOnFilesTest {
    @Test
    public void inst() throws Exception {
        StoreOnFiles sof = new StoreOnFiles();
        EntrySet entries = new EntrySet();
        sof.write(entries);
    }
}
