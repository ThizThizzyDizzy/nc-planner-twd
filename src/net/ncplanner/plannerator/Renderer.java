package net.ncplanner.plannerator;
import com.codename1.ui.Graphics;
import com.codename1.ui.plaf.Style;
import java.util.ArrayList;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.FormattedText;
import net.ncplanner.plannerator.planner.StringUtil;
import net.ncplanner.plannerator.simplelibrary.image.Color;
import net.ncplanner.plannerator.simplelibrary.image.Image;
public class Renderer{
    private final Graphics g;
    public Renderer(Graphics g){
        this.g = g;
    }
    public void fillRect(double left, double top, double right, double bottom){
        if(right<left){
            double r = left;
            double l = right;
            right = r;
            left = l;
        }
        if(bottom<top){
            double b = top;
            double t = bottom;
            bottom = b;
            top = t;
        }
        g.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    }
    public void drawImage(Image image, double left, double top, double right, double bottom){
        drawImage(TextureManager.toCN1(image), left, top, right, bottom);
    }
    public void drawImage(com.codename1.ui.Image image, double left, double top, double right, double bottom){
        if(right<left){
            double r = left;
            double l = right;
            right = r;
            left = l;
        }
        if(bottom<top){
            double b = top;
            double t = bottom;
            bottom = b;
            top = t;
        }
        if(image==null)fillRect(left, top, right, bottom);
        else g.drawImage(image, (int)left, (int)top, (int)(right-left), (int)(bottom-top));
    }
    public void drawCenteredText(double left, double top, double right, String text){
        int width = g.getFont().stringWidth(text);
        g.drawString(text, (int)((left+right)/2-width/2), (int)top);
    }
    public void drawText(double left, double top, double right, String text){
        int width = g.getFont().stringWidth(text);
        if(width>(right-left)){
            do{
                text = text.substring(0, text.length()-1);
                if(text.isEmpty())break;
            }while(g.getFont().stringWidth(text+"...")>(right-left));
        }
        g.drawString(text, (int)left, (int)top);
    }
    public void drawFormattedText(double left, double top, double right, FormattedText text, int snap){
        if(g.getFont().stringWidth(text.toString())>right-left){
            text.trimSlightly();
            drawFormattedText(left, top, right, text, snap);
            return;
        }
        int width = g.getFont().stringWidth(text.toString());
        if(snap==0){
            left = (left+right)/2-width/2;
        }
        if(snap>0){
            left = right-width;
        }
        while(text!=null){
            if(text.color!=null)setColor(text.color);
            int deco = 0;
            if(text.strikethrough)deco|=Style.TEXT_DECORATION_STRIKETHRU;
            if(text.underline)deco|=Style.TEXT_DECORATION_UNDERLINE;
            g.drawString(text.text, (int)left, (int)top, deco);
            left+=g.getFont().stringWidth(text.text);
            text = text.next;
        }
    }
    public FormattedText drawFormattedTextWithWrap(double left, double top, double right, FormattedText text, int snap){
        if(g.getFont().stringWidth(text.toString())>right-left){
            String txt = text.text;
            text.trimSlightlyWithoutElipses();
            FormattedText also = drawFormattedTextWithWrap(left, top, right, text, snap);
            txt = txt.substring(text.text.length());
            return new FormattedText(also!=null?also.text+txt:txt, text.color, text.bold, text.italic, text.underline, text.strikethrough);
        }
        int textWidth = g.getFont().stringWidth(text.toString());
        if(snap==0){
            left = (left+right)/2-textWidth/2;
        }
        if(snap>0){
            left = right-textWidth;
        }
        if(text.color!=null)setColor(text.color);
        int deco = 0;
        if(text.strikethrough)deco|=Style.TEXT_DECORATION_STRIKETHRU;
        if(text.underline)deco|=Style.TEXT_DECORATION_UNDERLINE;
        g.drawString(text.text, (int)left, (int)top, deco);
        left+=textWidth;
        if(text.next!=null){
            return drawFormattedTextWithWrap(left+textWidth, top, right, text, snap);
        }
        return null;
    }
    public FormattedText drawFormattedTextWithWordWrap(double left, double top, double right, FormattedText text, int snap){
        ArrayList<FormattedText> words = text.split(" ");
        if(words.isEmpty())return drawFormattedTextWithWrap(left, top, right, text, snap);
        String str = words.get(0).text;
        double length = right-left;
        for(int i = 1; i<words.size(); i++){
            String string = str+" "+words.get(i).text;
            if(g.getFont().stringWidth(string.trim())>=length){
                drawFormattedTextWithWrap(left, top, right, new FormattedText(str, text.color, text.bold, text.italic, text.underline, text.strikethrough), snap);
                return new FormattedText(StringUtil.replaceFirst(text.text, "\\Q"+str, "").trim());
            }else{
                str = string;
            }
        }
        return drawFormattedTextWithWrap(left, top, right, text, snap);
    }
    public String drawTextWithWordWrap(double left, double top, double right, String text){
        return drawFormattedTextWithWordWrap(left, top, right, new FormattedText(text), -1).toString();
    }
    public void drawCircle(double x, double y, double innerRadius, double outerRadius){
        int resolution = (int)(Math.PI*outerRadius);
        double angle = 0;
        double[] xp = new double[resolution*2];
        double[] yp = new double[resolution*2];
        for(int i = 0; i<resolution; i++){
            xp[i] = x+(Math.cos(Math.toRadians(angle-90))*innerRadius);
            yp[i] = y+(Math.sin(Math.toRadians(angle-90))*innerRadius);
            xp[resolution*2-i-1] = x+(Math.cos(Math.toRadians(angle-90))*outerRadius);
            yp[resolution*2-i-1] = y+(Math.sin(Math.toRadians(angle-90))*outerRadius);
            angle+=(360d/(resolution-1));
            if(angle>=360)angle-=360;
        }
        fillPolygon(xp, yp);
    }
    public void drawRegularPolygon(double x, double y, double radius, int quality, double angle, int texture){
        if(quality<3){
            throw new IllegalArgumentException("A polygon must have at least 3 sides!");
        }
        double[] xs = new double[quality];
        double[] ys = new double[quality];
        for(int i = 0; i<quality; i++){
            xs[i] = x+Math.cos(Math.toRadians(angle-90))*radius;
            ys[i] = y+Math.sin(Math.toRadians(angle-90))*radius;
            angle+=(360D/quality);
        }
        fillPolygon(xs, ys);
    }
    public void drawOval(double x, double y, double xRadius, double yRadius, double xThickness, double yThickness, int quality, int texture){
        drawOval(x, y, xRadius, yRadius, xThickness, yThickness, quality, texture, 0, quality-1);
    }
    public void drawOval(double x, double y, double xRadius, double yRadius, double thickness, int quality, int texture){
        drawOval(x, y, xRadius, yRadius, thickness, thickness, quality, texture, 0, quality-1);
    }
    public void drawOval(double x, double y, double xRadius, double yRadius, double thickness, int quality, int texture, int left, int right){
        drawOval(x, y, xRadius, yRadius, thickness, thickness, quality, texture, left, right);
    }
    public void drawOval(double x, double y, double xRadius, double yRadius, double xThickness, double yThickness, int quality, int texture, int left, int right){
        if(quality<3){
            throw new IllegalArgumentException("Quality must be >=3!");
        }
        while(left<0)left+=quality;
        while(right<0)right+=quality;
        while(left>quality)left-=quality;
        while(right>quality)right-=quality;
        double angle = 0;
        for(int i = 0; i<quality; i++){
            boolean inRange = false;
            if(left>right)inRange = i>=left||i<=right;
            else inRange = i>=left&&i<=right;
            double[] xp = new double[4];
            double[] yp = new double[4];
            if(inRange){
                xp[0] = x+Math.cos(Math.toRadians(angle-90))*xRadius;
                yp[0] = y+Math.sin(Math.toRadians(angle-90))*yRadius;
                xp[1] = x+Math.cos(Math.toRadians(angle-90))*(xRadius-xThickness);
                yp[1] = y+Math.sin(Math.toRadians(angle-90))*(yRadius-yThickness);
            }
            angle+=(360D/quality);
            if(inRange){
                xp[2] = x+Math.cos(Math.toRadians(angle-90))*(xRadius-xThickness);
                yp[2] = y+Math.sin(Math.toRadians(angle-90))*(yRadius-yThickness);
                xp[3] = x+Math.cos(Math.toRadians(angle-90))*xRadius;
                yp[3] = y+Math.sin(Math.toRadians(angle-90))*yRadius;
                fillPolygon(xp, yp);
            }
        }
    }
    public void setWhite(){
        setColor(Core.theme.getWhiteColor());
    }
    public void setWhite(float alpha){
        setColor(Core.theme.getWhiteColor(), alpha);
    }
    public void setColor(Color c){
        g.setColor(c.getRGB());
        g.setAlpha(c.getAlpha());
    }
    public void setColor(Color c, float alpha){
        g.setColor(c.getRGB());
        g.setAlpha((int)(c.getAlpha()*alpha));
    }
    public void fillPolygon(double[] xPoints, double[] yPoints){
        int[] ixPoints = new int[xPoints.length];
        for(int i = 0; i<ixPoints.length; i++){
            ixPoints[i] = (int)xPoints[i];
        }
        int[] iyPoints = new int[yPoints.length];
        for(int i = 0; i<iyPoints.length; i++){
            iyPoints[i] = (int)yPoints[i];
        }
        g.fillPolygon(ixPoints, iyPoints, xPoints.length);
    }
}