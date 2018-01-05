package org.jesteban.clockomatic.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Esta clase representa una entrada de tiempo:
 * - Se considera vacia si tiene 0 milisegundos
 */

public class Entry extends Object {
    private Calendar registerDate = null;


    // This is the working day, usually is same as registerDate by can be day before on a rotating shifts
    // is a string key formated as FORMAT_BELONGING_DAY
    private String belongingDay = null;
    public static final String EMPTY_STRING = "EMPTY";
    public static final String FORMAT_BELONGING_DAY = "yyyy/MM/dd";
    public static final String FORMAT_BELONGING_MONTH = "yyyy/MM/";

    public Entry() {
    }

    public Entry(Calendar date) {
        if (date != null) {
            registerDate = (Calendar) date.clone();
            registerDate.set(Calendar.MILLISECOND, 0);
            registerDate.set(Calendar.SECOND, 0);
            belongingDay = extractBeloningDay(registerDate);
        }
    }

    public Entry(Entry other) {
        init(other.getRegisterDate(), other.getBelongingDay());
    }

    public Entry(Calendar date, String pBelongingDay) {
        init(date, pBelongingDay);
    }

    public Entry(String date, String belongingDay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        init(cal,belongingDay);
    }

    private void init(Calendar date, String pBelongingDay) {
        if (date != null) registerDate = (Calendar) date.clone();
        this.belongingDay = pBelongingDay;
        if (pBelongingDay == null) this.belongingDay = extractBeloningDay(registerDate);
    }

    public String getBelongingDay() {
        return belongingDay;
    }

    public Calendar getRegisterDate() {
        return registerDate;
    }

    public boolean isEmpty() {
        return (registerDate == null);
    }


    public String toString() {
        if (isEmpty()) return EMPTY_STRING;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:s z");
        String registerDateText = sdf.format(this.registerDate.getTime());
        return belongingDay + " / " + registerDateText;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Entry entry = (Entry) obj;
        if (entry == this) return true;

        if ((entry.belongingDay != this.belongingDay)) {
            if ((entry.belongingDay == null) || (this.belongingDay == null)) return false;
            if (!entry.belongingDay.equals(this.belongingDay)) return false;
        }

        if (entry.registerDate == this.registerDate) return true;
        if (entry.registerDate == null || this.registerDate == null) return false;
        return entry.registerDate.equals(this.registerDate);
    }


    // https://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java
    // https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method
    @Override
    public int hashCode() {
        int hash = 0;
        if (registerDate != null) hash = registerDate.hashCode();
        if (belongingDay != null) hash = 37 * hash + belongingDay.hashCode();
        return hash;
    }

    public static String extractBeloningDay(Calendar d) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_BELONGING_DAY);
        return sdf.format(d.getTime());
    }

    static public class BelongingDay{
        String belongingDay;

        public String getBelongingDay() {
            return belongingDay;
        }

        public Calendar getBelongingDayDate() {
            return belongingDayDate;
        }

        Calendar belongingDayDate;

        public BelongingDay(String belongingDay) throws ParseException {
            this.belongingDay = belongingDay;
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_BELONGING_DAY);
            Date date = sdf.parse(belongingDay);
            belongingDayDate = Calendar.getInstance();
            belongingDayDate.setTime(date);
        }
    }
}
