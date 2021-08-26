package planner.module;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import multiblock.configuration.Configuration;
import multiblock.overhaul.fusion.OverhaulFusionReactor;
import planner.file.FileReader;
public class FusionTestModule extends Module{
    public FusionTestModule(){
        super("fusion_test");
    }
    @Override
    public String getDisplayName(){
        return "Fusion Test";
    }
    @Override
    public String getDescription(){
        return "A testbed for future overhaul fusion reactors.";
    }
    @Override
    public void addMultiblockTypes(ArrayList multiblockTypes){
        multiblockTypes.add(new OverhaulFusionReactor());
    }
    @Override
    public void addConfigurations(){
        Configuration.configurations.add(FileReader.read(() -> {
            try{
                return Resources.open("/configurations.res").getData("fusion_test.ncpf");
            }catch(IOException ex){
                return null;
            }
        }).configuration.addAlternative("Fusion"));
    }
}