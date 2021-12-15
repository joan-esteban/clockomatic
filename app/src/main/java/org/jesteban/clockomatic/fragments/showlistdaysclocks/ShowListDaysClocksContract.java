package org.jesteban.clockomatic.fragments.showlistdaysclocks;

import org.jesteban.clockomatic.fragments.infoperiodview.InfoPeriodViewContract;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;

import java.util.ArrayList;
import java.util.List;


public class ShowListDaysClocksContract {
    public interface View extends ViewBase<Presenter> {
        void update();
        InfoDayViewContract.Presenter getInfoDayPresenter(int idx);
        InfoPeriodViewContract.Presenter getInfoPeriodViewPresenter();

    }

    public interface Presenter extends PresenterBase {
        void clickOnDay(Entry.BelongingDay day);
        int getItemCount();
        void updateInfoDay(int position, InfoDayViewContract.Presenter presenter);

        //public void selectedMonth(Calendar day);
        //public void editMonth(String month);
    }
}
