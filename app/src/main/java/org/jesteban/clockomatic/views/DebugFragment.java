package org.jesteban.clockomatic.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import org.jesteban.clockomatic.R;

public class DebugFragment extends Fragment {
    View view = null;
    MyCalendarDayView dayView=null;

    public DebugFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_debug, container, false);
        //View kk = view.findViewById(R.id.debug_day_view2);
        //dayView = (DayDetailView) view.findViewById(R.id.debug_day_view2);
        TableRow layout = (TableRow)view.findViewById(R.id.debug_layout_row_2);
        dayView = new MyCalendarDayView(getContext());
        layout.addView(dayView);
        Button button = (Button) view.findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayView.setWorkingDay(!dayView.isWorkingDay());
            }
        });

        Button button2 = (Button) view.findViewById(R.id.button_debug_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayView.setSmall(!dayView.isSmall());
            }
        });
        MyCalendarDayView dayView2;
        for (int i=18 ; i<23; i++) {
            dayView2 = new MyCalendarDayView(getContext());
            dayView2.setTextMiddle(Integer.toString(i));
            dayView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyCalendarDayView clickedDayView = (MyCalendarDayView) v;
                    clickedDayView.setSmall(!clickedDayView.isSmall());
                }
            });
            layout.addView(dayView2);
        };

        return view;

    }

}
