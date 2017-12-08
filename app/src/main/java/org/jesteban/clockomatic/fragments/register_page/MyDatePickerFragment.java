package org.jesteban.clockomatic.fragments.register_page;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jesteban.clockomatic.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;


public class MyDatePickerFragment extends Fragment {
    private static final Logger LOGGER = Logger.getLogger(MyDatePickerFragment.class.getName());

    private OnFragmentInteractionListener mListener;
    private Calendar currentDate = Calendar.getInstance();
    private TextView currentDayTextView = null;
    private int whatCalendarChange = Calendar.DATE;
    private String patternToShow = "yyyy/MM/dd";

    public MyDatePickerFragment() {
        // Required empty public constructor
    }

    public static MyDatePickerFragment newInstance() {
        return new MyDatePickerFragment();
    }

    public void setMonthMode() {
        whatCalendarChange = Calendar.MONTH;
        patternToShow = "yyyy/MM";
        refresh();
    }

    public void setDay(Calendar date) {
        if (currentDate.equals(date)) return;
        currentDate = (Calendar) date.clone();
        refresh();
        LOGGER.info("setting new Date " + date);
        mListener.onChangedSelectedDay(date);
    }

    public Calendar getDay() {
        return currentDate;
    }

    public void setPreviousDay() {
        Calendar newDate = (Calendar) currentDate.clone();
        newDate.add(whatCalendarChange, -1);
        setDay(newDate);
    }

    public void setNextDay() {
        Calendar newDate = (Calendar) currentDate.clone();
        newDate.add(whatCalendarChange, +1);
        setDay(newDate);
    }

    private void refresh() {
        SimpleDateFormat sdf = new SimpleDateFormat(patternToShow);
        String dateText = sdf.format(this.currentDate.getTime());
        if (currentDayTextView != null) currentDayTextView.setText(dateText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGGER.info("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_date_picker, container, false);
        currentDayTextView = (TextView) view.findViewById(R.id.current_day);
        ImageButton previousDayButton = (ImageButton) view.findViewById(R.id.button_previous_day);
        previousDayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPreviousDay();
            }
        });

        ImageButton nextDayButton = (ImageButton) view.findViewById(R.id.button_next_day);
        nextDayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNextDay();
            }
        });

        refresh();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (OnFragmentInteractionListener) parent;
        } else {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onChangedSelectedDay(Calendar day);
    }
}
