package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.WorkScheduleDb;

import java.util.List;

public interface WorkScheduleProviderContract extends  Provider {
    static final String  KEY_PROVIDER="workScheduler";

    void subscribe(WorkScheduleProviderContract.Listener listener);
    void setCompanyId(int companyId);
    void setBelongingDay(Entry.BelongingDay day);
    WorkScheduleContract getWorkSchedule();
    UndoAction setWorkSchedule(int id);
    UndoAction clearWorkSchedule();

    List<WorkScheduleDb.WorkScheduleData> getAvailableWorkSchedule();

    public interface Listener {
        void onChangeWorkScheduler();
    }
}
