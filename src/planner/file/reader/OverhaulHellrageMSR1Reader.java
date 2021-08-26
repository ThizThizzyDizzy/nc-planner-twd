package planner.file.reader;
import com.codename1.util.StringUtil;
import com.codename1.util.regex.RE;
import java.io.InputStream;
import java.util.HashMap;
import multiblock.configuration.overhaul.fissionmsr.BlockRecipe;
import multiblock.overhaul.fissionmsr.OverhaulMSR;
import planner.Core;
import planner.file.FormatReader;
import planner.file.JSON;
import planner.file.NCPFFile;
public class OverhaulHellrageMSR1Reader implements FormatReader{
    @Override
    public boolean formatMatches(InputStream in){
        JSON.JSONObject hellrage = JSON.parse(in);
        JSON.JSONObject saveVersion = hellrage.getJSONObject("SaveVersion");
        int major = saveVersion.getInt("Major");
        int minor = saveVersion.getInt("Minor");
        int build = saveVersion.getInt("Build");
        JSON.JSONObject fuelVessels = hellrage.getJSONObject("FuelCells");
        for(String name : fuelVessels.keySet()){
            if(!name.startsWith("[F4]"))return false;//that's not an MSR!
        }
        return major==2&&minor==0&&build>=1&&build<=6;
    }
    @Override
    public synchronized NCPFFile read(InputStream in){
        JSON.JSONObject hellrage = JSON.parse(in);
        String dimS = hellrage.getString("InteriorDimensions");
        String[] dims = new RE(",").split(dimS);
        OverhaulMSR msr = new OverhaulMSR(null, Integer.parseInt(dims[0]), Integer.parseInt(dims[1]), Integer.parseInt(dims[2]));
        JSON.JSONObject heatSinks = hellrage.getJSONObject("HeatSinks");
        for(String name : heatSinks.keySet()){
            multiblock.configuration.overhaul.fissionmsr.Block block = null;
            for(multiblock.configuration.overhaul.fissionmsr.Block blok : Core.configuration.overhaul.fissionMSR.allBlocks){
                for(String nam : blok.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ", "coolant", "heater", "liquid").equalsIgnoreCase(Core.superReplace(name.toLowerCase(), "water", "standard", " ", "")))block = blok;
            }
            if(block==null)throw new IllegalArgumentException("Unknown block: "+name);
            JSON.JSONArray array = heatSinks.getJSONArray(name);
            for(Object blok : array){
                String blokLoc = (String) blok;
                String[] blockLoc = new RE(",").split(blokLoc);
                int x = Integer.parseInt(blockLoc[0]);
                int y = Integer.parseInt(blockLoc[1]);
                int z = Integer.parseInt(blockLoc[2]);
                msr.setBlockExact(x, y, z, new multiblock.overhaul.fissionmsr.Block(Core.configuration, x, y, z, block));
                if(block.heater&&!block.allRecipes.isEmpty())msr.getBlock(x,y,z).recipe = block.allRecipes.get(0);
            }
        }
        JSON.JSONObject moderators = hellrage.getJSONObject("Moderators");
        for(String name : moderators.keySet()){
            multiblock.configuration.overhaul.fissionmsr.Block block = null;
            for(multiblock.configuration.overhaul.fissionmsr.Block blok : Core.configuration.overhaul.fissionMSR.allBlocks){
                for(String nam : blok.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ", "moderator").equalsIgnoreCase(Core.superRemove(name, " ")))block = blok;
            }
            if(block==null)throw new IllegalArgumentException("Unknown block: "+name);
            JSON.JSONArray array = moderators.getJSONArray(name);
            for(Object blok : array){
                String blokLoc = (String) blok;
                String[] blockLoc = new RE(",").split(blokLoc);
                int x = Integer.parseInt(blockLoc[0]);
                int y = Integer.parseInt(blockLoc[1]);
                int z = Integer.parseInt(blockLoc[2]);
                msr.setBlockExact(x, y, z, new multiblock.overhaul.fissionmsr.Block(Core.configuration, x, y, z, block));
            }
        }
        multiblock.configuration.overhaul.fissionmsr.Block conductor = null;
        for(multiblock.configuration.overhaul.fissionmsr.Block blok : Core.configuration.overhaul.fissionMSR.allBlocks){
            for(String nam : blok.getLegacyNames())if(nam.equalsIgnoreCase("conductor"))conductor = blok;
        }
        if(conductor==null)throw new IllegalArgumentException("Unknown block: Conductor");
        JSON.JSONArray conductors = hellrage.getJSONArray("Conductors");
        if(conductors!=null){
            for(Object blok : conductors){
                String blokLoc = (String) blok;
                String[] blockLoc = new RE(",").split(blokLoc);
                int x = Integer.parseInt(blockLoc[0]);
                int y = Integer.parseInt(blockLoc[1]);
                int z = Integer.parseInt(blockLoc[2]);
                msr.setBlockExact(x, y, z, new multiblock.overhaul.fissionmsr.Block(Core.configuration, x, y, z, conductor));
            }
        }
        multiblock.configuration.overhaul.fissionmsr.Block vessel = null;
        for(multiblock.configuration.overhaul.fissionmsr.Block blok : Core.configuration.overhaul.fissionMSR.allBlocks){
            if(blok.fuelVessel)vessel = blok;
        }
        if(vessel==null)throw new IllegalArgumentException("Unknown block: Fuel Vessel");
        JSON.JSONObject fuelVessels = hellrage.getJSONObject("FuelCells");
        HashMap<multiblock.overhaul.fissionmsr.Block, multiblock.configuration.overhaul.fissionmsr.Block> sources = new HashMap<>();
        for(String name : fuelVessels.keySet()){
            String[] fuelSettings = new RE(";").split(name);
            String fuelName = fuelSettings[0];
            boolean source = Boolean.parseBoolean(fuelSettings[1]);
            BlockRecipe fuel = null;
            for(BlockRecipe feul : vessel.allRecipes){
                for(String nam : feul.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ").equalsIgnoreCase(Core.superRemove(fuelName.substring(4), " ")))fuel = feul;
            }
            if(fuelName.startsWith("[F4]"))fuelName = fuelName.substring(4)+" Fluoride";
            for(BlockRecipe feul : vessel.allRecipes){
                for(String nam : feul.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ").equalsIgnoreCase(Core.superRemove(fuelName, " ")))fuel = feul;
            }
            if(fuel==null)throw new IllegalArgumentException("Unknown fuel: "+name);
            multiblock.configuration.overhaul.fissionmsr.Block src = null;
            float highest = 0;
            for(multiblock.configuration.overhaul.fissionmsr.Block scr : Core.configuration.overhaul.fissionMSR.allBlocks){
                if(scr.source&&scr.sourceEfficiency>highest){
                    src = scr;
                    highest = src.sourceEfficiency;
                }
            }
            if(src==null)throw new IllegalArgumentException("Unknown block: "+name);
            JSON.JSONArray array = fuelVessels.getJSONArray(name);
            for(Object blok : array){
                String blokLoc = (String) blok;
                String[] blockLoc = new RE(",").split(blokLoc);
                int x = Integer.parseInt(blockLoc[0]);
                int y = Integer.parseInt(blockLoc[1]);
                int z = Integer.parseInt(blockLoc[2]);
                msr.setBlockExact(x, y, z, new multiblock.overhaul.fissionmsr.Block(Core.configuration, x, y, z, vessel));
                msr.getBlock(x, y, z).recipe = fuel;
                if(source)sources.put(msr.getBlock(x, y, z), src);
            }
        }
        for(multiblock.overhaul.fissionmsr.Block key : sources.keySet()){
            key.addNeutronSource(msr, sources.get(key));
        }
        NCPFFile file = new NCPFFile();
        msr.buildDefaultCasingOnConvert();
        file.multiblocks.add(msr);
        return file;
    }
}