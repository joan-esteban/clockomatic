package org.jesteban.clockomatic.fragments.registerpage;

import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.Calendar;


public class RegisterPageContract {
    public interface View extends ViewBase<Presenter> {
        public void showDate(Calendar date);
    }

    public interface Presenter extends PresenterBase {
        boolean register(Entry entry);
        void selected(Calendar date);
        boolean remove(Entry entry);
    }
}
