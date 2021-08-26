package planner.module;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import multiblock.configuration.AddonConfiguration;
import multiblock.configuration.Configuration;
import multiblock.overhaul.fissionmsr.OverhaulMSR;
import multiblock.overhaul.fissionsfr.OverhaulSFR;
import multiblock.overhaul.turbine.OverhaulTurbine;
import planner.file.FileReader;
public class OverhaulModule extends Module{
    public OverhaulModule(){
        super("overhaul", true);
    }
    @Override
    public String getDisplayName(){
        return "Overhaul";
    }
    @Override
    public String getDescription(){
        return "All the base NuclearCraft: Overhauled multiblocks";
    }
    @Override
    public void addMultiblockTypes(ArrayList multiblockTypes){
        multiblockTypes.add(new OverhaulSFR());
        multiblockTypes.add(new OverhaulMSR());
        multiblockTypes.add(new OverhaulTurbine());
    }
    @Override
    public void addConfigurations(){
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("extreme_reactors.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("ic2.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("qmd.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("trinity.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("ncouto.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("moar_heat_sinks.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("moar_fuels.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("moar_fuels_lite.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("moar_fuels_ultra_lite.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("moar_reactor_functionality.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("nuclear_oil_refining.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("nuclear_tree_factory.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("bes.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("aop.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("nco_confectionery.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("thorium_mixed_fuels.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("inert_matrix_fuels.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("alloy_heat_sinks.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("spicy_heat_sinks_stable.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
        Configuration.internalAddons.add(() -> {
            return AddonConfiguration.convert(FileReader.read(() -> {
                try{
                    return Resources.open("/addons.res").getData("spicy_heat_sinks_unstable.ncpf");
                }catch(IOException ex){
                    return null;
                }
            }).configuration);
        });
    }
}