package org.jesteban.clockomatic.fragments.infodayvieew;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;


import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.views.MyCalendarDayView;
import org.jesteban.clockomatic.views.MyPairedEntriesGridView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract.View.ExpandedLevel.COLLAPSED;
import static org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract.View.ExpandedLevel.FULL;
import static org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract.View.ExpandedLevel.MEDIUM;


/**
 *  This class show a digested day info
 */

public class InfoDayView  extends LinearLayout implements  InfoDayViewContract.View{
    MyCalendarDayView calendarDayView = null;
    MyPairedEntriesGridView pairedEntriesGridView = null;
    //MyCalendarDayView calendarDayInfoView = null;
    TextView [] calendarDayInfoTitle = new TextView[2];
    TextView [] calendarDayInfoView =new TextView[2];
    TextView workingScheduleName = null;
    TextView textExtraLine = null;
    LinearLayout layoutExtended= null;
    ImageButton buttonExpand = null;

    CardView card = null;
    InfoDayViewContract.Presenter presenter = null;
    InfoDayVisualData data = null;
    ExpandedLevel expandedLevel = ExpandedLevel.FULL;

    public InfoDayView(Context context) {
        this(context,null);
    }

    public InfoDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        populate(this);
    }

    public InfoDayView(Context context, AttributeSet attrs, ViewGroup viewGroup){
        super(context, attrs);
        populate(viewGroup);
    }

    private void populate(ViewGroup viewGroup){
        inflate(getContext(), R.layout.view_info_day, viewGroup);
        //View view = inflater.inflate(R.layout.fragment_edit_clocks_day, container, false);
        calendarDayView = (MyCalendarDayView) this.findViewById(R.id.calendar_day_view);
        calendarDayView.setTextUpper("empty");
        calendarDayView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickOnCalendar();
            }
        });
        pairedEntriesGridView = (MyPairedEntriesGridView) this.findViewById(R.id.paired_entries_grid_view);
        calendarDayInfoView[0] = (TextView) this.findViewById(R.id.calendar_day_info_view);
        calendarDayInfoView[1]= (TextView) this.findViewById(R.id.calendar_day_info_view_2);
        calendarDayInfoTitle[0] = (TextView) this.findViewById(R.id.calendar_info_title);
        calendarDayInfoTitle[1] = (TextView) this.findViewById(R.id.calendar_info_title_2);
        workingScheduleName = (TextView) this.findViewById(R.id.working_schedule_name);
        textExtraLine= (TextView) this.findViewById(R.id.text_extra_line);
        card = (CardView) this.findViewById(R.id.view_info_day_card);
        card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickOnCard();
            }
        });
        pairedEntriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.clickOnEntryItem(position);
            }
        });
        layoutExtended = (LinearLayout) findViewById(R.id.layout_extended_contents);

        buttonExpand = (ImageButton) findViewById(R.id.button_expand);
        if (buttonExpand!=null){
            buttonExpand.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNextExpandedLevel();
                }
            });
        }
    }

    public void setNextExpandedLevel(){
        switch (expandedLevel) {
            case FULL:
                setExpandedLevel(COLLAPSED);
                break;
            case MEDIUM:
                setExpandedLevel(FULL);
                break;
            case COLLAPSED:
                setExpandedLevel(MEDIUM);
                break;
        }
    }
    @Override
    public void setExpandedLevel(ExpandedLevel level){
        expandedLevel = level;
        switch (level){
            case FULL:
                layoutExtended.setVisibility(VISIBLE);
                pairedEntriesGridView.getLayoutParams().height = WRAP_CONTENT;
                pairedEntriesGridView.requestLayout();
                buttonExpand.setImageResource(R.drawable.ic_expand_less_black_24dp);
                break;
            case MEDIUM:
                layoutExtended.setVisibility(GONE);
                pairedEntriesGridView.getLayoutParams().height = WRAP_CONTENT;
                pairedEntriesGridView.requestLayout();
                buttonExpand.setImageResource(R.drawable.ic_expand_more_black_24dp);
                break;
            case COLLAPSED:
                layoutExtended.setVisibility(GONE);
                pairedEntriesGridView.getLayoutParams().height = 132;
                pairedEntriesGridView.requestLayout();
                buttonExpand.setImageResource(R.drawable.ic_expand_more_black_24dp);
                break;
        }
    }

    @Override
    public void showMessage(String msg){
        Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
                .show();
    }


    @Override
    public void showData(InfoDayVisualData data) {
        if (data.dayData!=null) calendarDayView.showData(data.dayData);
        if (data.entriesData!=null) pairedEntriesGridView.showPairedEntries(data.entriesData);
        if (data.briefData!=null){
            for (int i=0;i<calendarDayInfoTitle.length;i++) {
                if (data.briefData.title.length>i) {
                    calendarDayInfoTitle[i].setText(data.briefData.title[i]);
                    calendarDayInfoTitle[i].setVisibility(VISIBLE);

                } else calendarDayInfoTitle[i].setVisibility(GONE);
            }
            for (int i=0;i<calendarDayInfoView.length;i++) {
                if (data.briefData.text.length>i) {
                    calendarDayInfoView[i].setText(data.briefData.text[i]);
                    calendarDayInfoView[i].setVisibility(VISIBLE);
                    calendarDayInfoView[i].setTextColor(data.briefData.color[i]);
                } else calendarDayInfoView[i].setVisibility(GONE);
            }
        }
        if (data.workingScheduleName!=null){
            workingScheduleName.setText(data.workingScheduleName);
        } else workingScheduleName.setText("");
        if (data.textExtraLine != null){
            textExtraLine.setVisibility(VISIBLE);
            textExtraLine.setText(data.textExtraLine);
        } else{
            textExtraLine.setVisibility(VISIBLE);
            textExtraLine.setText("....");
        }
        this.data = data;
    }

    @Override
    public InfoDayVisualData getData() {
        return this.data;
    }

    @Override
    public void setPresenter(InfoDayViewContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public InfoDayViewContract.Presenter getPresenter() {
        return this.presenter;
    }
}
