package org.jesteban.clockomatic.helpers;


import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.model.Entry;

public class EntryKindSupport {
    private EntryKindSupport() {
        //Can't be instantiated
    }

    public static int getIconId(Entry.Kind v) {
        switch (v) {
            case WORKING:
                return R.drawable.working_time_icon;
            default:
                return R.drawable.ic_toys_black_24dp;
        }
    }
}
