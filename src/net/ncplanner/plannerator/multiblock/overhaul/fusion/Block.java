package net.ncplanner.plannerator.multiblock.overhaul.fusion;
import java.util.ArrayList;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Direction;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.AbstractPlacementRule;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.ITemplateAccess;
import net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.BlockRecipe;
import net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.PlacementRule;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.MathUtil;
import net.ncplanner.plannerator.planner.StringUtil;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.simplelibrary.image.Color;
import net.ncplanner.plannerator.simplelibrary.image.Image;
public class Block extends net.ncplanner.plannerator.multiblock.Block implements ITemplateAccess<net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block> {
    /**
     * MUST ONLY BE SET WHEN MERGING CONFIGURATIONS!!!
     */
    public net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block template;
    public net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.BlockRecipe recipe;
    private boolean breedingBlanketValid;
    private boolean breedingBlanketAugmented;
    private boolean heatsinkValid;
    private boolean reflectorValid;
    public float efficiencyMult;//breeding blankets
    public float heatMult;//heating blankets
    public float efficiency;//heating blankets
    public OverhaulFusionReactor.Cluster cluster;
    public Block(Configuration configuration, int x, int y, int z, net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block template){
        super(configuration, x, y, z);
        if(template==null)throw new IllegalArgumentException("Cannot create null block!");
        this.template = template;
    }
    @Override
    public net.ncplanner.plannerator.multiblock.Block newInstance(int x, int y, int z){
        return new Block(getConfiguration(), x, y, z, template);
    }
    @Override
    public void copyProperties(net.ncplanner.plannerator.multiblock.Block other){
        ((Block)other).recipe = recipe;
    }
    @Override
    public Image getBaseTexture(){
        return template.texture;
    }
    @Override
    public Image getTexture(){
        return template.displayTexture;
    }
    @Override
    public String getName(){
        return template.getDisplayName();
    }
    @Override
    public void clearData(){
        breedingBlanketValid = breedingBlanketAugmented = heatsinkValid = reflectorValid = false;
        cluster = null;
        efficiency = heatMult = efficiencyMult = 0;
    }
    @Override
    public String getTooltip(Multiblock multiblock){
        String tip = getName();
        if(recipe!=null){
            tip+="\n"+recipe.getInputDisplayName();
        }
        OverhaulFusionReactor fusion = (OverhaulFusionReactor)multiblock;
        if(isHeatingBlanket()){
            tip+="\nHeating Blanket "+(isHeatingBlanketActive()?"Active":"Inactive");
            if(isHeatingBlanketActive()){
                tip+="\nHeat Multiplier: "+MathUtil.percent(heatMult, 0)+"\n"
                        + "Heat Produced: "+heatMult*fusion.recipe.heat+"H/t\n"
                        + "Efficiency: "+MathUtil.percent(efficiency, 0)+"\n";
            }
        }
        if(isBreedingBlanket()){
            if(recipe==null||!template.breedingBlanketHasBaseStats)tip+="\nNo Recipe";
            tip+="\nBreeding Blanket "+(breedingBlanketAugmented?"Augmented":(breedingBlanketValid?"Valid":"Invalid"))
                    + "\nEfficiency multiplier: "+MathUtil.round(efficiencyMult, 2)+"\n";
        }
        if(isReflector()){
            if(recipe==null&&!template.reflectorHasBaseStats)tip+="\nNo Recipe";
            tip+="\nReflector "+(reflectorValid?"Valid":"Invalid");
            if(template.reflectorHasBaseStats||recipe!=null){
                tip+="\nEfficiency: "+(recipe==null?template.reflectorEfficiency:recipe.reflectorEfficiency);
            }
        }
        if(isHeatsink()){
            if(recipe==null&&!template.heatsinkHasBaseStats)tip+="\nNo Recipe";
            tip+="\nHeatsink "+(heatsinkValid?"Valid":"Invalid");
            //TODO show heatsink validity check errors
        }
        OverhaulFusionReactor.Cluster cluster = this.cluster;
        if(cluster!=null){
            if(!cluster.isCreated()){
                tip+="\nInvalid cluster!";
            }
            if(!cluster.isConnectedToWall){
                tip+="\nCluster is not connected to a connector!";
            }
            if(cluster.netHeat>0){
                tip+="\nCluster is heat-positive!";
            }
            if(cluster.coolingPenaltyMult<1){
                tip+="\nCluster is penalized for overcooling!";
            }
        }
        return tip;
    }
    @Override
    public String getListTooltip(){
        String tip = getName();
        if(template.core&&template.getDisplayName().contains("ium")){
            tip+="\nThe stuff that Cores are made out of\n(Not to be confused with Corium)";
        }
        if(isConnector())tip+="\nConnector";
        if(isElectromagnet())tip+="\nElectromagnet";
        if(isConductor())tip+="\nConductor";
        if(isHeatingBlanket())tip+="\nHeating Blanket";
        if(isBreedingBlanket()){
            tip+="\nBreeding Blanket";
            if(template.breedingBlanketHasBaseStats){
                tip+="\nEfficiency: "+template.breedingBlanketEfficiency;
                tip+="\nHeat: "+template.breedingBlanketHeat;
            }
        }
        if(isReflector()){
            tip+="\nReflector";
            if(template.reflectorHasBaseStats){
                tip+="\nEfficiency: "+template.reflectorEfficiency;
            }
        }
        if(isShielding()){
            tip+="\nShielding";
            if(template.shieldingHasBaseStats){
                tip+="\nShieldiness: "+template.shieldingShieldiness;
            }
        }
        if(isHeatsink()){
            tip+="\nHeatsink";
            if(template.heatsinkHasBaseStats){
                tip+="\nCooling: "+template.heatsinkCooling+"H/t";
            }
        }
        for(AbstractPlacementRule<PlacementRule.BlockType, net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block> rule : template.rules){
            tip+="\nRequires "+rule.toString();
        }
        return tip;
    }
    @Override
    public void renderOverlay(Renderer renderer, double x, double y, double width, double height, Multiblock multiblock){
        if(!isValid()){
            drawOutline(renderer, x, y, width, height, Core.theme.getBlockColorOutlineInvalid());
        }
        if(isBreedingBlanketAugmented()){
            drawOutline(renderer, x, y, width, height, Core.theme.getBlockColorOutlineActive());
        }
        OverhaulFusionReactor.Cluster cluster = this.cluster;
        if(cluster!=null){
            Color primaryColor = null;
            if(cluster.netHeat>0){
                primaryColor = Core.theme.getClusterOverheatingColor();
            }
            if(cluster.coolingPenaltyMult<1){
                primaryColor = Core.theme.getClusterOvercoolingColor();
            }
            if(primaryColor!=null){
                renderer.setColor(primaryColor, .125f);
                renderer.fillRect(x, y, x+width, y+height);
                renderer.setColor(primaryColor, .75f);
                double border = width/8;
                boolean top = cluster.contains(this.x, this.y, z-1);
                boolean right = cluster.contains(this.x+1, this.y, z);
                boolean bottom = cluster.contains(this.x, this.y, z+1);
                boolean left = cluster.contains(this.x-1, this.y, z);
                if(!top||!left||!cluster.contains(this.x-1, this.y, z-1)){//top left
                    renderer.fillRect(x, y, x+border, y+border);
                }
                if(!top){//top
                    renderer.fillRect(x+width/2-border, y, x+width/2+border, y+border);
                }
                if(!top||!right||!cluster.contains(this.x+1, this.y, z-1)){//top right
                    renderer.fillRect(x+width-border, y, x+width, y+border);
                }
                if(!right){//right
                    renderer.fillRect(x+width-border, y+height/2-border, x+width, y+height/2+border);
                }
                if(!bottom||!right||!cluster.contains(this.x+1, this.y, z+1)){//bottom right
                    renderer.fillRect(x+width-border, y+height-border, x+width, y+height);
                }
                if(!bottom){//bottom
                    renderer.fillRect(x+width/2-border, y+height-border, x+width/2+border, y+height);
                }
                if(!bottom||!left||!cluster.contains(this.x-1, this.y, z+1)){//bottom left
                    renderer.fillRect(x, y+height-border, x+border, y+height);
                }
                if(!left){//left
                    renderer.fillRect(x, y+height/2-border, x+border, y+height/2+border);
                }
            }
            Color secondaryColor = null;
            if(!cluster.isConnectedToWall){
                secondaryColor = Core.theme.getClusterDisconnectedColor();
            }
            if(!cluster.isCreated()){
                secondaryColor = Core.theme.getClusterInvalidColor();
            }
            if(secondaryColor!=null){
                renderer.setColor(secondaryColor, .75f);
                double border = width/8;
                boolean top = cluster.contains(this.x, this.y, z-1);
                boolean right = cluster.contains(this.x+1, this.y, z);
                boolean bottom = cluster.contains(this.x, this.y, z+1);
                boolean left = cluster.contains(this.x-1, this.y, z);
                if(!top){//top
                    renderer.fillRect(x+border, y, x+width/2-border, y+border);
                    renderer.fillRect(x+width/2+border, y, x+width-border, y+border);
                }
                if(!right){//right
                    renderer.fillRect(x+width-border, y+border, x+width, y+height/2-border);
                    renderer.fillRect(x+width-border, y+height/2+border, x+width, y+height-border);
                }
                if(!bottom){//bottom
                    renderer.fillRect(x+border, y+height-border, x+width/2-border, y+height);
                    renderer.fillRect(x+width/2+border, y+height-border, x+width-border, y+height);
                }
                if(!left){//left
                    renderer.fillRect(x, y+border, x+border, y+height/2-border);
                    renderer.fillRect(x, y+height/2+border, x+border, y+height-border);
                }
            }
        }
    }
    public boolean isInert(){
        return template.cluster&&!template.functional;
    }
    @Override
    public boolean isValid(){
        return isInert()||isActive()||breedingBlanketValid;
    }
    @Override
    public boolean isActive(){
        return isConductor()||isBreedingBlanketAugmented()||isHeatingBlanketActive()||isHeatsinkActive()||isReflectorActive()||isShielding()||template.core||isConnector()||isElectromagnet();
    }
    public boolean isConductor(){
        return template.conductor;
    }
    public boolean isConnector(){
        return template.connector;
    }
    public boolean isBreedingBlanketAugmented(){
        if(!template.breedingBlanketHasBaseStats&&recipe==null)return false;
        return isBreedingBlanket()&&breedingBlanketAugmented&&(recipe==null?template.breedingBlanketAugmented:recipe.breedingBlanketAugmented);
    }
    public boolean isBreedingBlanket(){
        return template.breedingBlanket;
    }
    public boolean isHeatingBlanketActive(){
        return isHeatingBlanket();
    }
    public boolean isHeatingBlanket(){
        return template.heatingBlanket;
    }
    public boolean isHeatsinkActive(){
        if(!template.heatsinkHasBaseStats&&recipe==null)return false;
        return isHeatsink()&&heatsinkValid;
    }
    public boolean isHeatsink(){
        return template.heatsink;
    }
    public boolean isShielding(){
        return template.shielding;
    }
    public boolean isReflector(){
        return template.reflector;
    }
    public boolean isReflectorActive(){
        return isReflector()&&reflectorValid;
    }
    @Override
    public boolean isCore(){
        return isReflector()||isBreedingBlanket()||isHeatingBlanket();
    }
    @Override
    public boolean hasRules(){
        return !template.rules.isEmpty();
    }
    @Override
    public boolean calculateRules(Multiblock multiblock){
        for(AbstractPlacementRule<PlacementRule.BlockType, net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block> rule : template.rules){
            if(!rule.isValid(this, (OverhaulFusionReactor) multiblock)){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean matches(net.ncplanner.plannerator.multiblock.Block template){
        if(template==null)return false;
        if(template instanceof Block){
            return ((Block) template).template==this.template;
        }
        return false;
    }
    @Override
    public boolean canRequire(net.ncplanner.plannerator.multiblock.Block oth){
        if(template.heatsink)return requires(oth, null);
        Block other = (Block) oth;
        if(template.conductor)return other.template.cluster;
        return false;
    }
    @Override
    public boolean requires(net.ncplanner.plannerator.multiblock.Block oth, Multiblock mb){
        if(!template.heatsink)return false;
        Block other = (Block) oth;
        int totalDist = Math.abs(oth.x-x)+Math.abs(oth.y-y)+Math.abs(oth.z-z);
        if(totalDist>1)return false;//too far away
        if(hasRules()){
            for(AbstractPlacementRule<PlacementRule.BlockType, net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block> rule : template.rules){
                if(ruleHas((PlacementRule) rule, other))return true;
            }
        }
        return false;
    }
    private boolean ruleHas(PlacementRule rule, Block b){
        if(rule.block==b.template)return true;
        for(AbstractPlacementRule<PlacementRule.BlockType, net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block> rul : rule.rules){
            if(ruleHas((PlacementRule) rul, b))return true;
        }
        return false;
    }
    @Override
    public boolean canGroup(){
        if(template.breedingBlanket)return false;
        return template.heatsink;
    }
    @Override
    public boolean canBeQuickReplaced(){
        return template.heatsink;
    }
    @Override
    public net.ncplanner.plannerator.multiblock.Block copy(){
        Block copy = new Block(getConfiguration(), x, y, z, template);
        copy.recipe = recipe;
        copy.breedingBlanketValid = breedingBlanketValid;
        copy.breedingBlanketAugmented = breedingBlanketAugmented;
        copy.heatsinkValid = heatsinkValid;
        copy.reflectorValid = reflectorValid;
        copy.efficiencyMult = efficiencyMult;
        copy.heatMult = heatMult;
        copy.efficiency = efficiency;
        copy.cluster = cluster;//TODO probably shouldn't do that
        return copy;
    }
    @Override
    public boolean isEqual(net.ncplanner.plannerator.multiblock.Block other){
        return other instanceof Block&&((Block)other).template==template;
    }
    public boolean canCluster(){
        return template.cluster;
    }
    public boolean isElectromagnet(){
        return template.electromagnet;
    }
    public void calculateBreedingBlanket(OverhaulFusionReactor reactor){
        if(!template.breedingBlanket)return;
        if(!template.breedingBlanketHasBaseStats&&recipe==null)return;//empty breeding blanket
        breedingBlanketValid = true;//shrug
        efficiencyMult = 1;
        FOR:for(Direction d : Direction.values()){
            int X = x+d.x;
            int Y = y+d.y;
            int Z = z+d.z;
            boolean foundPlasma = false;
            while(reactor.getLocationCategory(X, Y, Z)==OverhaulFusionReactor.LocationCategory.PLASMA){
                foundPlasma = true;
                X+=d.x;
                Y+=d.y;
                Z+=d.z;
            }
            if(!foundPlasma)continue;
            Block b = reactor.getBlock(X, Y, Z);
            if(b==null)continue;
            if(b.isReflector()){
                if(!b.template.reflectorHasBaseStats&&b.recipe==null)continue;//empty reflector
                b.reflectorValid = true;
                efficiencyMult += (b.recipe==null?b.template.reflectorEfficiency:b.recipe.reflectorEfficiency)-1;
                breedingBlanketAugmented = true;
            }
        }
    }
    public void calculateHeatingBlanket(OverhaulFusionReactor reactor){
        if(!template.heatingBlanket)return;
        heatMult = 1;
        efficiency = 1;
        for(Direction d : Direction.values()){
            Block b = reactor.getBlock(x+d.x, y+d.y, z+d.z);
            if(b==null)continue;
            if(b.isBreedingBlanket()){
                if(!b.template.breedingBlanketHasBaseStats&&b.recipe==null)continue;//empty reflector
                efficiency+=(b.recipe==null?b.template.breedingBlanketEfficiency:b.recipe.breedingBlanketEfficiency)*b.efficiencyMult;
                heatMult+=(b.recipe==null?b.template.breedingBlanketEfficiency:b.recipe.breedingBlanketEfficiency)*b.efficiencyMult;
            }
        }
    }
    /**
     * Calculates the heatsink
     * @param reactor the reactor
     * @return <code>true</code> if the heatsink state has changed
     */
    public boolean calculateHeatsink(OverhaulFusionReactor reactor){
        if(!isHeatsink())return false;
        if(!template.heatsinkHasBaseStats&&recipe==null)return false;//empty heatsink
        boolean wasValid = heatsinkValid;
        for(AbstractPlacementRule<PlacementRule.BlockType, net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block> rule : template.rules){
            if(!rule.isValid(this, reactor)){
                heatsinkValid = false;
                return wasValid!=heatsinkValid;
            }
        }
        heatsinkValid = true;
        return wasValid!=heatsinkValid;
    }
    public boolean isFunctional(){
        if(canCluster()&&(cluster==null||!cluster.isCreated()))return false;
        return template.functional&&(isActive()||breedingBlanketValid);
    }
    @Override
    public void convertTo(Configuration to) throws MissingConfigurationEntryException{
        template = to.overhaul.fusion.convert(template);
        recipe = template.convert(recipe);
        configuration = to;
    }
    @Override
    public ArrayList<String> getSearchableNames(){
        ArrayList<String> searchables = template.getSearchableNames();
        for(String s : StringUtil.split(getListTooltip(), "\n"))searchables.add(s.trim());
        return searchables;
    }
    @Override
    public ArrayList<String> getSimpleSearchableNames(){
        return template.getSimpleSearchableNames();
    }
    @Override
    public net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Block getTemplate() {
        return template;
    }
    @Override
    public String getPinnedName(){
        return template.getPinnedName();
    }
    @Override
    public boolean hasRecipes(){
        return !template.allRecipes.isEmpty();
    }
    @Override
    public ArrayList<BlockRecipe> getRecipes(){
        return template.allRecipes;
    }
}