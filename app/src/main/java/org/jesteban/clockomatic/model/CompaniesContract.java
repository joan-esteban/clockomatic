package org.jesteban.clockomatic.model;


import android.database.SQLException;

import java.util.List;
import java.util.Observer;

public interface CompaniesContract {

    List<Company> getAllCompaniesEnabled();

    UndoAction addCompany(Company c) throws SQLException;

    UndoAction removeCompany(int index);

    Boolean setActiveCompany(int index);

    Company getActiveCompany();

    //Company getDefaultCompany();

    //Boolean setDefaultCompany();

    long getCountEntriesForCompany(int companyId);


    void addObserver(Observer o);

    void deleteObserver(Observer o);
}
