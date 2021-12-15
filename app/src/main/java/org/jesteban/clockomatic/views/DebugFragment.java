package org.jesteban.clockomatic.views;


import android.app.Dialog;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayView;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewPresenterNoProviderLink;
import org.jesteban.clockomatic.fragments.workSchedulerEditDialog.WorkSchedulerEditDialog;
import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.SettingsDb;
import org.jesteban.clockomatic.store.ClockmaticDb;


public class DebugFragment extends Fragment {


    View view = null;
    MyCalendarDayView dayView = null;
    MyPairedEntriesGridView myLinkEntriesGridView = null;
    InfoDayView infoDayView = null;
    InfoDayViewPresenterNoProviderLink infoDayViewPresenter = null;
    TextView textView = null;
    Button debugButton = null;

    public DebugFragment() {
        // Required empty public constructor
    }

    public EntrySet getDemoEntries() {
        return new EntrySet();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_debug, container, false);
        debugButton = (Button) view.findViewById(R.id.debug_button);
        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(getContext())
                        .setTitle("Choose color")
                        .initialColor(0)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .build()
                        .show();
            }
        });

        Button debugButton2 = (Button) view.findViewById(R.id.debug_button2);
        debugButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WorkSchedulerEditDialog dialog = new WorkSchedulerEditDialog(getActivity());
                dialog.show();
            }
        });

        return view;

    }

    public void createDb() {
        ClockmaticDb clockmaticDb = new ClockmaticDb(getContext());
        SettingsDb settingsDb = new SettingsDb(clockmaticDb);
        CompaniesDb companiesDb = new CompaniesDb(clockmaticDb, settingsDb);
        Company c = new Company();
        try {
            companiesDb.addCompany(c);
        } catch (SQLException e) {
            textView.setText(e.getLocalizedMessage());
            return;
        }
        clockmaticDb.test();

    }
}
