package org.jesteban.clockomatic.helpers;

import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.TimeFrame;
import org.jesteban.clockomatic.model.WorkScheduleContract;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It get information of a set of entries belonging to same day
 */

public class InfoDayEntry {
    private static final Logger LOGGER = Logger.getLogger(InfoDayEntry.class.getName());

    //private EntrySet entries = null;
    private List<PairedEntry> entryPairs=null;
    private WorkScheduleContract workSchedule = null;
    private Entry bestLeavingWork = null;
    private Entry.BelongingDay belongingDay;


    public InfoDayEntry(){
        this.belongingDay = null;
        //entries = new EntrySet();
        entryPairs = new ArrayList<>();
    }
    public InfoDayEntry(InfoDayEntry v){
        this.belongingDay = new Entry.BelongingDay(v.getBelongingDay());
        ArrayList<PairedEntry> originalPaired = (ArrayList) v.getPairsInfo();
        entryPairs = (List<PairedEntry>) originalPaired.clone();
        //entries = null;
    }
    public InfoDayEntry(List<PairedEntry> entryPairs, Entry.BelongingDay belongingDay){
        this.entryPairs = entryPairs;
        this.belongingDay = belongingDay;
    }

    public InfoDayEntry(Entry.BelongingDay belongingDay){
        this.belongingDay = new Entry.BelongingDay(belongingDay);
        //entries = new EntrySet();
        entryPairs = new ArrayList<>();
    }
    public InfoDayEntry(EntrySet entries){
        init(entries, null);
    }
    public InfoDayEntry(EntrySet entries, String belongingDay) throws ParseException {
        if (belongingDay==null){
            init(entries, null);
        } else init(entries,new Entry.BelongingDay(belongingDay));

    }
    public InfoDayEntry(EntrySet entries, Entry.BelongingDay belongingDay){
        init(entries, belongingDay);
    }

    private void init(EntrySet entries, Entry.BelongingDay belongingDay){
        if (belongingDay==null) {
            if ((entries != null) && (!entries.getEntries().isEmpty())) {
                belongingDay = entries.getEntries().get(0).getBelongingDayObject();
            } else {
                this.belongingDay = null;
                //this.entries = null;
                this.entryPairs = null;
                return;
            }
        }
        this.belongingDay = new Entry.BelongingDay(belongingDay);
        if (!checkSanity(entries,belongingDay.getDay())){
            LOGGER.log(Level.SEVERE, "Inconsistent data on InfoDayEntry");
            throw new IllegalArgumentException(String.format("InfoDayEntry entries Inconsistent data!, any entry doesnt belong to day [%s]  or are null", belongingDay.toString()));
        } else {
            //this.entries = cleanDuplicatedEntries(entries);
            entries = cleanDuplicatedEntries(entries);
            entryPairs = createPairsInfo(entries);
        }
    }

    public void setGeneratedWorkSchedule(WorkScheduleContract workSchedule){this.workSchedule=workSchedule;}
    public WorkScheduleContract getGeneratedWorkSchedule(){return workSchedule;}

    public Entry.BelongingDay getBelongingDay() {
        return belongingDay;
    }
    /**
     * It returns if the day need more registers, for example
     * It have a odd number or none, or some register are duplicated
     * @return
     */
    public boolean isUnfinishDay(){
        if (entryPairs==null) return false;
        //return (entries.getEntries().size()%2!=0);
        for (PairedEntry e : entryPairs){
            if (!e.isFull()) return true;
        }
        return false;
    }

    private List<PairedEntry> createPairsInfo(EntrySet entries){
        if (entries==null) return null;
        ArrayList<PairedEntry> list = new ArrayList<>();
        PairedEntry current =null;
        for (Entry entry : entries){
            // Ignore entries that are not WORKING type
            if (entry.getKind()==Entry.Kind.WORKING) {
                if (current == null) {
                    current = new PairedEntry();
                    list.add(current);
                    current.starting = entry;
                } else {
                    current.finish = entry;
                    current = null;
                }
            }

        }
        return list;
    }

    public List<PairedEntry> getPairsInfo(){
        //if (entryPairs==null) entryPairs = createPairsInfo();
        return entryPairs;
    }
    public InfoDayEntry getTimeSegment(TimeFrame segment){
        return getTimeSegment(segment.getStartTime(), segment.getFinishTime());
    }
    public InfoDayEntry getTimeSegment(Calendar start, Calendar finish){

        List<PairedEntry> entryPairs = getPairsInfo();
        List<PairedEntry> result = new ArrayList<>();
        for (PairedEntry pair : entryPairs){
            if (pair.isFull()) {
                if (pair.isContained(start) && pair.isContained(finish)) {
                    PairedEntry newPair = new PairedEntry(pair);
                    newPair.starting.setRegisterDate(start);
                    newPair.finish.setRegisterDate(finish);
                    result.add(newPair);

                } else if (pair.isContained(start)) {
                    for (PairedEntry split : pair.split(pair, start)) {
                        if (!split.isInFuture(start)) result.add(split);
                    }
                } else if (pair.isContained(finish)) {
                    for (PairedEntry split : pair.split(pair, finish)) {
                        if (!split.isInPast(finish)) result.add(split);
                    }
                } else if ((pair.starting.getRegisterDate().compareTo(start) >= 0) &&
                        (pair.finish.getRegisterDate().compareTo(finish) <= 0)) {
                    result.add(pair);
                }
            } else result.add(pair);
        }
        return new InfoDayEntry(result, getBelongingDay());
    }

