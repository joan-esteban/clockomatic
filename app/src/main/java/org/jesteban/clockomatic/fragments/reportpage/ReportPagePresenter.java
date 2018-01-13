package org.jesteban.clockomatic.fragments.reportpage;

import android.support.annotation.NonNull;

import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedMonthProviderContract;
import org.jesteban.clockomatic.controllers.PresenterBase;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Presenter mix
 */
public class ReportPagePresenter implements ReportPageContract.Presenter, EntriesProviderContract.Listener {
    private static final Logger LOGGER = Logger.getLogger(ReportPagePresenter.class.getName());

    private PresenterBase parent;
    private EntriesProviderContract entries;
    private SelectedMonthProviderContract selectedMonth = null;

    private ReportPageContract.View view = null;

    public ReportPagePresenter( @NonNull ReportPageContract.View view){
        this.view = view;
        this.parent = null;
    }

    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProviderContract i){
        LOGGER.info("setEntriesProvider");
        entries = i;
        entries.subscribe(this);
    }

    // This is fill with DependencyInjectorBinding
    public void setSelectedMonthProvider(@NonNull SelectedMonthProviderContract i){
        LOGGER.info("setSelectedMonthProvider");
        selectedMonth = i;
        //selectedMonth.subscribe(this);
    }

    @Override
    public void onChangeEntries() {

    }

    @Override
    public void selectedMonth(Calendar day) {
        selectedMonth.setSelectedMonth(day);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/");
        //String dayPrefix = sdf.format(day.getTime());
        view.showSelectedMonth(day);
        //viewClocksMonthFragment.setShowBelongingMonthPrefix(dayPrefix);
        //presenter.setMonthReport(day);

    }




    @Override
    public List<Provider> getBindings() {
        List<Provider> list = parent.getBindings();
        list.add(selectedMonth);
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {
    }

    @Override
    public void startUi() {
        view.showSelectedMonth(selectedMonth.getSelectedMonth());
    }
}
