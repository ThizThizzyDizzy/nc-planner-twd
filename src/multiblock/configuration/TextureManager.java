package multiblock.configuration;
import com.codename1.ui.util.Resources;
import com.codename1.util.MathUtil;
import com.codename1.util.regex.RE;
import java.io.IOException;
import simplelibrary.image.Color;
import simplelibrary.image.Image;
public class TextureManager{
    public static Image getImage(String texture){
        String path = "";
        while(texture.contains("/")){
            String[] split = new RE("\\/").split(texture);
            path+="/"+split[0];
            for(int i = 2; i<split.length-2; i++)texture+=split[i]+"/";
            texture += split[split.length-1];
        }
        try{
            com.codename1.ui.Image img = Resources.open(path+".res").getImage(texture);
            return fromCN1(img);
        }catch(IOException ex){
            System.err.println("Couldn't read image "+texture+" in "+path+".res");
            return new Image(1, 1);
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
        Image image = new Image(img.getWidth(), img.getHeight());
        for(int x = 0; x<img.getWidth(); x++){
            for(int y = 0; y<img.getHeight(); y++){
                image.setRGB(x, y, img.getRGB()[y*image.getWidth()+x]);
            }
        }
        return image;
    }
    public static com.codename1.ui.Image toCN1(Image img){
        int[] rgb = new int[img.getWidth()*img.getHeight()];
        for(int y = 0; y<img.getHeight(); y++){
            for(int x = 0; x<img.getWidth(); x++){
                rgb[y*img.getWidth()+x] = img.getRGB(x, y);
            }
        }
        return com.codename1.ui.Image.createImage(rgb, img.getWidth(), img.getHeight());
    }
}