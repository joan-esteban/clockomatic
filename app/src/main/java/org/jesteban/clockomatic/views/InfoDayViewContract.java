package org.jesteban.clockomatic.views;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.helpers.InfoDayEntry;

import java.util.List;

public class InfoDayViewContract {
    public interface View  {
        public void showPairedEntries(List<MyPairedEntryViewContract.PairedEntryVisualData> data);
        public void showCalendarDay(MyCalendarDayViewContract.CalendarDayViewVisualData data);
        public void showCalendarDayInfo(MyCalendarDayViewContract.CalendarDayViewVisualData data);

    }

    public interface Presenter extends PresenterBase {
        public void showInfoDay(InfoDayEntry infoDay);
    }
}
