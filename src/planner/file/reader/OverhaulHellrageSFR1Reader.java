package planner.file.reader;
import com.codename1.util.regex.RE;
import java.io.InputStream;
import java.util.HashMap;
import multiblock.configuration.overhaul.fissionsfr.BlockRecipe;
import multiblock.overhaul.fissionsfr.OverhaulSFR;
import planner.Core;
import planner.file.FormatReader;
import planner.file.JSON;
import planner.file.NCPFFile;
public class OverhaulHellrageSFR1Reader implements FormatReader{
    @Override
    public boolean formatMatches(InputStream in){
        JSON.JSONObject hellrage = JSON.parse(in);
        JSON.JSONObject saveVersion = hellrage.getJSONObject("SaveVersion");
        int major = saveVersion.getInt("Major");
        int minor = saveVersion.getInt("Minor");
        int build = saveVersion.getInt("Build");
        JSON.JSONObject fuelCells = hellrage.getJSONObject("FuelCells");
        for(String name : fuelCells.keySet()){
            if(name.startsWith("[F4]"))return false;//that's an MSR!
        }
        return major==2&&minor==0&&build>=1&&build<=6;
    }
    @Override
    public synchronized NCPFFile read(InputStream in){
        JSON.JSONObject hellrage = JSON.parse(in);
        String dimS = hellrage.getString("InteriorDimensions");
        String[] dims = new RE(",").split(dimS);
        OverhaulSFR sfr = new OverhaulSFR(null, Integer.parseInt(dims[0]), Integer.parseInt(dims[1]), Integer.parseInt(dims[2]), Core.configuration.overhaul.fissionSFR.allCoolantRecipes.get(0));
        JSON.JSONObject heatSinks = hellrage.getJSONObject("HeatSinks");
        for(String name : heatSinks.keySet()){
            multiblock.configuration.overhaul.fissionsfr.Block block = null;
            for(multiblock.configuration.overhaul.fissionsfr.Block blok : Core.configuration.overhaul.fissionSFR.allBlocks){
                for(String nam : blok.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ", "heatsink", "liquid").equalsIgnoreCase(Core.superRemove(name.toLowerCase(), " ")))block = blok;
            }
            if(block==null)throw new IllegalArgumentException("Unknown block: "+name);
            JSON.JSONArray array = heatSinks.getJSONArray(name);
            for(Object blok : array){
                String blokLoc = (String) blok;
                String[] blockLoc = new RE(",").split(blokLoc);
                int x = Integer.parseInt(blockLoc[0]);
                int y = Integer.parseInt(blockLoc[1]);
                int z = Integer.parseInt(blockLoc[2]);
                sfr.setBlockExact(x, y, z, new multiblock.overhaul.fissionsfr.Block(Core.configuration, x, y, z, block));
            }
        }
        JSON.JSONObject moderators = hellrage.getJSONObject("Moderators");
        for(String name : moderators.keySet()){
            multiblock.configuration.overhaul.fissionsfr.Block block = null;
            for(multiblock.configuration.overhaul.fissionsfr.Block blok : Core.configuration.overhaul.fissionSFR.allBlocks){
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
                sfr.setBlockExact(x, y, z, new multiblock.overhaul.fissionsfr.Block(Core.configuration, x, y, z, block));
            }
        }
        multiblock.configuration.overhaul.fissionsfr.Block conductor = null;
        for(multiblock.configuration.overhaul.fissionsfr.Block blok : Core.configuration.overhaul.fissionSFR.allBlocks){
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
                sfr.setBlockExact(x, y, z, new multiblock.overhaul.fissionsfr.Block(Core.configuration, x, y, z, conductor));
            }
        }
        multiblock.configuration.overhaul.fissionsfr.Block cell = null;
        for(multiblock.configuration.overhaul.fissionsfr.Block blok : Core.configuration.overhaul.fissionSFR.allBlocks){
            if(blok.fuelCell)cell = blok;
        }
        if(cell==null)throw new IllegalArgumentException("Unknown block: Fuel Cell");
        JSON.JSONObject fuelCells = hellrage.getJSONObject("FuelCells");
        HashMap<multiblock.overhaul.fissionsfr.Block, multiblock.configuration.overhaul.fissionsfr.Block> sources = new HashMap<>();
        for(String name : fuelCells.keySet()){
            String[] fuelSettings = new RE(";").split(name);
            String fuelName = fuelSettings[0];
            boolean source = Boolean.parseBoolean(fuelSettings[1]);
            BlockRecipe fuel = null;
            for(BlockRecipe feul : cell.allRecipes){
                for(String nam : feul.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ").equalsIgnoreCase(Core.superRemove(fuelName.substring(4), " ")))fuel = feul;
            }
            if(fuelName.startsWith("[OX]"))fuelName = fuelName.substring(4)+" Oxide";
            if(fuelName.startsWith("[NI]"))fuelName = fuelName.substring(4)+" Nitride";
            if(fuelName.startsWith("[ZA]"))fuelName = fuelName.substring(4)+"-Zirconium Alloy";
            for(BlockRecipe feul : cell.allRecipes){
                for(String nam : feul.getLegacyNames())if(Core.superRemove(nam.toLowerCase(), " ").equalsIgnoreCase(Core.superRemove(fuelName, " ")))fuel = feul;
            }
            if(fuel==null)throw new IllegalArgumentException("Unknown fuel: "+name);
            multiblock.configuration.overhaul.fissionsfr.Block src = null;
            float highest = 0;
            for(multiblock.configuration.overhaul.fissionsfr.Block scr : Core.configuration.overhaul.fissionSFR.allBlocks){
                if(scr.source&&scr.sourceEfficiency>highest){
                    src = scr;
                    highest = src.sourceEfficiency;
                }
            }
            if(src==null)throw new IllegalArgumentException("Unknown block: "+name);
            JSON.JSONArray array = fuelCells.getJSONArray(name);
            for(Object blok : array){
                String blokLoc = (String) blok;
                String[] blockLoc = new RE(",").split(blokLoc);
                int x = Integer.parseInt(blockLoc[0]);
                int y = Integer.parseInt(blockLoc[1]);
                int z = Integer.parseInt(blockLoc[2]);
                sfr.setBlockExact(x, y, z, new multiblock.overhaul.fissionsfr.Block(Core.configuration, x, y, z, cell));
                sfr.getBlock(x, y, z).recipe = fuel;
                if(source)sources.put(sfr.getBlock(x, y, z), src);
            }
        }
        for(multiblock.overhaul.fissionsfr.Block key : sources.keySet()){
            key.addNeutronSource(sfr, sources.get(key));
        }
        NCPFFile file = new NCPFFile();
        sfr.buildDefaultCasingOnConvert();
        file.multiblocks.add(sfr);
        return file;
    }
}