package net.ncplanner.plannerator.multiblock.configuration.underhaul;
import java.util.ArrayList;
import java.util.Objects;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.AddonConfiguration;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.PartialConfiguration;
import net.ncplanner.plannerator.multiblock.configuration.underhaul.fissionsfr.FissionSFRConfiguration;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
public class UnderhaulConfiguration{
    public FissionSFRConfiguration fissionSFR;
    public Config save(Configuration parent, boolean partial){
        Config config = Config.newConfig();
        if(fissionSFR!=null){
            config.set("fissionSFR", fissionSFR.save(parent, partial));
        }
        return config;
    }
    public void apply(UnderhaulConfiguration partial, ArrayList<Multiblock> multiblocks, PartialConfiguration parent){
        if(fissionSFR!=null){
            partial.fissionSFR = new FissionSFRConfiguration();
            fissionSFR.apply(partial.fissionSFR, multiblocks, parent);
        }
    }
    public void apply(AddonConfiguration addon, Configuration parent){
        if(fissionSFR!=null){
            addon.underhaul.fissionSFR = new FissionSFRConfiguration();
            addon.self.underhaul.fissionSFR = new FissionSFRConfiguration();
            fissionSFR.apply(addon, parent);
        }
    }
    @Override
    public boolean equals(Object obj){
        if(obj!=null&&obj instanceof UnderhaulConfiguration){
            return Objects.equals(fissionSFR, ((UnderhaulConfiguration)obj).fissionSFR);
        }
        return super.equals(obj);
    }
    public void convertAddon(AddonConfiguration parent, Configuration convertTo) throws MissingConfigurationEntryException{
        if(convertTo.underhaul==null)throw new IllegalArgumentException("Cannot convert to null underhaul configuration!");
        if(fissionSFR!=null){
            fissionSFR.convertAddon(parent, convertTo);
        }
    }
    public void makeAddon(UnderhaulConfiguration parent, UnderhaulConfiguration addon){
        if(fissionSFR!=null){
            addon.fissionSFR = new FissionSFRConfiguration();
            fissionSFR.makeAddon(parent.fissionSFR, addon.fissionSFR);
        }
    }
}