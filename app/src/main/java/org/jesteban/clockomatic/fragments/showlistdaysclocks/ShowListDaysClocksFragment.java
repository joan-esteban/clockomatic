package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.DynamicWidgets;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayView;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewPresenterNoProviderLink;

import java.util.List;

public class ShowListDaysClocksFragment extends Fragment implements ShowListDaysClocksContract.View{
    private ShowListDaysClocksContract.Presenter presenter = null;
    private TableLayout layout = null;
    private DynamicWidgets<InfoDayView> dynamicInfoDayViews = new DynamicWidgets<>(new DynamicInfoDayViewActions());


    public ShowListDaysClocksFragment() {
        // Required empty public constructor
    }

    public static ShowListDaysClocksFragment newInstance() {
        return new ShowListDaysClocksFragment();
    }



    @Override
    public void showData(List<InfoDayViewContract.View.InfoDayVisualData> daysData) {
        syncWithState(daysData);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_clocks_month, container, false);
        layout = (TableLayout) view.findViewById(R.id.month_table_layout);
        //syncWithState(null);
        return view;
    }

    @Override
    public void setPresenter(ShowListDaysClocksContract.Presenter presenter) {
        assert (presenter != null);
        this.presenter = presenter;
        presenter.startUi();
    }

    @Override
    public ShowListDaysClocksContract.Presenter getPresenter() {
        return this.presenter;
    }




    public void syncWithState(List<InfoDayViewContract.View.InfoDayVisualData> daysData) {

        dynamicInfoDayViews.setAllWidgetAsUnused();
        int idx = 0;
        for (InfoDayViewContract.View.InfoDayVisualData data : daysData){
            InfoDayView infoDayView = dynamicInfoDayViews.getWidget(idx);
            infoDayView.setBackgroundResource(R.drawable.layout_bg_line_bottom);
            //infoDayView.setBackgroundResource(R.drawable.layout_bg_month);
            infoDayView.setPadding(-5,-5,5,5);
            infoDayView.setVisibility(View.VISIBLE);
            infoDayView.showData(data);

            infoDayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoDayView idv = (InfoDayView) v;
                    Snackbar.make(getView(), "ERROR!!!!!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    presenter.clickOnDay(idv.getData().belongingDay);
                }
            });
            idx++;
        }
        dynamicInfoDayViews.removeWidgetUnused();

    }




    protected class DynamicInfoDayViewActions implements DynamicWidgets.OnMyActions<InfoDayView> {
        @Override
        public InfoDayView createWidget(int idx) {
            InfoDayView res =  new InfoDayView(getContext());
            res.setPresenter(new InfoDayViewPresenterNoProviderLink(res,getContext()));
            res.setVisibility(View.VISIBLE);
            res.setId(View.generateViewId());

            layout.addView(res);
            return res;
        }

        @Override
        public void removeWidgetFromView(InfoDayView widget) {
            if (widget == null) return;
            widget.setVisibility(View.GONE);
        }


    }

}
