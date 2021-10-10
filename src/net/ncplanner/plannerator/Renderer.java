package net.ncplanner.plannerator;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.plaf.Style;
import net.ncplanner.plannerator.planner.FormattedText;
public class Renderer{
    private final Graphics g;
    public Renderer(Graphics g){
        this.g = g;
    }
    public void fillRect(int left, int top, int right, int bottom){
        if(right<left){
            int r = left;
            int l = right;
            right = r;
            left = l;
        }
        if(bottom<top){
            int b = top;
            int t = bottom;
            bottom = b;
            top = t;
        }
        g.fillRect(left, top, right-left, bottom-top);
    }
    public void drawImage(Image image, int left, int top, int right, int bottom){
        if(right<left){
            int r = left;
            int l = right;
            right = r;
            left = l;
        }
        if(bottom<top){
            int b = top;
            int t = bottom;
            bottom = b;
            top = t;
        }
        if(image==null)fillRect(left, top, right, bottom);
        else g.drawImage(image, left, top, right-left, bottom-top);
    }
    public void drawFormattedText(int x, int y, FormattedText str){
        while(str!=null){
            int deco = 0;
            if(str.strikethrough)deco|=Style.TEXT_DECORATION_STRIKETHRU;
            g.drawString(str.text, x, y, deco);
            x+=g.getFont().stringWidth(str.text);
            str = str.next;
        }
    }
}