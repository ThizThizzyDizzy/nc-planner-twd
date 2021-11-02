package net.ncplanner.plannerator.multiblock;
import java.util.ArrayList;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.IBlockRecipe;
import net.ncplanner.plannerator.planner.Pinnable;
import net.ncplanner.plannerator.planner.Queue;
import net.ncplanner.plannerator.planner.StringUtil;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.simplelibrary.image.Color;
import net.ncplanner.plannerator.simplelibrary.image.Image;
public abstract class Block implements Pinnable{
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
    public <T extends Block> Queue<T> getAdjacent(Multiblock<T> multiblock){
        Queue<T> adjacent = new Queue<>();
        for(Direction direction : Direction.values()){
            if(!multiblock.contains(x+direction.x, y+direction.y, z+direction.z))continue;
            T b = multiblock.getBlock(x+direction.x, y+direction.y, z+direction.z);
            if(b!=null)adjacent.enqueue(b);
        }
        return adjacent;
    }
    public <T extends Block> Queue<T> getActiveAdjacent(Multiblock<T> multiblock){
        Queue<T> adjacent = new Queue<>();
        for(Direction direction : Direction.values()){
            if(!multiblock.contains(x+direction.x, y+direction.y, z+direction.z))continue;
            T b = multiblock.getBlock(x+direction.x, y+direction.y, z+direction.z);
            if(b!=null&&b.isActive())adjacent.enqueue(b);
        }
        return adjacent;
    }
    public abstract String getTooltip(Multiblock multiblock);
    public abstract String getListTooltip();
    public void render(Renderer renderer, double x, double y, double width, double height, boolean renderOverlay, Multiblock multiblock){
        render(renderer, x, y, width, height, renderOverlay, 1, multiblock);
    }
    public void render(Renderer renderer, double x, double y, double width, double height, boolean renderOverlay, float alpha, Multiblock multiblock){
        if(getTexture()==null){
            renderer.setColor(new Color(255,0,255));
            renderer.fillRect(x, y, x+width, y+height);
            renderer.setColor(new Color(0,0,0));
            renderer.fillRect(x, y, x+width/2, y+height/2);
            renderer.fillRect(x+width/2, y+height/2, x+width, y+height);
        }else{
            renderer.setWhite(alpha);
            renderer.drawImage(getTexture(), x, y, x+width, y+height);
        }
        if(renderOverlay)renderOverlay(renderer,x,y,width,height, multiblock);
    }
    public void renderGrayscale(Renderer renderer, double x, double y, double width, double height, boolean renderOverlay, Multiblock multiblock){
        renderGrayscale(renderer, x, y, width, height, renderOverlay, 1, multiblock);
    }
    public void renderGrayscale(Renderer renderer, double x, double y, double width, double height, boolean renderOverlay, float alpha, Multiblock multiblock){
        if(getGrayscaleTexture()==null){
            renderer.setColor(new Color(191,191,191));
            renderer.fillRect(x, y, x+width, y+height);
            renderer.setColor(new Color(0,0,0));
            renderer.fillRect(x, y, x+width/2, x+height/2);
            renderer.fillRect(x+width/2, y+height/2, x+width, x+height);
        }else{
            renderer.setWhite(alpha);
            renderer.drawImage(getGrayscaleTexture(), x, y, x+width, y+height);
        }
        if(renderOverlay)renderOverlay(renderer,x,y,width,height, multiblock);
    }
    public abstract void renderOverlay(Renderer renderer, double x, double y, double width, double height, Multiblock multiblock);
    public void drawCircle(Renderer renderer, double x, double y, double width, double height, Color color){
        renderer.setColor(color);
        renderer.drawCircle(x+width/2, y+height/2, width/4, width*3/8);
        renderer.setWhite();
    }
    public void drawOutline(Renderer renderer, double x, double y, double width, double height, Color color){
        renderer.setColor(color);
        drawOutline(renderer, x, y, width, height, 1/32d, color);
        renderer.setWhite();
    }
    public void drawOutline(Renderer renderer, double x, double y, double width, double height, double dInset, Color color){
        renderer.setColor(color);
        int inset = (int)(width*dInset);
        int outline = (int)(width*dInset*2);
        renderer.fillRect(x+inset, y+inset, width-inset*2, outline);
        renderer.fillRect(x+inset, y+height-inset-outline, width-inset*2, outline);
        renderer.fillRect(x+inset, y+inset+outline, outline, height-inset*2-outline*2);
        renderer.fillRect(x+width-inset-outline, y+inset+outline, outline, height-inset*2-outline*2);
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
    public abstract boolean canRequire(Block other);
    public abstract boolean requires(Block other, Multiblock mb);
    public abstract boolean canGroup();
    public abstract boolean canBeQuickReplaced();
    public boolean defaultEnabled(){
        return true;
    }
    public abstract Block copy();
    public abstract boolean isEqual(Block other);
    public boolean roughMatch(String blockNam){
        blockNam = StringUtil.toLowerCase(blockNam);
        if(blockNam.endsWith("s"))blockNam = blockNam.substring(0, blockNam.length()-1);
        blockNam = StringUtil.superRemove(StringUtil.replace(blockNam, "_", " "), "liquid ", " cooler", " heat sink", " heatsink", " sink", " neutron shield", " shield", " moderator", " coolant", " heater", "fuel ", " reflector");
        if(blockNam.endsWith("s"))blockNam = blockNam.substring(0, blockNam.length()-1);
        String blockName = getName();
        if(blockName.endsWith("s"))blockName = blockName.substring(0, blockName.length()-1);
        blockName = StringUtil.superRemove(StringUtil.replace(StringUtil.toLowerCase(blockName), "_", " "), "reactor ", "liquid ", " cooler", " heat sink", " heatsink", " sink", " neutron shield", " shield", " moderator", " coolant", " heater", "fuel ", " reflector");
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
    public abstract boolean hasRecipes();
    public abstract ArrayList<? extends IBlockRecipe> getRecipes();
}