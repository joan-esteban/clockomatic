package org.jesteban.clockomatic.providers;


import java.util.Calendar;

public interface  ShowPageProviderContract extends Provider{
    public static final String KEY_PROVIDER ="showPage";
    public static enum PageId{
        REGISTER_PAGE,
        REPORT_PAGE,
        DEBUG_PAGE
    }
    public boolean setShowPage(PageId page);
    public PageId getShowPage();

    void subscribe(Listener listener);

    public interface Listener {
        void onChangeShowPage();
    }
}
