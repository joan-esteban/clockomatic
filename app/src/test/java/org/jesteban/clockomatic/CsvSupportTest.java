package org.jesteban.clockomatic;

import org.jesteban.clockomatic.helpers.CsvSupport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CsvSupportTest {
    CsvSupport csv = new CsvSupport();
    @Test
    public void testSerailizeNullMustReturnNothing(){
        assertEquals("", csv.serializeLine(null));
    }
    @Test
    public void testSerailizeEmptyListMustReturnNothing(){
        String [] fields= {};
        assertEquals("", csv.serializeLine(fields));
    }
    @Test
    public void testSerailizeOneItemList(){
        String [] fields= {"field1"};
        assertEquals("field1;", csv.serializeLine(fields));
    }

    @Test
    public void testSerailizeTwoItemListMustReturnNoFieldSeparator(){
        String [] fields= {"field1","f2"};
        assertEquals("field1;f2;", csv.serializeLine(fields));
    }
    @Test
    public void testSerailizeFieldsWithEmptyString(){
        String [] fields= {"field1","f2",""};
        assertEquals("field1;f2;;", csv.serializeLine(fields));
    }

    @Test
    public void testSerailizeFieldsWithSemicolon(){
        String [] fields= {"field1","f;2",null};
        assertEquals("field1;f\\;2;;", csv.serializeLine(fields));
    }

    @Test
    public void testUnspaceStringWithoutScapes(){
        assertEquals("hola", csv.unescape("hola"));
    }
    @Test
    public void testUnspaceStringWithScapes(){
        assertEquals("hol;a", csv.unescape("hol\\;a"));
    }
    @Test
    public void testUnserailize(){
        String [] fields =  csv.deserializeLine("f1;f2;f3;");
        assertEquals(4, fields.length);
    }
    void printStrings(String[] fields){
        for (String f : fields) {
            System.out.println(f);
        }
    }
    @Test
    public void testUnserailizeWithScapes(){
        String [] fields =  csv.deserializeLine("f1;f\\;2;f3;");
        String [] expected = {"f1","f;2", "f3"};

        assertEquals("f;2", fields[1]);
    }

    @Test
    public void testDesearilizeEmptyFile(){
        List<String []>  result = csv.deserialize("");
        assertEquals(0,result.size());
    }
    @Test
    public void testDesearilizeMultiplesLinesNotEndingCr(){
        List<String []>  result = csv.deserialize("f1;f\\;2;f3;\nf4;f5;f6");
        assertEquals(2,result.size());
        assertEquals("f3", result.get(0)[2]);
        assertEquals("f6", result.get(1)[2]);
    }

    @Test
    public void testSearilizeMultiplesLines(){
        List<String[]> data = new ArrayList<>();
        data.add( new String []{"1","2"});
        data.add( new String []{"3","4"});
        String ser = csv.serialize(data);
        assertEquals("1;2;\n3;4;\n", ser);

    }

    @Test
    public void testDesearilizeLineWithBlanks(){
        String [] result = csv.deserializeLine("I1;1;a;;");
        printStrings(result);
        assertEquals("I1", result[0]);
        assertEquals("1", result[1]);
        assertEquals("a", result[2]);
        assertEquals("", result[3]);
    }
}
