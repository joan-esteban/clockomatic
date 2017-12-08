package org.jesteban.clockomatic.helpers;

import org.jesteban.clockomatic.model.Entry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Give a representation of a Entry in Html
 */

public class Entry2Html {
    private SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");

    public String getJustHours(Entry entry) {
        if (entry == null) return "";
        Calendar cal = entry.getRegisterDate();
        if (cal == null) return "";
        String hour = sdfHour.format(cal.getTime());
        String minute = sdfMinute.format(cal.getTime());
        return "<font face='console'><b>" + hour + "</b>:<b>" + minute + "</b></font>";
    }

    public String getJustHoursPlain(Entry entry) {
        if (entry == null) return "";
        Calendar cal = entry.getRegisterDate();
        if (cal == null) return "";
        String hour = sdfHour.format(cal.getTime());
        String minute = sdfMinute.format(cal.getTime());
        return hour + ":" + minute + "";
    }
}
