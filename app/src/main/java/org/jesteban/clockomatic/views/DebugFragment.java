package org.jesteban.clockomatic.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.text.ParseException;

import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.SizeStyle.BIG;
import static org.jesteban.clockomatic.views.MyCalendarDayViewContract.SizeStyle.SMALL;


public class DebugFragment extends Fragment {



    View view = null;
    MyCalendarDayView dayView=null;
    MyPairedEntriesGridView myLinkEntriesGridView = null;
    InfoDayView infoDayView = null;
    InfoDayViewPresenter infoDayViewPresenter = null;
    public DebugFragment() {
        // Required empty public constructor
    }

    public EntrySet getDemoEntries(){
        EntrySet res = new EntrySet();
        /*
        try {

            res.addEntry(new Entry("1/1/2018 8:05:01", null));
            res.addEntry(new Entry("1/1/2018 13:01:01", null));
            res.addEntry(new Entry("1/1/2018 14:53:01", null));
            res.addEntry(new Entry("1/1/2018 18:13:01", null));

            //res.addEntry(new Entry("1/1/2018 18:14:01", null));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        return res;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_debug, container, false);
        TableLayout layout = (TableLayout)view.findViewById(R.id.debug_layout_table);
        dayView = new MyCalendarDayView(getContext());
        layout.addView(dayView);
        myLinkEntriesGridView = (MyPairedEntriesGridView) view.findViewById(R.id.debug_my_link_entries_grid_view);

        Button button = (Button) view.findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dayView.setWorkingDay(!dayView.isWorkingDay());
                myLinkEntriesGridView.add();
            }
        });

        Button button2 = (Button) view.findViewById(R.id.button_debug_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCalendarDayViewContract.SizeStyle currentStyle = dayView.getSizeStyle();
                if (currentStyle == BIG){
                    dayView.setSizeStyle(SMALL);
                } else{
                    dayView.setSizeStyle(BIG);
                }
            }
        });
        MyCalendarDayView dayView2;
        for (int i=18 ; i<23; i++) {
            TableRow tableRow = new TableRow(getContext());
            dayView2 = new MyCalendarDayView(getContext());
            dayView2.setTextMiddle(Integer.toString(i));
            dayView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyCalendarDayView dayView = (MyCalendarDayView) v;
                    MyCalendarDayViewContract.SizeStyle currentStyle = dayView.getSizeStyle();
                    if (currentStyle == BIG){
                        dayView.setSizeStyle(SMALL);
                    } else{
                        dayView.setSizeStyle(BIG);
                    }
                }
            });
            //dayView2.setSmall(false);
            tableRow.addView(dayView2);
            layout.addView(tableRow);
        }
        infoDayView = (InfoDayView) view.findViewById(R.id.debug_info_day_view);
        infoDayViewPresenter = new InfoDayViewPresenter(infoDayView, getContext());
        InfoDayEntry infoDayEntry = new InfoDayEntry(getDemoEntries(), "2018/1/1");
        infoDayViewPresenter.showInfoDay(infoDayEntry);
        return view;

    }

}
