package org.jesteban.clockomatic.fragments.registerpage;


import android.content.Context;
import android.support.annotation.NonNull;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.PresenterBase;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.model.WorkScheduleContract;
import org.jesteban.clockomatic.model.WorkScheduleDb;
import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.providers.UndoProviderContract;
import org.jesteban.clockomatic.providers.WorkScheduleProviderContract;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChooseWorkSchedulePresenter implements  ChooseWorkScheduleContract.Presenter,
        WorkScheduleProviderContract.Listener{
    private static final Logger LOGGER = Logger.getLogger(ChooseWorkSchedulePresenter.class.getName());

    private PresenterBase  parent = null;
    private ChooseWorkScheduleContract.View view = null;
    private Context context = null;
    private WorkScheduleProviderContract workScheduleProvider = null;
    private UndoProviderContract undoProvider = null;

    List<WorkScheduleDb.WorkScheduleData> showingData = null;

    ChooseWorkSchedulePresenter(ChooseWorkScheduleContract.View view, Context context){
        this.view = view;
        this.context = context;
    }
    @Override
    public List<Provider> getBindings() {
        List<Provider> list = parent.getBindings();
        return list;
    }

    @Override
    public void setParent(PresenterBase parent) {
        this.parent = parent;
    }

    @Override
    public void onAttachChild(PresenterBase child) {
        //Nothing to do
    }

    @Override
    public void startUi() {
        onChangeWorkScheduler();
    }

    @Override
    public void onSelectedWorkSchedule(String name, int index) {
        LOGGER.info(String.format("onSelectedWorkSchedule name=%s index=%d", name, index));

        if (index==0){
            // Delete
            UndoAction undo = workScheduleProvider.clearWorkSchedule();
            if (undo != null) {
                undo.description = context.getResources().getString(R.string.msg_undo_clear_ws);
                undoProvider.push(undo);
            }
        } else {
            WorkScheduleDb.WorkScheduleData ws = showingData.get(index - 1);
            UndoAction undo = workScheduleProvider.setWorkSchedule(ws.getId());
            if (undo != null) {
                undo.description = context.getResources().getString(R.string.msg_undo_set_ws);
                undoProvider.push(undo);
            }
        }
    }
    public void setUndoProvider(@NonNull UndoProviderContract i){
        undoProvider = i;
        //entries.subscribe(this);
    }
    public void setWorkScheduleProvider(@NonNull WorkScheduleProviderContract i){
        workScheduleProvider = i;
        workScheduleProvider.subscribe(this);
    }
    private int getIndexForWs(int wsId){
        int idx=0;
        for (WorkScheduleDb.WorkScheduleData data: showingData){
            if (data.getId()==wsId) return idx;
            idx++;
        }
        return -1;
    }
    @Override
    public void onChangeWorkScheduler() {
        List<String> names = new ArrayList<>();
        names.add("---");
        showingData = workScheduleProvider.getAvailableWorkSchedule();
        for (WorkScheduleDb.WorkScheduleData ws : showingData){
            names.add(ws.getName());
        }

        view.setAvailableWorkSchedule(names);
        view.setVisibleWorkSchedule(true);
        WorkScheduleContract ws = workScheduleProvider.getWorkSchedule();
        if (ws!=null) {
            view.SetSelectedWorkSchedule(getIndexForWs(ws.getId())+1);
        } else view.SetSelectedWorkSchedule(0);
    }
}
