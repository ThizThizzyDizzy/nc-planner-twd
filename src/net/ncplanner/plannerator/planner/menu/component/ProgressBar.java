package net.ncplanner.plannerator.planner.menu.component;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Task;
public abstract class ProgressBar extends Container{
    private final int textHeight;
    private final int textInset;
    private final int progressBarHeight;
    public ProgressBar(int scale){
        this(scale*20, scale*2, scale*6);
    }
    public ProgressBar(int textHeight, int textInset, int progressBarHeight){
        this.textHeight = textHeight;
        this.textInset = textInset;
        this.progressBarHeight = progressBarHeight;
    }
    @Override
    public void paint(Graphics g){
        Task task = getTask();
        int Y = getY();
        while(task!=null){
            g.setColor(Core.theme.getSecondaryComponentColor().getRGB());
            g.fillRect(getX(), Y, getWidth(), textHeight+progressBarHeight+textInset*3);
            g.setColor(Core.theme.getComponentTextColor().getRGB());
            g.drawString(task.name, getX()+textInset, Y+textInset);
            g.setColor(Core.theme.getProgressBarBackgroundColor().getRGB());
            g.fillRect(getX()+textInset, Y+textHeight+textInset*2, getWidth()-textInset, progressBarHeight);
            double w = getWidth()-textInset*2;
            g.setColor(Core.theme.getProgressBarColor().getRGB());
            g.fillRect(getX()+textInset, Y+textHeight+textInset*2, (int)(textInset+w*task.getProgressD()), progressBarHeight);
            task = task.getCurrentSubtask();
            Y+=textHeight+progressBarHeight+textInset*3;
        }
    }
    @Override
    protected Dimension calcPreferredSize(){
        Dimension siz = super.calcPreferredSize();
        siz.setHeight(Math.max(siz.getHeight(), (textHeight+textInset+progressBarHeight+textInset*3)*getTask().getDepth()));
        return siz;
    }
    public abstract Task getTask();
}