package org.jesteban.clockomatic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.EntriesDb;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.SettingsDb;
import org.jesteban.clockomatic.store.ClockmaticDb;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class EntriesDbTest {
    ClockmaticDb db = null;
    EntriesDb entriesDb = null;
    CompaniesDb companiesDb = null;
    int companyId = 0;
    Calendar date =  Calendar.getInstance();

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new ClockmaticDb(appContext,null,false);
        entriesDb = new EntriesDb(db);
        companiesDb = new CompaniesDb(db, new SettingsDb(db));
        Company company = new Company("test_entries","nothing");
        companiesDb.addCompany(company);
        companyId = company.getId();
    }

    @Test
    public void register() throws Exception {

        Entry entry = new Entry(date);
        entriesDb.register(companyId, entry);
    }

    @Test
    public void remove() throws Exception {
    }

    @Test
    public void getEntries() throws Exception {
    }

    @Test
    public void getEntriesBelongingDay() throws Exception {
        entriesDb.register(companyId, new Entry(date));
        EntrySet entries = entriesDb.getEntriesBelongingDay(companyId, date);

        assertEquals(1,entries.getEntries().size() );
    }

    @Test
    public void getEntriesBelongingMonth() throws Exception {
        entriesDb.register(companyId, new Entry(date));
        EntrySet entries = entriesDb.getEntriesBelongingMonth(companyId, date);

        assertEquals(1,entries.getEntries().size() );
    }

    @Test
    public void wipeStore() throws Exception {
        entriesDb.register(companyId, new Entry(date));
        assertTrue (!entriesDb.getEntriesBelongingDay(companyId, date).getEntries().isEmpty() );
        entriesDb.wipeStore(companyId);
        assertTrue (entriesDb.getEntriesBelongingDay(companyId, date).getEntries().isEmpty() );
    }




}