package org.jesteban.clockomatic.model;



public interface WorkingCalendarContract {
    public enum Kind{
        FREESTYLE,
        SPLIT,
        CONTINOUS,
        ROTATING_SHIFTS,
        FREELANCE
    }
    String getName();
}
