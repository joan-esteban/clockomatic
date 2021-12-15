package org.jesteban.clockomatic.fragments.choosecompany;


import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Company;

import java.util.List;

public class ChooseCompanyContract {
    public interface View extends ViewBase<Presenter> {
        public void showCompanies(List<Company> companies);
    }

    public interface Presenter extends PresenterBase {
        boolean selectedCompany(Company c);
    }
}
