package net.ncplanner.plannerator.planner.theme.legacy;
import com.codename1.util.MathUtil;
import net.ncplanner.plannerator.planner.theme.ColorTheme;
import net.ncplanner.plannerator.simplelibrary.image.Color;
public abstract class LegacyTheme extends ColorTheme{
    public double moderatorLineDecalColorPow = 3;
    public LegacyTheme(String name){
        super(name);
    }
    @Override
    public Color getDecalColorAdjacentCell(){
        return getRGBA(1,1,0,1);
    }
    @Override
    public Color getDecalColorAdjacentModerator(){
        return getGreen();
    }
    @Override
    public Color getDecalColorAdjacentModeratorLine(float efficiency){
        return getRGBA(0, (float)Math.max(0,Math.min(1,MathUtil.pow(efficiency, moderatorLineDecalColorPow)/2)),1,1);
    }
    @Override
    public Color getDecalColorUnderhaulModeratorLine(){
        return getBlue();
    }
    @Override
    public Color getDecalColorReflectorAdjacentModeratorLine(){
        return getRGBA(1, 0.5f, 0, 1);
    }
    @Override
    public Color getDecalColorOverhaulModeratorLine(float efficiency){
        return getRGBA(0, (float)Math.max(0,Math.min(1,MathUtil.pow(efficiency, moderatorLineDecalColorPow)/2)),1,1);
    }
    @Override
    public Color getDecalTextColor(){
        return getTextColor();
    }
    @Override
    public Color getDecalColorNeutronSourceTarget(){
        return getRGBA(1, 0.5f, 0, 1);
    }
    @Override
    public Color getDecalColorNeutronSourceNoTarget(){
        return getRed();
    }
    @Override
    public Color getDecalColorNeutronSourceLine(){
        return getRGBA(1, 0.5f, 0, 1);
    }
    @Override
    public Color getDecalColorNeutronSource(){
        return getRGBA(1, 0.5f, 0, 1);
    }
    @Override
    public Color getDecalColorModeratorActive(){
        return getGreen();
    }
    @Override
    public Color getDecalColorMissingCasing(){
        return getRed();
    }
    @Override
    public Color getDecalColorMissingBlade(){
        return getRed();
    }
    @Override
    public Color getDecalColorIrradiatorAdjacentModeratorLine(){
        return getRGBA(0, 0, 1, 1);
    }
    @Override
    public Color getDecalColorCellFlux(int flux, int criticality){
        float r = 1-((flux*2f/criticality)-1f);
        float g = (flux*2f/criticality);
        return getRGBA(Math.max(0, Math.min(1,r)), Math.max(0,Math.min(1,g)), 0, 1);
    }
    @Override
    public Color getDecalColorBlockValid(){
        return getGreen();
    }
    @Override
    public Color getDecalColorBlockInvalid(){
        return getRed();
    }
    @Override
    public Color getBlockColorOutlineInvalid(){
        return getRed();
    }
    @Override
    public Color getBlockColorOutlineActive(){
        return getGreen();
    }
    @Override
    public Color getBlockColorSourceCircle(float efficiency, boolean selfPriming){
        if(efficiency>1)return getRGBA(0, 1, (float)Math.min(.9, MathUtil.pow(efficiency-1, .25)), 1);
        float fac = selfPriming?1:(float) MathUtil.pow(efficiency, 10);
        float r = selfPriming?0:Math.min(1, -2*fac+2);
        float g = selfPriming?0:Math.min(1, fac*2);
        float b = selfPriming?1:0;
        return getRGBA(r, g, b, 1);
    }
    @Override
    public Color getClusterOverheatingColor(){
        return getRGBA(Color.RED);
    }
    @Override
    public Color getClusterOvercoolingColor(){
        return getRGBA(Color.BLUE);
    }
    @Override
    public Color getClusterDisconnectedColor(){
        return getRGBA(Color.PINK);
    }
    @Override
    public Color getClusterInvalidColor(){
        return getRGBA(Color.WHITE);
    }
    @Override
    public Color getTooltipInvalidTextColor(){
        return getRed();
    }
    @Override
    public Color getTooltipTextColor(){
        return getTextColor();
    }
    @Override
    public Color getEditorToolTextColor(){
        return getTextColor();
    }
    @Override
    public Color getEditorToolBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public abstract Color getSelectionColor();
    @Override
    public Color getEditorBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getImageExportBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getImageExportTextColor(){
        return getTextColor();
    }
    @Override
    public Color getComponentTextColor(){
        return getTextColor();
    }
    @Override
    public Color getMouseoverSelectedComponentColor(){
        return getSelectedMultiblockColor();
    }
    @Override
    public Color getSelectedComponentColor(){
        return getSelectedMultiblockColor();
    }
    @Override
    public Color getMouseoverComponentColor(){
        return average(getSelectedMultiblockColor(), getButtonColor());
    }
    @Override
    public Color getComponentColor(){
        return getButtonColor();
    }
    @Override
    public Color getEditorBackgroundMouseoverColor(){
        return getBrighterEditorListBorderColor();
    }
    @Override
    public Color getEditorGridColor(){
        return getTextColor();
    }
    @Override
    public Color getSuggestionOutlineColor(){
        return getGreen();
    }
    @Override
    public Color getEditorMouseoverLightColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getEditorMouseoverDarkColor(){
        return getTextColor();
    }
    @Override
    public Color getEditorMouseoverLineColor(){
        return average(getEditorListBorderColor(), getTextColor());
    }
    @Override
    public Color getEditorListBackgroundMouseoverColor(){
        return getBrighterEditorListBorderColor();
    }
    @Override
    public Color getEditorListBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getEditorListLightSelectedColor(){
        return getDarkerEditorListBorderColor();
    }
    @Override
    public Color getEditorListDarkSelectedColor(){
        return getDarkerTextColor();
    }
    @Override
    public Color getEditorListLightMouseoverColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getEditorListDarkMouseoverColor(){
        return getTextColor();
    }
    @Override
    public Color getMultiblockSelectedInputColor(){
        return getRGBA(1, 1, 0, 1);
    }
    @Override
    public Color getMultiblockInvalidInputColor(){
        return getRGBA(0, 0, 0, 1);
    }
    @Override
    public Color getSecondaryComponentColor(){
        return getDarkButtonColor();
    }
    @Override
    public Color getProgressBarBackgroundColor(){
        return getRGBA(0, 0, 0, 1);
    }
    @Override
    public Color getProgressBarColor(){
        return getGreen();
    }
    @Override
    public Color getMultiblockDisplayBorderColor(){
        return getDarkButtonColor();
    }
    @Override
    public Color getMultiblockDisplayBackgroundColor(){
        return getButtonColor();
    }
    @Override
    public Color getToggleBlockFadeout(){
        return getFadeout();
    }
    @Override
    public Color getTutorialBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getScrollbarButtonColor(){
        return getListColor();
    }
    @Override
    public Color getBlockUnknownColor(){
        return getRGBA(1, 1, 0, 1);
    }
    @Override
    public Color getBlockTextColor(){
        return getTextColor();
    }
    @Override
    public Color getScrollbarBackgroundColor(){
        return getListBackgroundColor();
    }
    @Override
    public Color getComponentPressedColor(){
        return getDarkerButtonColor();
    }
    @Override
    public Color getComponentMouseoverColor(){
        return getBrighterButtonColor();
    }
    @Override
    public Color getComponentDisabledColor(){
        return getDarkerButtonColor();
    }
    @Override
    public Color getSecondaryComponentPressedColor(){
        return getDarkerDarkButtonColor();
    }
    @Override
    public Color getSecondaryComponentMouseoverColor(){
        return getBrighterDarkButtonColor();
    }
    @Override
    public Color getSecondaryComponentDisabledColor(){
        return getDarkerDarkButtonColor();
    }
    @Override
    public Color getSliderColor(){
        return getBrighterButtonColor();
    }
    @Override
    public Color getSliderPressedColor(){
        return getBrighterDarkerDarkerButtonColor();
    }
    @Override
    public Color getSliderMouseoverColor(){
        return getBrighterBrighterButtonColor();
    }
    @Override
    public Color getSliderDisabledColor(){
        return getBrighterDarkerButtonColor();
    }
    @Override
    public Color getSecondarySliderColor(){
        return getBrighterButtonColor();
    }
    @Override
    public Color getSecondarySliderPressedColor(){
        return getBrighterDarkerDarkerDarkButtonColor();
    }
    @Override
    public Color getSecondarySliderMouseoverColor(){
        return getBrighterBrighterDarkButtonColor();
    }
    @Override
    public Color getSecondarySliderDisabledColor(){
        return getBrighterDarkerDarkerDarkButtonColor();
    }
    @Override
    public Color getTextBoxBorderColor(){
        return getDarkerListColor();
    }
    @Override
    public Color getTextBoxColor(){
        return getListColor();
    }
    @Override
    public Color getTextViewBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getToggleBoxBorderColor(){
        return average(getButtonColor(), getBackgroundColor());
    }
    @Override
    public Color getSecondaryToggleBoxBorderColor(){
        return average(getDarkButtonColor(), getBackgroundColor());
    }
    @Override
    public Color getToggleBoxBackgroundColor(){
        return getBackgroundColor();
    }
    @Override
    public Color getToggleBoxSelectedColor(){
        return getGreen();
    }
    @Override
    public Color getToggleBoxMouseoverColor(){
        return getRGBA(.2f, .6f, .2f, 1);
    }
    @Override
    public Color getMouseoverUnselectableComponentColor(){
        return getSelectedMultiblockColor();
    }
    @Override
    public Color getConfigurationSidebarColor(){
        return getSidebarColor();
    }
    @Override
    public Color getConfigurationWarningTextColor(){
        return getRed();
    }
    @Override
    public Color getConfigurationDividerColor(){
        return average(getMetadataPanelBackgroundColor(), getSidebarColor());
    }
    @Override
    public Color getDialogBorderColor(){
        return average(getTextColor(), getBackgroundColor());
    }
    @Override
    public Color getDialogBackgroundColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getCreditsImageColor(){
        return getBackgroundColor();
    }
    @Override
    public Color getCreditsBrightImageColor(){
        return average(getBackgroundColor(), getWhite());
    }
    @Override
    public Color getCreditsTextColor(){
        return getTextColor();
    }
    @Override
    public Color get3DMultiblockOutlineColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color get3DDeviceoverOutlineColor(){
        return getEditorListBorderColor();
    }
    @Override
    public Color getAddButtonTextColor(){
        return getGreen();
    }
    @Override
    public Color getDeleteButtonTextColor(){
        return getRed();
    }
    @Override
    public Color getConvertButtonTextColor(){
        return getRGBA(1, 0.5f, 0, 1);
    }
    @Override
    public Color getInputsButtonTextColor(){
        return getRGBA(1, 1, 0, 1);
    }
    @Override
    public abstract Color getMetadataPanelBackgroundColor();
    @Override
    public abstract Color getMetadataPanelHeaderColor();
    @Override
    public Color getMetadataPanelTextColor(){
        return getTextColor();
    }
    @Override
    public Color getMultiblocksListHeaderColor(){
        return getHeader2Color();
    }
    @Override
    public Color getRecoveryModeColor(int index){
        return index%2==0?getRGBA(1, 1, 0, 1):getRGBA(0, 0, 0, 1);
    }
    @Override
    public Color getRecoveryModeTextColor(){
        return getRGBA(1, 1, 0, 1);
    }
    @Override
    public Color getRotateMultiblockTextColor(){
        return getTextColor();
    }
    @Override
    public Color getResizeMenuTextColor(){
        return getTextColor();
    }
    @Override
    public Color getMenuBackgroundColor(){
        return getBackgroundColor();
    }
    @Override
    public Color getSettingsSidebarColor(){
        return getSidebarColor();
    }
    @Override
    public Color getWhiteColor(){
        return getWhite();
    }
    @Override
    public Color getTutorialTextColor(){
        return getTextColor();
    }
    @Override
    public Color getSettingsMergeTextColor(){
        return getTextColor();
    }
    public abstract Color getBackgroundColor();
    public abstract Color getTextColor();
    public abstract Color getHeaderColor();
    public abstract Color getHeader2Color();
    public abstract Color getListColor();
    public abstract Color getSelectedMultiblockColor();
    public abstract Color getListBackgroundColor();
    public abstract Color getRed();
    public abstract Color getGreen();
    public abstract Color getBlue();
    public abstract Color getEditorListBorderColor();
    public abstract Color getDarkButtonColor();
    public abstract Color getButtonColor();
    public abstract Color getSidebarColor();
    public abstract Color getRGBA(float r, float g, float b, float a);
    public abstract Color getWhite();
    public abstract Color getFadeout();
    public Color getRGBA(Color color){
        return getRGBA(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
    }
    //TODO no brighter()/darker()
    public Color getBrighterEditorListBorderColor(){
        return brighter(getEditorListBorderColor());
    }
    public Color getDarkerEditorListBorderColor(){
        return darker(getEditorListBorderColor());
    }
    public Color getDarkerTextColor(){
        return darker(getTextColor());
    }
    public Color getDarkerDarkButtonColor(){
        return darker(getDarkButtonColor());
    }
    public Color getBrighterDarkButtonColor(){
        return brighter(getDarkButtonColor());
    }
    public Color getDarkerButtonColor(){
        return darker(getButtonColor());
    }
    public Color getBrighterButtonColor(){
        return brighter(getButtonColor());
    }
    public Color getDarkerDarkerDarkButtonColor(){
        return darker(darker(getDarkButtonColor()));
    }
    public Color getDarkerDarkerButtonColor(){
        return darker(darker(getButtonColor()));
    }
    public Color getBrighterDarkerDarkerDarkButtonColor(){
        return brighter(darker(darker(getDarkButtonColor())));
    }
    public Color getBrighterBrighterDarkButtonColor(){
        return brighter(brighter(getDarkButtonColor()));
    }
    public Color getBrighterDarkerDarkerButtonColor(){
        return brighter(darker(darker(getButtonColor())));
    }
    public Color getBrighterBrighterButtonColor(){
        return brighter(brighter(getButtonColor()));
    }
    public Color getBrighterDarkerButtonColor(){
        return brighter(darker(getButtonColor()));
    }
    public Color getDarkerListColor(){
        return darker(getListColor());
    }
}