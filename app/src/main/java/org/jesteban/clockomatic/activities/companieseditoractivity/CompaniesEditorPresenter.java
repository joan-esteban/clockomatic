package org.jesteban.clockomatic.activities.companieseditoractivity;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.PresenterBasic;
import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.SettingsDb;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.UndoProvider;
import org.jesteban.clockomatic.store.ClockmaticDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CompaniesEditorPresenter extends PresenterBasic<CompaniesEditorContract.View>
        implements CompaniesEditorContract.Presenter,
        UndoProvider.Listener,
        Observer {

    public static final int NEW_COMPANY_ID = -1;

    private ClockmaticDb clockmaticDb = null;
    private CompaniesDb companiesDb = null;
    private SettingsDb settingsDb;
    private UndoProvider undoProvider = new UndoProvider();
    private Context context = null;
    private List<Company> listCompanies = null;

    public CompaniesEditorPresenter(@NonNull CompaniesEditorContract.View view, Context context) {
        super(view);
        clockmaticDb = new ClockmaticDb(context);
        settingsDb = new SettingsDb(clockmaticDb);
        companiesDb = new CompaniesDb(clockmaticDb, settingsDb);
        undoProvider.subscribe(this);
        this.context = context;
        refreshListCompanies();
        companiesDb.addObserver(this);
    }


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
        // Nothing to do
    }

    @Override
    public int getCompaniesSize() {
        return listCompanies.size();
    }

    @Override
    public Company getCompany(int index) {
        if (index == NEW_COMPANY_ID) {
            final int[] colors = {Color.WHITE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA};
            int lastId = this.listCompanies.get(listCompanies.size() - 1).getId();
            Company c = new Company(context.getResources().getText(R.string.default_company_name).toString(),
                    context.getResources().getText(R.string.default_company_desc).toString());

            c.setColor(colors[(lastId + 1) % colors.length]);

            return c;
        }
        return listCompanies.get(index);
    }

    @Override
    public long getCompanyNumEntries(int index) {
        Company c = listCompanies.get(index);
        return companiesDb.getCountEntriesForCompany(c.getId());
    }

    @Override
    public void clickOnAddCompany() {
        Company c = getCompany(NEW_COMPANY_ID);
        view.showEditDialog(NEW_COMPANY_ID, R.string.title_companies_editor_add_new_company, c, true, false);
    }

    @Override
    public void clickOnEditDialogRemoveCompany(int index) {
        Company c = listCompanies.get(index);
        long nEntries = companiesDb.getCountEntriesForCompany(c.getId());
        if (nEntries > 0) {
            UndoAction undo = companiesDb.setStateCompany(c.getId(), false);
            if (undo != null) {
                undo.description = String.format(context.getResources().getString(R.string.msg_undo_disabled_company),c.getName());
                undoProvider.push(undo);
            }

        } else {
            try {
                UndoAction undo = companiesDb.removeCompany(c.getId());
                if (undo != null) {
                    undo.description = String.format(context.getResources().getString(R.string.msg_undo_removed_company),c.getName());
                    undoProvider.push(undo);
                }
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        refresh();
    }

    @Override
    public Boolean clickOnEditDialogOk(int index, Company company) {
        UndoAction undo = null;
        if (company.getName().isEmpty()) {
            view.showError(context.getResources().getString(R.string.msg_error_empty_company_name));
            return false;
        }

        if (index >= 0) {
            undo = companiesDb.updateCompany(company);
            if (undo != null) undo.description = String.format(context.getResources().getString(R.string.msg_undo_modified_company),company.getName());
        } else {
            try {
                undo = companiesDb.addCompany(company);
                if (undo != null) undo.description = String.format(context.getResources().getString(R.string.msg_undo_add_company),company.getName());
            } catch (Exception e) {
                view.showError(e.getMessage());
            }

        }
        refresh();
        if (undo != null) undoProvider.push(undo);
        return true;
    }

    @Override
    public void clickOnListItem(int index) {
        Company company = new Company(getCompany(index));
        view.showEditDialog(index, R.string.title_companies_editor_settings, company, false, (listCompanies.size() > 1));
    }

    @Override
    public void clickOnDisableCompany(int index, Boolean state) {
        Company c = listCompanies.get(index);
        companiesDb.setStateCompany(c.getId(), state);
        refresh();
    }

    @Override
    public void onPushUndo() {
        UndoAction undoAction = undoProvider.peek();
        view.showUndoMessage(undoAction);
    }

    @Override
    public void onPopUndo() {
        if (!undoProvider.isEmpty()) {
            onPushUndo();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }

    private void refreshListCompanies() {
        listCompanies = companiesDb.getAllCompaniesIncludingDisabled();
    }

    private void refresh() {
        refreshListCompanies();
        view.refreshCompaniesList();
    }
}
