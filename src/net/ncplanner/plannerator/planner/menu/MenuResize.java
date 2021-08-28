package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import net.ncplanner.plannerator.multiblock.Multiblock;
public class MenuResize extends Form{
    public MenuResize(Multiblock multiblock){
        super(new BorderLayout());
        Button back = new Button("Back");
        back.addActionListener((evt) -> {
            new MenuEdit(multiblock).showBack();
        });
        add(TOP, back);
    }
}