package org.jesteban.clockomatic.fragments.registerpage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.editclocksday.EditClockDayPresenter;
import org.jesteban.clockomatic.fragments.editclocksday.EditClocksDayV2Fragment;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayView;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewPresenter;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.views.MyDateTimePickerFragment;

import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RegisterPageFragment extends Fragment implements MyDateTimePickerFragment.OnMyDateTimePickerFragmentListener,
        RegisterPageContract.View {
    private static final Logger LOGGER = Logger.getLogger(RegisterPageFragment.class.getName());
    RegisterPageContract.Presenter presenter = null;
    private MyDateTimePickerFragment myDateTimePickerFragment = null;
    private View view=null;
    private FloatingActionButton floatingRegisterButton = null;

    public RegisterPageFragment() {
        // Required empty public constructor
    }

    public static RegisterPageFragment newInstance() {
        return new RegisterPageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // https://stackoverflow.com/questions/14083950/duplicate-id-tag-null-or-parent-id-with-another-fragment-for-com-google-androi
        if (view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_register_page, container, false);
        } catch (InflateException e){
            LOGGER.log(Level.SEVERE, "Exception inflating view for RegisterPage due in debug there are 3 pages" + e.toString());
        }
        myDateTimePickerFragment = (MyDateTimePickerFragment) getChildFragmentManager().findFragmentById(R.id.fragment_my_date_time_picker);
        EditClocksDayV2Fragment viewClocksDayFragment = (EditClocksDayV2Fragment) getChildFragmentManager().findFragmentById(R.id.fragment_view_clocks_day);
        SewingBox.sewPresentersView(new EditClockDayPresenter(viewClocksDayFragment, getContext()), presenter, viewClocksDayFragment);
        // This is the brief info show
        /*
        DayDetailFragment dayDetailFragment = (DayDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detailed_info_day);
        if (dayDetailFragment == null) {

        }
        SewingBox.sewPresentersView(new DayDetailPresenter(dayDetailFragment, getContext()), presenter, dayDetailFragment);
        */
        InfoDayView infoDayView = (InfoDayView) view.findViewById(R.id.info_day_view);
        if (infoDayView == null) throw new NoSuchElementException("can't find fragment for day detail");
        floatingRegisterButton = (FloatingActionButton) view.findViewById(R.id.register_main);
        floatingRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressRegisterDate();
            }
        });
        SewingBox.sewPresentersView(new InfoDayViewPresenter(infoDayView, getContext()), presenter, infoDayView);
        ChooseWorkScheduleView chooseWorkScheduleView = (ChooseWorkScheduleView) getChildFragmentManager().findFragmentById(R.id.choose_work_schedule);
        SewingBox.sewPresentersView(new ChooseWorkSchedulePresenter(chooseWorkScheduleView, getContext()), presenter, chooseWorkScheduleView);
        chooseWorkScheduleView.getPresenter().startUi();
        return view;
    }
    private void pressRegisterDate() {
        Calendar registerDate = myDateTimePickerFragment.getComposedDate(null);
        register(registerDate);
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
            presenter.register(entry);
        }
    }

    @Override
    public void selected(Calendar date) {
        LOGGER.info("selected Date " + date.getTime() + " / " + date);
        presenter.selected(date);
    }

    @Override
    public void setPresenter(RegisterPageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public RegisterPageContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void showDate(Calendar date) {
        if (myDateTimePickerFragment != null)
            LOGGER.info("RegisterPageFragmet::showDate setDate to " + date.getTime());
            myDateTimePickerFragment.setDate(date);
    }
    @Override
    public void showMessage(String text) {
        Snackbar.make(getView(), text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
