package org.jesteban.clockomatic.store.serializers;

import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.Serializer;

import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.logging.Logger;

/**
 * This class implement a read/write for CSV format
 */

public class SerializerEntrySetCsv implements Serializer<EntrySet> {
    private static final Logger LOGGER = Logger.getLogger(SerializerEntrySetCsv.class.getName());

    private static final String VERSION_SIGNATURE = "A";
    private static final Character FIELD_SEPARATOR = ';';
    private static final Character NEXT_REGISTER_SEPARATOR = '\n';

    private static final String FIELD_SEPARATOR_SPLITER = ";";
    private static final String REGISTER_SEPARATOR_SPLITER = "\\r\\n|\\n|\\r";

    protected String serializeEntry(Entry entry){
        return VERSION_SIGNATURE + FIELD_SEPARATOR +
                entry.getBelongingDay() + FIELD_SEPARATOR +
                Long.toString(entry.getRegisterDate().getTime().getTime()) + FIELD_SEPARATOR +
                entry.getRegisterDate().getTime() + FIELD_SEPARATOR +
                NEXT_REGISTER_SEPARATOR;
    }

    protected Entry deserializeEntry(String data) throws Exception {
        String[] fields = data.split(FIELD_SEPARATOR_SPLITER);
        if (fields.length<2){
            LOGGER.warning("Error deserializing because not enough fields : " + fields);
            return null;
        }
        switch (fields[0]) {
            case VERSION_SIGNATURE:
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(Long.parseLong(fields[2])) );
                return new Entry(cal, fields[1]);
            default:
                LOGGER.warning("Error deserializing because version is " + fields[0]+ " expected " + VERSION_SIGNATURE);
                throw new InputMismatchException("Unknown data format " + fields[0]);
        }
    }


    @Override
    public String serialize(EntrySet entries){
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : entries){
            stringBuilder.append(serializeEntry(entry));
        }
        return stringBuilder.toString();
    }
    @Override
    public void deserialize(String data, EntrySet destination) throws Exception {
        LOGGER.info("deserializeLine");
        EntrySet result = destination;
        // https://stackoverflow.com/questions/13464954/how-do-i-split-a-string-by-line-break
        String lines[] = data.split(REGISTER_SEPARATOR_SPLITER);
        for (String line : lines){
            if (line.length()>0) {
                try {
                    Entry entry = deserializeEntry(line);
                    result.addEntry(entry);
                } catch (Exception e){
                    LOGGER.warning("Something wrong with: [" + line + "] " + e);
                    throw e;
                }
            }
        }
    }


}
