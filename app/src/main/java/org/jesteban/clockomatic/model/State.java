package org.jesteban.clockomatic.model;

import java.util.Calendar;

public class State {

    public static class Status {
        public enum StatusId {
            OK,
            ERROR
        }

        public StatusId statusId = StatusId.OK;
        public String statusDesc = "";

        public Boolean isOk() {
            return (statusId == StatusId.OK);
        }

        public void setOk() {
            statusId = StatusId.OK;
            statusDesc = "";
        }

        public void setError(String desc) {
            statusId = StatusId.ERROR;
            statusDesc = desc;
        }
    }

    public Entry getFloatingEntry() {
        return floatingEntry;
    }

    public void setFloatingEntry(Entry entry) {
        this.floatingEntry = entry;
    }

    public EntrySet getEntries() {
        return entries;
    }

    public void setEntries(EntrySet entries) {
        this.entries = entries;
    }

    public Boolean addEntry(Entry entry) {
        if (status.isOk()) {
            this.entries.addEntry(entry);
            return true;
        }
        return false;
    }

    public Boolean removeEntry(Entry entry) {
        this.entries.removeEntry(entry);
        return true;
    }

    public Status getStatus() {
        return status;
    }


    private Entry floatingEntry = new Entry(Calendar.getInstance());
    private EntrySet entries = null;
    private Status status = new Status();

}
