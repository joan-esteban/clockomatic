package org.jesteban.clockomatic.views;


import java.util.List;

public class MyPairedEntriesGridViewContract {
    public interface View  {
        public void showPairedEntries(List<MyPairedEntryViewContract.PairedEntryVisualData> data);
    }


}
