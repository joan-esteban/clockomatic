package org.jesteban.clockomatic.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MyGanttEntriesView extends View {
    private Rect totalRect=new Rect();
    private final Paint mPaint = new Paint();
    private List<InfoDayEntry.EntryPairs>  entryPairs  = null;

    public MyGanttEntriesView(Context context) {
        this(context, null,0);
    }

    public MyGanttEntriesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyGanttEntriesView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        entryPairs = new ArrayList<>();
        InfoDayEntry.EntryPairs pair = new InfoDayEntry.EntryPairs();
        try {
            pair.starting = new Entry("1/1/2017 8:30:0","1/1/2017");
            pair.finish= new Entry("1/1/2017 13:05:0","1/1/2017");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        entryPairs.add (pair);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(0);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());
        int fitw = Math.min(w, getMeasuredWidth());
        int fith = Math.min(h, getMeasuredHeight());
        float ww = (float)fitw - xpad;
        float hh = (float)fith - ypad;
        totalRect= new Rect(0,0,(int)ww,(int)hh);
    }

    @Override
    public void onDraw(Canvas canvas) {

    }
}
