package net.ncplanner.plannerator.multiblock.editor.decal;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.editor.Decal;
import net.ncplanner.plannerator.planner.Core;
public class BlockInvalidDecal extends Decal{
    public BlockInvalidDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Renderer renderer, double x, double y, double blockSize){
        renderer.setColor(Core.theme.getDecalColorBlockInvalid(), .125f);
        renderer.fillRect(x, y, x+blockSize, y+blockSize);
    }
    @Override
    public String getTooltip(){
        return "Block is invalid";
    }
}