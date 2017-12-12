package org.jesteban.clockomatic.fragments.reportpage.showdaydetail;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageContract;

import java.util.Calendar;

public class DayDetailContract {
    public interface View extends ViewBase<Presenter> {
        public void showDayName(String dayName);
        public void showDayNumber(String dayNumber);
        public void showEntries(String entries);
        public void showInfo(String info);
    }

    public interface Presenter extends PresenterBase {
        //public void selectedMonth(Calendar day);
    }
}
