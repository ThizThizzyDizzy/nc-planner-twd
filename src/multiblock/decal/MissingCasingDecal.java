package multiblock.decal;
import com.codename1.ui.Graphics;
import multiblock.Decal;
import planner.Core;
public class MissingCasingDecal extends Decal{
    public MissingCasingDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorMissingCasing().getRGB());
        g.fillRect(x+blockSize*3/8, y+blockSize*3/8, x+blockSize*5/8, y+blockSize*5/8);
    }
    @Override
    public String getTooltip(){
        return "Missing casing";
    }
}