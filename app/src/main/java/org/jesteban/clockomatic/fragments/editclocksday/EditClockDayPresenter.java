package org.jesteban.clockomatic.fragments.editclocksday;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.entryeditdialog.EntryEditDialogContract;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.providers.UndoProviderContract;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

public class EditClockDayPresenter implements EditClockDayContract.Presenter,
        SelectedDayProviderContract.Listener,
        EntriesProviderContract.Listener,
        EntryEditDialogContract.Presenter.Listener {
    private static final Logger LOGGER = Logger.getLogger(EditClockDayPresenter.class.getName());
    private PresenterBase parent = null;
    private SelectedDayProviderContract selectedDay = null;
    private EntriesProviderContract entriesProvider = null;
    private UndoProviderContract undoProvider = null;

    private EditClockDayContract.View view = null;
    private Context context = null;

    public EditClockDayPresenter(EditClockDayContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public List<Provider> getBindings() {
        return parent.getBindings();
    }

    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        // Nothing
    }

    @Override
    public void startUi() {
        // Nothing to do
    }

    @Override
    public boolean selectedEntry(int position, Entry entry) throws IOException {
        view.showEditEntryDialog(position, entry);
        return true;
    }

    @Override
    public void onChangeSelectedDay() {
        syncWithView();
    }

    @Override
    public void onChangeEntries() {
        syncWithView();
    }

    private void syncWithView() {
        Entry floatingEntry = selectedDay.getFloatingEntry();
        EntrySet entriesDay = null;
        try {
            entriesDay = entriesProvider.getEntriesBelongingDayStartWith(floatingEntry.getBelongingDay());
        } catch (Exception e) {
            // TODO: On error???
        }
        view.showEntries(entriesDay);
    }

    @Override
    public void onEntryEditDialogCancel() {
        //Nothing to do
    }

    @Override
    public void onEntryEditDialogOk(Entry initialEntry, Entry modifiedEntry) {
        LOGGER.info("Dialog give me next entry to update: " + modifiedEntry.toString());
        try {
            UndoAction undoRemove = entriesProvider.remove(initialEntry);
            UndoAction undoRegister = entriesProvider.register(modifiedEntry);
            undoRegister.add(undoRemove);
            undoRegister.description = String.format(context.getResources().getString(R.string.msg_undo_modified_date), initialEntry.toShortString());
            undoProvider.push(undoRegister);
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.info("Error onEntryEditDialogOk: " + e.toString());
        }
    }

    @Override
    public void onEntryEditDialogRemove(Entry entry) {
        LOGGER.info("Dialog give me next entry to remove: " + entry.toString());
        try {
            UndoAction undo = entriesProvider.remove(entry);
            if (undo != null) {
                undo.description = String.format(context.getResources().getString(R.string.msg_undo_remove_date), entry.toShortString());
                undoProvider.push(undo);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.info("Error onEntryEditDialogRemove: " + e.toString());
        }
    }


    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProviderContract i) {
        entriesProvider = i;
        entriesProvider.subscribe(this);
    }

    public void setSelectedDayProvider(@NonNull SelectedDayProviderContract i) {
        selectedDay = i;
        selectedDay.subscribe(this);
    }

    public void setUndoProvider(@NonNull UndoProviderContract i) {
        undoProvider = i;
    }
}
