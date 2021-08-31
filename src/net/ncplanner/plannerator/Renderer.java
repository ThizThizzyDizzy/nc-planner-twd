package net.ncplanner.plannerator;
import com.codename1.ui.Graphics;
public class Renderer{
    private final Graphics g;
    public Renderer(Graphics g){
        this.g = g;
    }
    public void fillRect(int left, int top, int right, int bottom){
        g.fillRect(left, top, right-left, bottom-top);
    }
}