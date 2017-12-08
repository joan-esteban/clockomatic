package org.jesteban.clockomatic.fragments.register_page;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.helpers.DependencyInjector;
import org.jesteban.clockomatic.helpers.DynamicWidgets;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.State;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class ViewClocksDayFragment extends Fragment implements Observer,DependencyInjector.Injectable<StateController> {
    private static final Logger LOGGER = Logger.getLogger(ViewClocksDayFragment.class.getName());
    private StateController stateController = null;



    private String showBelongingDayPrefix = "";
    private RelativeLayout layout = null;
    DynamicWidgets<Button> buttonWidgets = null;

    @Override //DependencyInjector.Injectable<StateController>
    public boolean setDependency(StateController dependency) {
        if (dependency==null) return false;
        stateController = dependency;
        stateController.addObserver(this);
        syncWithState(stateController.getState());
        return true;
    }

    private void createButtonWidgets(){
        buttonWidgets = new DynamicWidgets<>(new DynamicWidgets.OnMyActions<Button>() {
            @Override
            public Button createWidget(int idx) {
                Button previous = null;
                if (idx>0) previous = buttonWidgets.getWidget(idx-1);
                Button button = new Button(getContext());
                button.setId(View.generateViewId());

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if (previous != null) {
                    int itemsPerRow = 4;
                    if (getResources().getConfiguration().orientation!=ORIENTATION_PORTRAIT) itemsPerRow=3;
                    if (idx%itemsPerRow!=0) {
                        lp.addRule(RelativeLayout.RIGHT_OF, previous.getId());
                        if (idx>itemsPerRow){
                            previous = buttonWidgets.getWidget(idx-itemsPerRow);
                            lp.addRule(RelativeLayout.BELOW, previous.getId());
                        }
                    } else{ // Salto de linea
                        previous = buttonWidgets.getWidget(idx-itemsPerRow);
                        lp.addRule(RelativeLayout.BELOW, previous.getId());

                        lp.addRule(RelativeLayout.END_OF, previous.getId());
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, previous.getId());
                    }
                }
                layout.addView(button,lp);
                return button;
            }

            @Override
            public void removeWidgetFromView(Button widget) {
                if (widget==null) return;
                widget.setVisibility(View.GONE);
            }
        });
    }


    public ViewClocksDayFragment() {
        // Required empty public constructor
        createButtonWidgets();
    }

    public static ViewClocksDayFragment newInstance() {
        return new ViewClocksDayFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_clocks_day, container, false);
        layout = (RelativeLayout) view.findViewById(R.id.framelayout_view_clocks_day);

        if (stateController!= null) syncWithState(stateController.getState());

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (stateController!= null) stateController.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (stateController!= null) stateController.deleteObserver(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stateController!= null) stateController.deleteObserver(this);
    }

    public void setShowBelongingDayPrefix(String dayPrefix) {
        LOGGER.info("setShowBelongingDayPrefix dayPrefix = " + dayPrefix);
        this.showBelongingDayPrefix = dayPrefix;
        if (stateController!= null) syncWithState(stateController.getState());
    }

    public Button getButton(){
        Button button = new Button(getContext());
        layout.addView(button);
        return button;
    }
    public void removeEntry(Entry entry){
        stateController.remove(entry);
    }

    private void setButtonHourAsPortrait(Button button, int sizeEntries){

        int h =getContext().getResources().getDisplayMetrics().heightPixels;
        double ratio = ((float) (h))/300.0;
        int height = (int)(ratio*24);
        if (sizeEntries>8) {
            height = (int)(ratio*20);
        }
        if (sizeEntries>12) {
            height = (int)(ratio*18);
        }
        button.getLayoutParams().height = height;
    }

    public void syncWithState(State state){

        if (state == null) return;
        LOGGER.info("syncWithState " + state.getEntries());
        int buttonIdx = 0;
        Entry2Html aux = new Entry2Html();
        buttonWidgets.setAllWidgetAsUnused();
        EntrySet entries = state.getEntries().getEntriesBelongingDayStartWith(showBelongingDayPrefix);
        for (Entry entry : entries){


            Button button = buttonWidgets.getWidget(buttonIdx);
            button.setText(aux.getJustHoursPlain(entry));
            button.setTag(entry);
            button.setVisibility(View.VISIBLE);
            if (getResources().getConfiguration().orientation==ORIENTATION_PORTRAIT) {
                setButtonHourAsPortrait(button, entries.getEntries().size());
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    removeEntry((Entry)button.getTag());
                }
            });
            buttonIdx++;
        }
        buttonWidgets.removeWidgetUnused();
    }

    @Override
    public void update(Observable observable, Object arg) {
        LOGGER.info("update from a Observable");
        if (observable instanceof StateController) {
            StateController state = (StateController) observable;
            LOGGER.info("update from StateController");
            syncWithState(state.getState());
        }
    }
}
