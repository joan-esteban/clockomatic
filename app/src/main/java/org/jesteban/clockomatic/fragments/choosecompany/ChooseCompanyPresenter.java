package org.jesteban.clockomatic.fragments.choosecompany;


import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.providers.Provider;

import java.util.ArrayList;
import java.util.List;

public class ChooseCompanyPresenter implements ChooseCompanyContract.Presenter {
    @Override
    public List<Provider> getBindings() {
        return new ArrayList<>();
    }

    @Override
    public void setParent(PresenterBase parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startUi() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean selectedCompany(Company c) {
        return false;
    }
}
