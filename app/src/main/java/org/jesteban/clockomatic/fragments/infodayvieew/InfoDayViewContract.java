package org.jesteban.clockomatic.fragments.infodayvieew;


import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.views.MyCalendarDayViewContract;
import org.jesteban.clockomatic.views.MyPairedEntryViewContract;

import java.util.List;

public class InfoDayViewContract {
    public interface View extends ViewBase<Presenter> {
        static public class InfoDayBriefData{
            String [] title = new String[2];
            String [] text = new String[2];
            int [] color = new int[2];
        }

        enum ExpandedLevel{
            FULL,
            MEDIUM,
            COLLAPSED
        }

        static public class InfoDayVisualData{
            public MyCalendarDayViewContract.CalendarDayViewVisualData dayData = null;
            public List<MyPairedEntryViewContract.PairedEntryVisualData> entriesData = null;
            public InfoDayBriefData briefData = null;
            public Entry.BelongingDay belongingDay = null;
            public String workingScheduleName = null;
            public String textExtraLine = null;
        }

        void showData(InfoDayVisualData data);
        InfoDayVisualData getData();
        void showMessage(String msg);
        void setExpandedLevel(ExpandedLevel level);

    }

    public interface Presenter extends PresenterBase {
        interface OnAction{
            void execute();
        }
        void showInfoDay(List<InfoDayEntry> infoDay, int infoDayIndexForWorkSchedule);
        void clickOnCard();
        void clickOnEntryItem(int position);
        void clickOnCalendar();
        void setOnClickOnCardAction(OnAction action);
        void setOnClickOnEntryItem(OnAction action);
        void setOnClickOnCalendar(OnAction action);

    }
}
