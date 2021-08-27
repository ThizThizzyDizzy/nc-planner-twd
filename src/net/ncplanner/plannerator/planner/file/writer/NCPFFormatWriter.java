package net.ncplanner.plannerator.planner.file.writer;
import java.io.IOException;
import java.io.OutputStream;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.planner.file.FileFormat;
import net.ncplanner.plannerator.planner.file.FormatWriter;
import net.ncplanner.plannerator.planner.file.NCPFFile;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
public class NCPFFormatWriter extends FormatWriter{
    @Override
    public FileFormat getFileFormat(){
        return FileFormat.NCPF;
    }
    @Override
    public void write(NCPFFile ncpf, OutputStream stream){
        Config header = Config.newConfig();
        header.set("version", NCPFFile.SAVE_VERSION);
        header.set("count", ncpf.multiblocks.size());
        Config meta = Config.newConfig();
        for(String key : ncpf.metadata.keySet()){
            String value = ncpf.metadata.get(key);
            if(value.trim().isEmpty())continue;
            meta.set(key,value);
        }
        if(meta.properties().length>0){
            header.set("metadata", meta);
        }
        header.save(stream);
        ncpf.configuration.save(null, Config.newConfig()).save(stream);
        for(Multiblock m : ncpf.multiblocks){
            try{
                m.save(ncpf, ncpf.configuration, stream);
            }catch(MissingConfigurationEntryException ex){
                throw new RuntimeException(ex);
            }
        }
        try{
            stream.close();
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
    @Override
    public boolean isMultiblockSupported(Multiblock multi){
        return true;
    }
}