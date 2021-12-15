package org.jesteban.clockomatic.store;

import org.jesteban.clockomatic.model.EntrySet;


public interface  Serializer<OBJECT> {
    /**
     * Serialize a set of entries
     * @param entries
     * @return a representative string
     * @throws Exception
     */
    String serialize(OBJECT entries) throws Exception;
    void deserialize(String entry, OBJECT destination) throws Exception;

}
