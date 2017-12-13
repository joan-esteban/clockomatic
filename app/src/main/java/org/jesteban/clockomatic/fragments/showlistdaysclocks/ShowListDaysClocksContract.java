package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.model.EntrySet;


public class ShowListDaysClocksContract {
    public interface View extends ViewBase<Presenter> {
        public void showTitle(String title);
        public void showEntries(EntrySet entries);
    }

    public interface Presenter extends PresenterBase {
        //public void selectedMonth(Calendar day);
        //public void editMonth(String month);
    }
}
