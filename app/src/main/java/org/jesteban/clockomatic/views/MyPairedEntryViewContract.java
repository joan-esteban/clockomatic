package org.jesteban.clockomatic.views;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.helpers.InfoDayEntry;

import java.util.Calendar;
import java.util.List;

public class MyPairedEntryViewContract {

    static public class PairedEntryVisualData {

        public PairedEntryVisualData(String initialHour, String endHour){
            this.initialHour = initialHour;
            this.endHour = endHour;
        }

        public String getInitialHour() {
            return initialHour;
        }

        public String getEndHour() {
            return endHour;
        }

        private String initialHour;
        private String endHour;


    }

    public interface View  {
        public void showPairedEntry(PairedEntryVisualData data);
    }

    public interface Presenter extends PresenterBase {
        public void showPairedEntry(InfoDayEntry.PairedEntry pairedEntry);
    }

}
