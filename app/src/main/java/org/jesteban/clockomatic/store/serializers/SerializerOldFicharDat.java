package org.jesteban.clockomatic.store.serializers;


import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.Serializer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class SerializerOldFicharDat implements Serializer {
    @Override
    public String serialize(EntrySet entries) throws Exception {
        return null;
    }

    @Override
    public EntrySet deserialize(String lines) throws Exception {
        EntrySet result = new EntrySet();
        // https://stackoverflow.com/questions/13464954/how-do-i-split-a-string-by-line-break
        String lstLines[] = lines.split(REGISTER_SEPARATOR_SPLITER);
        for (String line : lstLines){
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
        return result;


    }

    protected Entry deserializeEntry(String line) throws Exception {
        String[] fields = line.split(FIELD_SEPARATOR_SPLITER);
        if (fields.length>4) {
            String strDate = fields[1];
            int hour = Integer.parseInt(fields[2]);
            int minute=Integer.parseInt(fields[3]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strDate + " " +fields[2] + ":" + fields[3] + ":00" ));
            //Date date = new Date(Long.parseLong(fields[4]));
            //cal.setTime(date);
            return new Entry(cal);
        };
        return null;
    }

    private static final Logger LOGGER = Logger.getLogger(SerializerOldFicharDat.class.getName());
    private static final String FIELD_SEPARATOR_SPLITER = "\\|";
    private static final String REGISTER_SEPARATOR_SPLITER = "\\r\\n|\\n|\\r";
}
