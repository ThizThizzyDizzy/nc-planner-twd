package planner.theme;
import planner.Supplier;
import simplelibrary.image.Color;
public class ChangingTheme extends Theme{
    protected Theme current;
    private final Supplier<Theme> theme;
    public ChangingTheme(String name, Supplier<Theme> theme){
        super(name);
        this.theme = theme;
        current = theme.get();
    }
    @Override
    public void onSet(){
        current = theme.get();
    }
    @Override
    public Color getDecalColorAdjacentCell(){
        return current.getDecalColorAdjacentCell();
    }
    @Override
    public Color getDecalColorAdjacentModerator(){
        return current.getDecalColorAdjacentModerator();
    }
    @Override
    public Color getDecalColorAdjacentModeratorLine(float efficiency){
        return current.getDecalColorAdjacentModeratorLine(efficiency);
    }
    @Override
    public Color getDecalColorUnderhaulModeratorLine(){
        return current.getDecalColorUnderhaulModeratorLine();
    }
    @Override
    public Color getDecalColorReflectorAdjacentModeratorLine(){
        return current.getDecalColorReflectorAdjacentModeratorLine();
    }
    @Override
    public Color getDecalColorOverhaulModeratorLine(float efficiency){
        return current.getDecalColorOverhaulModeratorLine(efficiency);
    }
    @Override
    public Color getDecalTextColor(){
        return current.getDecalTextColor();
    }
    @Override
    public Color getDecalColorNeutronSourceTarget(){
        return current.getDecalColorNeutronSourceTarget();
    }
    @Override
    public Color getDecalColorNeutronSourceNoTarget(){
        return current.getDecalColorNeutronSourceNoTarget();
    }
    @Override
    public Color getDecalColorNeutronSourceLine(){
        return current.getDecalColorNeutronSourceLine();
    }
    @Override
    public Color getDecalColorNeutronSource(){
        return current.getDecalColorNeutronSource();
    }
    @Override
    public Color getDecalColorModeratorActive(){
        return current.getDecalColorModeratorActive();
    }
    @Override
    public Color getDecalColorMissingCasing(){
        return current.getDecalColorMissingCasing();
    }
    @Override
    public Color getDecalColorMissingBlade(){
        return current.getDecalColorMissingBlade();
    }
    @Override
    public Color getDecalColorIrradiatorAdjacentModeratorLine(){
        return current.getDecalColorIrradiatorAdjacentModeratorLine();
    }
    @Override
    public Color getDecalColorCellFlux(int flux, int criticality){
        return current.getDecalColorCellFlux(flux, criticality);
    }
    @Override
    public Color getDecalColorBlockValid(){
        return current.getDecalColorBlockValid();
    }
    @Override
    public Color getDecalColorBlockInvalid(){
        return current.getDecalColorBlockInvalid();
    }
    @Override
    public Color getBlockColorOutlineInvalid(){
        return current.getBlockColorOutlineInvalid();
    }
    @Override
    public Color getBlockColorOutlineActive(){
        return current.getBlockColorOutlineActive();
    }
    @Override
    public Color getBlockColorSourceCircle(float efficiency, boolean selfPriming){
        return current.getBlockColorSourceCircle(efficiency, selfPriming);
    }
    @Override
    public Color getClusterOverheatingColor(){
        return current.getClusterOverheatingColor();
    }
    @Override
    public Color getClusterOvercoolingColor(){
        return current.getClusterOvercoolingColor();
    }
    @Override
    public Color getClusterDisconnectedColor(){
        return current.getClusterDisconnectedColor();
    }
    @Override
    public Color getClusterInvalidColor(){
        return current.getClusterInvalidColor();
    }
    @Override
    public Color getTooltipInvalidTextColor(){
        return current.getTooltipInvalidTextColor();
    }
    @Override
    public Color getTooltipTextColor(){
        return current.getTooltipTextColor();
    }
    @Override
    public Color getEditorToolTextColor(){
        return current.getEditorToolTextColor();
    }
    @Override
    public Color getEditorToolBackgroundColor(){
        return current.getEditorToolBackgroundColor();
    }
    @Override
    public Color getSelectionColor(){
        return current.getSelectionColor();
    }
    @Override
    public Color getEditorBackgroundColor(){
        return current.getEditorBackgroundColor();
    }
    @Override
    public Color getImageExportBackgroundColor(){
        return current.getImageExportBackgroundColor();
    }
    @Override
    public Color getImageExportTextColor(){
        return current.getImageExportTextColor();
    }
    @Override
    public Color getComponentTextColor(){
        return current.getComponentTextColor();
    }
    @Override
    public Color getMouseoverSelectedComponentColor(){
        return current.getMouseoverSelectedComponentColor();
    }
    @Override
    public Color getSelectedComponentColor(){
        return current.getSelectedComponentColor();
    }
    @Override
    public Color getMouseoverComponentColor(){
        return current.getMouseoverComponentColor();
    }
    @Override
    public Color getComponentColor(){
        return current.getComponentColor();
    }
    @Override
    public Color getEditorBackgroundMouseoverColor(){
        return current.getEditorBackgroundMouseoverColor();
    }
    @Override
    public Color getEditorGridColor(){
        return current.getEditorGridColor();
    }
    @Override
    public Color getSuggestionOutlineColor(){
        return current.getSuggestionOutlineColor();
    }
    @Override
    public Color getEditorMouseoverLightColor(){
        return current.getEditorMouseoverLightColor();
    }
    @Override
    public Color getEditorMouseoverDarkColor(){
        return current.getEditorMouseoverDarkColor();
    }
    @Override
    public Color getEditorMouseoverLineColor(){
        return current.getEditorMouseoverLineColor();
    }
    @Override
    public Color getEditorListBackgroundMouseoverColor(){
        return current.getEditorListBackgroundMouseoverColor();
    }
    @Override
    public Color getEditorListBackgroundColor(){
        return current.getEditorListBackgroundColor();
    }
    @Override
    public Color getEditorListLightSelectedColor(){
        return current.getEditorListLightSelectedColor();
    }
    @Override
    public Color getEditorListDarkSelectedColor(){
        return current.getEditorListDarkSelectedColor();
    }
    @Override
    public Color getEditorListLightMouseoverColor(){
        return current.getEditorListLightMouseoverColor();
    }
    @Override
    public Color getEditorListDarkMouseoverColor(){
        return current.getEditorListDarkMouseoverColor();
    }
    @Override
    public Color getMultiblockSelectedInputColor(){
        return current.getMultiblockSelectedInputColor();
    }
    @Override
    public Color getMultiblockInvalidInputColor(){
        return current.getMultiblockInvalidInputColor();
    }
    @Override
    public Color getSecondaryComponentColor(){
        return current.getSecondaryComponentColor();
    }
    @Override
    public Color getProgressBarBackgroundColor(){
        return current.getProgressBarBackgroundColor();
    }
    @Override
    public Color getProgressBarColor(){
        return current.getProgressBarColor();
    }
    @Override
    public Color getMultiblockDisplayBorderColor(){
        return current.getMultiblockDisplayBorderColor();
    }
    @Override
    public Color getMultiblockDisplayBackgroundColor(){
        return current.getMultiblockDisplayBackgroundColor();
    }
    @Override
    public Color getToggleBlockFadeout(){
        return current.getToggleBlockFadeout();
    }
    @Override
    public Color getTutorialBackgroundColor(){
        return current.getTutorialBackgroundColor();
    }
    @Override
    public Color getScrollbarButtonColor(){
        return current.getScrollbarButtonColor();
    }
    @Override
    public Color getBlockUnknownColor(){
        return current.getBlockUnknownColor();
    }
    @Override
    public Color getBlockTextColor(){
        return current.getBlockTextColor();
    }
    @Override
    public Color getScrollbarBackgroundColor(){
        return current.getScrollbarBackgroundColor();
    }
    @Override
    public Color getComponentPressedColor(){
        return current.getComponentPressedColor();
    }
    @Override
    public Color getComponentMouseoverColor(){
        return current.getComponentMouseoverColor();
    }
    @Override
    public Color getComponentDisabledColor(){
        return current.getComponentDisabledColor();
    }
    @Override
    public Color getSecondaryComponentPressedColor(){
        return current.getSecondaryComponentPressedColor();
    }
    @Override
    public Color getSecondaryComponentMouseoverColor(){
        return current.getSecondaryComponentMouseoverColor();
    }
    @Override
    public Color getSecondaryComponentDisabledColor(){
        return current.getSecondaryComponentDisabledColor();
    }
    @Override
    public Color getSliderColor(){
        return current.getSliderColor();
    }
    @Override
    public Color getSliderPressedColor(){
        return current.getSliderPressedColor();
    }
    @Override
    public Color getSliderMouseoverColor(){
        return current.getSliderMouseoverColor();
    }
    @Override
    public Color getSliderDisabledColor(){
        return current.getSliderDisabledColor();
    }
    @Override
    public Color getSecondarySliderColor(){
        return current.getSecondarySliderColor();
    }
    @Override
    public Color getSecondarySliderPressedColor(){
        return current.getSecondarySliderPressedColor();
    }
    @Override
    public Color getSecondarySliderMouseoverColor(){
        return current.getSecondarySliderMouseoverColor();
    }
    @Override
    public Color getSecondarySliderDisabledColor(){
        return current.getSecondarySliderDisabledColor();
    }
    @Override
    public Color getTextBoxBorderColor(){
        return current.getTextBoxBorderColor();
    }
    @Override
    public Color getTextBoxColor(){
        return current.getTextBoxColor();
    }
    @Override
    public Color getTextViewBackgroundColor(){
        return current.getTextViewBackgroundColor();
    }
    @Override
    public Color getToggleBoxBorderColor(){
        return current.getToggleBoxBorderColor();
    }
    @Override
    public Color getSecondaryToggleBoxBorderColor(){
        return current.getSecondaryToggleBoxBorderColor();
    }
    @Override
    public Color getToggleBoxBackgroundColor(){
        return current.getToggleBoxBackgroundColor();
    }
    @Override
    public Color getToggleBoxSelectedColor(){
        return current.getToggleBoxSelectedColor();
    }
    @Override
    public Color getToggleBoxMouseoverColor(){
        return current.getToggleBoxMouseoverColor();
    }
    @Override
    public Color getMouseoverUnselectableComponentColor(){
        return current.getMouseoverUnselectableComponentColor();
    }
    @Override
    public Color getConfigurationSidebarColor(){
        return current.getConfigurationSidebarColor();
    }
    @Override
    public Color getConfigurationWarningTextColor(){
        return current.getConfigurationWarningTextColor();
    }
    @Override
    public Color getConfigurationDividerColor(){
        return current.getConfigurationDividerColor();
    }
    @Override
    public Color getDialogBorderColor(){
        return current.getDialogBorderColor();
    }
    @Override
    public Color getDialogBackgroundColor(){
        return current.getDialogBackgroundColor();
    }
    @Override
    public Color getCreditsImageColor(){
        return current.getCreditsImageColor();
    }
    @Override
    public Color getCreditsBrightImageColor(){
        return current.getCreditsBrightImageColor();
    }
    @Override
    public Color getCreditsTextColor(){
        return current.getCreditsTextColor();
    }
    @Override
    public Color get3DMultiblockOutlineColor(){
        return current.get3DMultiblockOutlineColor();
    }
    @Override
    public Color get3DDeviceoverOutlineColor(){
        return current.get3DDeviceoverOutlineColor();
    }
    @Override
    public Color getAddButtonTextColor(){
        return current.getAddButtonTextColor();
    }
    @Override
    public Color getDeleteButtonTextColor(){
        return current.getDeleteButtonTextColor();
    }
    @Override
    public Color getConvertButtonTextColor(){
        return current.getConvertButtonTextColor();
    }
    @Override
    public Color getInputsButtonTextColor(){
        return current.getInputsButtonTextColor();
    }
    @Override
    public Color getMetadataPanelBackgroundColor(){
        return current.getMetadataPanelBackgroundColor();
    }
    @Override
    public Color getMetadataPanelHeaderColor(){
        return current.getMetadataPanelHeaderColor();
    }
    @Override
    public Color getMetadataPanelTextColor(){
        return current.getMetadataPanelTextColor();
    }
    @Override
    public Color getMultiblocksListHeaderColor(){
        return current.getMultiblocksListHeaderColor();
    }
    @Override
    public Color getRecoveryModeColor(int index){
        return current.getRecoveryModeColor(index);
    }
    @Override
    public Color getRecoveryModeTextColor(){
        return current.getRecoveryModeTextColor();
    }
    @Override
    public Color getRotateMultiblockTextColor(){
        return current.getRotateMultiblockTextColor();
    }
    @Override
    public Color getResizeMenuTextColor(){
        return current.getResizeMenuTextColor();
    }
    @Override
    public Color getMenuBackgroundColor(){
        return current.getMenuBackgroundColor();
    }
    @Override
    public Color getSettingsSidebarColor(){
        return current.getSettingsSidebarColor();
    }
    @Override
    public Color getWhiteColor(){
        return current.getWhiteColor();
    }
    @Override
    public Color getTutorialTextColor(){
        return current.getTutorialTextColor();
    }
    @Override
    public Color getSettingsMergeTextColor(){
        return current.getSettingsMergeTextColor();
    }
}