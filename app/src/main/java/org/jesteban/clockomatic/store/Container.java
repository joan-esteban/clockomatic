package org.jesteban.clockomatic.store;

public interface Container {
    void put(String data) throws Exception;
    String get() throws Exception;
    void wipe();
}
