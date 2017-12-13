package org.jesteban.clockomatic.fragments.showdaydetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.showlistdaysclocks.ShowListDaysClocksFragment;

import java.util.logging.Logger;



public class  DayDetailFragment extends Fragment implements DayDetailContract.View{
    private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksFragment.class.getName());

    private TextView txtNameDay = null;
    private TextView txtNumDay = null;
    private TextView txtEntries = null;
    private TextView txtWorkInfo = null;
    public DayDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_detail, container, false);
        txtNameDay = (TextView) view.findViewById(R.id.text_name_day);
        txtNumDay = (TextView) view.findViewById(R.id.text_num_day);
        txtEntries = (TextView) view.findViewById(R.id.text_pair_entries);
        txtWorkInfo = (TextView) view.findViewById(R.id.text_work_info);
        return view;
    }


    @Override
    public void showDayName(String dayName) {
        txtNameDay.setText(dayName);
    }

    @Override
    public void showDayNumber(String dayNumber) {
        txtNumDay.setText(dayNumber);
    }

    @Override
    public void showEntries(String entries) {
        txtEntries.setText(Html.fromHtml(entries));
    }

    @Override
    public void showInfo(String info) {
        txtWorkInfo.setText(Html.fromHtml(info));
    }



    @Override
    public void setPresenter(DayDetailContract.Presenter presenter) {
        presenter.startUi();
    }
}
