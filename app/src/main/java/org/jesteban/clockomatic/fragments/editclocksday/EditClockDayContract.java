package org.jesteban.clockomatic.fragments.editclocksday;

import org.jesteban.clockomatic.fragments.entryeditdialog.EntryEditDialogContract;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.io.IOException;


public class EditClockDayContract {

    public interface View extends ViewBase<Presenter> {
        void showEntries(EntrySet entries);
        void deleteEntry(int position);
        EntryEditDialogContract.Presenter showEditEntryDialog(int position, Entry entry) throws IOException;
    }

    public interface Presenter extends PresenterBase {
        boolean selectedEntry(int position,Entry entry) throws IOException;
    }

}
