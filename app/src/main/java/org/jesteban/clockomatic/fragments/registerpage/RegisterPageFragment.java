package org.jesteban.clockomatic.fragments.registerpage;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.editclocksday.EditClockDayPresenter;
import org.jesteban.clockomatic.fragments.showdaydetail.DayDetailFragment;
import org.jesteban.clockomatic.fragments.showdaydetail.DayDetailPresenter;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.views.MyDateTimePickerFragment;
import org.jesteban.clockomatic.fragments.editclocksday.EditClocksDayFragment;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.State;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


public class RegisterPageFragment extends Fragment implements MyDateTimePickerFragment.OnMyDateTimePickerFragmentListener,
        RegisterPageContract.View {
    private static final Logger LOGGER = Logger.getLogger(RegisterPageFragment.class.getName());
    RegisterPageContract.Presenter presenter = null;
    private MyDateTimePickerFragment myDateTimePickerFragment = null;

    public RegisterPageFragment() {
        // Required empty public constructor
    }

    public static RegisterPageFragment newInstance() {
        return new RegisterPageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_page, container, false);
        myDateTimePickerFragment = (MyDateTimePickerFragment) getChildFragmentManager().findFragmentById(R.id.fragment_my_date_time_picker);
        EditClocksDayFragment viewClocksDayFragment = (EditClocksDayFragment) getChildFragmentManager().findFragmentById(R.id.fragment_view_clocks_day);
        SewingBox.sewPresentersView(new EditClockDayPresenter(viewClocksDayFragment), presenter, viewClocksDayFragment);

        DayDetailFragment dayDetailFragment = (DayDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detailed_info_day);
        if (dayDetailFragment == null) {
            throw new NoSuchElementException("can't find fragment for day detail");
        }
        SewingBox.sewPresentersView(new DayDetailPresenter(dayDetailFragment, getContext()), presenter, dayDetailFragment);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }


    @Override
    public void register(Calendar date) {
        if (presenter != null) {
            Entry entry = new Entry(date);
            if (!presenter.register(entry)) {
                Snackbar.make(getView(), "ERROR!!!!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else Snackbar.make(getView(), "Register Date " + entry, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void selected(Calendar date) {
        LOGGER.info("selected Date " + date.getTime() + " / " + date);
        presenter.selected(date);
    }

    public void syncWithState(State state) {
        if (state == null) return;
        LOGGER.info("syncWithState " + state.getFloatingEntry());
        if (myDateTimePickerFragment != null)
            myDateTimePickerFragment.setDate(state.getFloatingEntry().getRegisterDate());

    }


    @Override
    public void setPresenter(RegisterPageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDate(Calendar date) {
        if (myDateTimePickerFragment != null)
            LOGGER.info("RegisterPageFragmet::showDate setDate to " + date.getTime());
            myDateTimePickerFragment.setDate(date);
    }
}
