package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.ncplanner.plannerator.multiblock.Action;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.CuboidalMultiblock;
import net.ncplanner.plannerator.multiblock.EditorSpace;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.action.ClearSelectionAction;
import net.ncplanner.plannerator.multiblock.action.SetCoolantRecipeAction;
import net.ncplanner.plannerator.multiblock.action.SetFuelAction;
import net.ncplanner.plannerator.multiblock.action.SetFusionCoolantRecipeAction;
import net.ncplanner.plannerator.multiblock.action.SetFusionRecipeAction;
import net.ncplanner.plannerator.multiblock.action.SetSelectionAction;
import net.ncplanner.plannerator.multiblock.action.SetTurbineRecipeAction;
import net.ncplanner.plannerator.multiblock.action.SetblocksAction;
import net.ncplanner.plannerator.multiblock.configuration.IBlockRecipe;
import net.ncplanner.plannerator.multiblock.overhaul.fissionmsr.OverhaulMSR;
import net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.OverhaulSFR;
import net.ncplanner.plannerator.multiblock.overhaul.fusion.OverhaulFusionReactor;
import net.ncplanner.plannerator.multiblock.overhaul.turbine.OverhaulTurbine;
import net.ncplanner.plannerator.multiblock.underhaul.fissionsfr.UnderhaulSFR;
import net.ncplanner.plannerator.planner.Consumer;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Pinnable;
import net.ncplanner.plannerator.planner.Task;
import net.ncplanner.plannerator.planner.editor.ClipboardEntry;
import net.ncplanner.plannerator.planner.editor.Editor;
import net.ncplanner.plannerator.planner.editor.suggestion.Suggestion;
import net.ncplanner.plannerator.planner.editor.suggestion.Suggestor;
import net.ncplanner.plannerator.planner.editor.tool.EditorTool;
import net.ncplanner.plannerator.planner.editor.tool.PencilTool;
import net.ncplanner.plannerator.planner.menu.component.SquareButton;
import net.ncplanner.plannerator.planner.module.Module;
import net.ncplanner.plannerator.simplelibrary.image.Color;
public class MenuEdit extends Form implements Editor{
    private final ArrayList<EditorTool> editorTools = new ArrayList<>();
    public ArrayList<ClipboardEntry> clipboard = new ArrayList<>();
    private Task suggestionTask;
    private boolean autoRecalc = true;
    private EditorTool selectedTool;
    private final Container partsList;
    private final TextField partsSearch;
    private Block selectedBlock;
    private IBlockRecipe selectedBlockRecipe;
    private final Container multiblockSettingsPanel;
    private Container blockRecipesList;
    private Consumer<net.ncplanner.plannerator.multiblock.configuration.underhaul.fissionsfr.Fuel> setUnderFuelFunc;
    private Consumer<net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.CoolantRecipe> setCoolantRecipeFunc;
    private Consumer<net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.CoolantRecipe> setFusionCoolantRecipeFunc;
    private Consumer<net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Recipe> setFusionRecipeFunc;
    private Consumer<net.ncplanner.plannerator.multiblock.configuration.overhaul.turbine.Recipe> setTurbineRecipeFunc;
    private final TextArea editorTooltip;
    public final Container editorPanel;
    private int blockSize;
    {
        editorTools.add(new PencilTool(this, 0));
    }
    private final Multiblock multiblock;
    public static final int partsWide = 5;
    public final ArrayList<int[]> selection = new ArrayList<>();
    private ArrayList<Suggestion> suggestions = new ArrayList<>();
    private ArrayList<Suggestor> suggestors = new ArrayList<>();
    public MenuEdit(Multiblock multiblock){
        super(new BorderLayout());
        if(Core.recoveryMode)autoRecalc = false;
        this.multiblock = multiblock;
        Container leftSidebar = new Container(new BorderLayout());
        add(LEFT, leftSidebar);
        Container leftSidebarHeader = new Container(BoxLayout.y());
        leftSidebar.add(TOP, leftSidebarHeader);
        Container leftSidebarButtons = new Container(new BorderLayout());
        leftSidebarHeader.add(leftSidebarButtons);
        Container undoRedoButtons = new Container(BoxLayout.x());
        leftSidebarButtons.add(RIGHT, undoRedoButtons);
        Button back = new Button("Back");
        leftSidebarButtons.add(CENTER, back);
        back.addActionListener((evt) -> {
            suggestionTask = null;
            if(multiblock.calculationPaused)multiblock.recalculate();
            new MenuMain().showBack();
        });
        Button undo = new SquareButton(" "){
            @Override
            public void paint(Graphics g){
                super.paint(g);
                g.setColor(Core.theme.getComponentTextColor().getRGB());
                int tallness = getHeight()*3/2;
                Core.drawOval(g, getX()+getWidth()/2, getY()+getHeight()/2+tallness-getHeight()/16, getWidth(), tallness, getHeight()/8, 160, 0, 151, 10);
                Core.drawRegularPolygon(g, getX()+getWidth()/4, getY()+getHeight()*9/16, getWidth()/4, 3, -5, 0);
            }
        };
        undo.addActionListener((evt) -> {
            multiblock.undo(autoRecalc);
            if(Core.autoBuildCasing&&multiblock instanceof CuboidalMultiblock){
                ((CuboidalMultiblock)multiblock).buildDefaultCasing();
                if(autoRecalc)multiblock.recalculate();
            }
        });
        blockSize = undo.getPreferredH();
        undoRedoButtons.add(undo);
        Button redo = new SquareButton(" "){
            @Override
            public void paint(Graphics g){
                super.paint(g);
                g.setColor(Core.theme.getComponentTextColor().getRGB());
                int tallness = getHeight()*3/2;
                Core.drawOval(g, getX()+getWidth()/2, getY()+getHeight()/2+tallness-getHeight()/16, getWidth(), tallness, getHeight()/8, 160, 0, 150, 9);
                Core.drawRegularPolygon(g, getX()+getWidth()*3/4, getY()+getHeight()*9/16, getWidth()/4, 3, 5, 0);
            }
        };
        redo.addActionListener((evt) -> {
            multiblock.redo(autoRecalc);
            if(Core.autoBuildCasing&&multiblock instanceof CuboidalMultiblock){
                ((CuboidalMultiblock)multiblock).buildDefaultCasing();
                if(autoRecalc)multiblock.recalculate();
            }
        });
        undoRedoButtons.add(redo);
        Container leftSidebarHeaderPanel = new Container(new BorderLayout());
        leftSidebarHeader.add(leftSidebarHeaderPanel);
        Container tools = new Container(BoxLayout.y());
        tools.setScrollableY(true);
        leftSidebarHeaderPanel.add(LEFT, tools);
        ArrayList<Runnable> toolResets = new ArrayList<>();
        selectedTool = editorTools.get(0);
        for(EditorTool tool : editorTools){
            Button button = new SquareButton(" "){
                boolean isSelected = selectedTool==tool;
                {
                    toolResets.add(() -> {
                        boolean shouldRepaint = isSelected;
                        isSelected = false;
                        if(shouldRepaint)repaint();
                    });
                    addActionListener((evt) -> {
                        for(Runnable r : toolResets)r.run();
                        isSelected = true;
                        selectedTool = tool;
                        repaint();
                    });
                }
                @Override
                public void paint(Graphics g){
                    super.paint(g);
                    g.setColor(getStyle().getFgColor());
                    tool.render(g, getX(), getY(), getWidth(), getHeight());
                    if(isSelected){
                        int x = getX();
                        int y = getY();
                        int width = getWidth();
                        int height = getHeight();
                        int border = height/8;
                        g.setColor(Core.theme.getEditorListLightSelectedColor().getRGB());
                        g.setAlpha(216);
                        g.fillRect(x, y, border, border);
                        g.fillRect(x+width-border, y, border, border);
                        g.fillRect(x, y+height-border, border, border);
                        g.fillRect(x+width-border, y+height-border, border, border);
                        g.setColor(Core.theme.getEditorListDarkSelectedColor().getRGB());
                        g.fillRect(x+border, y, width-border*2, border);
                        g.fillRect(x+border, y+height-border, width-border*2, height);
                        g.fillRect(x, y+border, border, height-border*2);
                        g.fillRect(x+width-border, y+border, width, height-border*2);
                        g.setAlpha(255);
                    }
                }
            };
            button.getStyle().setBgColor(Core.theme.getEditorToolBackgroundColor().getRGB());
            button.getSelectedStyle().setBgColor(Core.theme.getEditorToolBackgroundColor().getRGB());
            button.getPressedStyle().setBgColor(Core.theme.getEditorToolBackgroundColor().getRGB());
            tools.add(button);
        }
        Container partsContainer = new Container(BoxLayout.y()){
            @Override
            protected Dimension calcPreferredSize(){
                Dimension d = super.calcPreferredSize();
                d.setHeight(Math.min(d.getHeight(), MenuEdit.this.getHeight()/2));
                return d;
            }
        };
        partsContainer.setScrollableY(true);
        leftSidebarHeaderPanel.add(CENTER, partsContainer);
        partsSearch = new TextField(Core.autoBuildCasing?"-port":"", "Search", 1, TextField.ANY);
        partsSearch.addDataChangedListener((type, index) -> {
            refreshPartsList();
        });
        partsContainer.add(partsSearch);
        partsList = new Container(BoxLayout.y());
        partsList.setScrollableY(true);
        partsContainer.add(partsList);
        editorTooltip = new TextArea("", 1, 5){
            @Override
            protected Dimension calcPreferredSize(){
                Dimension d = super.calcPreferredSize();
                d.setWidth(1);
                return d;
            }
        };
        editorTooltip.setSingleLineTextArea(false);
        editorTooltip.setEditable(false);
        leftSidebar.add(CENTER, editorTooltip);
        Container leftSidebarFooter = new Container(BoxLayout.y());
        leftSidebar.add(BOTTOM, leftSidebarFooter);
        Container leftSidebarFooterButtons = new Container(new BorderLayout());
        leftSidebarFooter.add(leftSidebarFooterButtons);
        Button recalc = new Button("Recalculate");
        recalc.addActionListener((evt) -> {
            multiblock.recalculate();
            repaint();
        });
        leftSidebarFooterButtons.add(CENTER, recalc);
        Button step = new Button("Step");
        step.addActionListener((evt) -> {
            multiblock.recalcStep();
            repaint();
        });
        leftSidebarFooterButtons.add(RIGHT, step);
        Container rightSidebar = new Container(new BorderLayout());
        add(RIGHT, rightSidebar);
        Container rightSidebarHeader = new Container(BoxLayout.y());
        Button resize = new Button("Resize");
        resize.addActionListener((evt) -> {
            multiblock.getResizeMenu().show();
        });
        rightSidebarHeader.add(resize);
        Container zoomPanel = new Container(new GridLayout(1, 2));
        Button zoomIn = new Button("Zoom in");
        zoomPanel.add(zoomIn);
        Button zoomOut = new Button("Zoom out");
        zoomPanel.add(zoomOut);
        zoomOut.addActionListener((e) -> {
            zoomOut();
        });
        zoomIn.addActionListener((e) -> {
            zoomIn();
        });
        rightSidebarHeader.add(zoomPanel);
        rightSidebar.add(TOP, rightSidebarHeader);
        multiblockSettingsPanel = new Container(new GridLayout(1));
        rightSidebar.add(CENTER, multiblockSettingsPanel);
        Container centerPanel = new Container(new BorderLayout());
        add(CENTER, centerPanel);
        Button metadata = new Button(multiblock.getName().isEmpty()?"Edit Metadata":(multiblock.getName()+" | Edit Metadata"));
        centerPanel.add(TOP, metadata);
        editorPanel = new Container(BoxLayout.y());
        editorPanel.setScrollableX(true);
        editorPanel.setScrollableY(true);
        centerPanel.add(CENTER, editorPanel);
        metadata.addActionListener((evt) -> {
            new MenuEditMetadata(multiblock.metadata, multiblock::resetMetadata, ()->{
                return new MenuEdit(multiblock);
            }).show();
        });
        refreshPartsList();
        if(multiblock instanceof UnderhaulSFR){
            Container panel = new Container(new BorderLayout());
            panel.add(TOP, new Label("Fuel"));
            Container list = new Container(BoxLayout.y());
            list.setScrollableY(true);
            panel.add(CENTER, list);
            multiblockSettingsPanel.add(panel);
            ArrayList<Runnable> resetFuncs = new ArrayList<>();
            HashMap<net.ncplanner.plannerator.multiblock.configuration.underhaul.fissionsfr.Fuel, Runnable> setFuncs = new HashMap<>();
            for(net.ncplanner.plannerator.multiblock.configuration.underhaul.fissionsfr.Fuel fuel : Core.configuration.underhaul.fissionSFR.allFuels){
                Button button = new Button(fuel.getDisplayName());
                Runnable setFunc = () -> {
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                };
                setFuncs.put(fuel, setFunc);
                if(((UnderhaulSFR)multiblock).fuel==fuel)setFunc.run();
                resetFuncs.add(() -> {
                    button.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                });
                button.addActionListener((evt) -> {
                    for(Runnable r : resetFuncs)r.run();
                    setFunc.run();
                    action(new SetFuelAction(this, fuel), true);
                });
                list.add(button);
            }
            setUnderFuelFunc = (f) -> {
                for(Runnable r : resetFuncs)r.run();
                setFuncs.get(f).run();
            };
        }
        if(multiblock instanceof OverhaulSFR){
            Container panel = new Container(new BorderLayout());
            panel.add(TOP, new Label("Coolant Recipe"));
            Container list = new Container(BoxLayout.y());
            list.setScrollableY(true);
            panel.add(CENTER, list);
            multiblockSettingsPanel.add(panel);
            ArrayList<Runnable> resetFuncs = new ArrayList<>();
            HashMap<net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.CoolantRecipe, Runnable> setFuncs = new HashMap<>();
            for(net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.CoolantRecipe recipe : Core.configuration.overhaul.fissionSFR.allCoolantRecipes){
                Button button = new Button(recipe.getInputDisplayName());
                Runnable setFunc = () -> {
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                };
                setFuncs.put(recipe, setFunc);
                if(((OverhaulSFR)multiblock).coolantRecipe==recipe)setFunc.run();
                resetFuncs.add(() -> {
                    button.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                });
                button.addActionListener((evt) -> {
                    for(Runnable r : resetFuncs)r.run();
                    setFunc.run();
                    action(new SetCoolantRecipeAction(this, recipe), true);
                });
                list.add(button);
            }
            setCoolantRecipeFunc = (r) -> {
                for(Runnable rr : resetFuncs)rr.run();
                setFuncs.get(r).run();
            };
        }
        if(multiblock instanceof OverhaulTurbine){
            Container panel = new Container(new BorderLayout());
            panel.add(TOP, new Label("Recipe"));
            Container list = new Container(BoxLayout.y());
            list.setScrollableY(true);
            panel.add(CENTER, list);
            multiblockSettingsPanel.add(panel);
            ArrayList<Runnable> resetFuncs = new ArrayList<>();
            HashMap<net.ncplanner.plannerator.multiblock.configuration.overhaul.turbine.Recipe, Runnable> setFuncs = new HashMap<>();
            for(net.ncplanner.plannerator.multiblock.configuration.overhaul.turbine.Recipe recipe : Core.configuration.overhaul.turbine.allRecipes){
                Button button = new Button(recipe.getInputDisplayName());
                Runnable setFunc = () -> {
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                };
                setFuncs.put(recipe, setFunc);
                if(((OverhaulTurbine)multiblock).recipe==recipe)setFunc.run();
                resetFuncs.add(() -> {
                    button.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                });
                button.addActionListener((evt) -> {
                    for(Runnable r : resetFuncs)r.run();
                    setFunc.run();
                    action(new SetTurbineRecipeAction(this, recipe), true);
                });
                list.add(button);
            }
            setCoolantRecipeFunc = (r) -> {
                for(Runnable rr : resetFuncs)rr.run();
                setFuncs.get(r).run();
            };
        }
        if(multiblock instanceof OverhaulFusionReactor){
            Container recipePanel = new Container(new BorderLayout());
            recipePanel.add(TOP, new Label("Recipe"));
            Container recipeList = new Container(BoxLayout.y());
            recipeList.setScrollableY(true);
            recipePanel.add(CENTER, recipeList);
            multiblockSettingsPanel.add(recipePanel);
            ArrayList<Runnable> recipeResetFuncs = new ArrayList<>();
            HashMap<net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Recipe, Runnable> recipeSetFuncs = new HashMap<>();
            for(net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Recipe recipe : Core.configuration.overhaul.fusion.allRecipes){
                Button button = new Button(recipe.getInputDisplayName());
                Runnable setFunc = ()->{
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                };
                recipeSetFuncs.put(recipe, setFunc);
                if(((OverhaulFusionReactor)multiblock).recipe==recipe)setFunc.run();
                recipeResetFuncs.add(() -> {
                    button.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                });
                button.addActionListener((evt) -> {
                    for(Runnable r : recipeResetFuncs)r.run();
                    setFunc.run();
                    action(new SetFusionRecipeAction(this, recipe), true);
                });
                recipeList.add(button);
            }
            setFusionRecipeFunc = (r)->{
                for(Runnable rr : recipeResetFuncs)rr.run();
                recipeSetFuncs.get(r).run();
            };
            Container coolantRecipePanel = new Container(new BorderLayout());
            coolantRecipePanel.add(TOP, new Label("Coolant Recipe"));
            Container coolantRecipeList = new Container(BoxLayout.y());
            coolantRecipeList.setScrollableY(true);
            coolantRecipePanel.add(CENTER, coolantRecipeList);
            multiblockSettingsPanel.add(coolantRecipePanel);
            ArrayList<Runnable> coolantRecipeResets = new ArrayList<>();
            HashMap<net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.CoolantRecipe, Runnable> coolantRecipeSets = new HashMap<>();
            for(net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.CoolantRecipe recipe : Core.configuration.overhaul.fusion.allCoolantRecipes){
                Button button = new Button(recipe.getInputDisplayName());
                Runnable setFunc = ()->{
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                };
                coolantRecipeSets.put(recipe, setFunc);
                if(((OverhaulFusionReactor)multiblock).coolantRecipe==recipe)setFunc.run();
                coolantRecipeResets.add(() -> {
                    button.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                });
                button.addActionListener((evt) -> {
                    for(Runnable r : coolantRecipeResets)r.run();
                    setFunc.run();
                    action(new SetFusionCoolantRecipeAction(this, recipe), true);
                });
                coolantRecipeList.add(button);
            }
            setFusionCoolantRecipeFunc = (r)->{
                for(Runnable rr : coolantRecipeResets)rr.run();
                coolantRecipeSets.get(r).run();
            };
        }
        refreshBlockRecipes();
        multiblock.getSuggestors(suggestors);
        for(Module m : Core.modules){
            if(m.isActive())m.getSuggestors(multiblock, suggestors);
        }
        //TODO add a panel for suggestors somewhere
        rebuildEditorSpaces();
    }
    @Override
    public void show(){
        super.show();
        onOpen();
    }
    @Override
    public void showBack(){
        super.showBack();
        onOpen();
    }
    private void onOpen(){
        if(recalculateOnOpen&&!Core.recoveryMode){
            autoRecalc = true;
            multiblock.recalculate();
        }
        recalculateOnOpen = true;
    }
    private boolean recalculateOnOpen = true;
    public void rebuildEditorSpaces(){
        editorPanel.removeAll();
        ArrayList<EditorSpace> editorSpaces = multiblock.getEditorSpaces();
        for(EditorSpace space : editorSpaces){
            ArrayList<Component> comps = new ArrayList<>();
            space.createComponents(this, comps, blockSize);
            for(int i = 0; i<comps.size(); i++){
                Component comp = comps.get(i);
                editorPanel.add(comp);//stack them all vertically cuz I'm lazy
            }
        }
        revalidate();
    }
    private void refreshPartsList(){
        List<Block> availableBlocks = ((Multiblock<Block>)multiblock).getAvailableBlocks();
        ArrayList<Block> searchedAvailable = Pinnable.searchAndSort(availableBlocks, partsSearch.getText());
        partsList.removeAll();
        boolean hasSelected = false;
        for(Block availableBlock : searchedAvailable){
            if(selectedBlock!=null&&selectedBlock.isEqual(availableBlock))hasSelected = true;
        }
        if(!hasSelected&&!searchedAvailable.isEmpty())selectedBlock = searchedAvailable.get(0);
        ArrayList<Runnable> resets = new ArrayList<>();
        Container c = null;
        int i = 0;
        searchedAvailable.add(0, null);
        for(Block availableBlock : searchedAvailable){
            if(selectedBlock==null){
                if(availableBlock==null)hasSelected = true;
            }else if(selectedBlock.isEqual(availableBlock))hasSelected = true;
            if(i==partsWide)i = 0;
            if(i==0){
                if(c!=null)partsList.add(c);
                c = new Container(BoxLayout.x());
            }
            i++;
            Button button = new SquareButton(" "){
                boolean isSelected = selectedBlock==availableBlock;
                {
                    resets.add(() -> {
                        boolean shouldRepaint = isSelected;
                        isSelected = false;
                        if(shouldRepaint)repaint();
                    });
                    addActionListener((evt) -> {
                        for(Runnable r : resets)r.run();
                        isSelected = true;
                        selectedBlock = availableBlock;
                        repaint();
                        refreshBlockRecipes();
                        refreshEditorTooltip();
                    });
                }
                @Override
                public void paint(Graphics g){
                    super.paint(g);
                    if(availableBlock!=null)availableBlock.render(g, getX(), getY(), getWidth(), getHeight(), false, null);
                    if(isSelected){
                        int x = getX();
                        int y = getY();
                        int width = getWidth();
                        int height = getHeight();
                        int border = height/8;
                        g.setColor(Core.theme.getEditorListLightSelectedColor().getRGB());
                        g.setAlpha(216);
                        g.fillRect(x, y, border, border);
                        g.fillRect(x+width-border, y, border, border);
                        g.fillRect(x, y+height-border, border, border);
                        g.fillRect(x+width-border, y+height-border, border, border);
                        g.setColor(Core.theme.getEditorListDarkSelectedColor().getRGB());
                        g.fillRect(x+border, y, width-border*2, border);
                        g.fillRect(x+border, y+height-border, width-border*2, height);
                        g.fillRect(x, y+border, border, height-border*2);
                        g.fillRect(x+width-border, y+border, width, height-border*2);
                        g.setAlpha(255);
                    }
                }
                @Override
                protected Dimension calcPreferredSize(){
                    Dimension d = super.calcPreferredSize();
                    d.setHeight(Math.max(d.getHeight(), getWidth()));
                    return d;
                }
            };
            button.getStyle().setBgColor(Core.theme.getEditorListBackgroundColor().getRGB());
            button.getSelectedStyle().setBgColor(Core.theme.getEditorListBackgroundColor().getRGB());
            button.getPressedStyle().setBgColor(Core.theme.getEditorListBackgroundColor().getRGB());
            if(i==partsWide){
                button.getStyle().setMarginRight(button.getPreferredH()/6);
                button.getSelectedStyle().setMarginRight(button.getPreferredH()/6);
                button.getPressedStyle().setMarginRight(button.getPreferredH()/6);
            }
            c.add(button);
        }
        if(c!=null)partsList.add(c);
        refreshBlockRecipes();
        refreshEditorTooltip();
    }
    private void refreshBlockRecipes(){
        if(blockRecipesList!=null){
            blockRecipesList.remove();
            revalidate();
        }
        if(selectedBlock!=null&&selectedBlock.hasRecipes()){
            List<? extends IBlockRecipe> recipes = selectedBlock.getRecipes();
            ArrayList<? extends IBlockRecipe> sorted = Pinnable.sort(recipes);
            blockRecipesList = new Container(BoxLayout.y());
            blockRecipesList.setScrollableY(true);
            multiblockSettingsPanel.add(blockRecipesList);
            ArrayList<Runnable> resets = new ArrayList<>();
            selectedBlockRecipe = sorted.get(0);
            for(IBlockRecipe recipe : sorted){
                Button button = new Button(recipe.getInputDisplayName());
                if(selectedBlockRecipe==recipe){
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                }
                resets.add(() -> {
                    button.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
                });
                button.addActionListener((evt) -> {
                    for(Runnable r : resets)r.run();
                    button.getStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    button.getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
                    selectedBlockRecipe = recipe;
                });
                blockRecipesList.add(button);
            }
            revalidate();
        }
        refreshEditorTooltip();
    }
    @Override
    public Block getSelectedBlock(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        return selectedBlock;
    }
    @Override
    public EditorTool getSelectedTool(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        return selectedTool;
    }
    @Override
    public net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.BlockRecipe getSelectedOverhaulFusionBlockRecipe(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        return (net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.BlockRecipe)selectedBlockRecipe;
    }
    @Override
    public net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionmsr.BlockRecipe getSelectedOverhaulMSRBlockRecipe(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        return (net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionmsr.BlockRecipe)selectedBlockRecipe;
    }
    @Override
    public net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.BlockRecipe getSelectedOverhaulSFRBlockRecipe(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        return (net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr.BlockRecipe)selectedBlockRecipe;
    }
    @Override
    public void setblocks(int id, SetblocksAction set){
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] b : set.locations){
            if(hasSelection(id)&&!isSelected(id, b[0], b[1], b[2]))toRemove.add(b);
        }
        set.locations.removeAll(toRemove);
        if(set.block!=null&&multiblock instanceof OverhaulSFR){
            net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.Block block = (net.ncplanner.plannerator.multiblock.overhaul.fissionsfr.Block)set.block;
            if(!block.template.allRecipes.isEmpty()||(block.template.parent!=null&&!block.template.parent.allRecipes.isEmpty())){
                block.recipe = getSelectedOverhaulSFRBlockRecipe(id);
            }
        }
        if(set.block!=null&&multiblock instanceof OverhaulMSR){
            net.ncplanner.plannerator.multiblock.overhaul.fissionmsr.Block block = (net.ncplanner.plannerator.multiblock.overhaul.fissionmsr.Block)set.block;
            if(!block.template.allRecipes.isEmpty()||(block.template.parent!=null&&!block.template.parent.allRecipes.isEmpty())){
                block.recipe = getSelectedOverhaulMSRBlockRecipe(id);
            }
        }
        if(set.block!=null&&multiblock instanceof OverhaulFusionReactor){
            net.ncplanner.plannerator.multiblock.overhaul.fusion.Block block = (net.ncplanner.plannerator.multiblock.overhaul.fusion.Block)set.block;
            if(!block.template.allRecipes.isEmpty()){
                block.recipe = getSelectedOverhaulFusionBlockRecipe(id);
            }
        }
        action(set, true);
    }
    @Override
    public void select(int id, int x1, int y1, int z1, int x2, int y2, int z2){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        ArrayList<int[]> is = new ArrayList<>();
        for(int x = Math.min(x1,x2); x<=Math.max(x1,x2); x++){
            for(int y = Math.min(y1,y2); y<=Math.max(y1,y2); y++){
                for(int z = Math.min(z1,z2); z<=Math.max(z1,z2); z++){
                    is.add(new int[]{x,y,z});
                }
            }
        }
        select(id, is);
    }
    @Override
    public void deselect(int id, int x1, int y1, int z1, int x2, int y2, int z2){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        ArrayList<int[]> is = new ArrayList<>();
        for(int x = Math.min(x1,x2); x<=Math.max(x1,x2); x++){
            for(int y = Math.min(y1,y2); y<=Math.max(y1,y2); y++){
                for(int z = Math.min(z1,z2); z<=Math.max(z1,z2); z++){
                    is.add(new int[]{x,y,z});
                }
            }
        }
        deselect(id, is);
    }
    public void select(int id, ArrayList<int[]> is){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        action(new SetSelectionAction(this, id, is), true);
    }
    public void setSelection(int id, ArrayList<int[]> is){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        action(new SetSelectionAction(this, id, is), true);
    }
    public void deselect(int id, ArrayList<int[]> is){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        clearSelection(id);
    }
    @Override
    public boolean isSelected(int id, int x, int y, int z){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        synchronized(selection){
            for(int[] s : selection){
                if(s==null)continue;//THIS SHOULD NEVER HAPPEN but it does anyway
                if(s[0]==x&&s[1]==y&&s[2]==z)return true;
            }
        }
        return false;
    }
    @Override
    public boolean hasSelection(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        synchronized(selection){
            return !selection.isEmpty();
        }
    }
    @Override
    public void selectCluster(int id, int x, int y, int z){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        if(multiblock instanceof OverhaulSFR){
            OverhaulSFR osfr = (OverhaulSFR) multiblock;
            OverhaulSFR.Cluster c = osfr.getCluster(osfr.getBlock(x, y, z));
            if(c==null)return;
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            select(id, is);
        }
        if(multiblock instanceof OverhaulMSR){
            OverhaulMSR omsr = (OverhaulMSR) multiblock;
            OverhaulMSR.Cluster c = omsr.getCluster(omsr.getBlock(x, y, z));
            if(c==null)return;
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            select(id, is);
        }
        if(multiblock instanceof OverhaulFusionReactor){
            OverhaulFusionReactor ofr = (OverhaulFusionReactor) multiblock;
            OverhaulFusionReactor.Cluster c = ofr.getCluster(ofr.getBlock(x, y, z));
            if(c==null)return;
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            select(id, is);
        }
    }
    @Override
    public void deselectCluster(int id, int x, int y, int z){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        if(multiblock instanceof OverhaulSFR){
            OverhaulSFR osfr = (OverhaulSFR) multiblock;
            OverhaulSFR.Cluster c = osfr.getCluster(osfr.getBlock(x, y, z));
            if(c==null)return;
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            deselect(id, is);
        }
        if(multiblock instanceof OverhaulMSR){
            OverhaulMSR omsr = (OverhaulMSR) multiblock;
            OverhaulMSR.Cluster c = omsr.getCluster(omsr.getBlock(x, y, z));
            if(c==null)return;
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            deselect(id, is);
        }
        if(multiblock instanceof OverhaulFusionReactor){
            OverhaulFusionReactor ofr = (OverhaulFusionReactor) multiblock;
            OverhaulFusionReactor.Cluster c = ofr.getCluster(ofr.getBlock(x, y, z));
            if(c==null)return;
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            deselect(id, is);
        }
    }
    @Override
    public void selectGroup(int id, int x, int y, int z){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        ArrayList<Block> g = multiblock.getGroup(multiblock.getBlock(x, y, z));
        if(g==null){
            selectAll(id);
            return;
        }
        ArrayList<int[]> is = new ArrayList<>();
        for(Block b : g){
            is.add(new int[]{b.x,b.y,b.z});
        }
        select(id, is);
    }
    @Override
    public void deselectGroup(int id, int x, int y, int z){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        ArrayList<Block> g = multiblock.getGroup(multiblock.getBlock(x, y, z));
        if(g==null){
            deselectAll(id);
            return;
        }
        ArrayList<int[]> is = new ArrayList<>();
        for(Block b : g){
            is.add(new int[]{b.x,b.y,b.z});
        }
        deselect(id, is);
    }
    @Override
    public void clearSelection(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        action(new ClearSelectionAction(this, id), true);
    }
    @Override
    public void addSelection(int id, ArrayList<int[]> sel){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        synchronized(selection){
            for(int[] is : selection){
                ArrayList<int[]> toRemove = new ArrayList<>();
                for(int[] i : sel){
                    if(i[0]==is[0]&&i[1]==is[1]&&i[2]==is[2]){
                        toRemove.add(i);
                    }
                }
                sel.removeAll(toRemove);
            }
            selection.addAll(sel);
        }
    }
    @Override
    public void copySelection(int id, int x, int y, int z){//like copySelection, but clipboardier
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        throw new UnsupportedOperationException("Copy is not supported!");
    }
    @Override
    public void cutSelection(int id, int x, int y, int z){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports one cursor!");
        throw new UnsupportedOperationException("Cut is not supported!");
    }
    @Override
    public ArrayList<int[]> getSelection(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports 1 cursor!");
        return selection;
    }
    @Override
    public Multiblock getMultiblock(){
        return multiblock;
    }
    @Override
    public void setCoolantRecipe(int idx){
        setCoolantRecipeFunc.accept(multiblock.getConfiguration().overhaul.fissionSFR.allCoolantRecipes.get(idx));
    }
    @Override
    public void setUnderhaulFuel(int idx){
        setUnderFuelFunc.accept(multiblock.getConfiguration().underhaul.fissionSFR.allFuels.get(idx));
    }
    @Override
    public void setFusionCoolantRecipe(int idx){
        setFusionCoolantRecipeFunc.accept(multiblock.getConfiguration().overhaul.fusion.allCoolantRecipes.get(idx));
    }
    @Override
    public void setFusionRecipe(int idx){
        setFusionRecipeFunc.accept(multiblock.getConfiguration().overhaul.fusion.allRecipes.get(idx));
    }
    @Override
    public void setTurbineRecipe(int idx){
        setTurbineRecipeFunc.accept(multiblock.getConfiguration().overhaul.turbine.allRecipes.get(idx));
    }
    @Override
    public ArrayList<ClipboardEntry> getClipboard(int id){
        if(id!=0)throw new IllegalArgumentException("Standard editor only supports 1 cursor!");
        return clipboard;
    }
    @Override
    public boolean isControlPressed(int id){
        return false;
    }
    @Override
    public boolean isShiftPressed(int id){
        return false;
    }
    @Override
    public boolean isAltPressed(int id){
        return false;
    }
    @Override
    public Color convertToolColor(Color color, int id){
        return color;//standard colors
    }
    @Override
    public ArrayList<Suggestion> getSuggestions(){
        return new ArrayList<>(suggestions);
    }
    @Override
    public Task getTask(){
        Task task = multiblock.getTask();
        if(task==null)task = suggestionTask;
        return task;
    }
    @Override
    public void action(Action action, boolean allowUndo){
        if(multiblock.calculationPaused)multiblock.recalculate();
        multiblock.action(action, autoRecalc, allowUndo);
        if(Core.autoBuildCasing&&multiblock instanceof CuboidalMultiblock){
            ((CuboidalMultiblock)multiblock).buildDefaultCasing();
            if(autoRecalc)multiblock.recalculate();
        }
        refreshEditorTooltip();
        repaint();
    }
    private void selectAll(int id){
        ArrayList<int[]> sel = new ArrayList<>();
        multiblock.forEachPosition((x, y, z) -> {
            sel.add(new int[]{x,y,z});
        });
        setSelection(id, sel);
    }
    private void deselectAll(int id){
        setSelection(id, new ArrayList<>());
    }
    private void refreshEditorTooltip(){
        String tooltip = selectedBlock==null?"Air":selectedBlock.getListTooltip();
        tooltip+="\n\n"+multiblock.getFullTooltip().toString();
        editorTooltip.setGrowByContent(true);
        editorTooltip.setText(tooltip);
    }
    private void zoomOut(){
        blockSize = Math.max(16, blockSize*3/4);
        rebuildEditorSpaces();
    }
    private void zoomIn(){
        if(blockSize>getHeight()/2)return;
        blockSize = blockSize*4/3;
        rebuildEditorSpaces();
    }
}