package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.CompaniesContract;
import org.jesteban.clockomatic.model.CompaniesDb;

import java.util.Observable;
import java.util.Observer;

public class CompaniesProvider implements CompaniesProviderContract, Observer {


    public CompaniesProvider(CompaniesDb companies){
        this.companies = companies;
        companies.addObserver(this);
    }

    public CompaniesContract get(){
        return companies;
    }




    public void subscribe(CompaniesProviderContract.Listener listener) {
        observable.add(listener);
    }

    @Override
    public void update(Observable o, Object arg) {
        observable.notify("onChangeCompanies");
    }

    private CompaniesDb companies = null;
    private ObservableDispatcher<CompaniesProviderContract.Listener> observable = new ObservableDispatcher<>();


    @Override
    public String getName() {
        return CompaniesProviderContract.KEY_PROVIDER;
    }
}
