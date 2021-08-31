package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.multiblock.Direction;
import net.ncplanner.plannerator.planner.Core;
public class NeutronSourceLineDecal extends Decal{
    private final Direction direction;
    public NeutronSourceLineDecal(int x, int y, int z, Direction direction){
        super(x, y, z);
        this.direction = direction;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        Renderer renderer = new Renderer(g);
        g.setColor(Core.theme.getDecalColorNeutronSourceLine().getRGB());
        switch(direction){
            case PX:
            case NX:
                renderer.fillRect(x, y+blockSize*3/8, x+blockSize, y+blockSize*5/8);
                break;
            case PY:
            case NY:
                renderer.fillRect(x+blockSize*3/8, y+blockSize*3/8, x+blockSize*5/8, y+blockSize*5/8);
                break;
            case PZ:
            case NZ:
                renderer.fillRect(x+blockSize*3/8, y, x+blockSize*5/8, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Neutron source path ("+direction.toString()+")";
    }
}