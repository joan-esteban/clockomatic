package org.jesteban.clockomatic;

import org.jesteban.clockomatic.store.containers.ContainerFile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ContainerFileTest {
    private final String  filename = "test.txt";
    private final ContainerFile.StringHolder  dataExample =new ContainerFile.StringHolder("this is my data");
    private final ContainerFile.StringHolder data = new ContainerFile.StringHolder();
    @Test
    public void write_file() throws Exception {
        ContainerFile cf = new ContainerFile(filename);
        cf.write(dataExample);
    }

    @Test(expected = Exception.class)
    public void error_reading_non_existing_file() throws Exception {
        ContainerFile cf = new ContainerFile("test2.txt");

        cf.read(data);
    }

    @Test
    public void read_written_data() throws Exception {
        ContainerFile fileToWrite = new ContainerFile(filename);
        fileToWrite.write(dataExample);
        ContainerFile fileToRead = new ContainerFile(filename);
        fileToRead.read(data);

        assertEquals(dataExample.toString(), data.toString());

    }

    @Test(expected = Exception.class)
    public void wipe_file() throws Exception {
        ContainerFile fileToWrite = new ContainerFile(filename);
        fileToWrite.write(dataExample);
        fileToWrite.wipe();
        ContainerFile fileToRead = new ContainerFile(filename);
        // Read from non existing file throw excepction
        fileToRead.read(data);
    }

    @Test
    public void append_overwrite() throws Exception {
        ContainerFile fileToWrite = new ContainerFile(filename);
        fileToWrite.wipe();
        fileToWrite.write(dataExample);
        fileToWrite.write(dataExample);
        ContainerFile fileToRead = new ContainerFile(filename);
        fileToRead.read(data);
        assertEquals(dataExample.toString(), data.toString());
    }

}
