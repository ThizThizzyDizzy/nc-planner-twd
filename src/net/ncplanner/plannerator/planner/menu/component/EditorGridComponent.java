package net.ncplanner.plannerator.planner.menu.component;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.Layout;
import java.util.ArrayList;
import net.ncplanner.plannerator.multiblock.BlockPos;
public abstract class EditorGridComponent extends Container{
    public EditorGridComponent(Layout layout){
        super(layout);
    }
    public abstract ArrayList<Component> getGridComponents();
    public abstract BlockPos getPos(Component component);
}