package org.jesteban.clockomatic.store;

import org.jesteban.clockomatic.model.EntrySet;


public interface  Serializer {
    /**
     * Serialize a set of entries
     * @param entries
     * @return a representative string
     * @throws Exception
     */
    String serialize(EntrySet entries) throws Exception;
    EntrySet deserialize(String entry) throws Exception;

}
