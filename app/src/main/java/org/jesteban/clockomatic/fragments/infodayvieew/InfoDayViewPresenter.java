package org.jesteban.clockomatic.fragments.infodayvieew;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.fragments.showdaydetail.DayDetailContract;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;

public class InfoDayViewPresenter extends  InfoDayViewPresenterNoProviderLink
                                  implements EntriesProviderContract.Listener, SelectedDayProviderContract.Listener{




    public InfoDayViewPresenter(@NonNull InfoDayViewContract.View view, Context current) {
        super(view, current);
    }

    @Override
    public void onChangeEntries() {
        InfoDayEntry infoDayEntry = new InfoDayEntry(this.entries.getEntries().getEntriesBelongingDayStartWith(selectedDay.getFilterBelongingForDay())
                , selectedDay.getFilterBelongingForDay());
        showInfoDay(infoDayEntry);
    }

    @Override
    public void onChangeSelectedDay() {
        onChangeEntries();
    }

    public void setSelectedDayProvider(@NonNull SelectedDayProviderContract p) {
        selectedDay = p;
        selectedDay.subscribe(this);
    }

    public void setEntriesProvider(@NonNull EntriesProviderContract i){
        entries = i;
        entries.subscribe(this);
    }


    protected SelectedDayProviderContract selectedDay = null;
    protected EntriesProviderContract entries = null;
}
