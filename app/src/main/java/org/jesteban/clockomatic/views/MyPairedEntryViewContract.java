package org.jesteban.clockomatic.views;


import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.InfoDayEntry;

public class MyPairedEntryViewContract {

    static public class PairedEntryVisualData {
        static final int NO_COLOR=0;
        static final int NO_ICON=0;

        public PairedEntryVisualData(String initialHour, String endHour){
            this(initialHour, endHour, "","");
        }
        public PairedEntryVisualData(String initialHour, String endHour, String sufixInitial, String sufixEnd){
            this(initialHour, endHour,sufixInitial,sufixEnd, NO_COLOR,NO_ICON);
        }
        public PairedEntryVisualData(String initialHour, String endHour, String sufixInitial, String sufixEnd, int color, int iconResourceId){
            this.initialHour = initialHour;
            this.endHour = endHour;
            this.sufixEnd = sufixEnd;
            this.sufixInitial = sufixInitial;
            this.color = color;
            this.iconResourceId = iconResourceId;
        }

        public String getInitialHour() {
            return initialHour;
        }

        public String getEndHour() {
            return endHour;
        }

        public String getSufixEnd() {
            return sufixEnd;
        }
        public String getSufixInitial() {
            return sufixInitial;
        }

        public int getColor() { return color;}
        public int getIconResourceId() {return iconResourceId;}

        private String initialHour;
        private String endHour;
        private String sufixInitial;
        private String sufixEnd;
        private int color = 0;
        private int iconResourceId = 0;

    }

    public interface View  {
        public void showPairedEntry(PairedEntryVisualData data);
    }

    public interface Presenter extends PresenterBase {
        public void showPairedEntry(InfoDayEntry.PairedEntry pairedEntry);
    }

}
