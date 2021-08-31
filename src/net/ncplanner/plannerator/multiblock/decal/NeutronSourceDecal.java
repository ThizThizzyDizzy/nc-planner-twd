package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.multiblock.Direction;
import net.ncplanner.plannerator.planner.Core;
public class NeutronSourceDecal extends Decal{
    private final Direction sourceDirection;
    public NeutronSourceDecal(int x, int y, int z, Direction sourceDirection){
        super(x, y, z);
        this.sourceDirection = sourceDirection;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorNeutronSource().getRGB());
        switch(sourceDirection){
            case NX:
                g.fillRect(x, y+blockSize/4, x+blockSize/8, y+blockSize*3/4);
                break;
            case NY:
                g.fillRect(x+blockSize/4, y+blockSize/4, x+blockSize*3/8, y+blockSize*3/8);//top left
                g.fillRect(x+blockSize/4, y+blockSize*5/8, x+blockSize*3/8, y+blockSize*3/4);//bottom left
                g.fillRect(x+blockSize*5/8, y+blockSize*5/8, x+blockSize*3/4, y+blockSize*3/4);//bottom right
                g.fillRect(x+blockSize*5/8, y+blockSize/4, x+blockSize*3/4, y+blockSize*3/8);//top right
                break;
            case NZ:
                g.fillRect(x+blockSize/4, y, x+blockSize*3/4, y+blockSize/8);
                break;
            case PX:
                g.fillRect(x+blockSize*7/8, y+blockSize/4, x+blockSize, y+blockSize*3/4);
                break;
            case PY:
                g.fillRect(x+blockSize*3/8, y+blockSize/4, x+blockSize*5/8, y+blockSize*3/8);//top
                g.fillRect(x+blockSize/4, y+blockSize*3/8, x+blockSize*3/8, y+blockSize*5/8);//left
                g.fillRect(x+blockSize*5/8, y+blockSize*3/8, x+blockSize*3/4, y+blockSize*5/8);//right
                g.fillRect(x+blockSize*3/8, y+blockSize*5/8, x+blockSize*5/8, y+blockSize*3/4);//bottom
                break;
            case PZ:
                g.fillRect(x+blockSize/4, y+blockSize*7/8, x+blockSize*3/4, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Has neutron source ("+sourceDirection.toString()+")";
    }
}