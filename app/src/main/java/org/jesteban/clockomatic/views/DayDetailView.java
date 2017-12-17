package org.jesteban.clockomatic.views;


import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jesteban.clockomatic.R;

public class DayDetailView extends LinearLayout {

    private final Paint mPaint = new Paint();

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




}
