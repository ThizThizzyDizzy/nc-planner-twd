package planner.editor.tool;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import java.util.ArrayList;
import java.util.Iterator;
import multiblock.Axis;
import multiblock.EditorSpace;
import multiblock.action.PasteAction;
import multiblock.configuration.TextureManager;
import planner.Core;
import planner.editor.ClipboardEntry;
import planner.editor.Editor;
public class PasteTool extends EditorTool{
    private int mouseX;
    private int mouseY;
    private int mouseZ;
    public PasteTool(Editor editor, int id){
        super(editor, id);
    }
    @Override
    public void render(Graphics g, int x, int y, int width, int height){
        g.setColor(Core.theme.getEditorToolTextColor().getRGB());
        g.fillRect(x+width*7/20, y+height*3/20, x+width*4/5, y+height*3/4);
        g.setColor(Core.theme.getEditorToolBackgroundColor().getRGB());
        g.fillRect(x+width*2/5, y+height*1/5, x+width*3/4, y+height*7/10);
        g.setColor(Core.theme.getEditorToolTextColor().getRGB());
        g.fillRect(x+width/5, y+height/4, x+width*13/20, y+height*17/20);
        g.setColor(Core.theme.getEditorToolBackgroundColor().getRGB());
        g.fillRect(x+width/4, y+height*3/10, x+width*3/5, y+height*4/5);
    }
    @Override
    public void mouseReset(EditorSpace editorSpace, int button){}
    @Override
    public void mousePressed(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0){
            ArrayList<ClipboardEntry> clipboard = new ArrayList<>(editor.getClipboard(id));
            for(Iterator<ClipboardEntry> it = clipboard.iterator(); it.hasNext();){
                ClipboardEntry entry = it.next();
                if(!editorSpace.contains(x+entry.x, y+entry.y, z+entry.z)||!editorSpace.isSpaceValid(entry.block, x+entry.x, y+entry.y, z+entry.z))it.remove();
            }
            editor.action(new PasteAction(clipboard, x, y, z), true);
        }
    }
    @Override
    public void mouseReleased(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){}
    @Override
    public void mouseDragged(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){}
    @Override
    public boolean isEditTool(){
        return true;
    }
    @Override
    public void drawGhosts(Graphics g, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, int x, int y, int width, int height, int blockSize, Image texture){
        if(mouseX==-1||mouseY==-1||mouseZ==-1)return;
        synchronized(editor.getClipboard(id)){
            for(ClipboardEntry entry : editor.getClipboard(id)){
                int bx = entry.x+mouseX;
                int by = entry.y+mouseY;
                int bz = entry.z+mouseZ;
                if(!editorSpace.isSpaceValid(entry.block, bx, by, bz))continue;
                Axis xAxis = axis.get2DXAxis();
                Axis yAxis = axis.get2DYAxis();
                int sx = bx*xAxis.x+by*xAxis.y+bz*xAxis.z-x1;
                int sy = bx*yAxis.x+by*yAxis.y+bz*yAxis.z-y1;
                int sz = bx*axis.x+by*axis.y+bz*axis.z;
                if(sz!=layer)continue;
                if(sx<x1||sx>x2)continue;
                if(sy<y1||sy>y2)continue;
                if(entry.block==null){
                    g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
                    g.setAlpha(127);
                    g.fillRect(x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
                }else{
                    g.setColor(Core.theme.getWhiteColor().getRGB());
                    g.setAlpha(127);
                    g.drawImage(TextureManager.toCN1(entry.block.getTexture()), x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
                }
            }
        }
        g.setAlpha(255);
    }
    @Override
    public String getTooltip(){
        return "Paste tool\nSelect a region, and press Ctrl-X or Ctrl+C to cut or copy the selection and open this tool.\nThen click any location in your reactor to paste the selection in multiple places.\nPress Escape or select a different tool when done.\nPress Ctrl+V to ready the most recently copied selection";
    }
    @Override
    public void mouseMoved(Object obj, EditorSpace editorSpace, int x, int y, int z){
        mouseX = x;
        mouseY = y;
        mouseZ = z;
    }
    @Override
    public void mouseMovedElsewhere(Object obj, EditorSpace editorSpace){
        mouseX = mouseY = mouseZ = -1;
    }
}