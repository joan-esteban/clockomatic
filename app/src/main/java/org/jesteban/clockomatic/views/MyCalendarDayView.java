package org.jesteban.clockomatic.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


//https://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android

//TODO: Take care density!!!


public class MyCalendarDayView extends View {
    private  int colorCalendarBg = 0;
    private  int colorCalendarText = 0;
    private boolean small=true;
    private String textUpper = "Divendres";
    private String textMiddle = "21";
    private String textBottom = "Juliol 07";
    DrawCalendarIcon drawCalendarIcon = new DrawCalendarIcon();

    private Rect calendarRect=new Rect();

    public MyCalendarDayView(Context context) {
        this(context, null,0);
    }

    public MyCalendarDayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCalendarDayView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setColorsGrey();
    }

    public String getTextUpper() {
        return textUpper;
    }

    public void setTextUpper(String textUpper) {
        this.textUpper = textUpper;
    }

    public String getTextMiddle() {
        return textMiddle;
    }

    public void setTextMiddle(String textMiddle) {
        this.textMiddle = textMiddle;
    }

    public String getTextBottom() {
        return textBottom;
    }

    public void setTextBottom(String textBottom) {
        this.textBottom = textBottom;
    }

    public void setColorsGrey(){
        colorCalendarBg = 0xff909090;
        colorCalendarText = 0xff304050;
        this.drawCalendarIcon.init(colorCalendarBg, colorCalendarText,small);
        invalidate();
    }
    public void setColorRed(){
        colorCalendarBg = 0xFFd09090;
        colorCalendarText = 0xff603030;
        this.drawCalendarIcon.init(colorCalendarBg, colorCalendarText,small);
        invalidate();
    }
    public void setColorBlue(){
        colorCalendarBg = 0xff9090d0;
        colorCalendarText = 0xff303060;
        this.drawCalendarIcon.init(colorCalendarBg, colorCalendarText,small);
        invalidate();
    }
    public void setWorkingDay(boolean workingDay) {
        isWorkingDay = workingDay;
        if (isWorkingDay()){
            setColorBlue();
        } else {
            setColorRed();
        }
    }

    public void setSmall(boolean v){
        this.small=v;
        this.drawCalendarIcon.init(colorCalendarBg, colorCalendarText,small);
        invalidate();
        // This force to recalculate size
        this.setMinimumHeight(0);
    }
    public boolean isSmall(){
        return this.small;
    }

    private  boolean isWorkingDay = false;
    public boolean isWorkingDay() {
        return isWorkingDay;
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());
        int fitw = Math.min(w, getSuggestedMinimumWidth());
        int fith = Math.min(h, getSuggestedMinimumHeight());
        float ww = (float)fitw - xpad;
        float hh = (float)fith - ypad;
        calendarRect= new Rect(0,0,(int)ww,(int)hh);
    }


    @Override
    public void onDraw(Canvas canvas) {
        drawCalendarIcon.drawCalendarIcon(canvas,calendarRect, textUpper, textMiddle,textBottom);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (getSuggestedMinimumHeight());
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

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        int minh =getSuggestedMinimumHeight() + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

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

        void init(int colorBg, int colorText, boolean small){
            if (small){
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
            canvas.drawText(upper, where.centerX(), where.bottom/4, this.paintUpperText);
            if (small){
                canvas.drawText(middle, where.centerX(), (int)(where.bottom*0.77), this.paintMiddleText);
            } else canvas.drawText(middle, where.centerX(), (int)(where.bottom*0.80), this.paintMiddleText);
            if (!small) canvas.drawText(bottom, where.centerX() , (int)(where.bottom*0.95), this.paintBottomText);
        }

        void drawBg(Canvas canvas, Rect where){
            int padding = 1;
            RectF rectF=new RectF(where.left + padding,where.top +padding, where.right-padding, where.bottom-padding);

            canvas.drawRoundRect(rectF,radiusBg,radiusBg,paintBgRect);
            canvas.drawLine(rectF.left,rectF.bottom*0.35f, rectF.right, rectF.bottom*0.35f,paintBgLine);
        }
    }
}
