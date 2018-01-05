package org.jesteban.clockomatic.views;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.helpers.InfoDayEntry;

import java.util.List;

public class InfoDayViewContract {
    public interface View  {
        static public class InfoDayVisualData{
            MyCalendarDayViewContract.CalendarDayViewVisualData dayData;
            List<MyPairedEntryViewContract.PairedEntryVisualData> entriesData;
            MyCalendarDayViewContract.CalendarDayViewVisualData briefData;
        }

        public void showData(InfoDayVisualData data);

    }

    public interface Presenter extends PresenterBase {
        public void showInfoDay(InfoDayEntry infoDay);
    }
}
