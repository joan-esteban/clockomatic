package org.jesteban.clockomatic.helpers;

import org.jesteban.clockomatic.fragments.report_page.ViewClocksMonthFragment;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It get information of a set of entries belonging to same day
 */

public class InfoDayEntry {
    private static final Logger LOGGER = Logger.getLogger(InfoDayEntry.class.getName());

    private EntrySet entries;

    /**
     * It checks that data is coherent
     * @return false if not
     */
    protected static boolean  checkSanity(EntrySet entries, String belongingDay){
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

    public InfoDayEntry(EntrySet entries, String belongingDay){
        if (!checkSanity(entries,belongingDay)){
            // TODO: launch exception?
            LOGGER.log(Level.SEVERE, "Inconsistent data on InfoDayEntry");
            this.entries = null;
        } else {
            this.entries = cleanDuplicatedEntries(entries);
        };
    }

    /**
     * It returns if the day need more registers, for example
     * It have a odd number or none, or some register are duplicated
     * @return
     */
    public boolean isUnfinishDay(){
        if (entries==null) return false;
        return (entries.getEntries().size()%2!=0);
    }
    public static class EntryPairs{
        public Entry starting;
        public Entry finish;

        public EntryPairs(){
            starting = null;
            finish = null;
        }

        public EntryPairs(Entry s, Entry e){
            starting = s;
            finish = e;
        }

        public long getMinutesWorkingTime(){
            if (starting==null || finish==null) return 0;
            return (finish.getRegisterDate().getTimeInMillis() - starting.getRegisterDate().getTimeInMillis())/ (1000*60);
        }
    }
    public List<EntryPairs> getPairsInfo(){
        if (entries==null) return null;
        ArrayList<EntryPairs> list = new ArrayList<>();
        EntryPairs current =null;
        for (Entry entry : entries){
            if (current==null){
                current = new EntryPairs();
                list.add(current);
                current.starting = entry;
            } else{
                current.finish = entry;
                current = null;
            }

        }
        return list;
    }
}
