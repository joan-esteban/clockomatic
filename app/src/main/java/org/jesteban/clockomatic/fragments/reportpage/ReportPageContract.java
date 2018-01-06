package org.jesteban.clockomatic.fragments.reportpage;

import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;

public class ReportPageContract {
    public interface View extends ViewBase<Presenter> {
        public void showSelectedMonth(Calendar month);
    }

    public interface Presenter extends PresenterBase {
        public void selectedMonth(Calendar day);
    }
}
