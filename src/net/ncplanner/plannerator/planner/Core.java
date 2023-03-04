package net.ncplanner.plannerator.planner;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Dialog;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.util.regex.RE;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.PartialConfiguration;
import net.ncplanner.plannerator.planner.file.FileWriter;
import net.ncplanner.plannerator.planner.file.NCPFFile;
import net.ncplanner.plannerator.planner.module.Module;
import net.ncplanner.plannerator.planner.theme.Theme;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
import net.ncplanner.plannerator.simplelibrary.image.Color;
import net.ncplanner.plannerator.simplelibrary.image.Image;
public class Core{
    public static final ArrayList<Multiblock> multiblocks = new ArrayList<>();
    public static final ArrayList<Multiblock> multiblockTypes = new ArrayList<>();
    public static HashMap<String, String> metadata = new HashMap<>();
    public static Configuration configuration = new Configuration(null, null, null);
    public static boolean recoveryMode = false;
    public static final ArrayList<String> pinnedStrs = new ArrayList<>();
    public static final ArrayList<Module> modules = new ArrayList<>();
    public static Theme theme = Theme.themes.get(0).get(0);
    private static final HashMap<Image, Boolean> alphas = new HashMap<>();
    public static boolean tutorialShown = false;
    public static boolean autoBuildCasing = true;
    public static String filename; //saved filename to default to when saving
    public static void resetMetadata(){
        metadata.clear();
        metadata.put("Name", "");
        metadata.put("Author", "");
    }
    public static String[] split(String str, String splitBy){
        ArrayList<String> splitArray = new ArrayList<>();
        StringTokenizer arr = new StringTokenizer(str, splitBy);//split by commas
        while(arr.hasMoreTokens())splitArray.add(arr.nextToken());
        return splitArray.toArray(new String[splitArray.size()]);
    }
    private static final String[] extraPossibilities = new String[]{"Got it", "Thanks", "Great", "Cool", "Alright", "Yep", "Awknowledged", "Aye", "Ignore", "Skip"};
    private static final Random rand = new Random();
    public static void showOKDialog(String title, String text){
        Dialog.show(title, text, rand.nextDouble()<.01?extraPossibilities[rand.nextInt(extraPossibilities.length)]:"OK", null);
    }
    public static boolean areImagesEqual(Image img1, Image img2) {
        if(img1==img2)return true;
        if(img1==null||img2==null)return false;
        if(img1.getWidth()!=img2.getWidth())return false;
        if(img1.getHeight()!=img2.getHeight())return false;
        for(int x = 0; x<img1.getWidth(); x++){
            for(int y = 0; y<img1.getHeight(); y++){
                if(img1.getRGB(x, y)!=img2.getRGB(x, y))return false;
            }
        }
        return true;
    }
    public static boolean hasAlpha(Image image){
        if(image==null)return false;
        if(!alphas.containsKey(image)){
            boolean hasAlpha = false;
            FOR:for(int x = 0; x<image.getWidth(); x++){
                for(int y = 0; y<image.getHeight(); y++){
                    if(new Color(image.getRGB(x, y)).getAlpha()!=255){
                        hasAlpha = true;
                        break FOR;
                    }
                }
            }
            alphas.put(image, hasAlpha);
        }
        return alphas.get(image);
    }
    public static void refreshModules(){
        multiblockTypes.clear();
        Configuration.clearConfigurations();
        for(Module m : modules){
            if(m.isActive()){
                m.addMultiblockTypes(multiblockTypes);
                m.addConfigurations();
            }
        }
    }
    public static void setTheme(Theme t){
        t.onSet();
        theme = t;
        UIManager.initNamedTheme("/theme", t.name);
    }
    public static void autosave(){
        FileSystemStorage fs = FileSystemStorage.getInstance();
        String file = fs.getAppHomePath()+"/autosave.ncpf";
        String cfgFile = fs.getAppHomePath()+"/config_autosave.ncpf";
        if(fs.exists(file))fs.delete(file);
        if(fs.exists(cfgFile))fs.delete(cfgFile);
        NCPFFile ncpf = new NCPFFile();//saving multiblocks first I guess
        ncpf.configuration = PartialConfiguration.generate(Core.configuration, Core.multiblocks);
        ncpf.multiblocks.addAll(Core.multiblocks);
        ncpf.metadata.putAll(Core.metadata);
        try(OutputStream stream = fs.openOutputStream(file)){
            FileWriter.write(ncpf, stream, FileWriter.NCPF);
        }catch(IOException ex){
            Log.e(ex);
        }
        //now the configuration
        try(OutputStream stream = fs.openOutputStream(cfgFile)){
            Config header = Config.newConfig();
            header.set("version", NCPFFile.SAVE_VERSION);
            header.set("count", 0);
            header.save(stream);
            Core.configuration.save(null, Config.newConfig()).save(stream);
        }catch(IOException ex){
            Log.e(ex);
        }
    }
    public static InputStream getInputStream(String path){
        String p = "";
        while(path.contains("/")){
            String[] split = new RE("\\/").split(path);
            if(p.isEmpty())p+="/"+split[0];
            else p+="_"+split[0];
            path = "";
            for(int i = 1; i<split.length-1; i++)path+=split[i]+"/";
            path += split[split.length-1];
        }
        try{
            return Resources.open(p+".res").getData(path);
        }catch(IOException ex){
            System.err.println("Couldn't read resource "+path+" in "+p+".res");
            return null;
        }
    }
}