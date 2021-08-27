package planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import planner.Core;
import planner.module.Module;
public class MenuModules extends Form{
    public MenuModules(){
        super(new BorderLayout());
        Container content = new Container(BoxLayout.y());
        Button done = new Button("Done");
        done.addActionListener((evt) -> {
            new MenuSettings().showBack();
        });
        add(BOTTOM, done);
        add(CENTER, content);
        for(Module m : Core.modules){
            Button b = new Button(m.getDisplayName()+" ("+(m.isActive()?"Active":"Inactive")+")");
            content.add(b);
            b.addActionListener((evt) -> {
                m.setActive(!m.isActive());
                b.setText(m.getDisplayName()+" ("+(m.isActive()?"Active":"Inactive")+")");
            });
        }
    }
}