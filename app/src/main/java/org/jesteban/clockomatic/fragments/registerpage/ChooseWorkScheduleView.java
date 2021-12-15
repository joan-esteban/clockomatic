package org.jesteban.clockomatic.fragments.registerpage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.choosecompany.ChooseCompanyContract;
import org.jesteban.clockomatic.fragments.editclocksday.EditClocksDayV2Fragment;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;
import org.jesteban.clockomatic.model.Company;

import java.util.List;
import java.util.logging.Logger;

import static android.view.View.GONE;

public class ChooseWorkScheduleView extends Fragment implements ChooseWorkScheduleContract.View {
    private static final Logger LOGGER = Logger.getLogger(ChooseWorkScheduleView.class.getName());
    ChooseWorkScheduleContract.Presenter presenter = null;
    Spinner spinner = null;
    CardView card = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_work_schedule, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner_choose_work_schedule);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onSelectedWorkSchedule(parent.getItemAtPosition(position).toString(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                presenter.onSelectedWorkSchedule(null, -1);

            }
        });
        card = (CardView) view.findViewById(R.id.card_work_schedule_picker);
        return view;
    }


    @Override
    public void setPresenter(ChooseWorkScheduleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ChooseWorkScheduleContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void setAvailableWorkSchedule(List<String> workScheduleNames) {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, workScheduleNames.toArray(new String[0]));
        spinner.setAdapter(adapter);
    }

    @Override
    public void setVisibleWorkSchedule(Boolean visible) {
        if (visible){
            card.setVisibility(View.VISIBLE);
        }   else {
            card.setVisibility(View.GONE);
        }
    }
    @Override
    public void SetSelectedWorkSchedule(int idx){
        spinner.setSelection(idx);
    }
}
