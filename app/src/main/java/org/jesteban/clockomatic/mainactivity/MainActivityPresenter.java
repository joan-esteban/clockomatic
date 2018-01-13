package org.jesteban.clockomatic.mainactivity;

import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.BuildConfig;
import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.providers.EntriesProvider;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.providers.SelectedDayProvider;
import org.jesteban.clockomatic.providers.SelectedMonthProviderContract;
import org.jesteban.clockomatic.providers.SelectedMonthProvider;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.helpers.DependencyInjectorBinding;
import org.jesteban.clockomatic.providers.ShowPageProvider;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivityPresenter implements MainActivityContract.Presenter, SelectedDayProviderContract.Listener,
                ShowPageProviderContract.Listener{
    private static final Logger LOGGER = Logger.getLogger(MainActivityPresenter.class.getName());
    private StateController stateController = new StateController();
    private EntriesProviderContract entriesProvider = new EntriesProvider(stateController);
    private SelectedDayProviderContract selectedDay = new SelectedDayProvider();
    private SelectedMonthProviderContract selectedMonth = new SelectedMonthProvider();
    private ShowPageProviderContract showPage = new ShowPageProvider();

    private MainActivityContract.View view = null;
    private Context context = null;
    public MainActivityPresenter(@NonNull MainActivityContract.View view, Context context){
        this.view = view;
        this.context = context;
        selectedDay.subscribe(this);
        showPage.subscribe(this);
    }

    @Override
    public List<Provider> getBindings() {
        List<Provider> list = new ArrayList<>();
        list.add(entriesProvider);
        list.add(selectedDay);
        list.add(selectedMonth);
        list.add(showPage);
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {

    }

    @Override
    public void onAttachChild(PresenterBase child) {
        child.setParent(this);
        DependencyInjectorBinding dib =new DependencyInjectorBinding();
        dib.inject(child, getBindings());
    }

    @Override
    public void startUi() {
        this.showPage.setShowPage(ShowPageProviderContract.PageId.REPORT_PAGE);
        if (BuildConfig.DEBUG){

        }
    }


    @Override
    public StateController getStateController(){
        return stateController;
    }

    @Override
    public void wipeStore() {
        stateController.wipeStore();
    }

    @Override
    public void OnViewChangeShowPage(ShowPageProviderContract.PageId id) {
        this.showPage.setShowPage(id);
    }

    @Override
    public void onSelectedMenuItem(int id) {
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_wipe_data:
                LOGGER.info("main Menu selected action_wipe_data");
                view.askConfirmWipeData();
                break;
            case R.id.action_about:
                LOGGER.info("main Menu selected action_about");
                view.showAbout();
                break;
            case R.id.action_debug:
                LOGGER.info("main Menu selected action_debug");
                view.showPage(ShowPageProviderContract.PageId.DEBUG_PAGE);
                break;
            default:
                LOGGER.info("unknown menu item selected");
        }
    }

    @Override
    public void onChangeSelectedDay() {
        view.showMessage("onChangeSelectedDay " + selectedDay.getFilterBelongingForDay());
        //view.showRegisterPage();
    }

    @Override
    public void onChangeShowPage() {
        view.showMessage("onChangeShowPage " );
        view.showPage(showPage.getShowPage());
    }
}
