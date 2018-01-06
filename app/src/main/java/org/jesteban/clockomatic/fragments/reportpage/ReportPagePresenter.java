package org.jesteban.clockomatic.fragments.reportpage;

import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.bindings.SelectedMonthProvider;
import org.jesteban.clockomatic.bindings.SelectedMonthProviderImpl;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.model.Entry;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Presenter mix
 */
public class ReportPagePresenter implements ReportPageContract.Presenter, EntriesProvider.Listener {
    private static final Logger LOGGER = Logger.getLogger(ReportPagePresenter.class.getName());

    private PresenterBase parent;
    private EntriesProvider entries;
    private SelectedMonthProvider selectedMonth = null;

    private ReportPageContract.View view = null;

    public ReportPagePresenter( @NonNull ReportPageContract.View view){
        this.view = view;
        this.parent = null;
    }

    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProvider i){
        LOGGER.info("setEntriesProvider");
        entries = i;
        entries.subscribe(this);
    }

    // This is fill with DependencyInjectorBinding
    public void setSelectedMonthProvider(@NonNull SelectedMonthProvider i){
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
