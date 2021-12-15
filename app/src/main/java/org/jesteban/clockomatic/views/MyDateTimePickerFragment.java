package org.jesteban.clockomatic.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import org.jesteban.clockomatic.R;

import java.util.Calendar;
import java.util.logging.Logger;


public class MyDateTimePickerFragment extends Fragment implements MyDatePickerFragment.OnFragmentInteractionListener,
        DatePickerDialog.OnDateSetListener{
    private static final Logger LOGGER = Logger.getLogger(MyDateTimePickerFragment.class.getName());
    private OnMyDateTimePickerFragmentListener mListener;
    private MyDatePickerFragment myDatePicker = null;
    private TimePicker timePicker = null;
    private ImageButton buttonExpand = null;

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


        buttonExpand = (ImageButton) view.findViewById(R.id.button_expand);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker2);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                LOGGER.info("timePicker listener: OnTimeChanged " + Integer.toString(hourOfDay));
                Calendar date = getComposedDate(null);
                mListener.selected(date);
            }
        });
        CardView cardEntryPicker = (CardView) view.findViewById(R.id.card_entry_picker);
        //cardEntryPicker.setLayoutAnimation();
        final ImageButton buttonExpand = (ImageButton) view.findViewById(R.id.button_expand);
        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpandState (timePicker.getVisibility()==View.GONE);

            }
        });
        setExpandState(false);
        return view;
    }
    public void setExpandState(boolean expanded){
        if (expanded){
            timePicker.setVisibility(View.VISIBLE);
            buttonExpand.setImageResource(R.drawable.ic_expand_less_black_24dp);

        } else {
            timePicker.setVisibility(View.GONE);
            buttonExpand.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }
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

    @Override
    public void onClickTextDay() {
        Calendar date = getComposedDate(null);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar date = getComposedDate(null);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setDate(date);
    }

    public interface OnMyDateTimePickerFragmentListener {
        void register(Calendar date);

        void selected(Calendar date);
    }
}
