package org.jesteban.clockomatic.fragments.reportpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPageFragment;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.fragments.reportpage.showlistdaysclocks.ShowListDaysClocksContract;
import org.jesteban.clockomatic.fragments.reportpage.showlistdaysclocks.ShowListDaysClocksMonthPresenter;
import org.jesteban.clockomatic.views.MyDatePickerFragment;
import org.jesteban.clockomatic.fragments.reportpage.showlistdaysclocks.ShowListDaysClocksFragment;


import java.util.Calendar;
import java.util.logging.Logger;

public class ReportPageFragment extends Fragment implements MyDatePickerFragment.OnFragmentInteractionListener,
                                ReportPageContract.View {
    private static final Logger LOGGER = Logger.getLogger(RegisterPageFragment.class.getName());
    private ReportPageContract.Presenter presenter = null;
    private MyDatePickerFragment myDatePickerFragment = null;
    private ShowListDaysClocksFragment viewClocksMonthFragment = null;
    private ShowListDaysClocksContract.Presenter showListDaysClocksMonthPresenter = null;//new ShowListDaysClocksMonthPresenter();


    public ReportPageFragment() {
        // Required empty public constructor
    }


    public static ReportPageFragment newInstance() {
        return new ReportPageFragment();
    }

    @Override
    public void setPresenter(ReportPageContract.Presenter presenter) {
        this.presenter =  presenter;
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
        viewClocksMonthFragment = (ShowListDaysClocksFragment) getFragment(R.id.fragment_view_clocks_month);
        showListDaysClocksMonthPresenter = new ShowListDaysClocksMonthPresenter(viewClocksMonthFragment);
        SewingBox.sewPresentersView(showListDaysClocksMonthPresenter,presenter,viewClocksMonthFragment);
        myDatePickerFragment.setMonthMode();
        //presenter.startUi();
        return view;
    }






    @Override // Callback from UI
    public void onChangedSelectedDay(Calendar day) {
        assert(presenter!=null);
        LOGGER.info("calling Presenter selectedMonth");
        presenter.selectedMonth(day);
    }

    @Override
    public void showSelectedMonth(Calendar month) {
        assert(viewClocksMonthFragment!=null);
        myDatePickerFragment.setDay(month);
        //viewClocksMonthFragment.setShowBelongingMonthPrefix(month);
        //showListDaysClocksMonthPresenter.
    }





}
