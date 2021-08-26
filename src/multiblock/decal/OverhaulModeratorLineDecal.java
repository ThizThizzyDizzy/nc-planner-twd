package multiblock.decal;
import com.codename1.ui.Graphics;
import multiblock.Decal;
import multiblock.Direction;
import planner.Core;
public class OverhaulModeratorLineDecal extends Decal{
    private final Direction direction;
    private final int flux;
    private final float efficiency;
    public OverhaulModeratorLineDecal(int x, int y, int z, Direction direction, int flux, float efficiency){
        super(x, y, z);
        this.direction = direction;
        this.flux = flux;
        this.efficiency = efficiency;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorOverhaulModeratorLine(efficiency).getRGB());
        switch(direction){
            case PX:
                g.fillRect(x, y+blockSize*11/20, x+blockSize, y+blockSize*3/4);
                break;
            case NX:
                g.fillRect(x, y+blockSize/4, x+blockSize, y+blockSize*9/20);
                break;
            case PY:
                g.fillRect(x+blockSize*11/20, y+blockSize/4, x+blockSize*3/4, y+blockSize*9/20);
                break;
            case NY:
                g.fillRect(x+blockSize/4, y+blockSize*11/20, x+blockSize*9/20, y+blockSize*3/4);
                break;
            case PZ:
                g.fillRect(x+blockSize/4, y, x+blockSize*9/20, y+blockSize);
                break;
            case NZ:
                g.fillRect(x+blockSize*11/20, y, x+blockSize*3/4, y+blockSize);
                break;
        }
        g.setColor(Core.theme.getDecalTextColor().getRGB());
        //TODO proper font settings
        switch(direction){
            case PX:
                g.drawString(flux+"", -blockSize+x, y+blockSize*11/20);
                break;
            case NX:
                g.drawString(flux+"", -blockSize+x, y+blockSize/4);
                break;
            case PY:
                g.drawString(flux+"", -blockSize+x+blockSize*11/20, y+blockSize/4);
                break;
            case NY:
                g.drawString(flux+"", -blockSize+x+blockSize/4, y+blockSize*11/20);
                break;
            case PZ:
                g.drawString(flux+"", -blockSize+x+blockSize/4, y+blockSize*2/5);
                break;
            case NZ:
                g.drawString(flux+"", -blockSize+x+blockSize*11/20, y+blockSize*2/5);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return flux+" flux "+direction.toString()+", Eff: "+efficiency;
    }
}