package org.jesteban.clockomatic.fragments.registerpage;


import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.providers.CompaniesProvider;
import org.jesteban.clockomatic.providers.CompaniesProviderContract;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.providers.UndoProviderContract;
import org.jesteban.clockomatic.providers.WorkScheduleProviderContract;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterPagePresenter implements RegisterPageContract.Presenter, SelectedDayProviderContract.Listener,
        CompaniesProviderContract.Listener, WorkScheduleProviderContract.Listener{
    private static final Logger LOGGER = Logger.getLogger(RegisterPagePresenter.class.getName());
    private PresenterBase  parent = null;
    private SelectedDayProviderContract selectedDay = null;
    private UndoProviderContract undoProvider = null;
    private WorkScheduleProviderContract workScheduleProvider = null;
    private CompaniesProvider companiesProvider = null;

    private EntriesProviderContract entries = null;
    private RegisterPageContract.View view = null;
    private Date lastForceSetDay = new Date();
    private String currentFilterBelongingForDay = "";
    private final int minimumTimeToForceDayInSeconds = 10;
    private Context context = null;
    // https://stackoverflow.com/questions/1877417/how-to-set-a-timer-in-android


    public RegisterPagePresenter(@NonNull RegisterPageContract.View view, Context context){
        this.view = view;
        this.context = context;
    }
    @Override
    public List<Provider> getBindings() {
        List<Provider> list = parent.getBindings();
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {

    }

    @Override
    public void startUi() {
        workScheduleProvider.setCompanyId(companiesProvider.get().getActiveCompany().getId());
    }

    @Override
    public boolean register(Entry entry) {
        try {
            UndoAction res = entries.register(entry);
            if (res != null) {
                res.description = String.format(context.getResources().getString(R.string.msg_undo_register_date), entry.toShortString());
                undoProvider.push(res);
            }
        } catch (SQLiteConstraintException e){
            LOGGER.warning(e.getClass().getName());
            LOGGER.warning(e.toString());
            view.showMessage(context.getResources().getString(R.string.error_entry_duplicated));
        } catch (Exception e){
            LOGGER.warning(e.getClass().getName());
            LOGGER.warning(e.toString());
            view.showMessage(e.getMessage());
        }
        return true;
    }

    @Override
    public void selected(Calendar date) {
        selectedDay.setSelecteDay(date);
    }

    @Override
    public boolean remove(Entry entry) {
        return false;
    }

    @Override
    public void onResume() {
        LOGGER.log(Level.INFO, "onResume setting current date");
        Date now = new Date();
        if ( (now.getTime() - lastForceSetDay.getTime()) > minimumTimeToForceDayInSeconds ) {
            LOGGER.log(Level.INFO, "onResume due a timeout I'm setting current date");
            Calendar cal = Calendar.getInstance();
            selectedDay.setSelecteDay(cal);
            view.showDate(cal);
        }
        workScheduleProvider.setCompanyId(companiesProvider.get().getActiveCompany().getId());
    }

    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProviderContract i){
        entries = i;
        //entries.subscribe(this);
    }
    public void setUndoProvider(@NonNull UndoProviderContract i){
        undoProvider = i;
        //entries.subscribe(this);
    }
    // This is fill with DependencyInjectorBinding
    public void setSelectedDayProvider(SelectedDayProviderContract i) {
        selectedDay = i;
        selectedDay.subscribe(this);
    }

    public void setWorkScheduleProvider(@NonNull WorkScheduleProviderContract i){
        workScheduleProvider = i;
        workScheduleProvider.subscribe(this);
    }

    public void setCompaniesProvider(@NonNull CompaniesProvider i){
        companiesProvider = i;
        companiesProvider.subscribe(this);
    }

    @Override
    public void onChangeSelectedDay() {
        if (currentFilterBelongingForDay.equals(selectedDay.getFilterBelongingForDay())) return;
        currentFilterBelongingForDay = selectedDay.getFilterBelongingForDay();
        workScheduleProvider.setBelongingDay(new Entry.BelongingDay(selectedDay.getSelectedDay()));
        view.showDate(selectedDay.getSelectedDay());
    }

    @Override
    public void onChangeCompanies() {
        workScheduleProvider.setCompanyId(companiesProvider.get().getActiveCompany().getId());
    }

    @Override
    public void onChangeWorkScheduler() {

    }
}
