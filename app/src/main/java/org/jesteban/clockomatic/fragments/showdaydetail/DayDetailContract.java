package org.jesteban.clockomatic.fragments.showdaydetail;


import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;

public class DayDetailContract {
    public interface View extends ViewBase<Presenter> {
        public void showDayName(String dayName);
        public void showDayNumber(String dayNumber);
        public void showEntries(String entries);
        public void showInfo(String info);
    }

    public interface Presenter extends PresenterBase {
    }
}
