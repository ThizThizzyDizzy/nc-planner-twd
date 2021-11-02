package net.ncplanner.plannerator.planner.editor.tool;
import com.codename1.ui.Image;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Axis;
import net.ncplanner.plannerator.multiblock.editor.EditorSpace;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.editor.Editor;
public class SelectionTool extends EditorTool{
    public SelectionTool(Editor editor, int id){
        super(editor, id);
    }
    private int[] leftDragStart;
    private int[] leftDragEnd;
    private int[] rightDragStart;
    private int[] rightDragEnd;
    @Override
    public void render(Renderer renderer, double x, double y, double width, double height){
        renderer.setColor(Core.theme.getEditorToolTextColor());
        renderer.fillRect(x+width/10, y+height/10, x+width/3, y+height/6);
        renderer.fillRect(x+width/10, y+height/10, x+width/6, y+height/3);
        renderer.fillRect(x+width-width/10, y+height/10, x+width-width/3, y+height/6);
        renderer.fillRect(x+width-width/10, y+height/10, x+width-width/6, y+height/3);
        renderer.fillRect(x+width/10, y+height-height/10, x+width/3, y+height-height/6);
        renderer.fillRect(x+width/10, y+height-height/10, x+width/6, y+height-height/3);
        renderer.fillRect(x+width-width/10, y+height-height/10, x+width-width/3, y+height-height/6);
        renderer.fillRect(x+width-width/10, y+height-height/10, x+width-width/6, y+height-height/3);
    }
    @Override
    public void drawGhosts(Renderer renderer, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, double x, double y, double width, double height, int blockSize, Image texture){
        if(leftDragEnd!=null&&leftDragStart!=null){
            float border = 1/8f;
            int minBX = Math.min(leftDragStart[0], leftDragEnd[0]);
            int minBY = Math.min(leftDragStart[1], leftDragEnd[1]);
            int minBZ = Math.min(leftDragStart[2], leftDragEnd[2]);
            int maxBX = Math.max(leftDragStart[0], leftDragEnd[0]);
            int maxBY = Math.max(leftDragStart[1], leftDragEnd[1]);
            int maxBZ = Math.max(leftDragStart[2], leftDragEnd[2]);
            Axis xAxis = axis.get2DXAxis();
            Axis yAxis = axis.get2DYAxis();
            int minSX = minBX*xAxis.x+minBY*xAxis.y+minBZ*xAxis.z-x1;
            int minSY = minBX*yAxis.x+minBY*yAxis.y+minBZ*yAxis.z-y1;
            int maxSX = maxBX*xAxis.x+maxBY*xAxis.y+maxBZ*xAxis.z-x1;
            int maxSY = maxBX*yAxis.x+maxBY*yAxis.y+maxBZ*yAxis.z-y1;
            if(maxSX>=x1&&maxSY>=y1&&minSX<=x2&&minSY<=y2){
                minSX = Math.max(0,Math.min(x2-x1,minSX));
                minSY = Math.max(0,Math.min(y2-y1,minSY));
                maxSX = Math.max(0,Math.min(x2-x1,maxSX));
                maxSY = Math.max(0,Math.min(y2-y1,maxSY));
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
        }
        if(rightDragEnd!=null&&rightDragStart!=null){
            float border = 1/8f;
            int minBX = Math.min(rightDragStart[0], rightDragEnd[0]);
            int minBY = Math.min(rightDragStart[1], rightDragEnd[1]);
            int minBZ = Math.min(rightDragStart[2], rightDragEnd[2]);
            int maxBX = Math.max(rightDragStart[0], rightDragEnd[0]);
            int maxBY = Math.max(rightDragStart[1], rightDragEnd[1]);
            int maxBZ = Math.max(rightDragStart[2], rightDragEnd[2]);
            Axis xAxis = axis.get2DXAxis();
            Axis yAxis = axis.get2DYAxis();
            int minSX = minBX*xAxis.x+minBY*xAxis.y+minBZ*xAxis.z-x1;
            int minSY = minBX*yAxis.x+minBY*yAxis.y+minBZ*yAxis.z-y1;
            int maxSX = maxBX*xAxis.x+maxBY*xAxis.y+maxBZ*xAxis.z-x1;
            int maxSY = maxBX*yAxis.x+maxBY*yAxis.y+maxBZ*yAxis.z-y1;
            if(maxSX>=x1&&maxSY>=y1&&minSX<=x2&&minSY<=y2){
                minSX = Math.max(0,Math.min(x2-x1,minSX));
                minSY = Math.max(0,Math.min(y2-y1,minSY));
                maxSX = Math.max(0,Math.min(x2-x1,maxSX));
                maxSY = Math.max(0,Math.min(y2-y1,maxSY));
                int minSZ = minBX*axis.x+minBY*axis.y+minBZ*axis.z;
                int maxSZ = maxBX*axis.x+maxBY*axis.y+maxBZ*axis.z;
                if(layer>=minSZ&&layer<=maxSZ){
                    renderer.setColor(Core.theme.getSelectionColor());
                    renderer.fillRect(x+blockSize*minSX, y+blockSize*minSY, x+blockSize*(maxSX+1), y+blockSize*(border+minSY));//top
                    renderer.fillRect(x+blockSize*minSX, y+blockSize*(maxSY+1-border), x+blockSize*(maxSX+1), y+blockSize*(maxSY+1));//bottom
                    renderer.fillRect(x+blockSize*minSX, y+blockSize*(minSY+border), x+blockSize*(border+minSX), y+blockSize*(maxSY+1-border));//left
                    renderer.fillRect(x+blockSize*(maxSX+1-border), y+blockSize*(minSY+border), x+blockSize*(maxSX+1), y+blockSize*(maxSY+1-border));//right
                }
            }
        }
        renderer.setWhite();
    }
    @Override
    public void mouseReset(EditorSpace editorSpace, int button){
        if(button==0)leftDragStart = leftDragEnd = null;
        if(button==1)rightDragStart = rightDragEnd = null;
    }
    @Override
    public void mousePressed(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(!editor.isControlPressed(id)){
            editor.clearSelection(id);
        }
        if(editor.isShiftPressed(id)){
            if(button==0)editor.selectGroup(id, x,y,z);
            if(button==1)editor.deselectGroup(id, x,y,z);
            return;
        }
        if(editor.isAltPressed(id)){
            if(button==0)editor.selectCluster(id, x,y,z);
            if(button==1)editor.deselectCluster(id, x,y,z);
            return;
        }
        if(button==0)leftDragStart = new int[]{x,y,z};
        if(button==1)rightDragStart = new int[]{x,y,z};
    }
    @Override
    public void mouseReleased(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0&&leftDragStart!=null)editor.select(id, leftDragStart[0], leftDragStart[1], leftDragStart[2], x, y, z);
        if(button==1&&rightDragStart!=null)editor.deselect(id, rightDragStart[0], rightDragStart[1], rightDragStart[2], x, y, z);
        mouseReset(editorSpace, button);
    }
    @Override
    public void mouseDragged(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0)leftDragEnd = new int[]{x,y,z};
        if(button==1)rightDragEnd = new int[]{x,y,z};
    }
    @Override
    public boolean isEditTool(){
        return false;
    }
    @Override
    public String getTooltip(){
        return "Select tool (S)\nUse this to select areas of the reactor\nCtrl-click to select multiple selections\nShift-click to select Groups of blocks that require each other\nAlt-click to select clusters\nEdits can only be made inside of selections (unless there are no selections)\nPress Delete to delete the selected area\nPress Escape to clear selection";
    }
    @Override
    public void mouseMoved(Object obj, EditorSpace editorSpace, int x, int y, int z){}
    @Override
    public void mouseMovedElsewhere(Object obj, EditorSpace editorSpace){}
}