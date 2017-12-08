package org.jesteban.clockomatic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.fragments.register_page.MyDatePickerFragment;
import org.jesteban.clockomatic.fragments.report_page.ViewClocksMonthFragment;
import org.jesteban.clockomatic.helpers.DependencyInjector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class ReportPageFragment extends Fragment implements MyDatePickerFragment.OnFragmentInteractionListener, Observer,DependencyInjector.Injectable<StateController>  {
    private static final Logger LOGGER = Logger.getLogger(RegisterPageFragment.class.getName());
    private StateController stateController = null;
    private MyDatePickerFragment myDatePickerFragment = null;
    private ViewClocksMonthFragment viewClocksMonthFragment = null;
    private final DependencyInjector<StateController> stateControllerInjector = new DependencyInjector<StateController>();

    public ReportPageFragment() {
        // Required empty public constructor
    }


    public static ReportPageFragment newInstance() {
        return new ReportPageFragment();
    }

    @Override // DependencyInjector.Injectable<StateController>
    public boolean setDependency(StateController dependency) {
        if (dependency==null) return false;
        this.stateController = dependency;
        stateController.addObserver(this);
        return true;
    }

    private Fragment getFragment(int id){
        Fragment res = getFragmentManager().findFragmentById(id);
        if (res==null) res = getChildFragmentManager().findFragmentById(id);
        return res;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_report_page, container, false);
        myDatePickerFragment = (MyDatePickerFragment)  getFragment(R.id.fragment_report_page_date_picker);
        viewClocksMonthFragment = (ViewClocksMonthFragment) getFragment(R.id.fragment_view_clocks_month);
        myDatePickerFragment.setMonthMode();
        stateControllerInjector.injectList(stateController, getChildFragmentManager().getFragments());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (stateController!= null) stateController.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (stateController!= null) stateController.deleteObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stateController!= null) stateController.deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object arg) {
        // Nothing to to, my fragment already have state
    }

    @Override
    public void onChangedSelectedDay(Calendar day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/");
        String dayPrefix = sdf.format(day.getTime());
        viewClocksMonthFragment.setShowBelongingMonthPrefix(dayPrefix);
    }
}
