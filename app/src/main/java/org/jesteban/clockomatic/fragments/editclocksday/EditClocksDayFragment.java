package org.jesteban.clockomatic.fragments.editclocksday;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.DynamicWidgets;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.logging.Logger;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class EditClocksDayFragment extends Fragment implements EditClockDayContract.View {
    private static final Logger LOGGER = Logger.getLogger(EditClocksDayFragment.class.getName());
    private EditClockDayContract.Presenter presenter = null;
    private RelativeLayout layout = null;
    private DynamicWidgets<Button> buttonWidgets = null;


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


    public EditClocksDayFragment() {
        // Required empty public constructor
        createButtonWidgets();
    }

    public static EditClocksDayFragment newInstance() {
        return new EditClocksDayFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_clocks_day, container, false);
        layout = (RelativeLayout) view.findViewById(R.id.framelayout_view_clocks_day);
        return view;
    }


    public Button getButton(){
        Button button = new Button(getContext());
        layout.addView(button);
        return button;
    }
    public void removeEntry(Entry entry){
        presenter.remove(entry);
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

    public void syncWithState(EntrySet entries){

        if (entries == null) return;

        int buttonIdx = 0;
        Entry2Html aux = new Entry2Html();
        buttonWidgets.setAllWidgetAsUnused();
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
    public void setPresenter(EditClockDayContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEntries(EntrySet entries) {
        syncWithState(entries);
    }
}
