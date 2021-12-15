package org.jesteban.clockomatic.helpers;

import android.support.annotation.NonNull;

import org.jesteban.clockomatic.providers.EntriesProviderContract;


public abstract class PresenterBasicProviderEntriesReady<VIEW_CLASS> extends PresenterBasic<VIEW_CLASS> implements EntriesProviderContract.Listener {
    protected EntriesProviderContract entries = null;

    public PresenterBasicProviderEntriesReady(@NonNull VIEW_CLASS view) {
        super(view);
    }


    // This is fill with DependencyInjectorBinding
    public void setEntriesProvider(@NonNull EntriesProviderContract i) {
        entries = i;
        entries.subscribe(this);
    }


}
