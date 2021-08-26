package planner.module;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import multiblock.configuration.Configuration;
import multiblock.underhaul.fissionsfr.UnderhaulSFR;
import planner.file.FileReader;
public class UnderhaulModule extends Module{
    public UnderhaulModule(){
        super("underhaul", true);
    }
    @Override
    public String getDisplayName(){
        return "Underhaul";
    }
    @Override
    public String getDescription(){
        return "All the base NuclearCraft multiblocks";
    }
    @Override
    public void addMultiblockTypes(ArrayList multiblockTypes){
        multiblockTypes.add(new UnderhaulSFR());
    }
    @Override
    public void addConfigurations(){
        Configuration.configurations.add(FileReader.read(() -> {
            try{
                return Resources.open("/configurations.res").getData("po3.ncpf");
            }catch(IOException ex){
                return null;
            }
        }).configuration.addAlternative("PO3"));
        Configuration.configurations.add(FileReader.read(() -> {
            try{
                return Resources.open("/configurations.res").getData("e2e.ncpf");
            }catch(IOException ex){
                return null;
            }
        }).configuration.addAlternative("E2E"));
    }
}