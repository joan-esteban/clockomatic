package org.jesteban.clockomatic.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Esta clase representa una entrada de tiempo:
 * - Se considera vacia si tiene 0 milisegundos
 */

public class Entry extends Object  implements Serializable {

    public enum Kind{
        WORKING,
        OTHER
    }

    private static final Logger LOGGER = Logger.getLogger(Entry.class.getName());
    public static final String EMPTY_STRING = "EMPTY";
    public static final String FORMAT_BELONGING_DAY = "yyyy/MM/dd";
    public static final String FORMAT_BELONGING_MONTH = "yyyy/MM/";
    public static final String FORMAT_CREATE_ENTRY = "dd/MM/yyyy HH:mm:ss";

    public static final SimpleDateFormat simpleDateFormatCreateEntry = new SimpleDateFormat(FORMAT_CREATE_ENTRY);

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
        init(other.getRegisterDate(), other.getBelongingDay(),other.getKind(), other.getDescription());
    }

    public Entry(Calendar date, String pBelongingDay) {
        init(date, pBelongingDay,Kind.WORKING, "");
    }

    public Entry(String date, String belongingDay) throws ParseException {
        SimpleDateFormat sdf = simpleDateFormatCreateEntry;
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        init(cal,belongingDay,Kind.WORKING, "");
    }

    public Entry(Calendar date, String belongingDay,Kind kind, String description)  {
        init(date,belongingDay,kind, description);
    }

    private void init(Calendar date, String pBelongingDay, Kind kind, String description) {
        BelongingDay belongingDay = null;
        try {
            if (pBelongingDay != null) belongingDay = new BelongingDay(pBelongingDay);
        } catch (ParseException e){
            LOGGER.warning("Error getting belongingDay from string [" + pBelongingDay + "] using date");
            pBelongingDay = null;

        }
        if (pBelongingDay == null) belongingDay = extractBeloningDay(registerDate);

        init(date,belongingDay,kind,description);
    }

    private void init(Calendar date, BelongingDay pBelongingDay, Kind kind, String description) {
        if (date != null) registerDate = (Calendar) date.clone();
        this.belongingDay = new BelongingDay(pBelongingDay);
        setKind(kind);
        setDescription(description);
    }
    public BelongingDay getBelongingDayObject(){
        return belongingDay;
    }
    public String getBelongingDay() {
        return belongingDay.getDay();
    }

    public Calendar getRegisterDate() {
        return registerDate;
    }
    public String getRegisterDateAsString(){
        return simpleDateFormatCreateEntry.format(getRegisterDate().getTime());
    }

    public void setRegisterDate(Calendar date){
        registerDate = date;
    }

    public Kind getKind(){return kind;}
    public void setKind(Kind kind){this.kind = kind;}


    public String getDescription(){return description;}
    public void setDescription(String desc){this.description = desc;}

    public boolean isEmpty() {
        return (registerDate == null);
    }

    protected static Calendar clearDataPart(Calendar original){
        Calendar cal = (Calendar)original.clone();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal;
    }

    public long getMinutesOfDay(){
        Calendar register = getRegisterDate();
        int hours = register.get(Calendar.HOUR_OF_DAY);
        int minutes = register.get(Calendar.MINUTE);
        return (TimeUnit.HOURS.toMinutes(hours) + minutes );
    }
    public long getMinutesOfDayFromBelongingDay(){
        BelongingDay belongingDayObject = belongingDay;
        Calendar belongs = belongingDayObject.getBelongingDayDate();
        Calendar register = getRegisterDate();
        long diffInMs = register.getTime().getTime() - belongs.getTime().getTime();
        return (TimeUnit.MILLISECONDS.toMinutes(diffInMs) );
    }

    public void setMinutesOfDay(int minutes){
        Calendar register = getRegisterDate();
        register.set(Calendar.HOUR_OF_DAY,minutes / 60);
        register.set(Calendar.MINUTE,minutes % 60);
        setRegisterDate(register);
    }

    // This function doesnt work for great distances!
    public long getDayOffsetBetweenBelongingDayAndRegister() throws ParseException {
        BelongingDay belongingDayObject = new BelongingDay(getBelongingDay());
        Calendar register = getRegisterDate();
        register = clearDataPart(register);

        Calendar belongs = belongingDayObject.getBelongingDayDate();
        belongs = clearDataPart(belongs);
        LOGGER.info(String.format("register=%s belongs=%s", register, belongs));
        long diffInMs = register.getTime().getTime() - belongs.getTime().getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMs);
        LOGGER.info(String.format("diffInMs=%d diffInDays=%d", diffInMs, diffInDays));
        return diffInDays;

    }

    public String getDayOffsetBetweenBelongingDayAndRegisterAsString() throws ParseException {
        long distance = getDayOffsetBetweenBelongingDayAndRegister();

        if (distance<0) return  Long.toString(distance);
        if (distance>0) return "+" + Long.toString(distance);
        return "";
    }
    public String toString() {
        if (isEmpty()) return EMPTY_STRING;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:s z");
        String registerDateText = sdf.format(this.getRegisterDate().getTime());
        return getKind().name() + " " + getBelongingDay()+ " / " + registerDateText + " desc:[" + getDescription();
    }

    public String toShortString(){
        if (isEmpty()) return EMPTY_STRING;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return  sdf.format(this.registerDate.getTime());
    }
    private boolean equalsStrings(String str1, String str2){
        if (str1 != str2){
            if (str1 == null || str2 ==null) return false;
            if (!str1.equals(str2)) return false;
        }
        return true;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Entry entry = (Entry) obj;
        if (entry == this) return true;
        if (entry.getKind() != getKind()) return false;
        if (!equalsStrings(entry.getDescription(),this.getDescription())) return false;
        if (!entry.getBelongingDay().equals(this.getBelongingDay())) return false;

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
        if (belongingDay != null) hash = 37 * hash + belongingDay.getDay().hashCode();
        if (description != null) hash = 37 * hash + description.hashCode();
        hash = 37 * hash + kind.hashCode();
        return hash;
    }

    public static BelongingDay extractBeloningDay(Calendar d) {
        return new BelongingDay(d);
    }

    public static  class BelongingDay implements Serializable {
        String day;
        Calendar belongingDayDate;

        public String getDay() {
            return day;
        }

        public Calendar getBelongingDayDate() {
            return belongingDayDate;
        }



        public BelongingDay(BelongingDay p){
            this.day = p.getDay();
            this.belongingDayDate = p.getBelongingDayDate();
        }

        public BelongingDay(String belongingDay) throws ParseException {
            this.day = belongingDay;
            belongingDayDate = getDateFromString(belongingDay);
        }

        public BelongingDay(Calendar cal)  {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_BELONGING_DAY);
            this.day = sdf.format(cal.getTime());
            belongingDayDate = clearDataPart(cal);


        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            BelongingDay other = (BelongingDay) obj;
            return (this.getDay().equals(other.getDay()));
        }

        Calendar getDateFromString(String txt) throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_BELONGING_DAY);
            Date date = sdf.parse(txt);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }


        
    }

    private Calendar registerDate = null;


    // This is the working day, usually is same as registerDate by can be day before on a rotating shifts
    // is a string key formated as FORMAT_BELONGING_DAY
    private BelongingDay belongingDay = null;
    private Kind kind = Kind.WORKING;
    private String description = "";
}
