package planner.editor.tool;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import multiblock.Axis;
import multiblock.EditorSpace;
import multiblock.action.SetblocksAction;
import planner.Core;
import planner.editor.Editor;
public class LineTool extends EditorTool{
    public LineTool(Editor editor, int id){
        super(editor, id);
    }
    private int[] leftDragStart;
    private int[] rightDragStart;
    private int[] leftDragEnd;
    private int[] rightDragEnd;
    @Override
    public void render(Graphics g, int x, int y, int width, int height){
        g.setColor(Core.theme.getEditorToolTextColor().getRGB());
        g.fillPolygon(new int[]{x+width/8, x+width/4, x+width*7/8, x+width*3/4}, new int[]{y+height*3/4, y+height*7/8, y+height/4, y+height/8}, 4);
    }
    @Override
    public void drawGhosts(Graphics g, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, int x, int y, int width, int height, int blockSize, Image texture){
        g.setColor(Core.theme.getWhiteColor().getRGB());
        g.setAlpha(127);
        if(leftDragEnd!=null&&leftDragStart!=null)raytrace(leftDragStart[0], leftDragStart[1], leftDragStart[2], leftDragEnd[0], leftDragEnd[1], leftDragEnd[2], (bx,by,bz) -> {
            if(!editorSpace.isSpaceValid(editor.getSelectedBlock(id), bx, by, bz))return;
            Axis xAxis = axis.get2DXAxis();
            Axis yAxis = axis.get2DYAxis();
            int sx = bx*xAxis.x+by*xAxis.y+bz*xAxis.z-x1;
            int sy = bx*yAxis.x+by*yAxis.y+bz*yAxis.z-y1;
            int sz = bx*axis.x+by*axis.y+bz*axis.z;
            if(sz!=layer)return;
            if(sx<x1||sx>x2)return;
            if(sy<y1||sy>y2)return;
            g.drawImage(texture, x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
        });
        g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
        if(rightDragEnd!=null&&rightDragStart!=null)raytrace(rightDragStart[0], rightDragStart[1], rightDragStart[2], rightDragEnd[0], rightDragEnd[1], rightDragEnd[2], (bx,by,bz) -> {
            Axis xAxis = axis.get2DXAxis();
            Axis yAxis = axis.get2DYAxis();
            int sx = bx*xAxis.x+by*xAxis.y+bz*xAxis.z-x1;
            int sy = bx*yAxis.x+by*yAxis.y+bz*yAxis.z-y1;
            int sz = bx*axis.x+by*axis.y+bz*axis.z;
            if(sz!=layer)return;
            if(sx<x1||sx>x2)return;
            if(sy<y1||sy>y2)return;
            g.fillRect(x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
        });
        g.setAlpha(255);
    }
    @Override
    public void mouseReset(EditorSpace editorSpace, int button){
        if(button==0)leftDragStart = leftDragEnd = null;
        if(button==1)rightDragStart = rightDragEnd = null;
    }
    @Override
    public void mousePressed(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0)leftDragStart = leftDragEnd = new int[]{x,y,z};
        if(button==1)rightDragStart = leftDragEnd = new int[]{x,y,z};
    }
    @Override
    public void mouseReleased(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0&&leftDragStart!=null){
            SetblocksAction set = new SetblocksAction(editor.getSelectedBlock(id));
            raytrace(leftDragStart[0], leftDragStart[1], leftDragStart[2], x, y, z, (X,Y,Z) -> {
                if(editorSpace.isSpaceValid(set.block, X, Y, Z))set.add(X, Y, Z);
            });
            editor.setblocks(id, set);
        }
        if(button==1&&rightDragStart!=null){
            SetblocksAction set = new SetblocksAction(null);
            raytrace(rightDragStart[0], rightDragStart[1], rightDragStart[2], x, y, z, (X,Y,Z) -> {
                set.add(X, Y, Z);
            });
            editor.setblocks(id, set);
        }
        mouseReset(editorSpace, button);
    }
    @Override
    public void mouseDragged(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0)leftDragEnd = new int[]{x,y,z};
        if(button==1)rightDragEnd = new int[]{x,y,z};
    }
    @Override
    public boolean isEditTool(){
        return true;
    }
    @Override
    public String getTooltip(){
        return "Line tool (L)\nUse this tool to draw blocks in a line through the multiblock\nHold CTRL to only place blocks where they are valid";
    }
    @Override
    public void mouseMoved(Object obj, EditorSpace editorSpace, int x, int y, int z){}
    @Override
    public void mouseMovedElsewhere(Object obj, EditorSpace editorSpace){}
}