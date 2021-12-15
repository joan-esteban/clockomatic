package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.model.CompaniesContract;

public interface CompaniesProviderContract extends  Provider  {
    static final String  KEY_PROVIDER="companies";



    void subscribe(CompaniesProviderContract.Listener listener);
    CompaniesContract get();

    public interface Listener {
        void onChangeCompanies();
    }
}
