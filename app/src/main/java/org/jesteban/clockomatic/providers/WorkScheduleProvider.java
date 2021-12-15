package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.CompaniesDb;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.WorkScheduleDb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorkScheduleProvider implements WorkScheduleProviderContract {
    private static final Logger LOGGER = Logger.getLogger(WorkScheduleProvider.class.getName());

    private WorkScheduleDb workScheduleDb = null;
    private CompaniesDb companiesDb = null;
    private ObservableDispatcher<WorkScheduleProvider.Listener> observable = new ObservableDispatcher<>();
    private int companyId = -1;
    private Company company = null;
    private Entry.BelongingDay day = null;
    public WorkScheduleProvider(WorkScheduleDb workScheduleDb, CompaniesDb companiesDb){
        this.workScheduleDb = workScheduleDb;
        this.companiesDb = companiesDb;
    }

    public void refresh(){
        if (companyId<0 || day==null) return;
        observable.notify("onChangeWorkScheduler");
    }

    @Override
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
        company = companiesDb.getCompany(companyId);
        refresh();
    }

    @Override
    public void setBelongingDay(Entry.BelongingDay day) {
        this.day = new Entry.BelongingDay(day);
        refresh();
    }

    @Override
    public WorkScheduleContract getWorkSchedule() {
        if (companyId<0 || day==null) return null;
        workScheduleDb.getWorkScheduleIdForDay(companyId,day.getDay(), company.getDefaultWorkSchedule() );
        return workScheduleDb.getWorkScheduleForDay(companyId, day);
    }

    @Override
    public UndoAction setWorkSchedule(int id) {
        if (companyId<0 || day==null) return null;
        LOGGER.info(String.format("setWorkSchedule %d", id));
        UndoAction undo = workScheduleDb.setWorkScheduleForDay(companyId, day, id);
        if (undo!=null) {
            undo.actions.add(new UndoNotifyWorkScheduleProvider(this));
            refresh();
        };
        return undo;
    }
    @Override
    public UndoAction clearWorkSchedule(){
        if (companyId<0 || day==null) return null;
        LOGGER.info(String.format("clearWorkSchedule"));
        UndoAction undo = workScheduleDb.deleteWorkScheduleForDay(companyId, day);
        if (undo!=null) {
            undo.actions.add(new UndoNotifyWorkScheduleProvider(this));
            refresh();
        }
        return undo;
    }

    @Override
    public List<WorkScheduleDb.WorkScheduleData> getAvailableWorkSchedule() {
        if (companyId<0 || day==null) return new ArrayList<>();
        return workScheduleDb.getAvailableWorkSchedule(companyId);
    }

    @Override
    public String getName() {
        return WorkScheduleProviderContract.KEY_PROVIDER;
    }

    @Override
    public void subscribe(Listener listener) {
        observable.add(listener);
    }

    // Undo Helpers ////////////////////////////////////////////////////////////////////////////////
    public class UndoNotifyWorkScheduleProvider implements UndoAction.UndoActionStepContract {
        WorkScheduleProvider workScheduleProvider = null;
        int companyId;
        Entry.BelongingDay day;

        public UndoNotifyWorkScheduleProvider(WorkScheduleProvider workScheduleProvider) {
            this.workScheduleProvider = workScheduleProvider;
        }
        @Override
        public void executeUndo() {
            workScheduleProvider.refresh();
        }
    }
}
