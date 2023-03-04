package net.ncplanner.plannerator.multiblock.editor.decal;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.editor.Decal;
import net.ncplanner.plannerator.planner.Core;
public class MissingBladeDecal extends Decal{
    public MissingBladeDecal(int x, int y, int z){
        super(x, y, z);
    }
    @Override
    public void render(Renderer renderer, double x, double y, double blockSize){
        renderer.setColor(Core.theme.getDecalColorMissingBlade());
        renderer.fillRect(x+blockSize*.375, y+blockSize*.375, x+blockSize*.625, y+blockSize*.625);
    }
    @Override
    public String getTooltip(){
        return "Missing blade";
    }
}