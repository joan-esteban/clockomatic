package org.jesteban.clockomatic.store;

import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.containers.ContainerFile;
import org.jesteban.clockomatic.store.serializers.SerializerCsv;

import java.io.File;
import java.util.logging.Logger;

/**
 * This class keep data on Filesystem
 */

public class StoreOnFiles {
    private static final Logger LOGGER = Logger.getLogger(StoreOnFiles.class.getName());
    private String storeBaseDir = "/sdcard/clockomatic/";
    private String fullFilename = storeBaseDir + "/entries.csv";
    ////////// CONSTRUCTORS ////////////////////////
    public StoreOnFiles(){
        createFolders();
    }

    public StoreOnFiles(String baseDir){
        storeBaseDir = baseDir;
        createFolders();
    }

    private void createFolders(){
        File file = new File(storeBaseDir );
        LOGGER.info("Creating missing folders for  "+storeBaseDir);
        if (!file.mkdirs()){
            LOGGER.warning("Cant create folder " + storeBaseDir);
        };
    }



    public void write(EntrySet entries) throws  Exception{
        Container container = new ContainerFile(fullFilename);
        Serializer serializer = new SerializerCsv();
        container.put(serializer.serialize(entries));
    }

    public EntrySet read() throws  Exception{
        Container container = new ContainerFile(fullFilename);
        Serializer serializer = new SerializerCsv();
        String data = container.get();
        return serializer.deserialize(data);
    }

    public void wipe(){
        Container container = new ContainerFile(fullFilename);
        container.wipe();
    }

}
