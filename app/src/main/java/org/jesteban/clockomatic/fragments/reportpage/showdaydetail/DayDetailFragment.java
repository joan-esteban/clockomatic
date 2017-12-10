package org.jesteban.clockomatic.fragments.reportpage.showdaydetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.reportpage.showlistdaysclocks.ShowListDaysClocksFragment;
import org.jesteban.clockomatic.helpers.DependencyInjector;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;



public class  DayDetailFragment extends Fragment implements Observer,DependencyInjector.Injectable<DayDetailFragment.DayDetailModel>{
    private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksFragment.class.getName());
    private DayDetailModel model = null;

    public static class DayDetailModel extends Observable{
        public EntrySet getEntries() {
            return entries;
        }

        public void setEntries(EntrySet entries) {
            this.entries = entries;
            setChanged();
            notifyObservers();
        }

        private EntrySet entries;

    }

    public DayDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_clocks_month, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (model != null) model.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (model != null) model.deleteObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (model != null) model.deleteObserver(this);
    }



    private void syncWithState(DayDetailModel model){

    }

    @Override // DependencyInjector.Injectable<StateController>
    public boolean setDependency(DayDetailModel dependency) {
        if (dependency==null) return false;
        model = dependency;
        model.addObserver(this);
        syncWithState(model);
        return true;
    }

    @Override
    public void update(Observable observable, Object arg) {
        LOGGER.log(Level.FINE, "update from a Observable");
        if (observable instanceof DayDetailModel) {
            DayDetailModel model = (DayDetailModel) observable;
            LOGGER.log(Level.FINE, "update from StateController");
            syncWithState(model);
        }
    }


}
