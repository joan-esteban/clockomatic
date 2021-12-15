package org.jesteban.clockomatic.activities.mainactivity;

import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.helpers.ViewBase;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;

import java.util.List;


public class MainActivityContract {
    public interface View extends ViewBase<Presenter> {
        void showMessage(String text);

        void showUndoMessage(final UndoAction undoAction);

        void showPage(ShowPageProviderContract.PageId id);

        void askConfirmWipeData();

        void showAbout();

        void showSettings();

        void showCompaniesEditor();

        void setAvailableCompanies(List<String> companies);

        void setVisibleAvailableCompanies(Boolean visible);

        void setSelectedCompany(int index, int color);
    }

    public interface Presenter extends PresenterBase {

        void wipeStore();

        void onViewChangeShowPage(ShowPageProviderContract.PageId id);

        void onSelectedMenuItem(int id);

        // if name = null -> index = -1 nothing selected
        void onSelectedCompany(String name, int index);

        void onFinishCompaniesEditor(Boolean isOk);
    }
}
