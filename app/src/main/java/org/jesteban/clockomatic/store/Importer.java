package org.jesteban.clockomatic.store;


import org.jesteban.clockomatic.model.State;

public interface Importer {
    boolean thereAreData();
    boolean importAllDataTo(State state);
}
