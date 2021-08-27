package net.ncplanner.plannerator.planner.theme;
import net.ncplanner.plannerator.planner.theme.legacy.SolidColorTheme;
import net.ncplanner.plannerator.simplelibrary.image.Color;
public class StandardTheme extends SolidColorTheme{
    public StandardTheme(String name, Color background, Color color, float rgbTint, float rgbSat){
        super(name, background, color, rgbTint, rgbSat);
    }
    @Override
    public Color getEditorMouseoverLineColor(){
        return getRGBA(1, .5f, 0, 1);
    }
}