package org.jesteban.clockomatic.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jesteban.clockomatic.R;

public class DayDetailView extends LinearLayout {



    public DayDetailView(Context context) {
        this(context, null,0);

    }

    public DayDetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DayDetailView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_detail, this, true);
    }

    public void setDayInfo(String dayName, String dayNumber){
        TextView tv = (TextView) findViewById(R.id.text_name_day);
        tv.setText(dayName);
        tv = (TextView) findViewById(R.id.text_num_day);
        tv.setText(dayNumber);
    }



}
