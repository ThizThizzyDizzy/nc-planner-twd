package net.ncplanner.plannerator.multiblock.configuration.overhaul.turbine;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.AbstractBlockContainer;
import net.ncplanner.plannerator.multiblock.configuration.AbstractPlacementRule;
import net.ncplanner.plannerator.multiblock.configuration.AddonConfiguration;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.PartialConfiguration;
import net.ncplanner.plannerator.multiblock.configuration.overhaul.OverhaulConfiguration;
import net.ncplanner.plannerator.multiblock.overhaul.turbine.OverhaulTurbine;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
import net.ncplanner.plannerator.simplelibrary.config2.ConfigList;
public class TurbineConfiguration extends AbstractBlockContainer<Block> {
    public ArrayList<Recipe> allRecipes = new ArrayList<>();
    /**
     * @deprecated You should probably be using allRecipes
     */
    @Deprecated
    public ArrayList<Recipe> recipes = new ArrayList<>();
    public int minWidth;
    public int minLength;
    public int maxSize;
    public int fluidPerBlade;
    public float throughputFactor;
    public float powerBonus;
    public float throughputEfficiencyLeniencyMult;
    public float throughputEfficiencyLeniencyThreshold;
    public String[] getAllBlocksStringList(){
        String[] strs = new String[allBlocks.size()];
        for(int i = 0; i<strs.length; i++){
            strs[i] = allBlocks.get(i).name;
        }
        return strs;
    }
    public Config save(Configuration parent, boolean partial){
        Config config = Config.newConfig();
        if(parent==null&&!partial){
            config.set("minWidth", minWidth);
            config.set("minLength", minLength);
            config.set("maxSize", maxSize);
            config.set("fluidPerBlade", fluidPerBlade);
            config.set("throughputEfficiencyLeniencyMult", throughputEfficiencyLeniencyMult);
            config.set("throughputEfficiencyLeniencyThreshold", throughputEfficiencyLeniencyThreshold);
            config.set("throughputFactor", throughputFactor);
            config.set("powerBonus", powerBonus);
        }
        ConfigList blocks = new ConfigList();
        for(Block block : this.blocks){
            blocks.add(block.save(parent, this, partial));
        }
        config.set("blocks", blocks);
        ConfigList recipes = new ConfigList();
        for(Recipe recipe : this.recipes){
            recipes.add(recipe.save(partial));
        }
        config.set("recipes", recipes);
        return config;
    }
    public void apply(TurbineConfiguration partial, ArrayList<Multiblock> multiblocks, PartialConfiguration parent){
        ArrayList<Block> usedBlocks = new ArrayList<>();
        ArrayList<Recipe> usedRecipes = new ArrayList<>();
        for(Multiblock mb : multiblocks){
            if(mb instanceof OverhaulTurbine){
                for(net.ncplanner.plannerator.multiblock.overhaul.turbine.Block b : ((OverhaulTurbine)mb).getBlocks(true)){
                    if(b.template!=null&&!usedBlocks.contains(b.template))usedBlocks.add(b.template);
                }
                if(!usedRecipes.contains(((OverhaulTurbine)mb).recipe))usedRecipes.add(((OverhaulTurbine)mb).recipe);
            }
        }
        ArrayList<Block> convertedBlocks = new ArrayList<>();
        for(Block b : usedBlocks){
            for(Block bl : blocks.isEmpty()?allBlocks:blocks){
                if(bl.name.equals(b.name))convertedBlocks.add(bl);
            }
        }
        usedBlocks = convertedBlocks;
        ArrayList<Recipe> convertedRecipes = new ArrayList<>();
        for(Recipe r : usedRecipes){
            for(Recipe cr : recipes.isEmpty()?allRecipes:recipes){
                if(cr.inputName.equals(r.inputName))convertedRecipes.add(cr);
            }
        }
        usedRecipes = convertedRecipes;
        partial.blocks.addAll(usedBlocks);
        parent.overhaul.turbine.allBlocks.addAll(usedBlocks);
        partial.recipes.addAll(usedRecipes);
        parent.overhaul.turbine.allRecipes.addAll(usedRecipes);
    }
    public void apply(AddonConfiguration addon, Configuration parent){
        Set<Block> usedBlocks = new HashSet<>();
        for(Block b : blocks){
            usedBlocks.addAll(getAllUsedBlocks(b));
            usedBlocks.removeAll(blocks);
        }
        //parent blocks
        ArrayList<Block> theBlocks = new ArrayList<>();
        for(Block b : parent.overhaul.turbine.blocks){
            if(usedBlocks.contains(b)){
                theBlocks.add(b);
            }
        }
        addon.overhaul.turbine.allBlocks.addAll(theBlocks);
        addon.overhaul.turbine.blocks.addAll(theBlocks);
        //self blocks
        addon.self.overhaul.turbine.blocks.addAll(blocks);
        addon.overhaul.turbine.allBlocks.addAll(blocks);
        //addon blocks
        for(Configuration addn : parent.addons){
            theBlocks = new ArrayList<>();
            if(addn.overhaul!=null&&addn.overhaul.turbine!=null){
                for(Block b : addn.overhaul.turbine.blocks){
                    if(usedBlocks.contains(b)){
                        theBlocks.add(b);
                    }
                }
            }
            addon.overhaul.turbine.allBlocks.addAll(theBlocks);
            if(!theBlocks.isEmpty()){
                boolean foundMatch = false;
                for(Configuration c : addon.addons){
                    if(c.overhaulNameMatches(addn)){
                        foundMatch = true;
                        if(c.overhaul==null)c.overhaul = new OverhaulConfiguration();
                        if(c.overhaul.turbine==null)c.overhaul.turbine = new TurbineConfiguration();
                        c.overhaul.turbine.blocks.addAll(theBlocks);
                    }
                }
                if(!foundMatch){
                    Configuration c = new PartialConfiguration(addn.name, addn.overhaulVersion, addn.overhaulVersion);
                    addon.addons.add(c);
                    c.addon = true;
                    c.overhaul = new OverhaulConfiguration();
                    c.overhaul.turbine = new TurbineConfiguration();
                    c.overhaul.turbine.blocks.addAll(theBlocks);
                }
            }
        }
        addon.self.overhaul.turbine.recipes.addAll(recipes);
        parent.overhaul.turbine.allRecipes.addAll(recipes);
    }
    public Block convert(Block template) throws MissingConfigurationEntryException{
        if(template==null)return null;
        for(Block block : allBlocks){
            for(String name : block.getLegacyNames()){
                if(name.equals(template.name))return block;
            }
        }
        for(Block block : blocks){
            for(String name : block.getLegacyNames()){
                if(name.equals(template.name))return block;
            }
        }
        throw new MissingConfigurationEntryException("Failed to find match for block "+template.name+"!");
    }
    public Recipe convert(Recipe template) throws MissingConfigurationEntryException{
        if(template==null)return null;
        for(Recipe recipe : allRecipes){
            for(String name : recipe.getLegacyNames()){
                if(name.equals(template.inputName))return recipe;
            }
        }
        for(Recipe recipe : recipes){
            for(String name : recipe.getLegacyNames()){
                if(name.equals(template.inputName))return recipe;
            }
        }
        throw new MissingConfigurationEntryException("Failed to find match for recipe "+template.inputName+"!");
    }
    @Override
    public boolean equals(Object obj){
        if(obj!=null&&obj instanceof TurbineConfiguration){
            TurbineConfiguration fsfrc = (TurbineConfiguration)obj;
            return Objects.equals(fsfrc.blocks, blocks)
                    &&Objects.equals(fsfrc.recipes, recipes)
                    &&minWidth==fsfrc.minWidth
                    &&minLength==fsfrc.minLength
                    &&maxSize==fsfrc.maxSize
                    &&fluidPerBlade==fsfrc.fluidPerBlade
                    &&throughputEfficiencyLeniencyMult==fsfrc.throughputEfficiencyLeniencyMult
                    &&throughputEfficiencyLeniencyThreshold==fsfrc.throughputEfficiencyLeniencyThreshold
                    &&throughputFactor==fsfrc.throughputFactor
                    &&powerBonus==fsfrc.powerBonus;
        }
        return false;
    }
    public void convertAddon(AddonConfiguration parent, Configuration convertTo) throws MissingConfigurationEntryException{
        if(convertTo.overhaul.turbine==null)throw new IllegalArgumentException("Cannot convert to null turbine configuration!");
        for(Block block : blocks){
            for(AbstractPlacementRule<PlacementRule.BlockType, Block> rule : getAllSubRules(block)){
                if(rule.block==null)continue;
                if(parent.overhaul!=null&&parent.overhaul.turbine!=null&&parent.overhaul.turbine.blocks.contains(rule.block)){
                    rule.block = convertTo.overhaul.turbine.convert(rule.block);
                }else if(blocks.contains(rule.block)){
                    //do nothing :)
                }else{
                    //in sub-addon, find and convert
                    boolean found = false;
                    for(Configuration addon : parent.addons){
                        if(addon.overhaul!=null&&addon.overhaul.turbine!=null){
                            if(addon.overhaul.turbine.blocks.contains(rule.block)){
                                rule.block = convertTo.findMatchingAddon(addon).overhaul.turbine.convert(rule.block);
                                found = true;
                            }
                        }
                    }
                    if(!found)throw new IllegalArgumentException("Could not convert block "+rule.block.name+"!");
                }
            }
        }
    }
    public void makeAddon(TurbineConfiguration parent, TurbineConfiguration addon){
        B:for(Block b : blocks){
            for(Block pb : parent.blocks){
                if(pb.name.equals(b.name)){
                    continue B;
                }
            }
            addon.blocks.add(b);
            parent.allBlocks.add(b);
        }
        C:for(Recipe c : recipes){
            for(Recipe pc : parent.recipes){
                if(pc.inputName.equals(c.inputName)){
                    continue C;
                }
            }
            addon.recipes.add(c);
            parent.allRecipes.add(c);
        }
    }
    public int getMinWidth(){
        return minWidth;
    }
    public int getMinLength(){
        return minLength;
    }
    public int getMaxSize(){
        return maxSize;
    }
    public int getFluidPerBlade(){
        return fluidPerBlade;
    }
    public float getThroughputFactor(){
        return throughputFactor;
    }
    public float getPowerBonus(){
        return powerBonus;
    }
    public float getThroughputEfficiencyLeniencyMult(){
        return throughputEfficiencyLeniencyMult;
    }
    public float getThroughputEfficiencyLeniencyThreshold(){
        return throughputEfficiencyLeniencyThreshold;
    }
    public void setMinWidth(int minWidth){
        this.minWidth = minWidth;
    }
    public void setMinLength(int minLength){
        this.minLength = minLength;
    }
    public void setMaxSize(int maxSize){
        this.maxSize = maxSize;
    }
    public void setFluidPerBlade(int fluidPerBlade){
        this.fluidPerBlade = fluidPerBlade;
    }
    public void setThroughputFactor(float throughputFactor){
        this.throughputFactor = throughputFactor;
    }
    public void setPowerBonus(float powerBonus){
        this.powerBonus = powerBonus;
    }
    public void setThroughputEfficiencyLeniencyMult(float throughputEfficiencyLeniencyMult){
        this.throughputEfficiencyLeniencyMult = throughputEfficiencyLeniencyMult;
    }
    public void setThroughputEfficiencyLeniencyThreshold(float throughputEfficiencyLeniencyThreshold){
        this.throughputEfficiencyLeniencyThreshold = throughputEfficiencyLeniencyThreshold;
    }
}