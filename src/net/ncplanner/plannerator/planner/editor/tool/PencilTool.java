package net.ncplanner.plannerator.planner.editor.tool;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import java.util.ArrayList;
import net.ncplanner.plannerator.multiblock.Axis;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.EditorSpace;
import net.ncplanner.plannerator.multiblock.action.SetblocksAction;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.editor.Editor;
public class PencilTool extends EditorTool{
    public PencilTool(Editor editor, int id){
        super(editor, id);
    }
    private int[] leftDragStart;
    private Object leftStart = null;
    private int[] rightDragStart;
    private Object rightStart = null;
    private ArrayList<int[]> leftSelectedBlocks = new ArrayList<>();
    private ArrayList<int[]> rightSelectedBlocks = new ArrayList<>();
    @Override
    public void render(Graphics g, int x, int y, int width, int height){
        g.setColor(Core.theme.getEditorToolTextColor().getRGB());
        g.fillPolygon(new int[]{x+width/4, x+width*3/8, x+width/4}, new int[]{y+height*3/4, y+height*3/4, y+height*5/8}, 3);
        g.fillPolygon(new int[]{x+width*2/5, x+width*11/40, x+width/2, x+width*5/8}, new int[]{y+height*29/40, y+height*3/5, y+height*3/8, y+height/2}, 4);
        g.fillPolygon(new int[]{x+width*21/40, x+width*13/20, x+width*3/4, x+width*5/8}, new int[]{y+height*7/20, y+height*19/40, y+height*3/8, y+height/4}, 4);
    }
    @Override
    public void mouseReset(EditorSpace editorSpace, int button){
        if(button==0&&leftDragStart==null)return;
        if(button==1&&rightDragStart==null)return;
        mouseReleased(null, editorSpace, 0, 0, 0, button);//allow you to release outside the editor grid and still place blocks
    }
    @Override
    public void mousePressed(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        Block selected = editor.getSelectedBlock(id);
        if(button==0){
            synchronized(leftSelectedBlocks){
                if(editorSpace.isSpaceValid(selected, x, y, z))leftSelectedBlocks.add(new int[]{x,y,z});
                leftDragStart = new int[]{x,y,z};
                leftStart = obj;
            }
        }
        if(button==1){
            synchronized(rightSelectedBlocks){
                rightSelectedBlocks.add(new int[]{x,y,z});
                rightDragStart = new int[]{x,y,z};
                rightStart = obj;
            }
        }
    }
    @Override
    public void mouseReleased(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0){
            SetblocksAction set = new SetblocksAction(editor.getSelectedBlock(id));
            synchronized(leftSelectedBlocks){
                for(int[] i : leftSelectedBlocks){
                    set.add(i[0], i[1], i[2]);
                }
            }
            editor.setblocks(id, set);
        }
        if(button==1){
            SetblocksAction set = new SetblocksAction(null);
            synchronized(rightSelectedBlocks){
                for(int[] i : rightSelectedBlocks){
                    set.add(i[0], i[1], i[2]);
                }
            }
            editor.setblocks(id, set);
        }
        if(button==0){
            leftDragStart = null;
            leftStart = null;
            synchronized(leftSelectedBlocks){
                leftSelectedBlocks.clear();
            }
        }
        if(button==1){
            rightDragStart = null;
            rightStart = null;
            synchronized(rightSelectedBlocks){
                rightSelectedBlocks.clear();
            }
        }
    }
    @Override
    public void mouseDragged(Object obj, EditorSpace editorSpace, int x, int y, int z, int button){
        if(button==0){
            if(obj!=leftStart){
                leftDragStart = new int[]{x,y,z};
                leftStart = obj;
            }
            if(leftDragStart!=null){
                if(leftDragStart[0]==x&&leftDragStart[1]==y&&leftDragStart[2]==z)return;
                raytrace(leftDragStart[0], leftDragStart[1], leftDragStart[2], x, y, z, (X,Y,Z) -> {
                    if(X==leftDragStart[0]&&Y==leftDragStart[1]&&Z==leftDragStart[2])return;
                    if(!editorSpace.isSpaceValid(editor.getSelectedBlock(id), X, Y, Z))return;
                    synchronized(leftSelectedBlocks){
                        for(int[] i : leftSelectedBlocks){
                            if(i[0]==X&&i[1]==Y&&i[2]==Z)return;
                        }
                        leftSelectedBlocks.add(new int[]{X,Y,Z});
                    }
                });
                leftDragStart = new int[]{x,y,z};
            }
        }
        if(button==1){
            if(obj!=rightStart){
                rightDragStart = new int[]{x,y,z};
                rightStart = obj;
            }
            if(rightDragStart!=null){
                if(rightDragStart[0]==x&&rightDragStart[1]==y&&rightDragStart[2]==z)return;
                raytrace(rightDragStart[0], rightDragStart[1], rightDragStart[2], x, y, z, (X,Y,Z) -> {
                    if(X==rightDragStart[0]&&Y==rightDragStart[1]&&Z==rightDragStart[2])return;
                    synchronized(rightSelectedBlocks){
                        for(int[] i : rightSelectedBlocks){
                            if(i[0]==X&&i[1]==Y&&i[2]==Z)return;
                        }
                        rightSelectedBlocks.add(new int[]{X,Y,Z});
                    }
                }, false);
                rightDragStart = new int[]{x,y,z};
            }
        }
    }
    @Override
    public boolean isEditTool(){
        return true;
    }
    @Override
    public void drawGhosts(Graphics g, EditorSpace editorSpace, int x1, int y1, int x2, int y2, int blocksWide, int blocksHigh, Axis axis, int layer, int x, int y, int width, int height, int blockSize, Image texture){
        g.setColor(Core.theme.getWhiteColor().getRGB());
        g.setAlpha(127);
        synchronized(leftSelectedBlocks){
            for(int[] i : leftSelectedBlocks){
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
                g.drawImage(texture, x+sx*blockSize, y+sy*blockSize, x+(sx+1)*blockSize, y+(sy+1)*blockSize);
            }
        }
        g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
        synchronized(rightSelectedBlocks){
            for(int[] i : rightSelectedBlocks){
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
        g.setAlpha(255);
    }
    @Override
    public String getTooltip(){
        return "Pencil tool (P)\nUse this tool to draw blocks one at a time\nHold CTRL to only place blocks where they are valid";
    }
    @Override
    public void mouseMoved(Object obj, EditorSpace editorSpace, int x, int y, int z){}
    @Override
    public void mouseMovedElsewhere(Object obj, EditorSpace editorSpace){
        leftStart = rightStart = null;
    }
}