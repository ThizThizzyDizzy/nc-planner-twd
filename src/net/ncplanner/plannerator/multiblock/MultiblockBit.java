package net.ncplanner.plannerator.multiblock;
import com.codename1.util.MathUtil;
import java.util.ArrayList;
public class MultiblockBit{
    protected static final ArrayList<Direction> directions = new ArrayList<>();
    static{
        for(Direction d : Direction.values())directions.add(d);
    }
    protected static final ArrayList<Axis> axes = axes();
    private static ArrayList<Axis> axes(){
        ArrayList<Axis> axes = new ArrayList<>();
        axes.add(Axis.X);
        axes.add(Axis.Y);
        axes.add(Axis.Z);
        return axes;
    }
    protected String percent(double n, int digits){
        double fac = MathUtil.pow(10, digits);
        double d = (Math.round(n*fac*100)/(double)Math.round(fac));
        return (digits==0?Math.round(d):d)+"%";
    }
    protected String round(double n, int digits){
        double fac = MathUtil.pow(10, digits);
        double d = Math.round(n*fac)/(double)Math.round(fac);
        return (digits==0?Math.round(d):d)+"";
    }
}