package org.jesteban.clockomatic.fragments.entryeditdialog;


import android.content.Context;
import android.os.Bundle;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.providers.Provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class EntryEditDialogPresenter implements EntryEditDialogContract.Presenter {
    private static final Logger LOGGER = Logger.getLogger(EntryEditDialogPresenter.class.getName());
    Entry entry = null;
    Entry entryOriginal = null;
    private EntryEditDialogContract.View view = null;
    private Context context = null;
    private EntryEditDialogContract.Presenter.Listener parentPresenter = null;
    private DateFormat dateFormatDate = DateFormat.getDateInstance(DateFormat.SHORT);
    private SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm");


    public EntryEditDialogPresenter(EntryEditDialogContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public List<Provider> getBindings() {
        return new ArrayList<>();
    }

    @Override
    public void setParent(PresenterBase parent) {
        parentPresenter = (EntryEditDialogContract.Presenter.Listener) parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startUi() {
        view.refresh(entry);
    }

    @Override
    public void setArgs(Bundle args) {
        byte[] entrySerialized = args.getByteArray("entry");
        ByteArrayInputStream bs = new ByteArrayInputStream(entrySerialized); // bytes es el byte[]
        try {
            ObjectInputStream is = new ObjectInputStream(bs);
            entry = (Entry) is.readObject();
            entryOriginal = new Entry(entry);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void setDate(int year, int month, int dayOfMonth) {
        Calendar calendar = entry.getRegisterDate();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        entry.setRegisterDate(calendar);
    }

    @Override
    public void dialogDatePickerAnswer(int year, int month, int dayOfMonth) {
        LOGGER.info(String.format("dialogDatePickerAnswer %d/%d/%d", year, month, dayOfMonth));
        setDate(year, month, dayOfMonth);
        view.refresh(entry);
    }

    void setTime(int hourOfDay, int minute) {
        Calendar calendar = entry.getRegisterDate();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        entry.setRegisterDate(calendar);
    }

    @Override
    public void dialogTimePickerAnswer(int hourOfDay, int minute) {
        LOGGER.info(String.format("dialogTimePickerAnswer %d:%d", hourOfDay, minute));
        setTime(hourOfDay, minute);
        view.refresh(entry);
    }

    @Override
    public EditorStatus changeKind(int position, long id) {
        Entry.Kind v = Entry.Kind.values()[position];
        if (entryOriginal.getKind() == v) return EditorStatus.UNCHANGED;
        entry.setKind(v);
        view.refresh(entry);
        return EditorStatus.MODIFIED;
    }

    @Override
    public EditorStatus changeTime(String txt) {
        if (txt.equals(getFormattedHour(entryOriginal.getRegisterDate())))
            return EditorStatus.UNCHANGED;
        if (txt.length() > 5) return EditorStatus.ERROR;
        try {
            Date date = dateFormatHour.parse(txt);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            setTime(hour, minute);
            LOGGER.info(String.format("change Time to : %d: %d from [%s]", hour, minute, txt));
        } catch (ParseException e) {
            e.printStackTrace();
            return EditorStatus.ERROR;
        }
        return EditorStatus.MODIFIED;
    }

    @Override
    public EditorStatus changeDate(String txt) {
        if (txt.equals(getFormattedDate(entryOriginal.getRegisterDate())))
            return EditorStatus.UNCHANGED;
        try {
            Date date = dateFormatDate.parse(txt);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            setDate(year, month, day);
        } catch (ParseException e) {
            e.printStackTrace();
            return EditorStatus.ERROR;
        }
        return EditorStatus.MODIFIED;
    }

    @Override
    public EditorStatus changeDescription(String txt) {
        if (txt.equals(entryOriginal.getDescription())) return EditorStatus.UNCHANGED;
        LOGGER.info(String.format("Description original[%s] -> [%s]", entryOriginal.getDescription(), txt));
        entry.setDescription(txt);
        return EditorStatus.MODIFIED;
    }

    @Override
    public void clickOnDay() {
        Calendar calendar = entry.getRegisterDate();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        view.showDialogDatePicker(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void clickOnTime() {
        Calendar calendar = entry.getRegisterDate();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        view.showDialogTimePicker(hour, minute, true);
    }

    @Override
    public void clickOnRemove(PresenterBase parentPresenter) {
        EntryEditDialogContract.Presenter.Listener l = (EntryEditDialogContract.Presenter.Listener) parentPresenter;
        l.onEntryEditDialogRemove(entryOriginal);
        view.close();
    }

    @Override
    public void clickOnOk(PresenterBase parentPresenter) {
        EntryEditDialogContract.Presenter.Listener l = (EntryEditDialogContract.Presenter.Listener) parentPresenter;
        l.onEntryEditDialogOk(entryOriginal, entry);
        view.close();
    }

    @Override
    public void clickOnCancel(PresenterBase parentPresenter) {
        view.close();
    }

    @Override
    public void clickOnRefresh() {
        entry = new Entry(entryOriginal);
        view.refresh(entry);
    }

    @Override
    public List<String> getAvailablesKind() {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.kind_array)));
    }

    @Override
    public String getFormattedDate(Calendar calendar) {
        return dateFormatDate.format(calendar.getTime());
    }

    @Override
    public String getFormattedHour(Calendar calendar) {
        return dateFormatHour.format(calendar.getTime());
    }
}
