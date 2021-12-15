package org.jesteban.clockomatic.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * This class give support to process Csv files
 */

public class CsvSupport {
    private static final Character FIELD_SEPARATOR = ';';
    private static final Character NEXT_REGISTER_SEPARATOR = '\n';

    private static final String FIELD_SEPARATOR_SPLITER = "(?<!\\\\);";
    private static final String REGISTER_SEPARATOR_SPLITER = "\\r\\n|\\n|\\r";

    public String escape(String data){
        return data.replace(""+ FIELD_SEPARATOR, "\\" + FIELD_SEPARATOR);
    }
    public String unescape(String data){
        return data.replace("\\" + FIELD_SEPARATOR, ""+ FIELD_SEPARATOR);
    }
    public String serializeLine(String [] data){
        if (data==null) return "";
        if (data.length==0) return "";
        StringBuilder result =new StringBuilder();
        for (String field : data){
            if (field!=null) {
                result.append(escape(field));
            }
            result.append(FIELD_SEPARATOR);
        }
        return result.toString();
    }

    public String [] deserializeLine(String data){
        if (data ==null || data.equals("")) return null;
        // To avoid delete empty string set limit to -1
        // https://stackoverflow.com/questions/14602062/java-string-split-removed-empty-values
        String [] fields =  data.split(FIELD_SEPARATOR_SPLITER,-1);
        String [] result = new String [fields.length];
        int idx=0;
        for (String f : fields) {
            result[idx]=unescape(f);
            idx++;
        }
        return result;
    }
    public String serialize(List<String []> data){
        StringBuilder result = new StringBuilder();
        for (String [] fields: data){
            String line = serializeLine(fields);
            if (line.length()>0){
                result.append(line);
                result.append(NEXT_REGISTER_SEPARATOR);
            }
        }
        return result.toString();
    }
    public List<String []> deserialize(String data){
        List<String[]> result = new ArrayList<>();
        if (data ==null || data.equals("")) return result;
        String [] lines = data.split(REGISTER_SEPARATOR_SPLITER);

        for (String line : lines){
            String [] fields = deserializeLine(line);
            if (fields!=null && fields.length>0){
                result.add(fields);
            }
        }
        return result;
    }


}
