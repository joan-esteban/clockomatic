package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.DynamicWidgets;

import java.util.List;

public class ShowListDaysClocksFragment extends Fragment implements ShowListDaysClocksContract.View {
    private ShowListDaysClocksContract.Presenter presenter = null;
    private TableLayout layout = null;
    private DynamicWidgets<MyWidget> dynamicTextViews = new DynamicWidgets<>(new DynamicTextViewsActions());


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

    class MyWidget{
        TextView textView = null;
        MyWidget(Context context){

            TableRow tableRow = new TableRow(context);

            textView = new TextView(context);
            textView.setId(View.generateViewId());
            tableRow.addView(textView);
            layout.addView(tableRow);
        }

        void setVisibility(int v){
            textView.setVisibility(v);
        }

        void setText(CharSequence text){
            textView.setText(text);
        }

    }

    protected class DynamicTextViewsActions implements DynamicWidgets.OnMyActions<MyWidget> {
        @Override
        public MyWidget createWidget(int idx) {
            return new MyWidget(getContext());
        }

        @Override
        public void removeWidgetFromView(MyWidget widget) {
            if (widget == null) return;
            widget.setVisibility(View.GONE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_clocks_month, container, false);
        layout = (TableLayout) view.findViewById(R.id.month_table_layout);
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
            MyWidget textView = dynamicTextViews.getWidget(idx);
            textView.setVisibility(View.VISIBLE);

            textView.setText(Html.fromHtml("<h1>" + dayData.dateName + "</h1>" + getTextForDay(dayData.entryText)));
            idx++;
        }
        dynamicTextViews.removeWidgetUnused();
    }


}
