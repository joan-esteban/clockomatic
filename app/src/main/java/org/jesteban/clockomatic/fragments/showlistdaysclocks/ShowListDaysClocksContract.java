package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.views.InfoDayViewContract;

import java.util.List;


public class ShowListDaysClocksContract {
    public interface View extends ViewBase<Presenter> {
        //public void showTitle(String title);
        //public void showEntries(List<Presenter.DayDataToShow> dayDatas);
        public void showData(List<InfoDayViewContract.View.InfoDayVisualData> daysData);
    }

    public interface Presenter extends PresenterBase {
        class DayDataToShow{
            String dateName = null;
            String [] entryText = null;
            String infoDay = null;
        }

        //public void selectedMonth(Calendar day);
        //public void editMonth(String month);
    }
}
