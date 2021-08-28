package net.ncplanner.plannerator.planner.menu.component;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Dimension;
public class SquareButton extends Button{
    public SquareButton(){
        super();
    }
    public SquareButton(Command cmd){
        super(cmd);
    }
    public SquareButton(Image icon){
        super(icon);
    }
    public SquareButton(String text){
        super(text);
    }
    public SquareButton(char icon){
        super(icon);
    }
    public SquareButton(Image icon, String id){
        super(icon, id);
    }
    public SquareButton(String text, Image icon){
        super(text, icon);
    }
    public SquareButton(String text, String id){
        super(text, id);
    }
    public SquareButton(char icon, String id){
        super(icon, id);
    }
    public SquareButton(String text, Image icon, String id){
        super(text, icon, id);
    }
    public SquareButton(String text, char icon, String id){
        super(text, icon, id);
    }
    public SquareButton(char icon, float iconSize, String id){
        super(icon, iconSize, id);
    }
    public SquareButton(String text, char icon, float iconSize, String id){
        super(text, icon, iconSize, id);
    }
    @Override
    protected Dimension calcPreferredSize(){
        Dimension size = super.calcPreferredSize();
        size.setWidth(Math.max(size.getWidth(), size.getHeight()));
        return size;
    }
}