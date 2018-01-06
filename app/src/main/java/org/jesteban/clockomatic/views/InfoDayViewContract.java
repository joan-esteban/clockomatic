package org.jesteban.clockomatic.views;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;

import java.util.List;

public class InfoDayViewContract {
    public interface View extends ViewBase<Presenter> {
        static public class InfoDayVisualData{
            public MyCalendarDayViewContract.CalendarDayViewVisualData dayData = null;
            public List<MyPairedEntryViewContract.PairedEntryVisualData> entriesData = null;
            public MyCalendarDayViewContract.CalendarDayViewVisualData briefData = null;
            public Entry.BelongingDay belongingDay = null;
        }

        public void showData(InfoDayVisualData data);
        public InfoDayVisualData getData();

    }

    public interface Presenter extends PresenterBase {
        public void showInfoDay(InfoDayEntry infoDay);
    }
}
