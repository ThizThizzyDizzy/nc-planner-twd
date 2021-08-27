package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import java.util.HashMap;
public class MenuEditMetadata extends Form{
    public MenuEditMetadata(HashMap<String, String> metadata, Runnable resetFunc){
        super(new BorderLayout());
        add(TOP, new Label("Metadata"));
        Button done = new Button("Done");
        add(BOTTOM, done);
        Container superContentPane = new Container(BoxLayout.y());
        Container contentPane = new Container(BoxLayout.y());
        add(CENTER, superContentPane);
        superContentPane.add(contentPane);
        done.addActionListener((evt) -> {
            resetFunc.run();
            for(Component c : contentPane.getChildrenAsList(true)){
                Container container = (Container)c;
                //metadata stuff
                TextField key = (TextField)container.getComponentAt(0);
                TextField value = (TextField)container.getComponentAt(1);
                if(key.getText().trim().isEmpty()&&value.getText().trim().isEmpty())continue;
                metadata.put(key.getText(), value.getText());
            }
            new MenuMain().showBack();
        });
        Button add = new Button("Add");
        add.addActionListener((evt) -> {
            addLine(contentPane, "", "");
            revalidate();
        });
        superContentPane.add(add);
        for(String key : metadata.keySet()){
            String value = metadata.get(key);
            addLine(contentPane, key, value);
        }
    }
    private void addLine(Container contentPane, String key, String value){
        Container subContainer = new Container(new GridLayout(1, 2));
        subContainer.add(new TextField(key, "Key"));
        subContainer.add(new TextField(value, "Value"));
        contentPane.add(subContainer);
    }
}