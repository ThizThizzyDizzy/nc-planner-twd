package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.multiblock.Direction;
import net.ncplanner.plannerator.planner.Core;
public class AdjacentModeratorDecal extends Decal{
    private final Direction cellDirection;
    public AdjacentModeratorDecal(int x, int y, int z, Direction cellDirection){
        super(x, y, z);
        this.cellDirection = cellDirection;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorAdjacentModerator().getRGB());
        Renderer renderer = new Renderer(g);
        switch(cellDirection){
            case NX:
                renderer.fillRect(x, y+blockSize/4, x+blockSize*3/16, y+blockSize*3/4);
                break;
            case NY:
                renderer.fillRect(x+blockSize/4, y+blockSize/4, x+blockSize*3/8, y+blockSize*3/8);//top left
                renderer.fillRect(x+blockSize/4, y+blockSize*5/8, x+blockSize*3/8, y+blockSize*3/4);//bottom left
                renderer.fillRect(x+blockSize*5/8, y+blockSize*5/8, x+blockSize*3/4, y+blockSize*3/4);//bottom right
                renderer.fillRect(x+blockSize*5/8, y+blockSize/4, x+blockSize*3/4, y+blockSize*3/8);//top right
                break;
            case NZ:
                renderer.fillRect(x+blockSize/4, y, x+blockSize*3/4, y+blockSize*3/16);
                break;
            case PX:
                renderer.fillRect(x+blockSize*13/16, y+blockSize/4, x+blockSize, y+blockSize*3/4);
                break;
            case PY:
                renderer.fillRect(x+blockSize*3/8, y+blockSize/4, x+blockSize*5/8, y+blockSize*3/8);//top
                renderer.fillRect(x+blockSize/4, y+blockSize*3/8, x+blockSize*3/8, y+blockSize*5/8);//left
                renderer.fillRect(x+blockSize*5/8, y+blockSize*3/8, x+blockSize*3/4, y+blockSize*5/8);//right
                renderer.fillRect(x+blockSize*3/8, y+blockSize*5/8, x+blockSize*5/8, y+blockSize*3/4);//bottom
                break;
            case PZ:
                renderer.fillRect(x+blockSize/4, y+blockSize*13/16, x+blockSize*3/4, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Adjacent to fuel cell ("+cellDirection.toString()+")";
    }
}