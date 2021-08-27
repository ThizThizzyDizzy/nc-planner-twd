package net.ncplanner.plannerator.multiblock;
import com.codename1.ui.Graphics;
public abstract class Decal{
    public final int x;
    public final int y;
    public final int z;
    public Decal(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public abstract void render(Graphics g, int x, int y, int blockSize);
    public abstract String getTooltip();
}