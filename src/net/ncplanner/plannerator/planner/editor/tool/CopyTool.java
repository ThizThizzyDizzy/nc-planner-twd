package net.ncplanner.plannerator.planner.editor.tool;
import com.codename1.ui.Image;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Axis;
import net.ncplanner.plannerator.multiblock.editor.EditorSpace;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.editor.Editor;
public class CopyTool extends EditorTool{
    public CopyTool(Editor editor, int id){
        super(editor, id);
    }
    private int[] dragStart;
    private int[] dragEnd;
    @Override
    public void render(Renderer renderer, double x, double y, double width, double height){
        renderer.setColor(Core.theme.getEditorToolTextColor());
        renderer.fillRect(x+width*.35, y+height*.15, x+width*.8, y+height*.75);
        renderer.setColor(Core.theme.getEditorToolBackgroundColor());
        renderer.fillRect(x+width*.4, y+height*.2, x+width*.75, y+height*.7);
        renderer.setColor(Core.theme.getEditorToolTextColor());
        renderer.fillRect(x+width*.2, y+height*.25, x+width*.65, y+height*.85);
        renderer.setColor(Core.theme.getEditorToolBackgroundColor());
        renderer.fillRect(x+width*.25, y+height*.3, x+width*.6, y+height*.8);
    }
    @Override
    public void drawGhosts(Renderer renderer, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, double x, double y, double width, double height, int blockSize, Image texture){
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
                renderer.setColor(Core.theme.getSelectionColor(), .5f);
                renderer.fillRect(x+blockSize*minSX, y+blockSize*minSY, x+blockSize*(maxSX+1), y+blockSize*(maxSY+1));
                renderer.setColor(Core.theme.getSelectionColor());
                renderer.fillRect(x+blockSize*minSX, y+blockSize*minSY, x+blockSize*(maxSX+1), y+blockSize*(border+minSY));//top
                renderer.fillRect(x+blockSize*minSX, y+blockSize*(maxSY+1-border), x+blockSize*(maxSX+1), y+blockSize*(maxSY+1));//bottom
                renderer.fillRect(x+blockSize*minSX, y+blockSize*(minSY+border), x+blockSize*(border+minSX), y+blockSize*(maxSY+1-border));//left
                renderer.fillRect(x+blockSize*(maxSX+1-border), y+blockSize*(minSY+border), x+blockSize*(maxSX+1), y+blockSize*(maxSY+1-border));//right
            }
        }
        renderer.setWhite();
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