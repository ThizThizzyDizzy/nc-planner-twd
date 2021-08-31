package net.ncplanner.plannerator.multiblock;
import com.codename1.ui.Form;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import net.ncplanner.plannerator.generator.Priority;
import net.ncplanner.plannerator.multiblock.action.SetblockAction;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.ppe.PostProcessingEffect;
import net.ncplanner.plannerator.multiblock.symmetry.Symmetry;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.FormattedText;
import net.ncplanner.plannerator.planner.Task;
import net.ncplanner.plannerator.planner.editor.suggestion.Suggestor;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.planner.file.NCPFFile;
import net.ncplanner.plannerator.planner.module.Module;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
import net.ncplanner.plannerator.simplelibrary.config2.ConfigNumberList;
public abstract class Multiblock<T extends Block> extends MultiblockBit{
    public long lastChangeTime;
    public Stack<Action> history = new Stack<>();
    public Stack<Action> future = new Stack<>();
    public ArrayList<Action> queue = new ArrayList<>();
    public HashMap<String, String> metadata = new HashMap<>();
    public Boolean showDetails = null;//details override
    public Configuration configuration;
    private boolean calculated = false;
    public HashMap<Module, Object> moduleData = new HashMap<Module, Object>();
    public Task calculateTask;
    public final int[] dimensions;
    public final ArrayList<Decal> decals = new ArrayList<>();
    public boolean calculationPaused = false;//if calculation is incomplete and paused
    {
        resetMetadata();
        lastChangeTime = System.currentTimeMillis();
    }
    public void resetMetadata(){
        metadata.clear();
        metadata.put("Name", "");
        metadata.put("Author", "");
    }
    public ArrayList<BlockGrid<T>> blockGrids = new ArrayList<>();
    public Multiblock(Configuration configuration, int... dimensions){
        this.configuration = configuration;
        this.dimensions = dimensions;
        createBlockGrids();
    }
    protected abstract void createBlockGrids();
    public T getBlock(int x, int y, int z){
        for(BlockGrid<T> grid : blockGrids){
            if(grid.contains(x,y,z))return grid.getBlock(x,y,z);
        }
        throw new IndexOutOfBoundsException("Position ("+x+","+y+","+z+") is not in this multiblock! check contains(...) first!");
    }
    public void setBlock(int x, int y, int z, T block){
        setBlockExact(x, y, z, block==null?null:(T)block.copy(x, y, z));
    }
    public void setBlockExact(int x, int y, int z, T block){
        for(BlockGrid<T> grid : blockGrids){
            if(grid.contains(x,y,z)){
                grid.setBlock(x, y, z, block);//block==null?null:(T)block.copy(x, y, z));//dunno why this was the same as setBlock :thonk:
                return;
            }
        }
    }
    public boolean isEmpty(){
        return getBlocks().isEmpty();
    }
    public abstract String getDefinitionName();
    public abstract Multiblock<T> newInstance(Configuration configuration);
    public abstract Multiblock<T> newInstance(Configuration configuration, int... dimensions);
    public final Multiblock<T> newInstance(){
        return newInstance(Core.configuration);
    }
    public abstract void getAvailableBlocks(List<T> blocks);
    public final List<T> getAvailableBlocks(){
        ArrayList<T> list = new ArrayList<>();
        getAvailableBlocks(list);
        return list;
    }
    public void clearData(List<T> blocks){
        for(T t : blocks){
            t.clearData();
        }
        moduleData.clear();
    }
    public abstract void genCalcSubtasks();
    /**
     * Do a single calculation step. An existing calculation must be completed before another calculation may be run!
     * @param blocks the blocks to calculate
     * @return true if there are more steps (false if calculation is complete)
     */
    public abstract boolean doCalculationStep(List<T> blocks, boolean addDecals);
    private boolean calculationStep(List<T> blocks, boolean addDecals){
        synchronized(decals){
            decals.clear();
        }
        return doCalculationStep(blocks, addDecals);
    }
    public void calculate(List<T> blocks){
        calculateTask = new Task("Calculating Multiblock");
        genCalcSubtasks();
        while(calculationStep(blocks, false));
        calculationPaused = false;
        synchronized(decals){
            decals.clear();
        }
        for(Module m : Core.modules){
            if(m.isActive()){
                Object result = m.calculateMultiblock(this);
                if(result!=null)moduleData.put(m, result);
            }
        }
        calculated = true;
        calculateTask = null;
    }
    public void recalcStep(){
        List<T> blox = getBlocks();
        if(!calculationPaused){
            clearData(blox);
            validate();
        }
        calcStep(blox);
    }
    private void calcStep(List<T> blocks){
        if(calculateTask==null){
            calculateTask = new Task("Calculating Multiblock");
            genCalcSubtasks();
            return;
        }
        if(!calculationStep(blocks, true)){
            calculationPaused = false;
            synchronized(decals){
                decals.clear();
            }
            for(Module m : Core.modules){
                if(m.isActive()){
                    Object result = m.calculateMultiblock(this);
                    if(result!=null)moduleData.put(m, result);
                }
            }
            calculated = true;
            calculateTask = null;
        }else calculationPaused = true;
    }
    private ArrayList<T> blocksCache = null;
    private boolean forceRescan = false;
    public ArrayList<T> getBlocks(){
        return getBlocks(false);
    }
    public ArrayList<T> getBlocks(boolean rescan){
        if(blocksCache!=null&&!rescan&&!forceRescan)return blocksCache;
        ArrayList<T> lastBlox = new ArrayList<>();
        for(BlockGrid<T> grid : blockGrids){
            lastBlox.addAll(grid.getBlocks());
        }
        return blocksCache = lastBlox;
    }
    public abstract FormattedText getTooltip(boolean full);
    public FormattedText getFullTooltip(){
        return getTooltip(true);
    }
    public FormattedText getShortenedTooltip(){
        return getTooltip(false);
    }
    public String getModuleTooltip(){
        String s = "";
        for(Module m : moduleData.keySet()){
            String str = m.getTooltip(this, moduleData.get(m));
            if(str!=null)s+="\n"+str;
        }
        return s;
    }
    public FormattedText getSaveTooltip(){
        FormattedText s = new FormattedText(getConfiguration().getSaveName(!getDefinitionName().contains("Underhaul"))+"\n");
        for(String key : metadata.keySet()){
            if(key.equalsIgnoreCase("name")){
                s.addText(metadata.get(key)+"\n");
            }
        }
        s.addText(getDimensionsStr()+" "+getDefinitionName());
        for(String key : metadata.keySet()){
            if(key.equalsIgnoreCase("name"))continue;
            if(metadata.get(key)!=null)s.addText(key+": "+metadata.get(key)+"\n");
        }
        s.addText("\n");
        FormattedText extra = getExtraSaveTooltip();
        if(!extra.isEmpty()){
            s.addText(extra).addText("\n");
        }
        return s.addText(getTooltip(true));
    }
    protected FormattedText getExtraSaveTooltip(){
        return new FormattedText();
    }
    public final void save(NCPFFile ncpf, Configuration configuration, OutputStream stream) throws MissingConfigurationEntryException{
        int id = getMultiblockID();
        if(id==-1)return;
        Config config = Config.newConfig();
        config.set("id", id);
        Config meta = Config.newConfig();
        for(String key : metadata.keySet()){
            String value = metadata.get(key);
            if(value.trim().isEmpty())continue;
            meta.set(key,value);
        }
        if(meta.properties().length>0){
            config.set("metadata", meta);
        }
        ConfigNumberList dimensions = new ConfigNumberList();
        for(int i : this.dimensions)dimensions.add(i);
        config.set("dimensions", dimensions);
        forceRescan = true;
        save(ncpf, configuration, config);
        config.save(stream);
    }
    protected void save(NCPFFile ncpf, Configuration configuration, Config config) throws MissingConfigurationEntryException{}
    /**
     * Get the ID to use for saving
     * @return the ID as used in NCPF format, or -1 if this multiblock cannot be saved
     */
    public abstract int getMultiblockID();
    public abstract void convertTo(Configuration to) throws MissingConfigurationEntryException;
    /**
     * @return true if anything changed
     */
    public abstract boolean validate();
    public String getName(){
        for(String key : metadata.keySet()){
            if(key.equalsIgnoreCase("name"))return metadata.get(key);
        }
        return "";
    }
    public boolean exists(){
        for(Multiblock m : Core.multiblockTypes){
            if(m.getDefinitionName().equals(getDefinitionName())){
                return true;
            }
        }
        return false;
    }
    public void undo(boolean calculate){
        if(!history.isEmpty()){
            lastChangeTime = System.currentTimeMillis();
            Action a = history.pop();
            ActionResult result = a.undo(this);
            if(calculate)recalculate(result);
            future.push(a);
        }
    }
    public void redo(boolean calculate){
        if(!future.isEmpty()){
            lastChangeTime = System.currentTimeMillis();
            Action a = future.pop();
            ActionResult result = a.apply(this, true);
            if(calculate)recalculate(result);
            history.push(a);
        }
    }
    public void action(Action action, boolean calculate, boolean allowUndo){
        lastChangeTime = System.currentTimeMillis();
        ActionResult result = action.apply(this, allowUndo);
        if(calculate)recalculate(result);
        future.clear();
        if(allowUndo)history.push(action);
    }
    public void recalculate(){
        while(calculationPaused)recalcStep();
        List<T> blox = getBlocks();
        clearData(blox);
        validate();
        calculate(blox);
    }
    private void recalculate(ActionResult result){
        forceRescan = true;
        ArrayList<T> affectedGroups = result.getAffectedGroups();
        recalculate(affectedGroups==null?getBlocks(true):affectedGroups);
        forceRescan = false;
        calculateTask = null;
    }
    private void recalculate(ArrayList<T> blox){
        forceRescan = true;
        clearData(blox);
        if(validate()){
            recalculate();
            return;
        }
        calculate(blox);
        forceRescan = false;
    }
    public ArrayList<T> getGroup(T block){
        ArrayList<T> group = new ArrayList<>();
        if(block==null)return group;
        if(!block.canGroup())return null;
        group.add(block);
        boolean somethingChanged;
        do{
            somethingChanged = false;
            for(T blok : getBlocks()){
                if(group.contains(blok))continue;
                boolean req = false;
                for(T b : group){
                    if(b.requires(blok, this)||blok.requires(b, this))req = true;
                }
                if(req){
                    group.add(blok);
                    somethingChanged = true;
                }
            }
        }while(somethingChanged);
        return group;
    }
    ArrayList<ArrayList<T>> groupCache = new ArrayList<>();
    private ArrayList<T> getGroupWithCache(T block){
        for(ArrayList<T> group : groupCache){
            if(group.contains(block))return group;
        }
        ArrayList<T> group = getGroup(block);
        groupCache.add(group);
        return group;
    }
    public ArrayList<T> getAffectedGroups(ArrayList<T> blocks){
        ArrayList<T> group = new ArrayList<>();
        groupCache.clear();
        for(int i = 0; i<blocks.size(); i++){
            T block = blocks.get(i);
            ArrayList<T> g = getGroupWithCache(block);
            if(g==null)return getBlocks();
            for(T b : g){
                if(!group.contains(b))group.add(b);
            }
        }
        return group;
    }
    public ArrayList<Priority> getGenerationPriorities(){
        ArrayList<Priority> priorities = new ArrayList<>();
        getGenerationPriorities(priorities);
        return priorities;
    }
    public abstract void getGenerationPriorities(ArrayList<Priority> priorities);
    public ArrayList<Priority.Preset> getGenerationPriorityPresets(){
        return getGenerationPriorityPresets(getGenerationPriorities());
    }
    public ArrayList<Priority.Preset> getGenerationPriorityPresets(ArrayList<Priority> priorities){
        ArrayList<Priority.Preset> presets = new ArrayList<>();
        getGenerationPriorityPresets(priorities, presets);
        return presets;
    }
    public abstract void getGenerationPriorityPresets(ArrayList<Priority> priorities, ArrayList<Priority.Preset> presets);
    public ArrayList<Symmetry> getSymmetries(){
        ArrayList<Symmetry> symmetries = new ArrayList<>();
        getSymmetries(symmetries);
        return symmetries;
    }
    public abstract void getSymmetries(ArrayList<Symmetry> symmetries);
    public ArrayList<PostProcessingEffect> getPostProcessingEffects(){
        ArrayList<PostProcessingEffect> postProcessingEffects = new ArrayList<>();
        getPostProcessingEffects(postProcessingEffects);
        return postProcessingEffects;
    }
    public abstract void getPostProcessingEffects(ArrayList<PostProcessingEffect> postProcessingEffects);
    public void queueAction(Action action){
        queue.add(action);
    }
    public void performActions(boolean allowUndo){
        future.clear();
        ArrayList<T> affected = new ArrayList<>();
        while(!queue.isEmpty()){
            Action action = queue.remove(0);
            ActionResult result = action.apply(this, allowUndo);
            ArrayList<T> affectedGroups = result.getAffectedGroups();
            if(affected!=null){
                if(affectedGroups==null)affected = null;
                else affected.addAll(affectedGroups);
            }
            history.push(action);
        }
        if(affected==null){
            recalculate(getBlocks(true));
            return;
        }
        Set<T> actual = new HashSet<>();
        for(T t : affected){
            T b = getBlock(t.x, t.y, t.z);
            if(b==null)continue;
            actual.add(b);
        }
        recalculate(new ArrayList<>(actual));
    }
    public boolean isBetterThan(Multiblock other, ArrayList<Priority> priorities){
        for(Priority p : priorities){
            double result = p.compare(this, other);
            if(result>0)return true;
            if(result<0)return false;
        }
        return false;
    }
    public int compareTo(Multiblock other, ArrayList<Priority> priorities){
        for(Priority p : priorities){
            double result = p.compare(this, other);
            if(result>0)return 1;
            if(result<0)return -1;
        }
        return 0;
    }
    public abstract Multiblock<T> blankCopy();
    public Multiblock<T> copy(){
        Multiblock<T> copy = doCopy();
        copy.metadata = new HashMap<>(metadata);
        copy.moduleData = new HashMap<>(moduleData);
        copy.calculated = calculated;
        return copy;
    }
    public abstract Multiblock<T> doCopy();
    public long millisSinceLastChange(){
        return System.currentTimeMillis()-lastChangeTime;
    }
    public int count(Object o){
        if(o==null){
            int[] total = new int[1];
            forEachPosition((x,y,z) -> {
                Block block = getBlock(x,y,z);
                for(Action a : queue){
                    if(a instanceof SetblockAction){
                        SetblockAction set = (SetblockAction)a;
                        if(set.x==x&&set.y==y&&set.z==z)block = set.block;
                    }
                }
                if(block==null)total[0]++;
            });
            return total[0];
        }
        if(o instanceof Block){
            return getBlocks((T)o);
        }
        return doCount(o);
    }
    protected abstract int doCount(Object o);
    public int getBlocks(T type){
        int[] total = new int[1];
        forEachPosition((x, y, z) -> {
            Block block = getBlock(x, y, z);
            for(Action a : queue){
                if(a instanceof SetblockAction){
                    SetblockAction set = (SetblockAction)a;
                    if(set.x==x&&set.y==y&&set.z==z)block = set.block;
                }
            }
            if(block==null)return;
            if(block.isEqual(type))total[0]++;
        });
        return total[0];
    }
    public abstract String getGeneralName();
    protected abstract boolean isCompatible(Multiblock<T> other);
    public boolean isShapeEqual(Multiblock<T> other){
        if(blockGrids.size()!=other.blockGrids.size())return false;
        for(int i = 0; i<blockGrids.size(); i++){
            BlockGrid grid = blockGrids.get(i);
            BlockGrid otherGrid = other.blockGrids.get(i);
            if(grid.x!=otherGrid.x)return false;
            if(grid.y!=otherGrid.y)return false;
            if(grid.z!=otherGrid.z)return false;
            if(grid.getWidth()!=otherGrid.getWidth())return false;
            if(grid.getHeight()!=otherGrid.getHeight())return false;
            if(grid.getDepth()!=otherGrid.getDepth())return false;
        }
        return isCompatible(other);
    }
    public Configuration getConfiguration(){
        if(configuration==null)return Core.configuration;//TODO maybe force it to have a specific configuration?
        return configuration;
    }
    public final ArrayList<FluidStack> getFluidOutputs(){
        ArrayList<FluidStack> outputs = new ArrayList<>();
        getFluidOutputs(outputs);
        return outputs;
    }
    protected abstract void getFluidOutputs(ArrayList<FluidStack> outputs);
    public final ArrayList<PartCount> getPartsList(){
        ArrayList<PartCount> parts = new ArrayList<>();
        getMainParts(parts);
        getExtraParts(parts);
        Collections.sort(parts);
        return parts;
    }
    protected void getMainParts(ArrayList<PartCount> parts){
        HashMap<T, Integer> blocks = new HashMap<>();
        FOR:for(T block : getBlocks(true)){
            for(T t : blocks.keySet()){
                if(t.isEqual(block)){
                    blocks.put(t, blocks.get(t)+1);
                    continue FOR;
                }
            }
            blocks.put(block, 1);
        }
        for(T block : blocks.keySet()){
            parts.add(new PartCount(block.getTexture(), block.getName(), blocks.get(block)));
        }
    }
    protected abstract void getExtraParts(ArrayList<PartCount> parts);
    public boolean isValid(Block block, int x, int y, int z){
        Block b = block.newInstance(x, y, z);
        return b.hasRules()&&b.calculateRules(this);
    }
    public boolean isValid(Block block, int x, int y, int z, T assumingBlock, int assumingX, int assumingY, int assumingZ){
        T was = getBlock(assumingX, assumingY, assumingZ);
        setBlockExact(assumingX, assumingY, assumingZ, assumingBlock);
        Block b = block.newInstance(x, y, z);
        boolean ret = b.hasRules()&&b.calculateRules(this);
        setBlockExact(assumingX, assumingY, assumingZ, was);
        return ret;
    }
    public abstract String getDescriptionTooltip();
    public float get3DPreviewScale(){
        return 1;
    }
    public boolean areBlocksEqual(Multiblock other){
        if(!isShapeEqual(other))return false;
        boolean[] isEqual = new boolean[1];
        isEqual[0] = true;
        forEachPosition((x, y, z) -> {
            if(!isEqual[0])return;
            Block a = getBlock(x, y, z);
            Block b = other.getBlock(x, y, z);
            if(a==b)return;//all good
            if(a==null||b==null)isEqual[0] = false;//if they were both null, a==b already caught it
            if(!a.isEqual(b))isEqual[0] = false;
        });
        return isEqual[0];
    }
    public ArrayList<Suggestor> getSuggestors(){
        ArrayList<Suggestor> suggestors = new ArrayList<>();
        getSuggestors(suggestors);
        return suggestors;
    }
    public abstract void getSuggestors(ArrayList<Suggestor> suggestors);
    public boolean isCalculated(){
        return calculated;
    }
    public Task getTask(){
        return calculateTask;
    }
    public ArrayList<EditorSpace<T>> getEditorSpaces(){
        ArrayList<EditorSpace<T>> spaces = new ArrayList<>();
        getEditorSpaces(spaces);
        return spaces;
    }
    public abstract void getEditorSpaces(ArrayList<EditorSpace<T>> editorSpaces);
    public void forEachPosition(BlockPosConsumer func){
        for(BlockGrid<T> grid : blockGrids){
            grid.forEachPosition(func);
        }
    }
    public BoundingBox getBoundingBox(){
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for(BlockGrid grid : blockGrids){
            minX = Math.min(minX,grid.x);
            minY = Math.min(minY,grid.y);
            minZ = Math.min(minZ,grid.z);
            maxX = Math.max(maxX, grid.x+grid.getWidth()-1);
            maxY = Math.max(maxY, grid.y+grid.getHeight()-1);
            maxZ = Math.max(maxZ, grid.z+grid.getDepth()-1);
        }
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    public boolean contains(int x, int y, int z){
        for(BlockGrid grid : blockGrids){
            if(grid.contains(x, y, z))return true;
        }
        return false;
    }
    public String getDimensionsStr(){
        return Arrays.toString(dimensions);
    }
    public int getTotalVolume(){
        int volume = 0;
        for(BlockGrid grid : blockGrids){
            volume+=grid.getVolume();
        }
        return volume;
    }
    public void clearCaches(){
        forceRescan = true;
        groupCache.clear();
    }
    public abstract String getPreviewTexture();
    /**
     *initialize a new multiblock, filling it with default features
     */
    public void init(){}
    public abstract Form getResizeMenu();
}