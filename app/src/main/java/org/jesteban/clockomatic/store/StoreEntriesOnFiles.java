package org.jesteban.clockomatic.store;

import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.store.containers.ContainerFile;
import org.jesteban.clockomatic.store.serializers.SerializerEntrySetCsv;

import java.io.File;
import java.util.logging.Logger;

/**
 * This class keep data on Filesystem
 */

public class StoreEntriesOnFiles implements StoreContract<EntrySet> {
    private static final Logger LOGGER = Logger.getLogger(StoreEntriesOnFiles.class.getName());
    private String storeBaseDir = "/sdcard/clockomatic/";
    private String fullFilename = storeBaseDir + "/entries.csv";
    ////////// CONSTRUCTORS ////////////////////////
    public StoreEntriesOnFiles(){
        createFolders();
    }

    public StoreEntriesOnFiles(String baseDir){
        storeBaseDir = baseDir;
        fullFilename = storeBaseDir + "/entries.csv";
        createFolders();
    }

    private void createFolders(){
        File file = new File(storeBaseDir );
        LOGGER.info("Creating missing folders for  "+storeBaseDir);
        if (!file.mkdirs()){
            LOGGER.warning("Cant create folder " + storeBaseDir);
        }
    }


    @Override
    public void write(EntrySet entries) throws  Exception{
        StoreContract<ContainerFile.StringHolder> container = new ContainerFile(fullFilename);
        Serializer serializer = new SerializerEntrySetCsv();
        container.write(new ContainerFile.StringHolder(serializer.serialize(entries)));
    }
    @Override
    public void read(EntrySet destination) throws  Exception{
        StoreContract<ContainerFile.StringHolder> container = new ContainerFile(fullFilename);
        Serializer<EntrySet> serializer = new SerializerEntrySetCsv();

        ContainerFile.StringHolder data = new ContainerFile.StringHolder();
        container.read(data);
        serializer.deserialize(data.getString(),destination);
    }
    @Override
    public void wipe(){
        StoreContract<ContainerFile.StringHolder> container = new ContainerFile(fullFilename);
        container.wipe();
    }

}
