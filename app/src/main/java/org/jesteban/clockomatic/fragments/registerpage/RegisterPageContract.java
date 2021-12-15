package org.jesteban.clockomatic.fragments.registerpage;

import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;


public class RegisterPageContract {
    public interface View extends ViewBase<Presenter> {
        void showDate(Calendar date);
        void showMessage(String text);
    }

    public interface Presenter extends PresenterBase {
        boolean register(Entry entry);
        void selected(Calendar date);
        boolean remove(Entry entry);
        void onResume();
    }
}
