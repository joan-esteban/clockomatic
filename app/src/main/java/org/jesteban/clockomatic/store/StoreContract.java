package org.jesteban.clockomatic.store;



public interface StoreContract<STORED_OBJECT> {
    void write(STORED_OBJECT data) throws  Exception;
    void read(STORED_OBJECT destination) throws  Exception;
    void wipe();
}
