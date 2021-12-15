package org.jesteban.clockomatic.fragments.showlistdaysclocks;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayView;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.providers.SelectedDayProviderContract;
import org.jesteban.clockomatic.providers.SelectedMonthProviderContract;
import org.jesteban.clockomatic.helpers.InfoDayEntry;
import org.jesteban.clockomatic.helpers.PresenterBasicProviderEntriesReady;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewContract;
import org.jesteban.clockomatic.fragments.infodayvieew.InfoDayViewPresenterNoProviderLink;
import org.jesteban.clockomatic.providers.WorkScheduleProviderContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ShowListDaysClocksMonthPresenter extends PresenterBasicProviderEntriesReady<ShowListDaysClocksContract.View>
        implements ShowListDaysClocksContract.Presenter,
        SelectedMonthProviderContract.Listener {
    private static final Logger LOGGER = Logger.getLogger(ShowListDaysClocksMonthPresenter.class.getName());
    private SelectedMonthProviderContract selectedMonth = null;
    private SelectedDayProviderContract selectedDay = null;
    private ShowPageProviderContract showPage = null;
    private WorkScheduleProviderContract workScheduleProvider = null;

    private Set<String> showingDays=null;
    public  class DataPerDay{
        public List<InfoDayEntry> infoDays;
        Entry.BelongingDay belongingDay;
        int indexInfoDayWorkScheduler;
        public InfoDayEntry getInfoDayWorkScheduler(){
            if (indexInfoDayWorkScheduler>=0) return infoDays.get(indexInfoDayWorkScheduler);
            return null;
        }
    }
    private Map<Integer,DataPerDay> showingInfoDays = new HashMap<>();

    public ShowListDaysClocksMonthPresenter(@NonNull ShowListDaysClocksContract.View view, Context context) {
        super(view);
    }

    @Override
    public void onChangeSelectedMonth() {
        LOGGER.info("onChangeSelectedMonth");
        onChangeEntries();
    }
    private InfoDayEntry getInfoDayEntryForWorkSchedule(Entry.BelongingDay belongingDay, InfoDayEntry infoDay){
        workScheduleProvider.setBelongingDay(belongingDay);
        WorkScheduleContract ws = workScheduleProvider.getWorkSchedule();
        if (ws==null) return null;
        InfoDayEntry res =  ws.getInfoDay(infoDay);
        res.setGeneratedWorkSchedule(ws);
        return res;
    }
    @Override
    public void onChangeEntries() {
        try {
            EntrySet entries = this.entries.getEntriesBelongingMonth(selectedMonth.getSelectedMonth());

            showingDays = entries.getDistintBelongingDays();
            showingInfoDays.clear();

            //view.setCountInfoDay(showingDays.size());
            int idx=0;
            for (final String day : showingDays) {
                DataPerDay entryInfo = new DataPerDay();
                entryInfo.belongingDay = new Entry.BelongingDay(day);

                InfoDayEntry infoDayEntry = new InfoDayEntry(entries.getEntriesBelongingDayStartWith(day), day);

                entryInfo.infoDays = new ArrayList<>();
                InfoDayEntry infoDayForWs = getInfoDayEntryForWorkSchedule(entryInfo.belongingDay, infoDayEntry);
                entryInfo.indexInfoDayWorkScheduler = -1;
                if (infoDayForWs!=null){
                    entryInfo.infoDays.add(infoDayForWs);
                    entryInfo.indexInfoDayWorkScheduler = 0;
                }
                entryInfo.infoDays.add(infoDayEntry);
                showingInfoDays.put(idx,entryInfo);
                idx++;
            }

        } catch (Exception e){
            //TODO: OnError???
        }
        view.getInfoPeriodViewPresenter().showData(showingInfoDays);
        view.update();
    }

    // This is fill with DependencyInjectorBinding
    public void setSelectedMonthProvider(SelectedMonthProviderContract i) {
        selectedMonth = i;
        selectedMonth.subscribe(this);
    }
    // This is fill with DependencyInjectorBinding
    public void setSelectedDayProvider(SelectedDayProviderContract i) {
        selectedDay = i;
        //selectedDay.subscribe(this);
    }
    public void setShowPageProvider(ShowPageProviderContract i) {
        showPage = i;
    }

    public void setWorkScheduleProvider(@NonNull WorkScheduleProviderContract i){
        workScheduleProvider = i;
        //workScheduleProvider.subscribe(this);
    }

    @Override
    public void startUi() {
        onChangeEntries();
    }

    @Override
    public void clickOnDay(Entry.BelongingDay day) {
        this.selectedDay.setSelecteDay(day.getBelongingDayDate());
        this.showPage.setShowPage(ShowPageProviderContract.PageId.REGISTER_PAGE);
    }

    @Override
    public int getItemCount() {
        if (showingDays!=null) return showingDays.size(); else return 0;
    }

    @Override
    public void updateInfoDay(int position, InfoDayViewContract.Presenter presenter) {
        final DataPerDay dataPerDay = showingInfoDays.get(position);
        if (dataPerDay!=null) {
            presenter.showInfoDay(dataPerDay.infoDays, dataPerDay.indexInfoDayWorkScheduler);

            presenter.setOnClickOnCalendar(new InfoDayViewContract.Presenter.OnAction() {
                @Override
                public void execute() {
                    selectedDay.setSelecteDay(dataPerDay.belongingDay.getBelongingDayDate());
                    showPage.setShowPage(ShowPageProviderContract.PageId.REGISTER_PAGE);
                }
            });
        }
    }
}
