package org.jesteban.clockomatic.model;

import org.jesteban.clockomatic.helpers.CalendarHelper;

import java.util.Calendar;

public class TimeFrame {
    Calendar start = null;
    Calendar finish = null;
    public TimeFrame(){
        start = null; finish=null;
    }
    public TimeFrame(Calendar start, Calendar finish){
        this.start = start;
        this.finish = finish;
    }

    public Calendar getStartTime(){ return start;}
    public Calendar getFinishTime() {return finish;}

    public Boolean isEmpty(){ return ( start==null && finish==null);}

    long getSizeInMinutes(){
        return (start.getTimeInMillis() - finish.getTimeInMillis())/ (1000*60);
    }

}
