package org.jesteban.clockomatic.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.jesteban.clockomatic.R;

import java.util.logging.Logger;


//https://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android

//TODO: Take care density!!!


public class MyCalendarDayView extends View implements MyCalendarDayViewContract.View{
    private static final Logger LOGGER = Logger.getLogger(MyCalendarDayView.class.getName());
    private  int colorCalendarBg = 0;
    private  int colorCalendarText = 0;
    private MyCalendarDayViewContract.CalendarDayViewVisualData data = new MyCalendarDayViewContract.CalendarDayViewVisualData();
    DrawCalendarIcon drawCalendarIcon = new DrawCalendarIcon();

    private Rect calendarRect=new Rect(0,0,70,70);

    public MyCalendarDayView(Context context) {
        this(context, null,0);
    }

    public MyCalendarDayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCalendarDayView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCalendarDayView,
                0, 0);
        try{
            data.textUpper = a.getString(R.styleable.MyCalendarDayView_textUpper);
            data.textMiddle = a.getString(R.styleable.MyCalendarDayView_textMiddle);
            data.textBottom = a.getString(R.styleable.MyCalendarDayView_textBottom);
            int style = a.getInteger(R.styleable.MyCalendarDayView_sizeStyle, MyCalendarDayViewContract.SizeStyle.SMALL.ordinal() );
            data.sizeStyle = data.sizeStyle.values()[style];
        } finally{

        }
        if (data.textUpper==null) data.textUpper="";
        if (data.textMiddle==null) data.textMiddle="";
        if (data.textBottom==null) data.textBottom="";

        setColorsGrey();
    }

    public String getTextUpper() {
        return data.textUpper;
    }

    public void setTextUpper(String textUpper) {
        data.textUpper = textUpper;
    }

    public String getTextMiddle() {
        return data.textMiddle;
    }

    public void setTextMiddle(String textMiddle) {
        data.textMiddle = textMiddle;
    }

    public String getTextBottom() {
        return data.textBottom;
    }

    public void setTextBottom(String textBottom) {
        data.textBottom = textBottom;
    }

    public void setColorsGrey(){
        setColor(MyCalendarDayViewContract.ColorStyle.GREY);
    }

    public void setColorRed(){
        setColor(MyCalendarDayViewContract.ColorStyle.RED);
    }
    public void setColorBlue(){
        setColor(MyCalendarDayViewContract.ColorStyle.BLUE);
    }

    public void setColor(MyCalendarDayViewContract.ColorStyle color){
        switch (color){
            case GREY:
                colorCalendarBg = 0xffb0b0b0;
                colorCalendarText = 0xff304050;
                break;
            case RED:
                colorCalendarBg = 0xFFd09090;
                colorCalendarText = 0xff603030;
                break;
            case BLUE:
                colorCalendarBg = 0xff9090d0;
                colorCalendarText = 0xff303060;
                break;
        }
        this.drawCalendarIcon.init(colorCalendarBg, colorCalendarText,data.sizeStyle);
        invalidate();
    }



    public void setSizeStyle(MyCalendarDayViewContract.SizeStyle v){
        data.sizeStyle=v;
        this.drawCalendarIcon.init(colorCalendarBg, colorCalendarText,data.sizeStyle);
        invalidate();
        // This force to recalculate size
        this.setMinimumHeight(0);
    }
    public MyCalendarDayViewContract.SizeStyle getSizeStyle(){
        return data.sizeStyle;
    }
    public boolean isSmall(){
        return data.sizeStyle == MyCalendarDayViewContract.SizeStyle.SMALL;
    }




    @Override
    public void showData(MyCalendarDayViewContract.CalendarDayViewVisualData data) {
        setTextUpper(data.textUpper);
        setTextMiddle(data.textMiddle);
        setTextBottom(data.textBottom);
        setSizeStyle(data.sizeStyle);
        setColor(data.colorStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());
        int fitw = Math.min(w, getSuggestedMinimumWidth());
        int fith = Math.max(h, getSuggestedMinimumHeight());
        float ww = (float)fitw - xpad;
        float hh = (float)fith - ypad;
        //LOGGER.info("onSizeChanged  h=" + Integer.toString(h) +
        //        "final h = " + Float.toString(hh) + "padding_left= " + Integer.toString(getPaddingLeft()));
        calendarRect= new Rect(getPaddingLeft(),getPaddingTop(),(int)ww + getPaddingLeft(),(int)hh +getPaddingTop());
    }


    @Override
    public void onDraw(Canvas canvas) {
        drawCalendarIcon.drawCalendarIcon(canvas,calendarRect, data.textUpper, data.textMiddle,data.textBottom);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        if (isSmall()) return 70;
        return 125;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        if (isSmall()) return 70;
        return 110;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = minw;

        int minh =getSuggestedMinimumHeight() + getPaddingBottom() + getPaddingTop();
        int h = minh;
        //LOGGER.info("onMeasure  heightMeasureSpec=" + Integer.toString(widthMeasureSpec) +
        //                    "h = " + Integer.toString(h) + " pading_left=" + Integer.toString(getPaddingLeft()));
        setMeasuredDimension(w, h);
    }



    class DrawCalendarIcon{
        private final Paint paintBgRect = new Paint();
        private final Paint paintBgLine = new Paint();
        private final Paint paintUpperText = new Paint();
        private final Paint paintMiddleText = new Paint();
        private final Paint paintBottomText = new Paint();
        private int factorTextUpper=11;
        private int factorMiddleUpper=28;
        private int factorBottonUpper=9;
        private int radiusBg= 20;

        void init(int colorBg, int colorText, MyCalendarDayViewContract.SizeStyle sizeStyle){
            if (sizeStyle== MyCalendarDayViewContract.SizeStyle.SMALL){
                factorTextUpper=7;
                factorMiddleUpper=28 /2;
                factorBottonUpper=9 /2;
                radiusBg= 20 /2;
            } else{
                factorTextUpper=11;
                factorMiddleUpper=28;
                factorBottonUpper=9;
                radiusBg= 20;
            }
            paintBgRect.setStyle(Paint.Style.FILL);
            paintBgRect.setStrokeWidth(1);
            paintBgRect.setColor(colorBg);

            paintBgLine.setStyle(Paint.Style.FILL);
            paintBgLine.setStrokeWidth(2);
            paintBgLine.setColor(Color.WHITE);

            paintUpperText.setAntiAlias(true);
            paintUpperText.setTextSize(factorTextUpper * getResources().getDisplayMetrics().density);
            paintUpperText.setStrokeWidth(2);
            paintUpperText.setColor(colorText);
            paintUpperText.setTextAlign(Paint.Align.CENTER);
            paintUpperText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

            paintMiddleText.setAntiAlias(true);
            paintMiddleText.setColor(colorText);
            paintMiddleText.setTextAlign(Paint.Align.CENTER);
            paintMiddleText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintMiddleText.setTextSize(factorMiddleUpper * getResources().getDisplayMetrics().density);

            paintBottomText.setAntiAlias(true);
            paintBottomText.setTextAlign(Paint.Align.CENTER);
            paintBottomText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            paintBottomText.setTextSize(factorBottonUpper * getResources().getDisplayMetrics().density);
            paintBottomText.setColor(colorText);
        }


        void drawCalendarIcon(Canvas canvas, Rect where,
                              String upper, String middle, String bottom)
        {
            drawBg(canvas,where);
            canvas.drawText(upper, where.centerX(), where.top + ((where.bottom-where.top)/4), this.paintUpperText);
            if (data.sizeStyle == MyCalendarDayViewContract.SizeStyle.SMALL){
                canvas.drawText(middle, where.centerX(), (int)(where.bottom*0.77), this.paintMiddleText);
            } else canvas.drawText(middle, where.centerX(), (int)(where.bottom*0.80), this.paintMiddleText);
            if (data.sizeStyle == MyCalendarDayViewContract.SizeStyle.BIG) canvas.drawText(bottom, where.centerX() , (int)(where.bottom*0.95), this.paintBottomText);
        }

        void drawBg(Canvas canvas, Rect where){
            int padding = 1;
            RectF rectF=new RectF(where.left + padding,where.top +padding, where.right-padding, where.bottom-padding);

            canvas.drawRoundRect(rectF,radiusBg,radiusBg,paintBgRect);
            canvas.drawLine(rectF.left,rectF.bottom*0.35f, rectF.right, rectF.bottom*0.35f,paintBgLine);
        }
    }
}
