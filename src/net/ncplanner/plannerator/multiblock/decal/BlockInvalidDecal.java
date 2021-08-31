package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.planner.Core;
public class BlockInvalidDecal extends Decal{
    public BlockInvalidDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorBlockInvalid().getRGB());
        g.setAlpha(32);
        Renderer renderer = new Renderer(g);
        renderer.fillRect(x, y, x+blockSize, y+blockSize);
        g.setAlpha(255);
    }
    @Override
    public String getTooltip(){
        return "Block is invalid";
    }
}