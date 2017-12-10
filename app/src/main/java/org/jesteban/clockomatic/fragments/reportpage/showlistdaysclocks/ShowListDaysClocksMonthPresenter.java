package org.jesteban.clockomatic.fragments.reportpage.showlistdaysclocks;


import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class ShowListDaysClocksMonthPresenter implements ShowListDaysClocksContract.Presenter,
        EntriesProvider.Listener,
        SelectedMonthProvider.Listener
    {
        private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksMonthPresenter.class.getName());
        private PresenterBase parent = null;
        private SelectedMonthProvider selectedMonth = null;
        private EntriesProvider entries = null;
        private ShowListDaysClocksContract.View view = null;

        public ShowListDaysClocksMonthPresenter(@NonNull ShowListDaysClocksContract.View view){
            LOGGER.info("created");
            this.view = view;
        }
        private String convertToPrefix(Calendar date){
            DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
            String showBelongingMonthPrefix = df.format(date.getTime());
            return showBelongingMonthPrefix;
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
        public void setEntriesProvider(EntriesProvider i){
            LOGGER.info("setEntriesProviderProvider");
            entries = i;
            entries.subscribe(this);
        }

        // This is fill with DependencyInjectorBinding
        public void setSelectedMonthProvider(SelectedMonthProvider i){
            selectedMonth = i;
            selectedMonth.subscribe(this);
        }

        @Override
        public List<Provider> getBindings() {
            return parent.getBindings();
        }

        @Override
        public void setParent(PresenterBase parent) {
            this.parent = parent;
        }

        @Override
        public void onAttachChild(PresenterBase child) {
            // A dont add childs
        }

        @Override
        public void startUi() {
            onChangeEntries();
        }
    }
