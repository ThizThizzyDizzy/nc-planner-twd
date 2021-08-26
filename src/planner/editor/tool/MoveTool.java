package planner.editor.tool;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import java.util.ArrayList;
import java.util.Iterator;
import multiblock.Axis;
import multiblock.Block;
import multiblock.BoundingBox;
import multiblock.EditorSpace;
import multiblock.action.CopyAction;
import multiblock.action.MoveAction;
import multiblock.configuration.TextureManager;
import planner.Core;
import planner.editor.Editor;
public class MoveTool extends EditorTool{
    public MoveTool(Editor editor, int id){
        super(editor, id);
    }
    private int[] leftDragStart;
    private int[] leftDragEnd;
    @Override
    public void render(Graphics g, int x, int y, int width, int height, int themeIndex){
        g.setColor(Core.theme.getEditorToolTextColor(themeIndex).getRGB());
        int w = width/16;
        int h = height/16;
        g.fillRect(x+width/2-w, y+height/4, x+width/2+w, y+height*3/4);
        g.fillRect(x+width/4, y+height/2-h, x+width*3/4, y+height/2+h);
        g.fillPolygon(new int[]{x+width/4+w, x+width/2, x+width*3/4-w}, new int[]{y+height/4, y+h, y+height/4}, 3);
        g.fillPolygon(new int[]{x+width/4+w, x+width/2, x+width*3/4-w}, new int[]{y+height*3/4, y+height-h, y+height*3/4}, 3);
        g.fillPolygon(new int[]{x+width/4, x+w, x+width/4}, new int[]{y+height/4+2, y+height/2, y+height*3/4-h}, 3);
        g.fillPolygon(new int[]{x+width*3/4, x+width-w, x+width*3/4}, new int[]{y+height/4+h, y+height/2, y+height*3/4-h}, 3);
    }
    @Override
    public void drawGhosts(Graphics g, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, int x, int y, int width, int height, int blockSize, Image texture){
        g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
        g.setAlpha(127);
        if(leftDragStart!=null&&leftDragEnd!=null){
            if(!editor.isControlPressed(id)){
                synchronized(editor.getSelection(id)){
                    for(int[] i : editor.getSelection(id)){
                        int bx = i[0];
                        int by = i[1];
                        int bz = i[2];
                        Axis xAxis = axis.get2DXAxis();
                        Axis yAxis = axis.get2DYAxis();
                        int sx = bx*xAxis.x+by*xAxis.y+bz*xAxis.z-x1;
                        int sy = bx*yAxis.x+by*yAxis.y+bz*yAxis.z-y1;
                        int sz = bx*axis.x+by*axis.y+bz*axis.z;
                        if(sz!=layer)continue;
                        if(sx<x1||sx>x2)continue;
                        if(sy<y1||sy>y2)continue;
                        g.fillRect(x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
                    }
                }
            }
            int[] diff = new int[]{leftDragEnd[0]-leftDragStart[0], leftDragEnd[1]-leftDragStart[1], leftDragEnd[2]-leftDragStart[2]};
            synchronized(editor.getSelection(id)){
                for(int[] i : editor.getSelection(id)){
                    int bx = i[0]+diff[0];
                    int by = i[1]+diff[1];
                    int bz = i[2]+diff[2];
                    BoundingBox bbox = editor.getMultiblock().getBoundingBox();
                    if(bx<bbox.x1||bx>bbox.x2)continue;
                    if(by<bbox.y1||by>bbox.y2)continue;
                    if(bz<bbox.z1||bz>bbox.z2)continue;
                    Axis xAxis = axis.get2DXAxis();
                    Axis yAxis = axis.get2DYAxis();
                    int sx = bx*xAxis.x+by*xAxis.y+bz*xAxis.z-x1;
                    int sy = bx*yAxis.x+by*yAxis.y+bz*yAxis.z-y1;
                    int sz = bx*axis.x+by*axis.y+bz*axis.z;
                    if(sz!=layer)continue;
                    if(sx<x1||sx>x2)continue;
                    if(sy<y1||sy>y2)continue;
                    Block b = editor.getMultiblock().getBlock(i[0], i[1], i[2]);
                    if(!editorSpace.isSpaceValid(b, bx, by, bz))continue;
                    if(b==null){
                        g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
                        g.fillRect(x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
                    }else{
                        g.setColor(Core.theme.getWhiteColor().getRGB());
                        g.drawImage(TextureManager.toCN1(b.getTexture()), x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
                    }
                }
            }
        }
        g.setAlpha(255);
    }
    @Override
    public void mouseReset(EditorSpace editorSpace, int button){
        if(button==0)leftDragStart = leftDragEnd = null;
    }
    @Override
    public void mousePressed(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0)leftDragStart = new int[]{x,y,z};
    }
    @Override
    public void mouseReleased(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(leftDragStart!=null&&leftDragEnd!=null){
            int dx = leftDragEnd[0]-leftDragStart[0], dy = leftDragEnd[1]-leftDragStart[1], dz = leftDragEnd[2]-leftDragStart[2];
            if(button==0&&leftDragStart!=null&&leftDragEnd!=null){
                ArrayList<int[]> selection = new ArrayList<>(editor.getSelection(id));
                for(Iterator<int[]> it = selection.iterator(); it.hasNext();){
                    int[] i = it.next();
                    Block b = editor.getMultiblock().getBlock(i[0], i[1], i[2]);
                    int bx = i[0]+dx;
                    int by = i[1]+dy;
                    int bz = i[2]+dz;
                    if(!editorSpace.isSpaceValid(b, bx, by, bz))it.remove();
                }
                if(editor.isControlPressed(id))editor.action(new CopyAction(editor, id, selection, editor.getSelection(id), dx, dy, dz), true);
                else editor.action(new MoveAction(editor, id, selection, editor.getSelection(id), dx, dy, dz), true);
            }
        }
        mouseReset(editorSpace, button);
    }
    @Override
    public void mouseDragged(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0)leftDragEnd = new int[]{x,y,z};
    }
    @Override
    public boolean isEditTool(){
        return false;
    }
    @Override
    public String getTooltip(){
        return "Move tool (M)\nUse this to move or copy selections\nHold Ctrl to copy selections\nHold Ctrl+Shift to copy selection, and keep the old selection";
    }
    @Override
    public void mouseMoved(Object obj, EditorSpace editorSpace, int x, int y, int z){}
    @Override
    public void mouseMovedElsewhere(Object obj, EditorSpace editorSpace){}
}