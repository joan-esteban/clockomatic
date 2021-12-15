package org.jesteban.clockomatic.fragments.infoperiodview;


import org.jesteban.clockomatic.fragments.showlistdaysclocks.ShowListDaysClocksMonthPresenter;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;

import java.util.Map;

public class InfoPeriodViewContract {
    public interface View extends ViewBase<Presenter> {
        static public class InfoPeriodVisualData{
            String numWorkingDays;
            String totalWorkingTime; // Total time working
            String hrWorkingTime; // Time worked using Human Resource calculation
        }

        void showData(InfoPeriodVisualData data);
    }
    public interface Presenter extends PresenterBase {
        View.InfoPeriodVisualData convertData(Map<Integer,ShowListDaysClocksMonthPresenter.DataPerDay> showingInfoDays);
        void showData(Map<Integer,ShowListDaysClocksMonthPresenter.DataPerDay> showingInfoDays);
    }
}
