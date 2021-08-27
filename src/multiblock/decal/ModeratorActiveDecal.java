package multiblock.decal;
import com.codename1.ui.Graphics;
import multiblock.Decal;
import multiblock.Direction;
import planner.Core;
public class ModeratorActiveDecal extends Decal{
    private final Direction cellDirection;
    public ModeratorActiveDecal(int x, int y, int z, Direction cellDirection){
        super(x, y, z);
        this.cellDirection = cellDirection;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorModeratorActive().getRGB());
        switch(cellDirection){
            case NX:
                g.fillRect(x, y+blockSize/4, x+blockSize*3/16, y+blockSize*3/4);
                break;
            case NY:
                g.fillRect(x+blockSize/4, y+blockSize/4, x+blockSize*3/8, y+blockSize*3/8);//top left
                g.fillRect(x+blockSize/4, y+blockSize*5/8, x+blockSize*3/8, y+blockSize*3/4);//bottom left
                g.fillRect(x+blockSize*5/8, y+blockSize*5/8, x+blockSize*3/4, y+blockSize*3/4);//bottom right
                g.fillRect(x+blockSize*5/8, y+blockSize/4, x+blockSize*3/4, y+blockSize*3/8);//top right
                break;
            case NZ:
                g.fillRect(x+blockSize/4, y, x+blockSize*3/4, y+blockSize*3/16);
                break;
            case PX:
                g.fillRect(x+blockSize*13/16, y+blockSize/4, x+blockSize, y+blockSize*3/4);
                break;
            case PY:
                g.fillRect(x+blockSize*3/8, y+blockSize/4, x+blockSize*5/8, y+blockSize*3/8);//top
                g.fillRect(x+blockSize/4, y+blockSize*3/8, x+blockSize*3/8, y+blockSize*5/8);//left
                g.fillRect(x+blockSize*5/8, y+blockSize*3/8, x+blockSize*3/4, y+blockSize*5/8);//right
                g.fillRect(x+blockSize*3/8, y+blockSize*5/8, x+blockSize*5/8, y+blockSize*3/4);//bottom
                break;
            case PZ:
                g.fillRect(x+blockSize/4, y+blockSize*13/16, x+blockSize*3/4, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Moderator Active ("+cellDirection.toString()+")";
    }
}