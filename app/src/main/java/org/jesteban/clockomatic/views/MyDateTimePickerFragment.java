package org.jesteban.clockomatic.views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import org.jesteban.clockomatic.R;

import java.util.Calendar;
import java.util.logging.Logger;


public class MyDateTimePickerFragment extends Fragment implements MyDatePickerFragment.OnFragmentInteractionListener {
    private static final Logger LOGGER = Logger.getLogger(MyDateTimePickerFragment.class.getName());
    private OnMyDateTimePickerFragmentListener mListener;
    private MyDatePickerFragment myDatePicker = null;
    private TimePicker timePicker = null;


    public MyDateTimePickerFragment() {
        // Empty constructor
    }

    public static MyDateTimePickerFragment newInstance() {
        return new MyDateTimePickerFragment();
    }


    public Calendar getComposedDate(Calendar day) {
        Calendar registerDate = day;
        if (day == null) {
            registerDate = (Calendar) myDatePicker.getDay().clone();
        }

        int hour = 0;
        int minute = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        registerDate.set(Calendar.MILLISECOND, 0);
        registerDate.set(Calendar.SECOND, 0);
        registerDate.set(Calendar.HOUR_OF_DAY, hour);
        registerDate.set(Calendar.MINUTE, minute);
        LOGGER.info("getComposedDate selected day " + registerDate.getTime());
        return registerDate;
    }

    private void pressRegisterDate() {
        Calendar registerDate = getComposedDate(null);
        mListener.register(registerDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LOGGER.info("onCreateView");
        View view = inflater.inflate(R.layout.fragment_my_date_time_picker, container, false);


        // getFragmentManager() can't find child fragment, it only contains fragment_my_date_time_picker (father)
        myDatePicker = (MyDatePickerFragment) getChildFragmentManager().findFragmentById(R.id.fragment_date_picker);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.register);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressRegisterDate();
            }
        });

        timePicker = (TimePicker) view.findViewById(R.id.timePicker2);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                LOGGER.info("timePicker listener: OnTimeChanged " + Integer.toString(hourOfDay));
                Calendar date = getComposedDate(null);
                mListener.selected(date);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (OnMyDateTimePickerFragmentListener) parent;
        } else {
            mListener = (OnMyDateTimePickerFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onChangedSelectedDay(Calendar day) {
        LOGGER.info("onChangedSelectedDay " + day);
        Calendar date = getComposedDate(day);
        mListener.selected(date);
    }

    public void setDate(Calendar date) {
        LOGGER.info("MyDateTimePickerFragment::setDate setDate to " + date.getTime());
        LOGGER.info("setDate HOUR=" +date.get(Calendar.HOUR_OF_DAY));
        LOGGER.info("setDate MIN=" +date.get(Calendar.MINUTE));
        timePicker.setCurrentHour(1);
        timePicker.setCurrentHour(date.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(date.get(Calendar.MINUTE));
        myDatePicker.setDay(date);
    }

    public interface OnMyDateTimePickerFragmentListener {
        void register(Calendar date);

        void selected(Calendar date);
    }
}
