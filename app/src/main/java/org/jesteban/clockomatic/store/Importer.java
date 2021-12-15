package org.jesteban.clockomatic.store;


import org.jesteban.clockomatic.providers.EntriesProviderContract;

public interface Importer {
    boolean thereAreData();
    boolean importAllDataTo(EntriesProviderContract state);
}
