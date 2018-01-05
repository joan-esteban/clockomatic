package org.jesteban.clockomatic.views;


public class MyCalendarDayViewContract {
    public enum SizeStyle{
        SMALL,
        BIG
    }
    public enum ColorStyle{
        GREY,
        RED,
        BLUE
    }

    public static class CalendarDayViewVisualData {
        public String textUpper = "";
        public String textMiddle = "";
        public String textBottom = "";
        public int colorCalendarBg = 0xffb0b0b0;
        public int colorCalendarText = 0xff304050;
        public SizeStyle sizeStyle = SizeStyle.SMALL;
        public ColorStyle colorStyle = ColorStyle.GREY;
    }

    public interface View  {
        public void showData(CalendarDayViewVisualData data);
    }
}
