package org.jesteban.clockomatic.fragments.showlistdaysclocks;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.helpers.PresenterBasicProviderEntriesReady;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class ShowListDaysClocksMonthPresenter extends PresenterBasicProviderEntriesReady<ShowListDaysClocksContract.View>
        implements ShowListDaysClocksContract.Presenter,
        SelectedMonthProvider.Listener
    {
        private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksMonthPresenter.class.getName());
        private SelectedMonthProvider selectedMonth = null;

        public ShowListDaysClocksMonthPresenter(@NonNull ShowListDaysClocksContract.View view){
            super(view);
        }
        private String convertToPrefix(Calendar date){
            DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
            return df.format(date.getTime());
        }

        @Override
        public void onChangeSelectedMonth() {
            LOGGER.info("onChangeSelectedMonth");
            onChangeEntries();
        }

        @Override
        public void onChangeEntries() {
            EntrySet entriesMonth = entries.getEntries().getEntriesBelongingDayStartWith(convertToPrefix(selectedMonth.getSelectedMonth()));
            view.showEntries(entriesMonth);
        }

        // This is fill with DependencyInjectorBinding
        public void setSelectedMonthProvider(SelectedMonthProvider i){
            selectedMonth = i;
            selectedMonth.subscribe(this);
        }


        @Override
        public void startUi() {
            onChangeEntries();
        }
    }
