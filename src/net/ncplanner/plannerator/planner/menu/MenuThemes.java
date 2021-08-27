package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.theme.Theme;
import net.ncplanner.plannerator.planner.theme.ThemeCategory;
public class MenuThemes extends Form{
    public MenuThemes(){
        super(new BorderLayout());
        Button done = new Button("Done");
        add(TOP, done);
        done.addActionListener((evt) -> {
            new MenuSettings().showBack();
        });
        Container content = new Container(new GridLayout(1, Theme.themes.size()));
        add(CENTER, content);
        for(ThemeCategory category : Theme.themes){
            Container subContainer = new Container(BoxLayout.y());
            subContainer.setScrollableY(true);
            subContainer.add(new Label(category.name));
            content.add(subContainer);
            for(Theme theme : category){
                Button button = new Button(theme.name);
                button.getStyle().setBgColor(theme.getComponentColor().getRGB());
                button.getSelectedStyle().setBgColor(theme.getComponentColor().getRGB());
                button.getPressedStyle().setBgColor(theme.getComponentPressedColor().getRGB());
                button.getStyle().setFgColor(theme.getComponentTextColor().getRGB());
                button.getSelectedStyle().setFgColor(theme.getComponentTextColor().getRGB());
                button.getPressedStyle().setFgColor(theme.getComponentTextColor().getRGB());
                subContainer.add(button);
                button.addActionListener((evt) -> {
                    Core.setTheme(theme);
                    refreshTheme();
                });
            }
        }
    }
}