package net.ncplanner.plannerator.multiblock.ppe;
import net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.Block;
import net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.OverhaulSFR;
public class SFRFill extends PostProcessingEffect<OverhaulSFR>{
    private final Block block;
    public SFRFill(Block block){
        super("Fill with "+block.getDisplayName(), true, true, false);
        this.block = block;
    }
    @Override
    public void apply(OverhaulSFR multiblock){
        multiblock.forEachPosition((x, y, z) -> {
            if(multiblock.getBlock(x, y, z)==null||multiblock.getBlock(x, y, z).isConductor()||multiblock.getBlock(x, y, z).isInert())multiblock.setBlock(x, y, z, new net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.Block(multiblock.getConfiguration(), x, y, z, block));
        });
    }
}