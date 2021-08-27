package multiblock.decal;
import com.codename1.ui.Graphics;
import multiblock.Decal;
import planner.Core;
public class BlockInvalidDecal extends Decal{
    public BlockInvalidDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorBlockInvalid().getRGB());
        g.setAlpha(32);
        g.fillRect(x, y, x+blockSize, y+blockSize);
    }
    @Override
    public String getTooltip(){
        return "Block is invalid";
    }
}