package org.jesteban.clockomatic.fragments.workSchedulerEditDialog;


import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;

public class WorkSchedulerEditDialogContract {
    public class WorkSchedulerData{
            public class EnterRestrictions{
                public int earlyTimeMinutes;
                public int laterTimeMinutes;

            }

        public boolean haveEnterRestrictions;
        public boolean haveLeaveRestrictions;
        public boolean haveMiddayRestrictions;
        public boolean haveWorkingTimeRestrictions;

    }
    public interface View extends ViewBase<Presenter> {


        void showDialogDatePicker(int year, int month, int dayOfMonth);

        void showDialogTimePicker(int hour, int minute, boolean is24HourView);

        void close();

        String getDate();

        String getHour();

        String getDescription();
    }

    public interface Presenter extends PresenterBase {

    }
}
