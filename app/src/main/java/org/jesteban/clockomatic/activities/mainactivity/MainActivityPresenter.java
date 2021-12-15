package org.jesteban.clockomatic.activities.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.activities.settingactivity.SettingsFragment;
import org.jesteban.clockomatic.model.EntriesDb;
import org.jesteban.clockomatic.model.SettingsDb;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.model.WorkScheduleDb;
import org.jesteban.clockomatic.providers.CompaniesProvider;
import org.jesteban.clockomatic.providers.CompaniesProviderContract;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.EntriesProvider;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.providers.SelectedDayProvider;
import org.jesteban.clockomatic.providers.SelectedMonthProviderContract;
import org.jesteban.clockomatic.providers.SelectedMonthProvider;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.DependencyInjectorBinding;
import org.jesteban.clockomatic.providers.ShowPageProvider;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;
import org.jesteban.clockomatic.providers.UndoProvider;
import org.jesteban.clockomatic.providers.UndoProviderContract;
import org.jesteban.clockomatic.providers.WorkScheduleProvider;
import org.jesteban.clockomatic.providers.WorkScheduleProviderContract;
import org.jesteban.clockomatic.store.ClockmaticDb;
import org.jesteban.clockomatic.store.ImportFicharFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivityPresenter implements MainActivityContract.Presenter, SelectedDayProviderContract.Listener,
                ShowPageProviderContract.Listener, CompaniesProviderContract.Listener,
                EntriesProviderContract.Listener,
                UndoProviderContract.Listener{
    private static final Logger LOGGER = Logger.getLogger(MainActivityPresenter.class.getName());
    private ClockmaticDb clockmaticDb = null;
    private SettingsDb settingsDb;
    private CompaniesDb companiesDb = null;
    private EntriesDb entriesDb = null;
    private WorkScheduleDb workScheduleDb = null;

    private CompaniesProvider companiesProvider = null;
    private EntriesProviderContract entriesProvider = null;
    private SelectedDayProviderContract selectedDay = new SelectedDayProvider();
    private SelectedMonthProviderContract selectedMonth = new SelectedMonthProvider();
    private ShowPageProviderContract showPage = new ShowPageProvider();
    private UndoProviderContract undoProvider = new UndoProvider();
    private WorkScheduleProviderContract workScheduleProvider = null;

    private List<Company> currentListCompanies = null;

    private MainActivityContract.View view = null;
    private Context context = null;

    static final  ShowPageProviderContract.PageId INITIAL_PAGE= ShowPageProviderContract.PageId.REGISTER_PAGE;


    public MainActivityPresenter(@NonNull MainActivityContract.View view, Context context){
        this.view = view;
        this.context = context;
        clockmaticDb = new ClockmaticDb(context);
        //clockmaticDb.test();
        settingsDb = new SettingsDb(clockmaticDb);
        companiesDb = new CompaniesDb(clockmaticDb, settingsDb);
        entriesDb = new EntriesDb(clockmaticDb);
        workScheduleDb = new WorkScheduleDb(clockmaticDb);
        companiesProvider = new CompaniesProvider(companiesDb);
        entriesProvider = new EntriesProvider(entriesDb);
        workScheduleProvider = new WorkScheduleProvider(workScheduleDb, companiesDb);
        selectedDay.subscribe(this);
        showPage.subscribe(this);
        companiesProvider.subscribe(this);
        entriesProvider.subscribe(this);
        undoProvider.subscribe(this);
    }

    @Override
    public List<Provider> getBindings() {
        List<Provider> list = new ArrayList<>();
        list.add(entriesProvider);
        list.add(selectedDay);
        list.add(selectedMonth);
        list.add(showPage);
        list.add(companiesProvider);
        list.add(undoProvider);
        list.add(workScheduleProvider);
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {
        // MainActivity doesnt have parent
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        child.setParent(this);
        DependencyInjectorBinding dib =new DependencyInjectorBinding();
        dib.inject(child, getBindings());
    }

    @Override
    public void startUi() {
        this.showPage.setShowPage(INITIAL_PAGE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean importData = sharedPreferences.getBoolean(SettingsFragment.KEY_PREFERENCE_IMPORT_OLD_DATA, true);
        if (importData){
            ImportFicharFiles importer = new ImportFicharFiles();
            importer.importAllDataTo(entriesProvider);
            view.showMessage("Imported old data");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SettingsFragment.KEY_PREFERENCE_IMPORT_OLD_DATA, false);
            editor.commit();
        }
        setCompaniesSelector();

    }
    private int findCompanyIndex(Company c){
        for (int i=0; i< currentListCompanies.size(); i++){
            if (c.getId() == currentListCompanies.get(i).getId()) return i;
        }
        return -1;
    }
    void setCompaniesSelector(){
        currentListCompanies = this.companiesProvider.get().getAllCompaniesEnabled();
        ArrayList<String> companiesName= new ArrayList<>();
        for (Company c : currentListCompanies ) {
            companiesName.add(c.getName() );
        }
        view.setAvailableCompanies(companiesName);
        view.setVisibleAvailableCompanies(companiesName.size()>1);
        Company activeCompany= companiesProvider.get().getActiveCompany();

        view.setSelectedCompany(findCompanyIndex(activeCompany), activeCompany.getColor() );
        entriesProvider.setCompanyId(activeCompany.getId() );
    }

    @Override
    public void onSelectedCompany(String name, int index) {
        LOGGER.info("onSelectedCompany "+ name );
        int companyId = currentListCompanies.get(index).getId();
        if (companiesProvider.get().getActiveCompany().getId()==companyId) return;
        companiesProvider.get().setActiveCompany(companyId);
        String msg = context.getResources().getString(R.string.msg_changed_companies);
        msg = String.format(msg, companiesProvider.get().getActiveCompany().getName());
        setCompaniesSelector();
        view.showMessage(msg);
    }

    @Override
    public void wipeStore() {
        try {
            UndoAction undoAction = entriesProvider.wipeStore();
            undoAction.description = context.getResources().getString(R.string.msg_undo_delete_data_company);
            undoProvider.push(undoAction);
        } catch (Exception e ){
            LOGGER.warning("error wipeStore: " + e.getMessage());
            view.showMessage("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void onViewChangeShowPage(ShowPageProviderContract.PageId id) {
        this.showPage.setShowPage(id);
    }

    @Override
    public void onSelectedMenuItem(int id) {
        switch (id) {
            case R.id.action_undo:
                if (!undoProvider.isEmpty()) onPushUndo();
                break;
            case R.id.action_settings:
                view.showSettings();
                break;
            case R.id.action_wipe_data:
                LOGGER.info("main Menu selected action_wipe_data");
                view.askConfirmWipeData();
                break;
            case R.id.action_about:
                LOGGER.info("main Menu selected action_about");
                view.showAbout();

                break;
            case R.id.action_debug:
                LOGGER.info("main Menu selected action_debug");
                view.showPage(ShowPageProviderContract.PageId.DEBUG_PAGE);
                break;
            case R.id.action_configure_companies:
                LOGGER.info("main Menu selected action_configure_companies");
                view.showCompaniesEditor();

                break;
            default:
                LOGGER.info("unknown menu item selected");
        }
    }

    @Override
    public void onFinishCompaniesEditor(Boolean isOk){
        LOGGER.info("reloading companiesProvider");
        String msg = context.getResources().getString(R.string.msg_changed_companies);
        msg = String.format(msg, companiesProvider.get().getActiveCompany().getName());
        view.showMessage(msg);
        setCompaniesSelector();
    }



    @Override
    public void onChangeSelectedDay() {
        // Nothing to do
    }

    @Override
    public void onChangeShowPage() {
        view.showPage(showPage.getShowPage());
    }

    @Override
    public void onChangeCompanies() {
        setCompaniesSelector();
    }

    @Override
    public void onChangeEntries(){
        setCompaniesSelector();
    }

    @Override
    public void onPushUndo() {
        UndoAction undoAction = undoProvider.peek();
        view.showUndoMessage(undoAction);
    }

    @Override
    public void onPopUndo() {
        if (!undoProvider.isEmpty()){
            onPushUndo();
        }
    }
}
