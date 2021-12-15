package org.jesteban.clockomatic.activities.companieseditoractivity;


import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.UndoAction;

public class CompaniesEditorContract {
    public interface View extends ViewBase<Presenter> {
        void refreshCompaniesList();

        void showEditDialog(final int position, int titleId, Company company, Boolean isAdd, Boolean canRemove);

        void showError(String msg);

        void showMessage(String text);

        void showUndoMessage(final UndoAction undoAction);
    }

    public interface Presenter extends PresenterBase {
        int getCompaniesSize();

        Company getCompany(int index);

        long getCompanyNumEntries(int index);

        void clickOnAddCompany();

        void clickOnEditDialogRemoveCompany(int index);

        Boolean clickOnEditDialogOk(int index, Company company);

        void clickOnListItem(int index);

        void clickOnDisableCompany(int index, Boolean state);
    }
}