    public long getTotalMinutesGaps(TimeFrame where){
        return getTotalMinutesGaps(where.getStartTime(), where.getFinishTime());
    }
    // This is not exact, it expect than pairs are inside segment
    public long getTotalMinutesGaps(Calendar start, Calendar finish){
        long totalTime = getTotalMinuteOfWork();
        long totalSegmentMinutes = PairedEntry.getDiffMinutes(start,finish);
        return totalSegmentMinutes-totalTime;
    }

    public long getTotalMinuteOfWork(){
        List<PairedEntry> entryPairs = getPairsInfo();
        long totalTime = 0;
        for (PairedEntry pair : entryPairs){
            totalTime += pair.getMinutesWorkingTime();
        }
        return totalTime;
    }
    public long getTotalMinuteOfWorkForHR(){
        List<PairedEntry> entryPairs = getPairsInfo();
        long totalTime = 0;
        for (PairedEntry pair : entryPairs){
            if (pair.countHr()) totalTime += pair.getMinutesWorkingTime();
        }
        return totalTime;
    }

    public long remainWorkingTime(){
        if (getGeneratedWorkSchedule()!=null){
            int expected =  getGeneratedWorkSchedule().getExpectedWorkingTimeInMinutes();
            long worked = getTotalMinuteOfWorkForHR();
            return expected-worked;
        } else return 0;
    }
    public boolean isExpectedWorkingTimeAchieved(){
        return (remainWorkingTime()<=0);
    }
    /// null is no new entry expected

    public void setBestEntryLeavingWork(Entry bestLeavingWork){
        this.bestLeavingWork = bestLeavingWork;
    }
    public Entry getBestEntryLeavingWork(){
        return bestLeavingWork;
    }


    /**
     * It checks that data is coherent
     * @return false if not
     */
    protected static boolean  checkSanity(EntrySet entries, String belongingDay){
        if (entries==null) return false;

        for (Entry entry : entries){
            if (!entry.getBelongingDay().equals(belongingDay)){
                LOGGER.log(Level.SEVERE, "Inconsistent data on InfoDayEntry" + entry.getBelongingDay() + " != " + belongingDay);
                return false;
            }
        }
        return true;
    }

    protected static EntrySet cleanDuplicatedEntries(EntrySet entries){
        // https://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
        return new EntrySet(new ArrayList<Entry>(new LinkedHashSet<Entry>(entries.getEntries())));
    }


    public static class PairedEntry {

        public Entry starting;
        public Entry finish;
        Boolean tampered = false; // It has been modified by program (it's not original user data)
        Boolean countHr = true;

        public PairedEntry(){
            starting = null;
            finish = null;
        }

        public PairedEntry(PairedEntry p){
            starting = new Entry(p.starting);
            if (p.finish!=null) finish = new Entry(p.finish);
                else finish=null;
            tampered = p.isTampered();
            countHr = p.countHr();
        }

        public PairedEntry(Entry s, Entry e){
            starting = s;
            finish = e;
        }

        //
        public Boolean isContained(Calendar date){
            if (!isFull()) return false;
            if (date.compareTo(finish.getRegisterDate())>=0) return false;
            if (date.compareTo(starting.getRegisterDate())<=0) return false;
            return true;
        }

        public Boolean isInFuture(Calendar date){

            int v = date.compareTo(starting.getRegisterDate());
            return (date.compareTo(starting.getRegisterDate())>0);
        }

        public Boolean isInPast(Calendar date){
            if (!isFull()) return (date.compareTo(starting.getRegisterDate())<0);
            return (date.compareTo(finish.getRegisterDate())<0);
        }

        public static List<PairedEntry> split(PairedEntry paired, Calendar splitPoint){
            List<InfoDayEntry.PairedEntry> result = new ArrayList<>();
            if (!paired.isFull()) {
                result.add(new PairedEntry(paired));
                return result;
            }
            if (!paired.isContained(splitPoint)){
                result.add( new PairedEntry(paired));
                return result;
            }

            // We need to create 2 paired!
            PairedEntry a = new PairedEntry(paired);
            a.finish.setRegisterDate(splitPoint);
            PairedEntry b = new PairedEntry(paired);
            b.starting.setRegisterDate(splitPoint);
            result.add(a);
            result.add(b);
            return result;
        }
        public static long getDiffMinutes(Calendar a, Calendar b){
            return (b.getTimeInMillis() - a.getTimeInMillis())/ (1000*60);
        }
        public long getMinutesWorkingTime(){
            if (starting==null || finish==null) return 0;
            //return (finish.getRegisterDate().getTimeInMillis() - starting.getRegisterDate().getTimeInMillis())/ (1000*60);
            return getDiffMinutes(starting.getRegisterDate(), finish.getRegisterDate());
        }

        public void setCountHr(Boolean v){
            countHr = v;
            setTampered();
        }
        public Boolean countHr(){
            return countHr;
        }
        public Entry getStarting(){ return starting;}
        public void setStarting(Entry e){
            starting = e;
            setTampered();
        }
        public Entry getFinish(){ return finish;}
        public void setFinish(Entry e){
            finish = e;
            setTampered();
        }

        public void setTampered(){
            tampered = true;
        }
        public Boolean isTampered(){
            return tampered;
        }

        public Boolean isFull(){
            return (starting!=null && finish!=null);
        }

        public String toString() {
            String res =" PAIR[";
            if (starting!=null) res += starting.toString() + " "; else res += "N/A";
            if (finish!=null) res += finish.toString() + "]"; else res += "N/A]";

            return res;
        }
    }


}
