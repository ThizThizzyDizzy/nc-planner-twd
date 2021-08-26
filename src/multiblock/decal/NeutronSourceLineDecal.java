package multiblock.decal;
import com.codename1.ui.Graphics;
import multiblock.Decal;
import multiblock.Direction;
import planner.Core;
public class NeutronSourceLineDecal extends Decal{
    private final Direction direction;
    public NeutronSourceLineDecal(int x, int y, int z, Direction direction){
        super(x, y, z);
        this.direction = direction;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorNeutronSourceLine().getRGB());
        switch(direction){
            case PX:
            case NX:
                g.fillRect(x, y+blockSize*3/8, x+blockSize, y+blockSize*5/8);
                break;
            case PY:
            case NY:
                g.fillRect(x+blockSize*3/8, y+blockSize*3/8, x+blockSize*5/8, y+blockSize*5/8);
                break;
            case PZ:
            case NZ:
                g.fillRect(x+blockSize*3/8, y, x+blockSize*5/8, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Neutron source path ("+direction.toString()+")";
    }
}