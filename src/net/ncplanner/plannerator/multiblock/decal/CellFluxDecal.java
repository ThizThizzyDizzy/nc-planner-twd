package net.ncplanner.plannerator.multiblock.decal;
import com.codename1.ui.Graphics;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.planner.Core;
public class CellFluxDecal extends Decal{
    private final int flux;
    private final int criticality;
    public CellFluxDecal(int x, int y, int z, int flux, int criticality){
        super(x, y, z);
        this.flux = flux;
        this.criticality = criticality;
    }
    @Override
    public void render(Graphics g, int x, int y, int blockSize){
        g.setColor(Core.theme.getDecalColorCellFlux(flux, criticality).getRGB());
        g.fillRect(x, y+blockSize*2/5, x+(int)(blockSize*Math.min(1, flux*1d/criticality)), y+blockSize*3/5);
        g.setColor(Core.theme.getDecalTextColor().getRGB());
        g.drawString(flux+"/"+criticality, x, y+blockSize*2/5);//TODO proper font settings
    }
    @Override
    public String getTooltip(){
        return "Cell flux: "+flux+"/"+criticality;
    }
}