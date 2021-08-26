package multiblock;
import com.codename1.ui.Graphics;
import com.codename1.util.StringUtil;
import java.util.ArrayList;
import multiblock.configuration.Configuration;
import multiblock.configuration.TextureManager;
import planner.Core;
import planner.Pinnable;
import planner.exception.MissingConfigurationEntryException;
import simplelibrary.image.Color;
import simplelibrary.image.Image;
public abstract class Block extends MultiblockBit implements Pinnable{
    protected Configuration configuration;
    public int x;
    public int y;
    public int z;
    private Image grayscaleTexture = null;
    public Block(Configuration configuration, int x, int y, int z){
        this.configuration = configuration;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public abstract Block newInstance(int x, int y, int z);
    public abstract void copyProperties(Block other);
    public abstract Image getBaseTexture();
    public abstract Image getTexture();
    private Image getGrayscaleTexture(){
        if(grayscaleTexture!=null)return grayscaleTexture;
        Image img = getTexture();
        if(img==null)return null;
        Image grayscale = new Image(img.getWidth(), img.getHeight());
        for(int x = 0; x<img.getWidth(); x++){
            for(int y = 0; y<img.getHeight(); y++){
                Color c = img.getColor(x, y);
                grayscale.setColor(x, y, Color.fromHSB(c.getHue(), c.getSaturation()*.25f, c.getBrightness(), c.getAlpha()));
            }
        }
        return grayscaleTexture = grayscale;
    }
    public abstract String getName();
    public abstract void clearData();
    public <T extends Block> ArrayList<T> getAdjacent(Multiblock<T> multiblock){
        ArrayList<T> adjacent = new ArrayList<>();
        for(Direction direction : directions){
            if(!multiblock.contains(x+direction.x, y+direction.y, z+direction.z))continue;
            T b = multiblock.getBlock(x+direction.x, y+direction.y, z+direction.z);
            if(b!=null)adjacent.add(b);
        }
        return adjacent;
    }
    public <T extends Block> ArrayList<T> getActiveAdjacent(Multiblock<T> multiblock){
        ArrayList<T> adjacent = new ArrayList<>();
        for(Direction direction : directions){
            if(!multiblock.contains(x+direction.x, y+direction.y, z+direction.z))continue;
            T b = multiblock.getBlock(x+direction.x, y+direction.y, z+direction.z);
            if(b!=null&&b.isActive())adjacent.add(b);
        }
        return adjacent;
    }
    public abstract String getTooltip(Multiblock multiblock);
    public abstract String getListTooltip();
    public void render(Graphics g, int x, int y, int width, int height, boolean renderOverlay, Multiblock multiblock){
        render(g, x, y, width, height, renderOverlay, 1, multiblock);
    }
    public void render(Graphics g, int x, int y, int width, int height, boolean renderOverlay, float alpha, Multiblock multiblock){
        if(getTexture()==null){
            g.setColor(new Color(255,0,255).getRGB());
            g.fillRect(x, y, width, height);
            g.setColor(new Color(0,0,0).getRGB());
            g.fillRect(x, y, width/2, height/2);
            g.fillRect(x+width/2, y+height/2, width/2, height/2);
        }else{
            g.setColor(Core.theme.getWhiteColor().getRGB());
            g.drawImage(TextureManager.toCN1(getTexture()), x, y, width, height);
        }
        if(renderOverlay)renderOverlay(g, x, y,width,height, multiblock);
    }
    public void renderGrayscale(Graphics g, int x, int y, int width, int height, boolean renderOverlay, Multiblock multiblock){
        renderGrayscale(g, x, y, width, height, renderOverlay, 1, multiblock);
    }
    public void renderGrayscale(Graphics g, int x, int y, int width, int height, boolean renderOverlay, float alpha, Multiblock multiblock){
        if(getGrayscaleTexture()==null){
            g.setColor(new Color(191,191,191).getRGB());
            g.fillRect(x, y, width, height);
            g.setColor(new Color(0,0,0).getRGB());
            g.fillRect(x, y, width/2, height/2);
            g.fillRect(x+width/2, y+height/2, width/2, height/2);
        }else{
            g.setColor(Core.theme.getWhiteColor().getRGB());
            g.drawImage(TextureManager.toCN1(getGrayscaleTexture()), x, y, width, height);
        }
        if(renderOverlay)renderOverlay(g, x,y,width,height, multiblock);
    }
    public abstract void renderOverlay(Graphics g, int x, int y, int width, int height, Multiblock multiblock);
    public void drawCircle(Graphics g, double x, double y, double width, double height, Color color){
        Core.drawCircle(g, x+width/2, y+height/2, width*(4/16d), width*(6/16d), color);
    }
    public void drawOutline(Graphics g, int x, int y, int width, int height, Color color){
        drawOutline(g, x, y, width, height, 1/32d, color);
    }
    public void drawOutline(Graphics g, int x, int y, int width, int height, double dInset, Color color){
        g.setColor(color.getRGB());
        int inset = (int)(width*dInset);
        int outline = (int)(width*dInset*2);
        g.fillRect(x+inset, y+inset, width-inset*2, outline);
        g.fillRect(inset, y+height-inset-outline, width-inset*2, outline);
        g.fillRect(inset, inset+outline, outline, height-inset*2-outline*2);
        g.fillRect(width-inset-outline, inset+outline, outline, height-inset*2-outline*2);
    }
    public Block copy(int x, int y, int z){
        Block b = newInstance(x, y, z);
        copyProperties(b);
        return b;
    }
    public abstract boolean isValid();
    public abstract boolean isActive();
    public abstract boolean isCore();
    public abstract boolean hasRules();
    public abstract boolean calculateRules(Multiblock multiblock);
    public abstract boolean matches(Block template);
    public abstract boolean requires(Block other, Multiblock mb);
    public abstract boolean canGroup();
    public abstract boolean canBeQuickReplaced();
    public boolean defaultEnabled(){
        return true;
    }
    public abstract Block copy();
    public abstract boolean isEqual(Block other);
    public boolean roughMatch(String blockNam){
        blockNam = blockNam.toLowerCase();
        if(blockNam.endsWith("s"))blockNam = blockNam.substring(0, blockNam.length()-1);
        blockNam = Core.superRemove(StringUtil.replaceAll(blockNam, "_", " "), "liquid ", " cooler", " heat sink", " heatsink", " sink", " neutron shield", " shield", " moderator", " coolant", " heater", "fuel ", " reflector");
        if(blockNam.endsWith("s"))blockNam = blockNam.substring(0, blockNam.length()-1);
        String blockName = getName();
        if(blockName.endsWith("s"))blockName = blockName.substring(0, blockName.length()-1);
        blockName = Core.superRemove(StringUtil.replaceAll(blockName.toLowerCase(), "_", " "), "liquid ", " cooler", " heat sink", " heatsink", " sink", " neutron shield", " shield", " moderator", " coolant", " heater", "fuel ", " reflector");
        if(blockName.endsWith("s"))blockName = blockName.substring(0, blockName.length()-1);
        return blockNam.equalsIgnoreCase(blockName);
    }
    public boolean isFullBlock(){
        return true;
    }
    public Configuration getConfiguration(){
        return configuration;
    }
    public abstract void convertTo(Configuration to) throws MissingConfigurationEntryException;
    public boolean shouldRenderFace(Block against){
        return against==null;
    }
}