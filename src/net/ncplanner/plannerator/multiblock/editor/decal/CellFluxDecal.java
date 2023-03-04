package net.ncplanner.plannerator.multiblock.editor.decal;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.editor.Decal;
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
    public void render(Renderer renderer, double x, double y, double blockSize){
        renderer.setColor(Core.theme.getDecalColorCellFlux(flux, criticality));
        renderer.fillRect(x, y+blockSize*.4, x+blockSize*Math.min(1, flux*1d/criticality), y+blockSize*.6);
        renderer.setColor(Core.theme.getDecalTextColor());
        renderer.drawCenteredText(x, y+blockSize*.4, x+blockSize, flux+"/"+criticality);//TODO proper font settings
    }
    @Override
    public String getTooltip(){
        return "Cell flux: "+flux+"/"+criticality;
    }
}