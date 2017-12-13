package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.DynamicWidgets;
S
import java.util.List;

public class ShowListDaysClocksFragment extends Fragment implements ShowListDaysClocksContract.View {
    private ShowListDaysClocksContract.Presenter presenter = null;
    private LinearLayout layout = null;
    private DynamicWidgets<TextView> dynamicTextViews = new DynamicWidgets<>(new DynamicTextViewsActions());


    public ShowListDaysClocksFragment() {
        // Required empty public constructor
    }

    public static ShowListDaysClocksFragment newInstance() {
        return new ShowListDaysClocksFragment();
    }

    @Override
    public void showTitle(String title) {
        // For future
    }

    @Override
    public void showEntries(List<ShowListDaysClocksContract.Presenter.DayDataToShow> dayDatas) {
        syncWithState(dayDatas);
    }


    protected class DynamicTextViewsActions implements DynamicWidgets.OnMyActions<TextView> {
        @Override
        public TextView createWidget(int idx) {
            TextView textView = new TextView(getContext());
            textView.setId(View.generateViewId());
            layout.addView(textView);
            return textView;
        }

        @Override
        public void removeWidgetFromView(TextView widget) {
            if (widget == null) return;
            widget.setVisibility(View.GONE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_clocks_month, container, false);
        layout = (LinearLayout) view.findViewById(R.id.month_main_layout);
        return view;
    }

    @Override
    public void setPresenter(ShowListDaysClocksContract.Presenter presenter) {
        assert (presenter != null);
        this.presenter = presenter;
        presenter.startUi();
    }


    private String getTextForDay(String[] txtEntries) {
        StringBuilder sb = new StringBuilder();
        if (txtEntries == null) return "None";

        for (String txt : txtEntries) {
            sb.append("<li>" + txt + "\n");
        }
        return sb.toString();
    }

    public void syncWithState(List<ShowListDaysClocksContract.Presenter.DayDataToShow> dayDatas) {

        dynamicTextViews.setAllWidgetAsUnused();
        int idx = 0;
        for (ShowListDaysClocksContract.Presenter.DayDataToShow dayData : dayDatas) {
            TextView textView = dynamicTextViews.getWidget(idx);
            textView.setVisibility(View.VISIBLE);

            textView.setText(Html.fromHtml("<h1>" + dayData.dateName + "</h1>" + getTextForDay(dayData.entryText)));
            idx++;
        }
        dynamicTextViews.removeWidgetUnused();
    }


}
