package net.ncplanner.plannerator.planner.theme;
import java.util.ArrayList;
import net.ncplanner.plannerator.planner.theme.legacy.SolidColorTheme;
import net.ncplanner.plannerator.simplelibrary.image.Color;
public abstract class Theme{
    public static ArrayList<ThemeCategory> themes = new ArrayList<>();
    public static final SolidColorTheme STANDARD, GOLD, CRACKER, CHOCOLATE, MARSHMALLOW;
    static{
        newCategory("General");
        addTheme(STANDARD = new StandardTheme("Light", new Color(100, 100, 100), new Color(1f, 1f, 1f, 1f), .625f, .75f));
        addTheme(new StandardTheme("Light, but darker", new Color(50, 50, 50), new Color(.5f, .5f, .5f, 1f), .3125f, .75f));
        newCategory("Materials");
        addTheme(new SolidColorTheme("Water", new Color(64, 78, 203)));
        addTheme(new SolidColorTheme("Iron", new Color(229, 229, 229)));
        addTheme(new SolidColorTheme("Redstone", new Color(144, 16, 8)));
        addTheme(new SolidColorTheme("Quartz", new Color(166, 164, 160)));
        addTheme(new SolidColorTheme("Obsidian", new Color(20, 18, 30)));
        addTheme(new SolidColorTheme("Nether Brick", new Color(68, 4, 7)));
        addTheme(new SolidColorTheme("Glowstone", new Color(143, 117, 68)));
        addTheme(new SolidColorTheme("Lapis", new Color(37, 65, 139)));
        addTheme(GOLD = new SolidColorTheme("Gold", new Color(254, 249, 85)));
        addTheme(new SolidColorTheme("Prismarine", new Color(101, 162, 144)));
        addTheme(new SolidColorTheme("Slime", new Color(119, 187, 101)));
        addTheme(new SolidColorTheme("End Stone", new Color(225, 228, 170)));
        addTheme(new SolidColorTheme("Purpur", new Color(165, 121, 165)));
        addTheme(new SolidColorTheme("Diamond", new Color(136, 230, 226)));
        addTheme(new SolidColorTheme("Emerald", new Color(82, 221, 119)));
        addTheme(new SolidColorTheme("Copper", new Color(222, 151, 109)));
        addTheme(new SolidColorTheme("Tin", new Color(222, 225, 242)));
        addTheme(new SolidColorTheme("Lead", new Color(65, 77, 77)));
        addTheme(new SolidColorTheme("Boron", new Color(160, 160, 160)));
        addTheme(new SolidColorTheme("Lithium", new Color(241, 241, 241)));
        addTheme(new SolidColorTheme("Magnesium", new Color(242, 220, 229)));
        addTheme(new SolidColorTheme("Manganese", new Color(173, 176, 201)));
        addTheme(new SolidColorTheme("Aluminum", new Color(213, 245, 233)));
        addTheme(new SolidColorTheme("Silver", new Color(241, 238, 246)));
        addTheme(new SolidColorTheme("Fluorite", new Color(132, 160, 142)));
        addTheme(new SolidColorTheme("Villiaumite", new Color(154, 109, 97)));
        addTheme(new SolidColorTheme("Carobbiite", new Color(160, 167, 82)));
        addTheme(new SolidColorTheme("Arsenic", new Color(147, 149, 137)));
        addTheme(new SolidColorTheme("Nitrogen", new Color(64, 166, 70)));
        addTheme(new SolidColorTheme("Helium", new Color(201, 76, 73)));
        addTheme(new SolidColorTheme("Enderium", new Color(0, 71, 75)));
        addTheme(new SolidColorTheme("Cryotheum", new Color(0, 150, 194)));
        addTheme(new SolidColorTheme("Beryllium", new Color(240, 244, 236)));
        addTheme(new SolidColorTheme("Graphite", new Color(18, 18, 18)));
        addTheme(new SolidColorTheme("Heavy Water", new Color(103, 71, 210)));
        addTheme(new SolidColorTheme("Reflector", new Color(186, 144, 94)));
        addTheme(new SolidColorTheme("Conductor", new Color(129, 129, 129)));
        addTheme(new SolidColorTheme("Old Heavy Water", new Color(40, 50, 100), new Color(0.5f, 0.5f, 1f, 1f), .625f, .875f, new Color(.875f,.875f,1f,1f)));
        newCategory("Colors");
        addTheme(new SolidColorTheme("Red", new Color(100, 0, 0), new Color(1f, 0f, 0f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Orange", new Color(100, 50, 0), new Color(1, 0.5f, 0), .625f, 1f));
        addTheme(new SolidColorTheme("Yellow", new Color(100, 100, 0), new Color(1f, 1f, 0f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Lime", new Color(50, 100, 0), new Color(.5f, 1f, 0), .625f, 1f));
        addTheme(new SolidColorTheme("Green", new Color(0, 100, 0), new Color(0f, 1f, 0f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Bluish green", new Color(0, 100, 50), new Color(0f, 1f, .5f), .625f, 1f));
        addTheme(new SolidColorTheme("Aqua", new Color(0, 100, 100), new Color(0f, 1f, 1f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Greenish Blue", new Color(0, 50, 100), new Color(0f, .5f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Blue", new Color(0, 0, 100), new Color(0f, 0f, 1f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Purple", new Color(50, 0, 100), new Color(.5f, 0f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Magenta", new Color(100, 0, 100), new Color(1f, 0f, 1f, 1f), .625f, 1f));
        addTheme(new SolidColorTheme("Pink", new Color(100, 0, 50), new Color(1f, 0f, .5f), .625f, 1f));
        addTheme(new SolidColorTheme("Light Pink", new Color(255, 110, 199)));
        newCategory("S'mores");
        addTheme(CRACKER = new SolidColorTheme("Cracker", new Color(201, 163, 64)));//or 150 106 1
        addTheme(CHOCOLATE = new SolidColorTheme("Chocolate", new Color(97, 20, 0)));
        addTheme(MARSHMALLOW = new SolidColorTheme("Marshmallow", new Color(248, 248, 248)));
    }
    private static ThemeCategory currentCategory;
    private static void newCategory(String name){
        themes.add(currentCategory = new ThemeCategory(name));
    }
    private static void addTheme(Theme theme){
        currentCategory.add(theme);
    }
    public static Theme getByName(String name){
        for(ThemeCategory cat : themes){
            for(Theme t : cat){
                if(t.name.equals(name))return t;
            }
        }
        return null;
    }
    public static Theme getByLegacyID(int i){
        ArrayList<Theme> allThemes = new ArrayList<>();
        for(ThemeCategory cat : themes)allThemes.addAll(cat);
        return allThemes.get(i);
    }
    public final String name;
    public Theme(String name){
        this.name = name;
    }
    public void onSet(){}
    public abstract Color getDecalColorAdjacentCell();
    public abstract Color getDecalColorAdjacentModerator();
    public abstract Color getDecalColorAdjacentModeratorLine(float efficiency);
    public abstract Color getDecalColorUnderhaulModeratorLine();
    public abstract Color getDecalColorReflectorAdjacentModeratorLine();
    public abstract Color getDecalColorOverhaulModeratorLine(float efficiency);
    public abstract Color getDecalTextColor();
    public abstract Color getDecalColorNeutronSourceTarget();
    public abstract Color getDecalColorNeutronSourceNoTarget();
    public abstract Color getDecalColorNeutronSourceLine();
    public abstract Color getDecalColorNeutronSource();
    public abstract Color getDecalColorModeratorActive();
    public abstract Color getDecalColorMissingCasing();
    public abstract Color getDecalColorMissingBlade();
    public abstract Color getDecalColorIrradiatorAdjacentModeratorLine();
    public abstract Color getDecalColorCellFlux(int flux, int criticality);
    public abstract Color getDecalColorBlockValid();
    public abstract Color getDecalColorBlockInvalid();
    public abstract Color getBlockColorOutlineInvalid();
    public abstract Color getBlockColorOutlineActive();
    public abstract Color getBlockColorSourceCircle(float efficiency, boolean selfPriming);
    public abstract Color getClusterOverheatingColor();
    public abstract Color getClusterOvercoolingColor();
    public abstract Color getClusterDisconnectedColor();
    public abstract Color getClusterInvalidColor();
    public abstract Color getTooltipInvalidTextColor();
    public abstract Color getTooltipTextColor();
    public abstract Color getEditorToolTextColor();
    public abstract Color getEditorToolBackgroundColor();
    public abstract Color getSelectionColor();
    public abstract Color getEditorBackgroundColor();
    public abstract Color getImageExportBackgroundColor();
    public abstract Color getImageExportTextColor();
    public abstract Color getComponentTextColor();
    public abstract Color getMouseoverSelectedComponentColor();
    public abstract Color getSelectedComponentColor();
    public abstract Color getMouseoverComponentColor();
    public abstract Color getComponentColor();
    public abstract Color getEditorBackgroundMouseoverColor();
    public abstract Color getEditorGridColor();
    public abstract Color getSuggestionOutlineColor();
    public abstract Color getEditorMouseoverLightColor();
    public abstract Color getEditorMouseoverDarkColor();
    public abstract Color getEditorMouseoverLineColor();
    public abstract Color getEditorListBackgroundMouseoverColor();
    public abstract Color getEditorListBackgroundColor();
    public abstract Color getEditorListLightSelectedColor();
    public abstract Color getEditorListDarkSelectedColor();
    public abstract Color getEditorListLightMouseoverColor();
    public abstract Color getEditorListDarkMouseoverColor();
    public abstract Color getMultiblockSelectedInputColor();
    public abstract Color getMultiblockInvalidInputColor();
    public abstract Color getSecondaryComponentColor();
    public abstract Color getProgressBarBackgroundColor();
    public abstract Color getProgressBarColor();
    public abstract Color getMultiblockDisplayBorderColor();
    public abstract Color getMultiblockDisplayBackgroundColor();
    public abstract Color getToggleBlockFadeout();
    public abstract Color getTutorialBackgroundColor();
    public abstract Color getScrollbarButtonColor();
    public abstract Color getBlockUnknownColor();
    public abstract Color getBlockTextColor();
    public abstract Color getScrollbarBackgroundColor();
    public abstract Color getComponentPressedColor();
    public abstract Color getComponentMouseoverColor();
    public abstract Color getComponentDisabledColor();
    public abstract Color getSecondaryComponentPressedColor();
    public abstract Color getSecondaryComponentMouseoverColor();
    public abstract Color getSecondaryComponentDisabledColor();
    public abstract Color getSliderColor();
    public abstract Color getSliderPressedColor();
    public abstract Color getSliderMouseoverColor();
    public abstract Color getSliderDisabledColor();
    public abstract Color getSecondarySliderColor();
    public abstract Color getSecondarySliderPressedColor();
    public abstract Color getSecondarySliderMouseoverColor();
    public abstract Color getSecondarySliderDisabledColor();
    public abstract Color getTextBoxBorderColor();
    public abstract Color getTextBoxColor();
    public abstract Color getTextViewBackgroundColor();
    public abstract Color getToggleBoxBorderColor();
    public abstract Color getSecondaryToggleBoxBorderColor();
    public abstract Color getToggleBoxBackgroundColor();
    public abstract Color getToggleBoxSelectedColor();
    public abstract Color getToggleBoxMouseoverColor();
    public abstract Color getMouseoverUnselectableComponentColor();
    public abstract Color getConfigurationSidebarColor();
    public abstract Color getConfigurationWarningTextColor();
    public abstract Color getConfigurationDividerColor();
    public abstract Color getDialogBorderColor();
    public abstract Color getDialogBackgroundColor();
    public abstract Color getCreditsImageColor();
    public abstract Color getCreditsBrightImageColor();
    public abstract Color getCreditsTextColor();
    public abstract Color get3DMultiblockOutlineColor();
    public abstract Color get3DDeviceoverOutlineColor();
    public abstract Color getAddButtonTextColor();
    public abstract Color getDeleteButtonTextColor();
    public abstract Color getConvertButtonTextColor();
    public abstract Color getInputsButtonTextColor();
    public abstract Color getMetadataPanelBackgroundColor();
    public abstract Color getMetadataPanelHeaderColor();
    public abstract Color getMetadataPanelTextColor();
    public abstract Color getMultiblocksListHeaderColor();
    public abstract Color getRecoveryModeColor(int index);
    public abstract Color getRecoveryModeTextColor();
    public abstract Color getRotateMultiblockTextColor();
    public abstract Color getResizeMenuTextColor();
    public abstract Color getMenuBackgroundColor();
    public abstract Color getSettingsSidebarColor();
    public abstract Color getWhiteColor();
    public abstract Color getTutorialTextColor();
    public abstract Color getSettingsMergeTextColor();
    public static Color average(Color c1, Color c2){
        return new Color((c1.getRed()+c2.getRed())/2, (c1.getGreen()+c2.getGreen())/2, (c1.getBlue()+c2.getBlue())/2, (c1.getAlpha()+c2.getAlpha())/2);
    }
    private static final double FACTOR = 0.7;
    public static Color brighter(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        int i = (int)(1.0/(1.0-FACTOR));
        if ( r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;
        return new Color(Math.min((int)(r/FACTOR), 255),
                         Math.min((int)(g/FACTOR), 255),
                         Math.min((int)(b/FACTOR), 255),
                         alpha);
    }
    public static Color darker(Color color) {
        return new Color(Math.max((int)(color.getRed()  *FACTOR), 0),
                         Math.max((int)(color.getGreen()*FACTOR), 0),
                         Math.max((int)(color.getBlue() *FACTOR), 0),
                         color.getAlpha());
    }
    public void printXML(){
        System.out.println("    <theme name=\""+name+"\">");
        System.out.println("        <val key=\"Button.align\" value=\"4\" />");
        System.out.println("        <border key=\"Button.border\" type=\"empty\" />");
        System.out.println("        <val key=\"Button.dis#bgColor\" value=\""+hex(getComponentDisabledColor().getRGB())+"\" />");
        System.out.println("        <border key=\"Button.dis#border\" type=\"empty\" />");
        System.out.println("        <val key=\"Button.dis#fgColor\" value=\""+hex(getComponentTextColor().getRGB())+"\" />");
        System.out.println("        <val key=\"Button.padding\" value=\"6.0,6.0,6.0,6.0\" />");
        System.out.println("        <val key=\"Button.press#bgColor\" value=\""+hex(getComponentPressedColor().getRGB())+"\" />");
        System.out.println("        <border key=\"Button.press#border\" type=\"empty\" />");
        System.out.println("        <val key=\"Button.press#padding\" value=\"6.0,6.0,6.0,6.0\" />");
        System.out.println("        <val key=\"Button.sel#align\" value=\"4\" />");
        System.out.println("        <val key=\"Button.sel#bgColor\" value=\""+hex(getComponentColor().getRGB())+"\" />");
        System.out.println("        <border key=\"Button.sel#border\" type=\"empty\" />");
        System.out.println("        <val key=\"Button.sel#padding\" value=\"6.0,6.0,6.0,6.0\" />");
        System.out.println("        <val key=\"DialogTitle.bgColor\" value=\""+hex(getDialogBackgroundColor().getRGB())+"\" />");
        System.out.println("        <val key=\"Form.bgColor\" value=\""+hex(getMenuBackgroundColor().getRGB())+"\" />");
        System.out.println("        <val key=\"Label.align\" value=\"4\" />");
        System.out.println("        <val key=\"Label.bgColor\" value=\""+hex(getSecondaryComponentColor().getRGB())+"\" />");
        System.out.println("        <border key=\"Slider.border\" type=\"line\" millimeters=\"false\" thickness=\"2.0\" color=\""+num(getTextBoxBorderColor().getRGB())+"\" />");
        System.out.println("        <border key=\"Slider.sel#border\" type=\"line\" millimeters=\"false\" thickness=\"2.0\" color=\""+num(getTextBoxBorderColor().getRGB())+"\" />");
        System.out.println("        <val key=\"SliderFull.bgColor\" value=\""+hex(getTextBoxColor().getRGB())+"\" />");
        System.out.println("        <val key=\"TextArea.bgColor\" value=\""+hex(getTextViewBackgroundColor().getRGB())+"\" />");
        System.out.println("        <border key=\"TextArea.border\" type=\"line\" millimeters=\"false\" thickness=\"2.0\" color=\""+num(getTextBoxBorderColor().getRGB())+"\" />");
        System.out.println("        <font key=\"TextArea.font\" type=\"system\" face=\"0\" style=\"0\" size=\"0\" />");
        System.out.println("        <val key=\"TextField.bgColor\" value=\""+hex(getTextBoxColor().getRGB())+"\" />");
        System.out.println("        <border key=\"TextField.border\" type=\"line\" millimeters=\"false\" thickness=\"2.0\" color=\""+num(getTextBoxBorderColor().getRGB())+"\" />");
        System.out.println("        <val key=\"Tooltip.bgColor\" value=\""+hex(getDialogBackgroundColor().getRGB())+"\" />");
        System.out.println("        <border key=\"Tooltip.border\" type=\"line\" millimeters=\"false\" thickness=\"2.0\" color=\""+num(getTooltipTextColor().getRGB())+"\" />");
        System.out.println("        <val key=\"bgColor\" value=\""+hex(getComponentColor().getRGB())+"\" />");
        System.out.println("        <border key=\"border\" type=\"empty\" />");
        System.out.println("        <val key=\"dis#bgColor\" value=\""+hex(getComponentDisabledColor().getRGB())+"\" />");
        System.out.println("        <val key=\"dis#fgColor\" value=\""+hex(getComponentTextColor().getRGB())+"\" />");
        System.out.println("        <val key=\"fgColor\" value=\""+hex(getComponentTextColor().getRGB())+"\" />");
        System.out.println("        <font key=\"font\" type=\"system\" face=\"0\" style=\"0\" size=\"16\" />");
        System.out.println("        <val key=\"margin\" value=\"0.0,0.0,0.0,0.0\" />");
        System.out.println("        <val key=\"press#bgColor\" value=\""+hex(getComponentPressedColor().getRGB())+"\" />");
        System.out.println("        <val key=\"press#fgColor\" value=\""+hex(getComponentTextColor().getRGB())+"\" />");
        System.out.println("        <val key=\"sel#bgColor\" value=\""+hex(getComponentColor().getRGB())+"\" />");
        System.out.println("        <val key=\"sel#fgColor\" value=\""+hex(getComponentTextColor().getRGB())+"\" />");
        System.out.println("    </theme>");
    }
    private static String hex(int rgb){
        String hex = Integer.toHexString(rgb).substring(2);
        return hex;
    }
    private static int num(int rgb){
        return Integer.parseInt(hex(rgb), 16);
    }
}