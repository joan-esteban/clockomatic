package org.jesteban.clockomatic.fragments.entryeditdialog;


import android.os.Bundle;

import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;
import java.util.List;

public class EntryEditDialogContract {
    public interface View extends ViewBase<EntryEditDialogContract.Presenter> {
        void refresh(Entry entry);

        void showDialogDatePicker(int year, int month, int dayOfMonth);

        void showDialogTimePicker(int hour, int minute, boolean is24HourView);

        void close();

        String getDate();

        String getHour();

        String getDescription();
    }

    public interface Presenter extends PresenterBase {
        void setArgs(Bundle args);

        void clickOnDay();

        void clickOnTime();

        void clickOnRemove(PresenterBase parentPresenter);

        void clickOnOk(PresenterBase parentPresenter);

        void clickOnCancel(PresenterBase parentPresenter);

        void clickOnRefresh();

        void dialogDatePickerAnswer(int year, int month, int dayOfMonth);

        void dialogTimePickerAnswer(int hourOfDay, int minute);

        EditorStatus changeKind(int position, long id);

        EditorStatus changeTime(String txt);

        EditorStatus changeDate(String txt);

        EditorStatus changeDescription(String txt);

        List<String> getAvailablesKind();

        String getFormattedDate(Calendar calendar);

        String getFormattedHour(Calendar calendar);

        enum EditorStatus {
            UNCHANGED,
            ERROR,
            MODIFIED
        }


        interface Listener {
            void onEntryEditDialogCancel();

            void onEntryEditDialogOk(Entry initialEntry, Entry modifiedEntry);

            void onEntryEditDialogRemove(Entry entry);

        }
    }


}
