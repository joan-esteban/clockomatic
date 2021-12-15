package org.jesteban.clockomatic.fragments.registerpage;


import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;

import java.util.List;

public class ChooseWorkScheduleContract {
    public interface View extends ViewBase<ChooseWorkScheduleContract.Presenter> {
        void setAvailableWorkSchedule(List<String> workScheduleNames);
        void setVisibleWorkSchedule(Boolean visible);
        void SetSelectedWorkSchedule(int idx);
    }
    public interface Presenter extends PresenterBase {
        // if name = null -> index = -1 nothing selected
        void onSelectedWorkSchedule(String name, int index);

    }
}
