package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.planner.Core;
public class BlockValidDecal extends Decal{
    public BlockValidDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorBlockValid().getRGB());
        g.setAlpha(32);
        g.fillRect(x, y, x+blockSize, y+blockSize);
    }
    @Override
    public String getTooltip(){
        return "Block is valid";
    }
}