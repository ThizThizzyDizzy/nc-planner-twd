package net.ncplanner.plannerator.multiblock.editor.ppe;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.Multiblock;
public class ClearInvalid extends PostProcessingEffect{
    public ClearInvalid(){
        super("Remove Invalid Blocks", true, true, true);
    }
    @Override
    public void apply(Multiblock multiblock){
        multiblock.forEachPosition((x, y, z) -> {
            Block b = multiblock.getBlock(x, y, z);
            if(b==null)return;
            if(!b.isValid())multiblock.setBlock(x, y, z, null);
        });
    }
    @Override
    public boolean defaultEnabled(){
        return true;
    }
}