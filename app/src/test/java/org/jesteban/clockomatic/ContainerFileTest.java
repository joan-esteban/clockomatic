package org.jesteban.clockomatic;

import org.jesteban.clockomatic.store.containers.ContainerFile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ContainerFileTest {
    private final String  filename = "test.txt";
    private final String  dataExample = "this is my data";

    @Test
    public void write_file() throws Exception {
        ContainerFile cf = new ContainerFile(filename);
        cf.put(dataExample);
    }

    @Test(expected = Exception.class)
    public void error_reading_non_existing_file() throws Exception {
        ContainerFile cf = new ContainerFile("test2.txt");
        cf.get();
    }

    @Test
    public void read_written_data() throws Exception {
        ContainerFile fileToWrite = new ContainerFile(filename);
        fileToWrite.put(dataExample);

        ContainerFile fileToRead = new ContainerFile(filename);
        String data = fileToRead.get();

        assertEquals(dataExample, data);

    }

    @Test(expected = Exception.class)
    public void wipe_file() throws Exception {
        ContainerFile fileToWrite = new ContainerFile(filename);
        fileToWrite.put(dataExample);
        fileToWrite.wipe();
        ContainerFile fileToRead = new ContainerFile(filename);
        // Read from non existing file throw excepction
        String data = fileToRead.get();
    }

    @Test
    public void dont_append_overwrite() throws Exception {
        ContainerFile fileToWrite = new ContainerFile(filename);
        fileToWrite.wipe();
        fileToWrite.put(dataExample);
        fileToWrite.put(dataExample);
        ContainerFile fileToRead = new ContainerFile(filename);
        String data = fileToRead.get();
        assertEquals(dataExample, data);
    }

}
