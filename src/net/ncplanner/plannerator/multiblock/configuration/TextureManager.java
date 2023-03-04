package net.ncplanner.plannerator.multiblock.configuration;
import com.codename1.ui.util.Resources;
import com.codename1.util.MathUtil;
import com.codename1.util.regex.RE;
import java.io.IOException;
import java.util.HashMap;
import net.ncplanner.plannerator.simplelibrary.image.Color;
import net.ncplanner.plannerator.simplelibrary.image.Image;
public class TextureManager{
    public static Image getImage(String texture){
        return fromCN1(getImageCN1(texture));
    }
    public static com.codename1.ui.Image getImageCN1(String texture){
        String path = "";
        while(texture.contains("/")){
            String[] split = new RE("\\/").split(texture);
            if(path.isEmpty())path+="/"+split[0];
            else path+="_"+split[0];
            texture = "";
            for(int i = 1; i<split.length-1; i++)texture+=split[i]+"/";
            texture += split[split.length-1];
        }
        try{
            return Resources.open(path+".res").getImage(texture+".png");
        }catch(IOException ex){
            System.err.println("Couldn't read image "+texture+" in "+path+".res");
            return com.codename1.ui.Image.createImage(1, 1);
        }
    }
    public static boolean SEPARATE_BRIGHT_TEXTURES = true;
    public static final float IMG_FAC = .003925f;
    public static final float IMG_POW = 2f;
    public static final float IMG_STRAIGHT_FAC = 1.5f;
    public static int convert(int c){
        if(SEPARATE_BRIGHT_TEXTURES){
            double f = IMG_FAC*MathUtil.pow(c, IMG_POW);
            float g = c/255f;
            double h = f*MathUtil.pow(g, IMG_STRAIGHT_FAC)+c*(1-MathUtil.pow(g, IMG_STRAIGHT_FAC));
            c = (int)h;
        }
        return c;
    }
    public static Color convert(Color color){
        return new Color(convert(color.getRed()), convert(color.getGreen()), convert(color.getBlue()), color.getAlpha());
    }
    public static Image convert(Image image){
        Image converted = new Image(image.getWidth(), image.getHeight());
        for(int x = 0; x<image.getWidth(); x++){
            for(int y = 0; y<image.getHeight(); y++){
                Color col = new Color(image.getRGB(x, y));
                converted.setRGB(x, y, convert(col).getRGB());
            }
        }
        return converted;
    }
    public static Image fromCN1(com.codename1.ui.Image img){
        if(img==null)return null;
        int[] rgb = img.getRGB();
        Image image = new Image(img.getWidth(), img.getHeight());
        for(int x = 0; x<img.getWidth(); x++){
            for(int y = 0; y<img.getHeight(); y++){
                image.setRGB(x, y, rgb[y*image.getWidth()+x]);
            }
        }
        return image;
    }
    private static HashMap<Image, com.codename1.ui.Image> cn1map = new HashMap<>();
    public static com.codename1.ui.Image toCN1(Image img){
        if(img==null)return null;
        if(cn1map.containsKey(img))return cn1map.get(img);
        int[] rgb = new int[img.getWidth()*img.getHeight()];
        for(int y = 0; y<img.getHeight(); y++){
            for(int x = 0; x<img.getWidth(); x++){
                rgb[y*img.getWidth()+x] = img.getRGB(x, y);
            }
        }
        com.codename1.ui.Image image = com.codename1.ui.Image.createImage(rgb, img.getWidth(), img.getHeight());
        cn1map.put(img, image);
        return image;
    }
}