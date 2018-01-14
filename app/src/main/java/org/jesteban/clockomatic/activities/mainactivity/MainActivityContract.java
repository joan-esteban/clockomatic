package org.jesteban.clockomatic.activities.mainactivity;

import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.controllers.ViewBase;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;


public class MainActivityContract {
    public interface View extends ViewBase<Presenter> {
        void showMessage(String text);
        //void showRegisterPage();
        //void showReportPage();
        //void showDebugPage();
        void showPage(ShowPageProviderContract.PageId id);
        void askConfirmWipeData();
        void showAbout();
        void showSettings();
    }

    public interface Presenter extends PresenterBase {
        StateController getStateController();
        void wipeStore();
        void OnViewChangeShowPage(ShowPageProviderContract.PageId id);
        void onSelectedMenuItem(int id);
    }
}
