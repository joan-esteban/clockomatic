package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.helpers.ObservableDispatcher;

import java.util.Calendar;

public class ShowPageProvider implements ShowPageProviderContract {
    private ObservableDispatcher<ShowPageProvider.Listener> observable = new ObservableDispatcher<>();
    private PageId showPage = PageId.REGISTER_PAGE;

    @Override
    public String getName() {
        return ShowPageProviderContract.KEY_PROVIDER;
    }

    @Override
    public boolean setShowPage(PageId page) {
        if (page == showPage) return false;
        showPage = page;
        observable.notify("onChangeShowPage");
        return true;
    }

    @Override
    public PageId getShowPage() {
        return showPage;
    }

    @Override
    public void subscribe(Listener listener)  {
        observable.add(listener);
    }
}
