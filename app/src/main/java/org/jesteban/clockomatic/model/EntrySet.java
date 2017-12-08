package org.jesteban.clockomatic.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EntrySet extends Object implements Iterable<Entry> {

    private ArrayList<Entry> entries = new ArrayList<>();

    public EntrySet() {
        // Create a empty EntrySet
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        EntrySet entrySet = (EntrySet) obj;
        return entries.equals(entrySet.entries);
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries.iterator();
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);
    }

    public void removeEntry(Entry entry) {
        this.entries.remove(entry);
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public EntrySet getEntriesPerPeriod(Calendar dateInitial, Calendar dateEnd) {
        EntrySet result = new EntrySet();
        for (Entry entry : entries) {
            if ((entry.getRegisterDate().after(dateInitial)) && (entry.getRegisterDate().before(dateEnd)))
                result.addEntry(entry);
        }
        return result;
    }

    /*
    Filter can be "yyyy/MM/" or "yyyyy/MM/dd"
     */
    public EntrySet getEntriesBelongingDayStartWith(String filter) {
        EntrySet result = new EntrySet();
        for (Entry entry : entries) {
            if (!entry.isEmpty() && (entry.getBelongingDay().startsWith(filter))) {
                result.addEntry(entry);
            }
        }

        Collections.sort(result.getEntries(), new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                Long s1 = o1.getRegisterDate().getTime().getTime();
                Long s2 = o2.getRegisterDate().getTime().getTime();
                return s1.compareTo(s2);
            }
        });

        return result;

    }

    public Set<String> getDistintBelongingDays() {
        Set<String> result = new LinkedHashSet<>();
        for (Entry entry : entries) {
            result.add(entry.getBelongingDay());
        }
        return result;
    }

    @Override
    public String toString() {
        return "EntrySet{" +
                "entries=" + entries +
                '}';
    }
}
