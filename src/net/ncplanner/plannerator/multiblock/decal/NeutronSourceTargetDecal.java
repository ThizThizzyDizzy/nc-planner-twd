package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.multiblock.Direction;
import net.ncplanner.plannerator.planner.Core;
public class NeutronSourceTargetDecal extends Decal{
    private final Direction cellDirection;
    public NeutronSourceTargetDecal(int x, int y, int z, Direction cellDirection){
        super(x, y, z);
        this.cellDirection = cellDirection;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        Renderer renderer = new Renderer(g);
        g.setColor(Core.theme.getDecalColorNeutronSourceTarget().getRGB());
        switch(cellDirection){
            case NX:
                renderer.fillRect(x, y+blockSize/4, x+blockSize/8, y+blockSize*3/4);
                break;
            case NY:
                renderer.fillRect(x+blockSize/4, y+blockSize/4, x+blockSize*3/8, y+blockSize*3/8);//top left
                renderer.fillRect(x+blockSize/4, y+blockSize*5/8, x+blockSize*3/8, y+blockSize*3/4);//bottom left
                renderer.fillRect(x+blockSize*5/8, y+blockSize*5/8, x+blockSize*3/4, y+blockSize*3/4);//bottom right
                renderer.fillRect(x+blockSize*5/8, y+blockSize/4, x+blockSize*3/4, y+blockSize*3/8);//top right
                break;
            case NZ:
                renderer.fillRect(x+blockSize/4, y, x+blockSize*3/4, y+blockSize/8);
                break;
            case PX:
                renderer.fillRect(x+blockSize*7/8, y+blockSize/4, x+blockSize, y+blockSize*3/4);
                break;
            case PY:
                renderer.fillRect(x+blockSize*3/8, y+blockSize/4, x+blockSize*5/8, y+blockSize*3/8);//top
                renderer.fillRect(x+blockSize/4, y+blockSize*3/8, x+blockSize*3/8, y+blockSize*5/8);//left
                renderer.fillRect(x+blockSize*5/8, y+blockSize*3/8, x+blockSize*3/4, y+blockSize*5/8);//right
                renderer.fillRect(x+blockSize*3/8, y+blockSize*5/8, x+blockSize*5/8, y+blockSize*3/4);//bottom
                break;
            case PZ:
                renderer.fillRect(x+blockSize/4, y+blockSize*7/8, x+blockSize*3/4, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Targeted block ("+cellDirection.toString()+")";
    }
}