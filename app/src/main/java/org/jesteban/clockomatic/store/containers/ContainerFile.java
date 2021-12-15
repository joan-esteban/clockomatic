package org.jesteban.clockomatic.store.containers;


import org.jesteban.clockomatic.store.StoreContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContainerFile implements StoreContract<ContainerFile.StringHolder> {
    private static final Logger LOGGER = Logger.getLogger(ContainerFile.class.getName());
    private String filename;

    public ContainerFile(String filename) {
        this.filename = filename;
    }

    @Override
    public void read(StringHolder destination) throws Exception {
        LOGGER.log(Level.INFO, String.format("getting data %s", filename));
        File file = new File(this.filename);
        if (!file.exists()) {
            LOGGER.log(Level.INFO, String.format("StoreEntries %s doesnt exists yet", this.filename));
            throw new FileNotFoundException("missing file " + this.filename);
        }
        // https://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int readBytes = fis.read(data);
            LOGGER.log(Level.INFO, String.format("read %s", readBytes));
            assert (readBytes == file.length());
            fis.close();
            LOGGER.log(Level.INFO, String.format("read data: %s", this.filename));
            String res = new String(data, "UTF-8");
            LOGGER.log(Level.INFO, String.format("read data: %s", res));
            destination.setString(res);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, String.format("error while reading  from: %s exception:[%s]", this.filename, e));
            throw e;
        } finally {
            if (fis != null) fis.close();
        }

    }

    @Override
    public void write(StringHolder data) throws Exception {
        LOGGER.log(Level.INFO, String.format("Writing data: %s", this.filename));
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename, false);
            fileWriter.write(data.getString());
        } catch (Exception e) {
            LOGGER.warning("Error writting file " + filename + " : " + e);
            throw e;
        } finally {
            if (fileWriter != null) {
                fileWriter.flush();
                fileWriter.close();
            }
        }
    }

    @Override
    public void wipe() {
        LOGGER.log(Level.INFO, String.format("(wipe) Deleteing file: %s", this.filename));
        File file = new File(this.filename);
        if (!file.delete()) {
            LOGGER.log(Level.SEVERE, String.format("Error deleting file %s", this.filename));
        }
    }

    public static class StringHolder {
        public String data = "";

        public StringHolder() {
        }

        public StringHolder(String str) {
            data = str;
        }

        public String getString() {
            return data;
        }

        public void setString(String data) {
            this.data = data;
        }

        public String toString() {
            return data;
        }
    }
}
