package multiblock.decal;
import com.codename1.ui.Graphics;
import multiblock.Axis;
import multiblock.Decal;
import planner.Core;
public class UnderhaulModeratorLineDecal extends Decal{
    private final Axis axis;
    public UnderhaulModeratorLineDecal(int x, int y, int z, Axis axis){
        super(x, y, z);
        this.axis = axis;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorUnderhaulModeratorLine().getRGB());
        switch(axis){
            case X:
                g.fillRect(x, y+blockSize*3/8, x+blockSize, y+blockSize*5/8);
                break;
            case Y:
                g.fillRect(x+blockSize*3/8, y+blockSize*3/8, x+blockSize*5/8, y+blockSize*5/8);
                break;
            case Z:
                g.fillRect(x+blockSize*3/8, y, x+blockSize*5/8, y+blockSize);
                break;
        }
    }
    @Override
    public String getTooltip(){
        return "Valid moderator line ("+axis.toString()+")";
    }
}