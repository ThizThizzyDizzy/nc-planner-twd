package net.ncplanner.plannerator.planner.menu.component;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import net.ncplanner.plannerator.multiblock.Axis;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.Decal;
import net.ncplanner.plannerator.multiblock.EditorSpace;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.multiblock.overhaul.fissionmsr.OverhaulMSR;
import net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.OverhaulSFR;
import net.ncplanner.plannerator.multiblock.overhaul.fusion.OverhaulFusionReactor;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.editor.suggestion.Suggestion;
import net.ncplanner.plannerator.planner.menu.MenuEdit;
public class EditorGrid extends Container{
    private float resonatingAlpha = .375f;//not resonating
    private byte resonatingAlphaB = (byte)(resonatingAlpha*255);
    public final Axis axis;
    public final Axis xAxis;
    public final Axis yAxis;
    private final EditorSpace editorSpace;
    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;
    private final int blocksWide, blocksHigh;
    private Image plasma;
    private final Multiblock multiblock;
    public final int layer;
    public final MenuEdit editor;
    public int blockSize;
    public EditorGrid(int blockSize, MenuEdit editor, Multiblock multiblock, EditorSpace editorSpace, int x1, int y1, int x2, int y2, Axis axis, int layer){
        super(BoxLayout.y());
        this.multiblock = multiblock;
        this.axis = axis;
        this.layer = layer;
        this.editor = editor;
        this.blockSize = blockSize;
        this.editorSpace = editorSpace;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        blocksWide = x2-x1+1;
        blocksHigh = y2-y1+1;
        xAxis = axis.get2DXAxis();
        yAxis = axis.get2DYAxis();
        getStyle().setMargin(blockSize/4, blockSize/4, blockSize/4, blockSize/4);
        for(int yy = 0; yy<blocksHigh; yy++){
            int y = yy;
            Container row = new Container(BoxLayout.x());
            add(row);
            for(int xx = 0; xx<blocksWide; xx++){
                int x = xx;
                int bx = (x+x1)*xAxis.x+(y+y1)*yAxis.x+layer*axis.x;
                int by = (x+x1)*xAxis.y+(y+y1)*yAxis.y+layer*axis.y;
                int bz = (x+x1)*xAxis.z+(y+y1)*yAxis.z+layer*axis.z;
                row.add(new Button(){
                    {
                        addActionListener((evt) -> {
                            editor.getSelectedTool(0).mousePressed(this, editorSpace, bx, by, bz, 0);
                            editor.getSelectedTool(0).mouseReleased(this, editorSpace, bx, by, bz, 0);
                        });
                    }
                    @Override
                    protected Dimension calcPreferredSize(){
                        return new Dimension(blockSize, blockSize);
                    }
                    @Override
                    public void paint(Graphics g){
                        if(!multiblock.contains(bx, by, bz))return;//render nothing at all :D
                        int border = blockSize/32;
                        g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
                        g.fillRect(getX(), getY(), getWidth(), getHeight());
                        g.setColor(Core.theme.getEditorGridColor().getRGB());
                        g.fillRect(getX(), getY(), getWidth(), border);
                        g.fillRect(getX(), getY()+getHeight()-border, getWidth(), border);
                        g.fillRect(getX(), getY()+border, border, getHeight()-border*2);
                        g.fillRect(getX()+getWidth()-border, getY()+border, border, getHeight()-border*2);
                        Block block = multiblock.getBlock(bx, by, bz);
                        if(block!=null){
                            block.render(g, getX(), getY(), getWidth(), getWidth(), true, multiblock);
                            boolean recipeMatches = false;
                            if(multiblock instanceof OverhaulSFR){
                                net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.Block bl = (net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.Block)block;
                                if(bl.recipe!=null&&bl.recipe==editor.getSelectedOverhaulSFRBlockRecipe(0))recipeMatches = true;
                            }
                            if(multiblock instanceof OverhaulMSR){
                                net.ncplanner.plannerator.multiblock.overhaul.fissionmsr.Block bl = (net.ncplanner.plannerator.multiblock.overhaul.fissionmsr.Block)block;
                                if(bl.recipe!=null&&bl.recipe==editor.getSelectedOverhaulMSRBlockRecipe(0))recipeMatches = true;
                            }
                            if(multiblock instanceof OverhaulFusionReactor){
                                net.ncplanner.plannerator.multiblock.overhaul.fusion.Block bl = (net.ncplanner.plannerator.multiblock.overhaul.fusion.Block)block;
                                if(bl.recipe!=null&&bl.recipe==editor.getSelectedOverhaulFusionBlockRecipe(0))recipeMatches = true;
                            }
                            if(recipeMatches){
                                g.setColor(Core.theme.getSelectionColor().getRGB());
                                g.fillRect(getX(), getY(), getWidth(), getHeight(), resonatingAlphaB);
                            }
                        }
                        if(multiblock instanceof OverhaulFusionReactor&&((OverhaulFusionReactor)multiblock).getLocationCategory(bx, by, bz)==OverhaulFusionReactor.LocationCategory.PLASMA){
                            if(plasma==null){
                                plasma = TextureManager.getImage("overhaul/fusion/plasma");
                            }
                            g.drawImage(plasma, getX(), getY(), getWidth(), getHeight());
                        }
                        synchronized(multiblock.decals){
                            for(Object o : multiblock.decals){
                                Decal decal = (Decal)o;
                                if(decal.x==bx&&decal.y==by&&decal.z==bz){
                                    decal.render(g, getX(), getY(), blockSize);
                                }
                            }
                        }
                        if(isBlockSelected(x, y)){
                            g.setColor(Core.theme.getSelectionColor().getRGB());
                            g.setAlpha(127);
                            g.fillRect(getX(), getY(), getWidth(), getHeight());
                            g.setAlpha(255);
                            int brdr = blockSize/8;
                            boolean top = isBlockSelected(x, y-1);
                            boolean right = isBlockSelected(x+1, y);
                            boolean bottom = isBlockSelected(x, y+1);
                            boolean left = isBlockSelected(x-1, y);
                            if(!top||!left||!isBlockSelected(x-1, y-1)){//top left
                                g.fillRect(getX(), getY(), getX()+brdr, getY()+brdr);
                            }
                            if(!top){//top
                                g.fillRect(getX()+brdr, getY(), getX()+blockSize-brdr, getY()+brdr);
                            }
                            if(!top||!right||!isBlockSelected(x+1, y-1)){//top right
                                g.fillRect(getX()+blockSize-brdr, getY(), getX()+blockSize, getY()+brdr);
                            }
                            if(!right){//right
                                g.fillRect(getX()+blockSize-brdr, getY()+brdr, getX()+blockSize, getY()+blockSize-brdr);
                            }
                            if(!bottom||!right||!isBlockSelected(x+1, y+1)){//bottom right
                                g.fillRect(getX()+blockSize-brdr, getY()+blockSize-brdr, getX()+blockSize, getY()+blockSize);
                            }
                            if(!bottom){//bottom
                                g.fillRect(getX()+brdr, getY()+blockSize-brdr, getX()+blockSize-brdr, getY()+blockSize);
                            }
                            if(!bottom||!left||!isBlockSelected(x-1, y+1)){//bottom left
                                g.fillRect(getX(), getY()+blockSize-brdr, getX()+brdr, getY()+blockSize);
                            }
                            if(!left){//left
                                g.fillRect(getX(), getY()+brdr, getX()+brdr, getY()+blockSize-brdr);
                            }
                        }
                        //TODO there's a better way do do this, but this'll do for now
                        for(Suggestion s : editor.getSuggestions()){
                            if(affects(s, x, y)){
                                if(s.selected&&s.result!=null){
                                    Block b = s.result.getBlock(bx, by, bz);
                                    g.setColor(Core.theme.getWhiteColor().getRGB());
                                    g.setAlpha(resonatingAlphaB+127);
                                    if(b==null){
                                        g.fillRect(getX(), getY(), getX()+blockSize, getY()+blockSize);
                                    }else{
                                        b.render(g, getX(), getY(), blockSize, blockSize, false, resonatingAlpha+.5f, s.result);
                                    }
                                }
                                g.setColor(Core.theme.getSuggestionOutlineColor().getRGB());
                                g.setAlpha(255);
                                int brdr = blockSize*3/40;
                                if(!s.selected)brdr/=3;
                                boolean top = affects(s, x, y-1);
                                boolean right = affects(s, x+1, y);
                                boolean bottom = affects(s, x, y+1);
                                boolean left = affects(s, x-1, y);
                                if(!top||!left||!affects(s, x-1, y-1)){//top left
                                    g.fillRect(getX(), getY(), getX()+brdr, getY()+brdr);
                                }
                                if(!top){//top
                                    g.fillRect(getX()+brdr, getY(), getX()+blockSize-brdr, getY()+brdr);
                                }
                                if(!top||!right||!affects(s, x+1, y-1)){//top right
                                    g.fillRect(getX()+blockSize-brdr, getY(), getX()+blockSize, getY()+brdr);
                                }
                                if(!right){//right
                                    g.fillRect(getX()+blockSize-brdr, getY()+brdr, getX()+blockSize, getY()+blockSize-brdr);
                                }
                                if(!bottom||!right||!affects(s, x+1, y+1)){//bottom right
                                    g.fillRect(getX()+blockSize-brdr, getY()+blockSize-brdr, getX()+blockSize, getY()+blockSize);
                                }
                                if(!bottom){//bottom
                                    g.fillRect(getX()+brdr, getY()+blockSize-brdr, getX()+blockSize-brdr, getY()+blockSize);
                                }
                                if(!bottom||!left||!affects(s, x-1, y+1)){//bottom left
                                    g.fillRect(getX(), getY()+blockSize-brdr, getX()+brdr, getY()+blockSize);
                                }
                                if(!left){//left
                                    g.fillRect(getX(), getY()+brdr, getX()+brdr, getY()+blockSize-brdr);
                                }
                            }
                        }
                    }
                });
            }
        }
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        editor.getSelectedTool(0).drawGhosts(g, editorSpace, x1, y1, x2, y2, blocksWide, blocksHigh, axis, layer, getX(), getY(), getWidth(), getHeight(), blockSize, (editor.getSelectedBlock(0)==null?null:TextureManager.toCN1(editor.getSelectedBlock(0).getTexture())));
    }
    public boolean isBlockSelected(int x, int y){
        x+=x1;
        y+=y1;
        if(x<x1||y<y1||x>x2||y>y2)return false;
        int bx = x*xAxis.x+y*yAxis.x+layer*axis.x;
        int by = x*xAxis.y+y*yAxis.y+layer*axis.y;
        int bz = x*xAxis.z+y*yAxis.z+layer*axis.z;
        return editor.isSelected(0, bx, by, bz);
    }
    private boolean affects(Suggestion s, int x, int y){
        x+=x1;
        y+=y1;
        if(x<x1||y<y1||x>x2||y>y2)return false;
        int bx = x*xAxis.x+y*yAxis.x+layer*axis.x;
        int by = x*xAxis.y+y*yAxis.y+layer*axis.y;
        int bz = x*xAxis.z+y*yAxis.z+layer*axis.z;
        return s.affects(bx, by, bz);
    }
}