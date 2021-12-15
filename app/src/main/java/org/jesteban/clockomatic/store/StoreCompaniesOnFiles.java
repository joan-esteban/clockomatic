package org.jesteban.clockomatic.store;


import org.jesteban.clockomatic.model.CompaniesContract;
import org.jesteban.clockomatic.store.containers.ContainerFile;
import org.jesteban.clockomatic.store.serializers.SerializerCompaniesCsv;

import java.io.File;
import java.util.logging.Logger;

public class StoreCompaniesOnFiles implements StoreContract<CompaniesContract> {
    private String storeBaseDir = null;


    public StoreCompaniesOnFiles(){
        this("/sdcard/clockomatic/");
    }
    public StoreCompaniesOnFiles(String baseDir){
        storeBaseDir = baseDir;
    }



    @Override
    public void write(CompaniesContract data) throws Exception {
        if (data.getAllCompaniesEnabled().isEmpty()) return;
        createFolders();
        StoreContract<ContainerFile.StringHolder> container = new ContainerFile(getFullFilename());
        Serializer<CompaniesContract> serializer = new SerializerCompaniesCsv();
        container.write(new ContainerFile.StringHolder(serializer.serialize(data)));
    }

    @Override
    public void read(CompaniesContract destination) throws Exception {
        StoreContract<ContainerFile.StringHolder> container = new ContainerFile(getFullFilename());
        Serializer<CompaniesContract> serializer = new SerializerCompaniesCsv();
        ContainerFile.StringHolder data = new ContainerFile.StringHolder();
        container.read(data);
        serializer.deserialize(data.getString(),destination);
    }



    @Override
    public void wipe() {
        StoreContract<ContainerFile.StringHolder> container = new ContainerFile(getFullFilename());
        container.wipe();
    }

    private String getFullFilename(){
        return storeBaseDir + "/companies.csv";
    }

    private void createFolders(){
        if (createdFolders) return;
        File file = new File(storeBaseDir );
        LOGGER.info("Creating missing folders for  "+storeBaseDir);
        if (!file.mkdirs()){
            LOGGER.warning("Cant create folder " + storeBaseDir);
        } else createdFolders=true;
    }
    private Boolean createdFolders = false;
    private static final Logger LOGGER = Logger.getLogger(StoreCompaniesOnFiles.class.getName());
}
