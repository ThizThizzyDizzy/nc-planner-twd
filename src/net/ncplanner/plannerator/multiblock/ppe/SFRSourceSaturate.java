package net.ncplanner.plannerator.multiblock.ppe;
import net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.Block;
import net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.OverhaulSFR;
public class SFRSourceSaturate extends PostProcessingEffect<OverhaulSFR>{
    private final Block source;
    public SFRSourceSaturate(Block block){
        super("Saturate with "+block.getDisplayName(), true, false, true);
        this.source = block;
    }
    @Override
    public void apply(OverhaulSFR multiblock){
        multiblock.forEachPosition((x, y, z) -> {
            if(multiblock.getBlock(x, y, z)!=null&&multiblock.getBlock(x, y, z).template.fuelCell)multiblock.getBlock(x, y, z).addNeutronSource(multiblock, source);
        });
    }
    @Override
    public boolean defaultEnabled(){
        return source.sourceEfficiency==1;
    }
}