package org.jesteban.clockomatic.views;


public class MyCalendarDayViewContract {
    public enum SizeStyle{
        SMALL,
        BIG
    }
    public enum ColorStyle{
        COLOR_MONDAY,
        COLOR_TUESDAY,
        COLOR_WEDNESDAY,
        COLOR_THURSDAY,
        COLOR_FRIDAY,
        COLOR_SATURDAY,
        COLOR_SUNDAY,
        GREY,
        RED,
        BLUE,
        CUSTOM
    }

    public static class CalendarDayViewVisualData {
        public String textUpper = "u";
        public String textMiddle = "m";
        public String textBottom = "b";
        public int colorCalendarBg = 0xffb0b0b0;
        public int colorCalendarText = 0xff304050;
        public SizeStyle sizeStyle = SizeStyle.SMALL;
        public ColorStyle colorStyle = ColorStyle.GREY;
    }

    public interface View  {
        public void showData(CalendarDayViewVisualData data);
    }
}
