package net.ncplanner.plannerator.planner.editor.tool;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import net.ncplanner.plannerator.multiblock.Axis;
import net.ncplanner.plannerator.multiblock.EditorSpace;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.editor.Editor;
public class CopyTool extends EditorTool{
    public CopyTool(Editor editor, int id){
        super(editor, id);
    }
    private int[] dragStart;
    private int[] dragEnd;
    @Override
    public void render(Graphics g, int x, int y, int width, int height){
        g.setColor(Core.theme.getEditorToolTextColor().getRGB());
        g.fillRect(x+width*7/20, y+height*3/20, x+width*4/5, y+height*3/4);
        g.setColor(Core.theme.getEditorToolBackgroundColor().getRGB());
        g.fillRect(x+width*2/5, y+height/5, x+width*3/4, y+height*7/10);
        g.setColor(Core.theme.getEditorToolTextColor().getRGB());
        g.fillRect(x+width/5, y+height/4, x+width*13/20, y+height*17/20);
        g.setColor(Core.theme.getEditorToolBackgroundColor().getRGB());
        g.fillRect(x+width/4, y+height*3/10, x+width*3/5, y+height*4/5);
    }
    @Override
    public void drawGhosts(Graphics g, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, int x, int y, int width, int height, int blockSize, Image texture){
        if(dragEnd!=null&&dragStart!=null){
            float border = 1/8f;
            int minBX = Math.min(dragStart[0], dragEnd[0]);
            int minBY = Math.min(dragStart[1], dragEnd[1]);
            int minBZ = Math.min(dragStart[2], dragEnd[2]);
            int maxBX = Math.max(dragStart[0], dragEnd[0]);
            int maxBY = Math.max(dragStart[1], dragEnd[1]);
            int maxBZ = Math.max(dragStart[2], dragEnd[2]);
            Axis xAxis = axis.get2DXAxis();
            Axis yAxis = axis.get2DYAxis();
            int minSX = Math.max(x1,Math.min(x2,minBX*xAxis.x+minBY*xAxis.y+minBZ*xAxis.z-x1));
            int minSY = Math.max(y1,Math.min(y2,minBX*yAxis.x+minBY*yAxis.y+minBZ*yAxis.z-y1));
            int maxSX = Math.max(x1,Math.min(x2,maxBX*xAxis.x+maxBY*xAxis.y+maxBZ*xAxis.z-x1));
            int maxSY = Math.max(y1,Math.min(y2,maxBX*yAxis.x+maxBY*yAxis.y+maxBZ*yAxis.z-y1));
            int minSZ = minBX*axis.x+minBY*axis.y+minBZ*axis.z;
            int maxSZ = maxBX*axis.x+maxBY*axis.y+maxBZ*axis.z;
            if(layer>=minSZ&&layer<=maxSZ){
                g.setColor(Core.theme.getSelectionColor().getRGB());
                g.setAlpha(127);
                g.fillRect(x+blockSize*minSX, y+blockSize*minSY, x+blockSize*(maxSX+1), y+blockSize*(maxSY+1));
                g.setColor(Core.theme.getSelectionColor().getRGB());
                g.setAlpha(255);
                g.fillRect(x+blockSize*minSX, y+blockSize*minSY, x+blockSize*(maxSX+1), y+(int)(blockSize*(border+minSY)));//top
                g.fillRect(x+blockSize*minSX, y+(int)(blockSize*(maxSY+1-border)), x+blockSize*(maxSX+1), y+blockSize*(maxSY+1));//bottom
                g.fillRect(x+blockSize*minSX, y+(int)(blockSize*(minSY+border)), x+(int)(blockSize*(border+minSX)), y+(int)(blockSize*(maxSY+1-border)));//left
                g.fillRect(x+(int)(blockSize*(maxSX+1-border)), y+(int)(blockSize*(minSY+border)), x+blockSize*(maxSX+1), y+(int)(blockSize*(maxSY+1-border)));//right
            }
        }
    }
    @Override
    public void mouseReset(EditorSpace editorSpace, int button){
        if(button==0)dragStart = dragEnd = null;
    }
    @Override
    public void mousePressed(Object layer, EditorSpace editorSpace, int x, int y, int z, int button){
        editor.clearSelection(id);
        if(button==0)dragStart = new int[]{x,y,z};
    }
    @Override
    public void mouseReleased(Object layer, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0&&dragStart!=null){
            editor.select(id, dragStart[0], dragStart[1], dragStart[2], x, y, z);
            editor.copySelection(id, (dragStart[0]+x)/2, (dragStart[1]+y)/2, (dragStart[2]+z)/2);
            editor.clearSelection(id);
        }
        mouseReset(editorSpace, button);
    }
    @Override
    public void mouseDragged(Object layer, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0)dragEnd = new int[]{x,y,z};
    }
    @Override
    public boolean isEditTool(){
        return false;
    }
    @Override
    public String getTooltip(){
        return "Copy tool\nUse this to select an area to copy\nOnce an area is selected, click to paste that selection";
    }
    @Override
    public void mouseMoved(Object obj, EditorSpace editorSpace, int x, int y, int z){}
    @Override
    public void mouseMovedElsewhere(Object obj, EditorSpace editorSpace){}
}