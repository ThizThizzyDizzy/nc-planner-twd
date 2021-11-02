package net.ncplanner.plannerator.multiblock.configuration.overhaul.turbine;
import java.util.ArrayList;
import java.util.Objects;
import net.ncplanner.plannerator.multiblock.configuration.AbstractPlacementRule;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.IBlockTemplate;
import net.ncplanner.plannerator.multiblock.configuration.RuleContainer;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Pinnable;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
import net.ncplanner.plannerator.simplelibrary.config2.ConfigList;
import net.ncplanner.plannerator.simplelibrary.config2.ConfigNumberList;
import net.ncplanner.plannerator.simplelibrary.image.Image;
public class Block extends RuleContainer<PlacementRule.BlockType, Block> implements Pinnable, IBlockTemplate {
    public static Block controller(String name, String displayName, String texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.setTexture(TextureManager.getImage(texture));
        block.controller = true;
        block.casing = true;
        return block;
    }
    public static Block casing(String name, String displayName, String texture, boolean edge){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.setTexture(TextureManager.getImage(texture));
        block.casing = true;
        block.casingEdge = edge;
        return block;
    }
    public static Block inlet(String name, String displayName, String texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.setTexture(TextureManager.getImage(texture));
        block.inlet = true;
        return block;
    }
    public static Block outlet(String name, String displayName, String texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.setTexture(TextureManager.getImage(texture));
        block.outlet = true;
        return block;
    }
    public static Block coil(String name, String displayName, String texture, float efficiency){
        Block coil = new Block(name);
        coil.displayName = displayName;
        coil.legacyNames.add(displayName);
        coil.coil = true;
        coil.coilEfficiency = efficiency;
        coil.setTexture(TextureManager.getImage(texture));
        return coil;
    }
    public static Block bearing(String name, String displayName, String texture){
        Block bearing = new Block(name);
        bearing.displayName = displayName;
        bearing.legacyNames.add(displayName);
        bearing.bearing = true;
        bearing.setTexture(TextureManager.getImage(texture));
        return bearing;
    }
    public static Block connector(String name, String displayName, String texture){
        Block connector = new Block(name);
        connector.displayName = displayName;
        connector.legacyNames.add(displayName);
        connector.connector = true;
        connector.setTexture(TextureManager.getImage(texture));
        return connector;
    }
    public static Block blade(String name, String displayName, String texture, float efficiency, float expansion){
        Block blade = new Block(name);
        blade.displayName = displayName;
        blade.legacyNames.add(displayName);
        blade.blade = true;
        blade.bladeEfficiency = efficiency;
        blade.bladeExpansion = expansion;
        blade.setTexture(TextureManager.getImage(texture));
        return blade;
    }
    public static Block stator(String name, String displayName, String texture, float expansion){
        Block blade = new Block(name);
        blade.displayName = displayName;
        blade.legacyNames.add(displayName);
        blade.blade = true;
        blade.bladeEfficiency = 0;
        blade.bladeExpansion = expansion;
        blade.bladeStator = true;
        blade.setTexture(TextureManager.getImage(texture));
        return blade;
    }
    public static Block shaft(String name, String displayName, String texture){
        Block shaft = new Block(name);
        shaft.displayName = displayName;
        shaft.legacyNames.add(displayName);
        shaft.shaft = true;
        shaft.setTexture(TextureManager.getImage(texture));
        return shaft;
    }
    public String name;
    public String displayName;
    public ArrayList<String> legacyNames = new ArrayList<>();
    public boolean blade = false;
    public float bladeEfficiency;
    public float bladeExpansion;
    public boolean bladeStator;//not just stator cuz it's the stator stat of the blade. makes sense.
    public boolean coil = false;
    public float coilEfficiency;
    public boolean bearing = false;
    public boolean shaft = false;
    public boolean connector = false;
    public boolean controller = false;
    public boolean casing = false;
    public boolean casingEdge = false;
    public boolean inlet = false;
    public boolean outlet = false;
    public Image texture;
    public Image displayTexture;
    public Block(String name){
        this.name = name;
    }
    public Config save(Configuration parent, TurbineConfiguration configuration, boolean partial){
        Config config = Config.newConfig();
        config.set("name", name);
        if(!partial){
            if(displayName!=null)config.set("displayName", displayName);
            if(!legacyNames.isEmpty()){
                ConfigList lst = new ConfigList();
                for(String s : legacyNames)lst.add(s);
                config.set("legacyNames", lst);
            }
        }
        if(bearing)config.set("bearing", true);
        if(shaft)config.set("shaft", true);
        if(connector)config.set("connector", true);
        if(controller)config.set("controller", true);
        if(casing)config.set("casing", true);
        if(casingEdge)config.set("casingEdge", true);
        if(inlet)config.set("inlet", true);
        if(outlet)config.set("outlet", true);
        if(blade){
            Config bladeCfg = Config.newConfig();
            bladeCfg.set("efficiency", bladeEfficiency);
            bladeCfg.set("expansion", bladeExpansion);
            bladeCfg.set("stator", bladeStator);
            config.set("blade", bladeCfg);
        }
        if(coil){
            Config coilCfg = Config.newConfig();
            coilCfg.set("efficiency", coilEfficiency);
            config.set("coil", coilCfg);
        }
        if(!partial&&texture!=null){
            ConfigNumberList tex = new ConfigNumberList();
            tex.add(texture.getWidth());
            for(int x = 0; x<texture.getWidth(); x++){
                for(int y = 0; y<texture.getHeight(); y++){
                    tex.add(texture.getRGB(x, y));
                }
            }
            config.set("texture", tex);
        }
        if(!rules.isEmpty()){
            ConfigList ruls = new ConfigList();
            for(AbstractPlacementRule<PlacementRule.BlockType, Block> rule : rules){
                ruls.add(rule.save(parent, configuration));
            }
            config.set("rules", ruls);
        }
        return config;
    }
    public Image getTexture(){
        return texture;
    }
    public void setTexture(Image image){
        texture = image;
        displayTexture = TextureManager.convert(image);
    }
    @Override
    public boolean stillEquals(RuleContainer rc){
        Block b = (Block)rc;
        return Objects.equals(b.name, name)
                &&Objects.equals(b.displayName, displayName)
                &&b.legacyNames.equals(legacyNames)
                &&b.blade==blade
                &&b.bladeEfficiency==bladeEfficiency
                &&b.bladeExpansion==bladeExpansion
                &&b.bladeStator==bladeStator
                &&b.coil==coil
                &&b.coilEfficiency==coilEfficiency
                &&b.bearing==bearing
                &&b.shaft==shaft
                &&b.connector==connector
                &&b.controller==controller
                &&b.casing==casing
                &&b.casingEdge==casingEdge
                &&b.inlet==inlet
                &&b.outlet==outlet
                &&Core.areImagesEqual(b.texture, texture);
    }
    public ArrayList<String> getLegacyNames(){
        ArrayList<String> allNames = new ArrayList<>(legacyNames);
        allNames.add(name);
        return allNames;
    }
    @Override
    public String getName(){
        return name;
    }
    public String getDisplayName(){
        return displayName==null?name:displayName;
    }
    @Override
    public ArrayList<String> getSearchableNames(){
        ArrayList<String> nams = getSimpleSearchableNames();
        for(AbstractPlacementRule<PlacementRule.BlockType, Block> r : rules)nams.addAll(r.getSearchableNames());
        return nams;
    }
    @Override
    public ArrayList<String> getSimpleSearchableNames(){
        ArrayList<String> nams = getLegacyNames();
        nams.add(getDisplayName());
        return nams;
    }
    @Override
    public Image getDisplayTexture() {
        return displayTexture;
    }
    @Override
    public String getPinnedName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }
    public void setLegacyNames(ArrayList<String> legacyNames){
        this.legacyNames = new ArrayList<>(legacyNames);
    }
    public boolean isBlade(){
        return blade;
    }
    public float getBladeEfficiency(){
        return bladeEfficiency;
    }
    public float getBladeExpansion(){
        return bladeExpansion;
    }
    public boolean isBladeStator(){
        return bladeStator;
    }
    public boolean isCoil(){
        return coil;
    }
    public float getCoilEfficiency(){
        return coilEfficiency;
    }
    public boolean isBearing(){
        return bearing;
    }
    public boolean isShaft(){
        return shaft;
    }
    public boolean isConnector(){
        return connector;
    }
    public boolean isController(){
        return controller;
    }
    public boolean isCasing(){
        return casing;
    }
    public boolean isCasingEdge(){
        return casingEdge;
    }
    public boolean isInlet(){
        return inlet;
    }
    public boolean isOutlet(){
        return outlet;
    }
    public void setBlade(boolean blade){
        this.blade = blade;
    }
    public void setBladeEfficiency(float bladeEfficiency){
        this.bladeEfficiency = bladeEfficiency;
    }
    public void setBladeExpansion(float bladeExpansion){
        this.bladeExpansion = bladeExpansion;
    }
    public void setBladeStator(boolean bladeStator){
        this.bladeStator = bladeStator;
    }
    public void setCoil(boolean coil){
        this.coil = coil;
    }
    public void setCoilEfficiency(float coilEfficiency){
        this.coilEfficiency = coilEfficiency;
    }
    public void setBearing(boolean bearing){
        this.bearing = bearing;
    }
    public void setShaft(boolean shaft){
        this.shaft = shaft;
    }
    public void setConnector(boolean connector){
        this.connector = connector;
    }
    public void setController(boolean controller){
        this.controller = controller;
    }
    public void setCasing(boolean casing){
        this.casing = casing;
    }
    public void setCasingEdge(boolean casingEdge){
        this.casingEdge = casingEdge;
    }
    public void setInlet(boolean inlet){
        this.inlet = inlet;
    }
    public void setOutlet(boolean outlet){
        this.outlet = outlet;
    }
}