package org.jesteban.clockomatic.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.jesteban.clockomatic.R;

import java.util.List;
import java.util.logging.Logger;

/**
 *  This class show a digested day info
 */

public class InfoDayView  extends TableRow implements  InfoDayViewContract.View{
    MyCalendarDayView calendarDayView = null;
    MyPairedEntriesGridView pairedEntriesGridView = null;
    MyCalendarDayView calendarDayInfoView = null;
    InfoDayViewContract.Presenter presenter = null;
    public InfoDayView(Context context) {
        this(context,null);
    }

    public InfoDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        populate();
    }

    private void populate(){
        inflate(getContext(), R.layout.view_info_day, this);
        calendarDayView = (MyCalendarDayView) this.findViewById(R.id.calendar_day_view);
        calendarDayView.setTextUpper("empty");
        pairedEntriesGridView = (MyPairedEntriesGridView) this.findViewById(R.id.paired_entries_grid_view);
        calendarDayInfoView = (MyCalendarDayView) this.findViewById(R.id.calendar_day_info_view);
    }



    @Override
    public void showData(InfoDayVisualData data) {
        if (data.dayData!=null) calendarDayView.showData(data.dayData);
        if (data.entriesData!=null) pairedEntriesGridView.showPairedEntries(data.entriesData);
        if (data.briefData!=null) calendarDayInfoView.showData(data.briefData);
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
