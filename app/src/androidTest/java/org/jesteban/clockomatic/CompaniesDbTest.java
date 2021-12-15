package org.jesteban.clockomatic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.SettingsDb;
import org.jesteban.clockomatic.store.ClockmaticDb;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CompaniesDbTest {
    ClockmaticDb db = null;
    CompaniesDb companiesDb = null;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new ClockmaticDb(appContext,null,false);
        companiesDb = new CompaniesDb(db, new SettingsDb(db));
    }

    @Test
    public void onNewDbMustBe1Company() throws Exception {
        List<Company> list = companiesDb.getAllCompaniesEnabled();
        assertEquals( 1,list.size());
    }

    @Test
    public void addCompanyInsertNewRegister() throws Exception {
        Company c = new Company("addCompanyInsertNewRegister", "desc");
        // by default companies must be enable
        //c.setIsEnabled(true);
        companiesDb.addCompany(c);
        List<Company> list = companiesDb.getAllCompaniesEnabled();
        assertEquals( "there are 1 default (Main) company and new one", 2,list.size());
    }

    @Test
    public void addCompanyFillIdOnCompanyAndEnabled() throws Exception {
        Company c = new Company("addCompanyFillIdOnCompanyParam", "desc");
        c.setId(-1);
        companiesDb.addCompany(c);
        assertNotEquals("On return company id is filled with id", -1,c.getId());
        assertEquals("On return company must be enabled", true,c.isEnabled());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cantRemoveLastCompnay() throws Exception {
        List<Company> list = companiesDb.getAllCompaniesEnabled();
        companiesDb.removeCompany(list.get(0).getId());
    }

    public void insertDateRegister(int companyId){
        SQLiteDatabase dbw = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClockmaticDb.TableEntries.FIELD_COMPANY_ID, companyId);
        values.put(ClockmaticDb.TableEntries.FIELD_BELONGING_DAY, "1245");
        values.put(ClockmaticDb.TableEntries.FIELD_REGISTER_DATE, "fdffdfsd");
        long newRowId = dbw.insertOrThrow(ClockmaticDb.TableEntries.TABLE_NAME, null, values);
    }

    @Test
    public void IfACompanyHaveDataIsNotDeleteIsMarkAsDeleted() throws Exception {
        Company c = new Company("IfACompanyHaveDataIsNotDeleteIsMarkAsDeleted", "desc");
        companiesDb.addCompany(c);
        int id = c.getId();
        insertDateRegister(id);
        companiesDb.removeCompany(id);
        Company c2 = companiesDb.getCompany(id);
        assertNotEquals(null, c2);
    }
    @Test
    public void disableCompany() throws Exception {
        Company c = new Company("IfACompanyHaveDataIsNotDeleteIsMarkAsDeleted", "desc");
        companiesDb.addCompany(c);
        int id =c.getId();
        companiesDb.setStateCompany(id,false);
        Company c2 = companiesDb.getCompany(id);
        assertFalse(c2.isEnabled());
    }

    @Test
    public void getAllCompaniesIncludingDisabled() throws Exception {
        Company c = new Company("IfACompanyHaveDataIsNotDeleteIsMarkAsDeleted", "desc");
        companiesDb.addCompany(c);
        int id =c.getId();
        companiesDb.setStateCompany(id,false);
        List<Company> lst = companiesDb.getAllCompaniesIncludingDisabled();
        Boolean found = false;
        for (Company company : lst){
            if (company.getId()==id) found =true;
        }
        assertTrue("On getAllCompaniesIncludingDisabled must return disabled company ", found);
    }


/*
    @Test
    public void getCompaniesDontReturnDisabledCompanies() throws Exception {
    }


    @Test
    public void removeCompany() throws Exception {
    }

    @Test
    public void setActiveCompany() throws Exception {
    }

    @Test
    public void getActiveCompany() throws Exception {
    }

    @Test
    public void getDefaultCompany() throws Exception {
    }

    @Test
    public void setDefaultCompany() throws Exception {
    }
*/

}