package planner;
import com.codename1.ui.Dialog;
import com.codename1.ui.Graphics;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.MathUtil;
import com.codename1.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import multiblock.Multiblock;
import multiblock.configuration.Configuration;
import planner.module.FusionTestModule;
import planner.module.Module;
import planner.module.OverhaulModule;
import planner.module.PrimeFuelModule;
import planner.module.RainbowFactorModule;
import planner.module.UnderhaulModule;
import planner.theme.Theme;
import simplelibrary.image.Color;
import simplelibrary.image.Image;
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
    static{
        resetMetadata();
        modules.add(new UnderhaulModule());
        modules.add(new OverhaulModule());
        modules.add(new FusionTestModule());
        modules.add(new RainbowFactorModule());
        modules.add(new PrimeFuelModule());
    }
    public static void resetMetadata(){
        metadata.clear();
        metadata.put("Name", "");
        metadata.put("Author", "");
    }
    public static void drawCircle(Graphics g, double x, double y, double innerRadius, double outerRadius, Color color){
        g.setColor(color.getRGB());
        int resolution = (int)(2*Math.PI*outerRadius);//an extra *2 to account for wavy surface?
        double angle = 0;
        for(int i = 0; i<resolution; i++){
            int[] xp = new int[4];
            int[] yp = new int[4];
            double inX = x+Math.cos(Math.toRadians(angle-90))*innerRadius;
            double inY = y+Math.sin(Math.toRadians(angle-90))*innerRadius;
            xp[0] = (int)inX;
            yp[0] = (int)inY;
            double outX = x+Math.cos(Math.toRadians(angle-90))*outerRadius;
            double outY = y+Math.sin(Math.toRadians(angle-90))*outerRadius;
            xp[1] = (int)outX;
            yp[1] = (int)outY;
            angle+=(360d/resolution);
            if(angle>=360)angle-=360;
            outX = x+Math.cos(Math.toRadians(angle-90))*outerRadius;
            outY = y+Math.sin(Math.toRadians(angle-90))*outerRadius;
            xp[2] = (int)outX;
            yp[2] = (int)outY;
            inX = x+Math.cos(Math.toRadians(angle-90))*innerRadius;
            inY = y+Math.sin(Math.toRadians(angle-90))*innerRadius;
            xp[3] = (int)inX;
            yp[3] = (int)inY;
            g.drawPolygon(xp, yp, 4);
        }
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
    public static int logBase(int base, int n){
        return (int)(MathUtil.log(n)/MathUtil.log(base));
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
    public static String superRemove(String str, String... patterns){
        for(String s : patterns)str = StringUtil.replaceAll(str, s, "");
        return str;
    }
    public static String superReplace(String str, String... strs){
        for(int i = 0; i<strs.length; i+=2){
            str = StringUtil.replaceAll(str, strs[i], strs[i+1]);
        }
        return str;
    }
    public static String substring(StringBuilder sb, int min){
        for(int i = 0; i<min; i++)sb.deleteCharAt(0);
        return sb.toString();
    }
    public static void setTheme(Theme t){
        t.onSet();
        theme = t;
        UIManager.initNamedTheme("/theme", t.name);
    }
}