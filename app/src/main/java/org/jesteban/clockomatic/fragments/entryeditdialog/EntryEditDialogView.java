package org.jesteban.clockomatic.fragments.entryeditdialog;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Entry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class EntryEditDialogView extends DialogFragment implements EntryEditDialogContract.View {
    private EntryEditDialogContract.Presenter presenter = null;
    private Entry entry = null;
    private TextView textBelonging = null;
    private EditText editDay = null;
    private EditText editTime = null;
    private EditText editDescription = null;
    private ImageButton buttonDate = null;
    private ImageButton buttonTime = null;
    private Button buttonCancel = null;
    private Button buttonOk = null;
    private ImageButton buttonRemove = null;
    private ImageButton buttonRefresh = null;
    private Spinner spinnerKind = null;
    private ImageView imageKind = null;

    public EntryEditDialogView() {
        // Empty constructor is requided
    }

    public static EntryEditDialogView newInstance(Entry entry) throws IOException {
        EntryEditDialogView frag = new EntryEditDialogView();
        Bundle args = new Bundle();
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bs);
            os.writeObject(entry);
            os.close();
            args.putByteArray("entry", bs.toByteArray());

            frag.setArguments(args);

        } catch (IOException e) {
            e.printStackTrace();
            throw (e);

        }
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new EntryEditDialogPresenter(this, getContext());
        View view = inflater.inflate(R.layout.entry_editor_dialog, container, false);
        getDialog().setTitle("Simple Dialog");
        presenter.setArgs(getArguments());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonRemove = (ImageButton) view.findViewById(R.id.button_remove);
        buttonRefresh = (ImageButton) view.findViewById(R.id.button_refresh);
        buttonDate = (ImageButton) view.findViewById(R.id.button_date);
        buttonTime = (ImageButton) view.findViewById(R.id.button_time);
        textBelonging = (TextView) view.findViewById(R.id.text_belonging);
        editDay = (EditText) view.findViewById(R.id.edit_day);
        editTime = (EditText) view.findViewById(R.id.edit_time);
        editDescription = (EditText) view.findViewById(R.id.edit_description);
        spinnerKind = (Spinner) view.findViewById(R.id.spinner_kind);
        imageKind = (ImageView) view.findViewById(R.id.image_kind);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.clickOnRefresh();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getTargetFragment();
                ViewBase f2 = (ViewBase) f;
                presenter.clickOnCancel(f2.getPresenter());
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getTargetFragment();
                ViewBase f2 = (ViewBase) f;
                presenter.clickOnOk(f2.getPresenter());
            }
        });
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getTargetFragment();
                ViewBase f2 = (ViewBase) f;
                presenter.clickOnRemove(f2.getPresenter());
            }
        });
        editDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing to do
            }

            @Override
            public void afterTextChanged(Editable s) {
                EntryEditDialogContract.Presenter.EditorStatus status = presenter.changeDate(s.toString());
                setColor(editDay, status);

            }
        });
        editTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing to do
            }

            @Override
            public void afterTextChanged(Editable s) {
                EntryEditDialogContract.Presenter.EditorStatus status = presenter.changeTime(s.toString());
                setColor(editTime, status);

            }
        });
        editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EntryEditDialogContract.Presenter.EditorStatus status = presenter.changeDescription(s.toString());
                setColor(editDescription, status);

            }
        });
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickOnDay();
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickOnTime();
            }
        });

        spinnerKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EntryEditDialogContract.Presenter.EditorStatus status = presenter.changeKind(position, id);
                setColor(spinnerKind, status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> availableKind = presenter.getAvailablesKind();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, availableKind);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKind.setAdapter(adapter);
        presenter.startUi();
    }

    int getColorForStatus(EntryEditDialogContract.Presenter.EditorStatus status) {
        switch (status) {
            case ERROR:
                return Color.RED;
            case MODIFIED:
                return Color.BLUE;
            case UNCHANGED:
                return Color.GRAY;
        }
        return Color.GRAY;
    }

    void setColor(TextView view, EntryEditDialogContract.Presenter.EditorStatus status) {
        view.setTextColor(getColorForStatus(status));
    }

    void setColor(Spinner view, EntryEditDialogContract.Presenter.EditorStatus status) {
        View v = view.getSelectedView();
        setColor((TextView) v, status);
    }

    @Override
    public EntryEditDialogContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(EntryEditDialogContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void refresh(Entry entry) {
        this.entry = entry;

        String formattedDate = presenter.getFormattedDate(entry.getRegisterDate());
        String formattedHour = presenter.getFormattedHour(entry.getRegisterDate());
        if (editDay != null) editDay.setText(formattedDate);
        if (editTime != null) editTime.setText(formattedHour);
        if (textBelonging != null) textBelonging.setText(entry.getBelongingDay());
        if (editDescription != null) editDescription.setText(entry.getDescription());
        if (spinnerKind != null) spinnerKind.setSelection(entry.getKind().ordinal());
        if (imageKind != null) {
            int resId;
            switch (entry.getKind()) {
                case WORKING:
                    resId = R.drawable.working_time_icon;
                    break;
                default:
                    resId = R.drawable.ic_toys_black_24dp;
            }
            imageKind.setImageResource(resId);
        }
    }

    @Override
    public void showDialogDatePicker(int year, int month, int dayOfMonth) {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.dialogDatePickerAnswer(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth);
        dialog.show();
    }

    @Override
    public void showDialogTimePicker(int hour, int minute, boolean is24HourView) {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                presenter.dialogTimePickerAnswer(hourOfDay, minute);
            }
        }, hour, minute, is24HourView);
        dialog.show();
    }

    @Override
    public void close() {
        this.dismiss();
    }

    @Override
    public String getDate() {
        return editDay.getText().toString();
    }

    @Override
    public String getHour() {
        return editTime.getText().toString();
    }

    @Override
    public String getDescription() {
        return editDescription.getText().toString();
    }

}
