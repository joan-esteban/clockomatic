package org.jesteban.clockomatic.fragments.editclocksday;

import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;



public class EditClockDayContract {

    public interface View extends ViewBase<Presenter> {
        public void showEntries(EntrySet entries);
    }

    public interface Presenter extends PresenterBase {
        boolean remove(Entry entry);
    }

}
