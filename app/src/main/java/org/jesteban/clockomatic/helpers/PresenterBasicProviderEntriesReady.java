package org.jesteban.clockomatic.helpers;

import android.support.annotation.NonNull;

import org.jesteban.clockomatic.bindings.EntriesProvider;
import org.jesteban.clockomatic.bindings.Provider;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.fragments.registerpage.editclocksday.EditClockDayContract;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageContract;
import org.jesteban.clockomatic.model.Entry;

import java.util.List;


public abstract class PresenterBasicProviderEntriesReady<VIEW_CLASS> extends PresenterBasic<VIEW_CLASS> implements EntriesProvider.Listener {
    protected EntriesProvider entries = null;

    public PresenterBasicProviderEntriesReady(@NonNull VIEW_CLASS view) {
        super(view);
    }


    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProvider i){
        entries = i;
        entries.subscribe(this);
    }


}
