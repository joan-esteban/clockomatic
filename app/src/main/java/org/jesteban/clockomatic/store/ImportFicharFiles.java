package org.jesteban.clockomatic.store;

import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.providers.EntriesProviderContract;
import org.jesteban.clockomatic.store.containers.ContainerFile;
import org.jesteban.clockomatic.store.serializers.SerializerOldFicharDat;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

/**
 * This class let to import data from a old app that keep record at /sdcard/fichar
 * - It keeps data in a file per month (fichardata_YYYYmm.dat)
 * - Each register is:
 *  0|YYYY/mm/dd|HH|mm|tstamp|tstamp_inserted_register|
 *
 *  Example:
 *  0|2016/10/03|8|51|1475471474668|1475477522764|
 *  0|2016/10/03|13|24|1475471474668|1475493861364|
 *  0|2016/10/03|14|15|1475493864395|1475496963689|
 *  0|2016/10/03|18|6|1475496971601|1475510809655|
 */

public class ImportFicharFiles implements  Importer{
    @Override
    public boolean thereAreData(){
        return true;
    }

    @Override
    public boolean importAllDataTo(EntriesProviderContract state){
        File[] files = getCandidatesFiles (this.currentBaseDir);
        if (files == null) return false;
        for (File file : files) {
            StoreContract<ContainerFile.StringHolder> container = new ContainerFile(file.getAbsolutePath());
            try {
                ContainerFile.StringHolder fileData =new ContainerFile.StringHolder();
                container.read(fileData);
                SerializerOldFicharDat serializer = new SerializerOldFicharDat();
                EntrySet entries = new EntrySet();
                serializer.deserialize(fileData.getString(),entries);
                for (Entry entry : entries){
                    state.register(entry);
                }
                } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private File[]  getCandidatesFiles(String path){
        File filePath = new File(path);
        if (filePath.exists() && filePath.isDirectory()) {
            return  filePath.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.isFile() && file.canRead());
                }
            });
        } else {
            File[] files = new File[1];
            files[0] = filePath;
            return files;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(StoreEntriesOnFiles.class.getName());
    private static final String PREFIX_FILENAME_DATA = "fichardata_";
    private static final String INFIX_FILENAME_DATA = "yyyyMM";
    private static final String SUFIX_FILENAME_DATA = ".dat";

    private static  String currentBaseDir = "/sdcard/fichar/";

}
