package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.editclocksday.EditClocksDayV2Fragment;
import org.jesteban.clockomatic.fragments.infoperiodview.InfoPeriodView;
import org.jesteban.clockomatic.fragments.infoperiodview.InfoPeriodViewContract;
import org.jesteban.clockomatic.fragments.infoperiodview.InfoPeriodViewPresenterNoProviderLink;
import org.jesteban.clockomatic.helpers.DynamicWidgets;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayView;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewPresenterNoProviderLink;
import org.jesteban.clockomatic.helpers.SewingBox;

import java.util.List;

public class ShowListDaysClocksFragment extends Fragment implements ShowListDaysClocksContract.View{
    private ShowListDaysClocksContract.Presenter presenter = null;
    private ListView layout = null;
    private InfoDayViewAdapter adapter = null;
    private InfoPeriodView infoPeriodView=null;
    private InfoPeriodViewContract.Presenter infoPeriodPresenter=null;

    public ShowListDaysClocksFragment() {
        // Required empty public constructor
    }

    public static ShowListDaysClocksFragment newInstance() {
        return new ShowListDaysClocksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_clocks_month, container, false);
        layout = (ListView) view.findViewById(R.id.month_table_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new InfoDayViewAdapter();
        layout.setAdapter(adapter);
        infoPeriodView = (InfoPeriodView) view.findViewById(R.id.info_month_view);
        infoPeriodPresenter = new InfoPeriodViewPresenterNoProviderLink(infoPeriodView);
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


    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public InfoDayViewContract.Presenter getInfoDayPresenter(int idx){
        return null;
    }

    @Override
    public InfoPeriodViewContract.Presenter getInfoPeriodViewPresenter() {
        return infoPeriodPresenter;
    }

    /*
    class InfoDayViewAdapter extends RecyclerView.Adapter<InfoDayViewAdapter.InfoDayViewHolder> {
        @Override
        public InfoDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            //LayoutInflater inflater = LayoutInflater.from(getActivity());
            //View view = inflater.inflate(R.layout.cardview_register_clock, null);
            InfoDayView view =  new InfoDayView(getActivity());
            view.setPresenter(new InfoDayViewPresenterNoProviderLink(view,getContext()));
            view.setVisibility(View.VISIBLE);
            view.setId(View.generateViewId());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            view.requestLayout();
            return new InfoDayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(InfoDayViewHolder holder, int position) {
            position = position;
            //holder.view.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
            presenter.updateInfoDay(position, holder.view.getPresenter());
            holder.view.requestLayout();
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        class InfoDayViewHolder extends RecyclerView.ViewHolder {
            public InfoDayView view;
            public InfoDayViewHolder(View itemView) {
                super(itemView);
                this.view =  (InfoDayView) itemView;
            }
        }
    }
    */

    class InfoDayViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (presenter==null) return 0;
            return presenter.getItemCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InfoDayView view = (InfoDayView) convertView;
            if (view == null) {
                view = new InfoDayView(getActivity(),null);
                view.setPresenter(new InfoDayViewPresenterNoProviderLink(view, getActivity()));
                view.setVisibility(View.VISIBLE);
                view.setId(View.generateViewId());
                view.setExpandedLevel(InfoDayViewContract.View.ExpandedLevel.COLLAPSED);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                //view.setLayoutParams(layoutParams);
                //view.requestLayout();
            }
            presenter.updateInfoDay(position,view.getPresenter());
            return view;
        }



    }

}
