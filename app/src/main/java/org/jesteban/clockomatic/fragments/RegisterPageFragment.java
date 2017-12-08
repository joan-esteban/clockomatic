package org.jesteban.clockomatic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.fragments.register_page.MyDateTimePickerFragment;
import org.jesteban.clockomatic.fragments.register_page.ViewClocksDayFragment;
import org.jesteban.clockomatic.helpers.DependencyInjector;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.State;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;


public class RegisterPageFragment extends Fragment implements MyDateTimePickerFragment.OnMyDateTimePickerFragmentListener, Observer, DependencyInjector.Injectable<StateController> {
    private static final Logger LOGGER = Logger.getLogger(RegisterPageFragment.class.getName());
    private StateController stateController = null;
    private MyDateTimePickerFragment myDateTimePickerFragment = null;
    private ViewClocksDayFragment viewClocksDayFragment = null;
    private final DependencyInjector<StateController> stateControllerInjector = new DependencyInjector<StateController>();

    public RegisterPageFragment() {
        // Required empty public constructor
    }

    public static RegisterPageFragment newInstance() {
        return new RegisterPageFragment();
    }

    @Override //DependencyInjector.Injectable<StateController>
    public boolean setDependency(StateController dependency) {
        if (dependency==null) return false;
        this.stateController = dependency;
        stateController.addObserver(this);
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_page, container, false);
        viewClocksDayFragment = (ViewClocksDayFragment) getChildFragmentManager().findFragmentById(R.id.fragment_view_clocks_day);
        myDateTimePickerFragment = (MyDateTimePickerFragment) getFragmentManager().findFragmentById(R.id.fragment_my_date_time_picker);
        if (stateController != null){
            stateControllerInjector.injectList(stateController, getChildFragmentManager().getFragments());
            syncWithState(stateController.getState());
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (stateController != null) stateController.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (stateController != null) stateController.deleteObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stateController != null) stateController.deleteObserver(this);
    }


    @Override
    public void register(Calendar date) {
        if (stateController != null) {
            Entry entry = new Entry(date);
            if (!stateController.register(entry)) {
                Snackbar.make(getView(), "ERROR!!!!!!" + stateController.getState().getStatus().statusDesc, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else Snackbar.make(getView(), "Register Date " + entry, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void selected(Calendar date) {
        LOGGER.info("selected Date " + date.getTime() + " / " + date);
        if (stateController != null) {
            Entry entry = new Entry(date);
            stateController.setFloatingEntry(entry);
        }
    }

    public void syncWithState(State state) {
        if (state == null) return;
        LOGGER.info("syncWithState " + state.getFloatingEntry());
        if (myDateTimePickerFragment != null)
            myDateTimePickerFragment.setDate(state.getFloatingEntry().getRegisterDate());
        LOGGER.info("syncWithState floating = " + state.getFloatingEntry());
        if (viewClocksDayFragment != null)
            viewClocksDayFragment.setShowBelongingDayPrefix(state.getFloatingEntry().getBelongingDay());
    }

    @Override
    public void update(Observable observable, Object arg) {
        LOGGER.info("update from a Observable");
        if (observable instanceof StateController) {
            StateController state = (StateController) observable;
            syncWithState(state.getState());
            LOGGER.info("update from StateController");
        }
    }


}
