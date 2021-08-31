package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.planner.Core;
public class MissingCasingDecal extends Decal{
    public MissingCasingDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorMissingCasing().getRGB());
        Renderer renderer = new Renderer(g);
        renderer.fillRect(x+blockSize*3/8, y+blockSize*3/8, x+blockSize*5/8, y+blockSize*5/8);
    }
    @Override
    public String getTooltip(){
        return "Missing casing";
    }
}