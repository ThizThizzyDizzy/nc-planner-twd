package net.ncplanner.plannerator.planner.menu;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import net.ncplanner.plannerator.multiblock.configuration.AddonConfiguration;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Supplier;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.planner.file.FileReader;
import net.ncplanner.plannerator.planner.file.NCPFFile;
public class MenuConfiguration extends Form{
    private final Configuration configuration;
    private boolean threadShouldStop = false;
    private boolean threadHasStopped = true;
    private final Container addonsList;
    public MenuConfiguration(Configuration configuration){
        super(new BorderLayout());
        Container sidebar = new Container(BoxLayout.y());
        sidebar.getStyle().setBgTransparency(255);
        sidebar.getStyle().setBgColor(Core.theme.getSettingsSidebarColor().getRGB());
        add(LEFT, sidebar);
        Container mainParentPane = new Container(new BorderLayout());
        Container topPane = new Container(BoxLayout.y());
        mainParentPane.add(TOP, topPane);
        add(CENTER, mainParentPane);
        Button done = new Button("Done");
        sidebar.add(done);
        done.addActionListener((evt) -> {
            new MenuSettings().showBack();
        });
        sidebar.add(new Label("Configuration"));
        topPane.add(new Label(configuration.name));
        Container versionsPane = new Container(new GridLayout(1, 2));
        topPane.add(versionsPane);
        versionsPane.getStyle().setBgTransparency(255);
        versionsPane.getStyle().setBgColor(Core.theme.getConfigurationDividerColor().getRGB());
        Container underhaulCont = new Container(BoxLayout.y());
        Container overhaulCont = new Container(BoxLayout.y());
        underhaulCont.getStyle().setMarginRight(3);
        overhaulCont.getStyle().setMarginLeft(3);
        versionsPane.add(underhaulCont);
        versionsPane.add(overhaulCont);
        Label underhaul = new Label("Underhaul");
        underhaul.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
        underhaul.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
        underhaulCont.add(underhaul);
        Label uv = new Label(configuration.underhaulVersion==null?"":configuration.underhaulVersion);
        uv.getStyle().setBgColor(Core.theme.getTextBoxColor().getRGB());
        uv.getSelectedStyle().setBgColor(Core.theme.getTextBoxColor().getRGB());
        underhaulCont.add(uv);
        Label overhaul = new Label("Overhaul");
        overhaul.getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
        overhaul.getSelectedStyle().setBgColor(Core.theme.getComponentColor().getRGB());
        overhaulCont.add(overhaul);
        Label ov = new Label(configuration.overhaulVersion==null?"":configuration.overhaulVersion);
        ov.getStyle().setBgColor(Core.theme.getTextBoxColor().getRGB());
        ov.getSelectedStyle().setBgColor(Core.theme.getTextBoxColor().getRGB());
        overhaulCont.add(ov);
        topPane.add(new Label("Addons"));
        addonsList = new Container(BoxLayout.y());
        addonsList.setScrollableY(true);
        mainParentPane.add(CENTER, addonsList);
        if(FileChooser.isAvailable()){
            Button importButton = new Button("Import Addon");
            mainParentPane.add(BOTTOM, importButton);
            importButton.addActionListener((evt) -> {
                FileChooser.showOpenDialog(".ncpf", (e) -> {
                    if(e!=null&&e.getSource()!=null){
                        String file = (String)e.getSource();
                        NCPFFile ncpf = FileReader.read(file);
                        if(ncpf==null)return;
                        try{
                            configuration.addAndConvertAddon(AddonConfiguration.convert(ncpf.configuration));
                        }catch(MissingConfigurationEntryException ex){
                            throw new RuntimeException(ex);
                        }
                        refresh();
                    }
                });
            });
        }
        this.configuration = configuration;
        refresh();
    }
    private void refresh(){
        if(!configuration.addon&&!threadShouldStop){
            threadShouldStop = true;
            new Thread(() -> {
                Object sync = new Object();
                synchronized(sync){
                    while(!threadHasStopped){
                        try{
                            sync.wait(5);
                        }catch(InterruptedException ex){}
                    }
                }
                threadHasStopped = threadShouldStop = false;
                int i = 0;
                synchronized(addonsList){
                    addonsList.removeAll();
                    for(Configuration c : configuration.addons){
                        addonsList.add(new AddonComponent(c, i%2==0));
                        i++;
                        revalidateLater();
                    }
                }
                C:for(Supplier<AddonConfiguration> c : Configuration.internalAddons){
                    AddonConfiguration got = Configuration.internalAddonCache.get(c);
                    if(got==null){
                        got = c.get();
                        Configuration.internalAddonCache.put(c, got);
                    }
                    for(Configuration cc : configuration.addons){
                        if(got.nameMatches(cc))continue C;//CC
                    }
                    if(threadShouldStop){
                        threadHasStopped = true;
                        return;
                    }
                    synchronized(addonsList){
                        addonsList.add(new InternalAddonComponent(c, got, i%2==0, Configuration.addonLinks.get(c)));
                        i++;
                        revalidateLater();
                    }
                }
                threadHasStopped = true;
                revalidate();
            }, "Addon caching thread").start();
        }
    }
    private class AddonComponent extends Container{
        public AddonComponent(Configuration addon, boolean dark){
            super(new BorderLayout());
            getStyle().setBgTransparency(255);
            getStyle().setBgColor((dark?Core.theme.getSecondaryComponentColor():Core.theme.getComponentColor()).getRGB());
            String str;
            if(addon.overhaulVersion==null&&addon.underhaulVersion==null){
                str = addon.name;
            }else if(addon.overhaulVersion!=null&&addon.underhaulVersion!=null){
                str = addon.name+" "+addon.overhaulVersion+" | "+addon.underhaulVersion;
            }else{
                str = addon.name+" "+(addon.overhaulVersion==null?addon.underhaulVersion:addon.overhaulVersion);
            }
            Label label = new Label(str);
            label.getStyle().setBgTransparency(0);
            label.getSelectedStyle().setBgTransparency(0);
            add(LEFT, label);
            Button remove = new Button("Remove");
            remove.getStyle().setBgColor(Core.theme.getSecondaryComponentColor().getRGB());
            remove.getSelectedStyle().setBgColor(Core.theme.getSecondaryComponentColor().getRGB());
            remove.getPressedStyle().setBgColor(Core.theme.getSecondaryComponentPressedColor().getRGB());
            add(RIGHT, remove);
            remove.addActionListener((evt) -> {
                remove.setEnabled(false);
                configuration.removeAddon(addon);
                refresh();
            });
        }
    }
    private class InternalAddonComponent extends Container{
        public InternalAddonComponent(Supplier<AddonConfiguration> addon, AddonConfiguration actualAddon, boolean dark, String url){
            super(new BorderLayout());
            getStyle().setBgTransparency(255);
            getStyle().setBgColor((dark?Core.theme.getSecondaryComponentColor():Core.theme.getComponentColor()).getRGB());
            String str;
            if(actualAddon.overhaulVersion==null&&actualAddon.underhaulVersion==null){
                str = actualAddon.name;
            }else if(actualAddon.overhaulVersion!=null&&actualAddon.underhaulVersion!=null){
                str = actualAddon.name+" "+actualAddon.overhaulVersion+" | "+actualAddon.underhaulVersion;
            }else{
                str = actualAddon.name+" "+(actualAddon.overhaulVersion==null?actualAddon.underhaulVersion:actualAddon.overhaulVersion);
            }
            Button label = new Button(str);
            label.getStyle().setBgTransparency(0);
            label.getSelectedStyle().setBgTransparency(0);
            label.getPressedStyle().setBgTransparency(0);
            label.addActionListener((evt) -> {
                Dialog.show("Open URL", "Navigate to CurseForge page for "+actualAddon.name+"?", new Command("No"), new Command("Yes"){
                    @Override
                    public void actionPerformed(ActionEvent evt){
                        Display.getInstance().execute(url);
                    }
                });
            });
            add(LEFT, label);
            Button add = new Button("Add");
            add.getStyle().setBgColor(Core.theme.getSecondaryComponentColor().getRGB());
            add.getSelectedStyle().setBgColor(Core.theme.getSecondaryComponentColor().getRGB());
            add.getPressedStyle().setBgColor(Core.theme.getSecondaryComponentPressedColor().getRGB());
            add(RIGHT, add);
            add.addActionListener((evt) -> {
                add.setEnabled(false);
                try{
                    Core.configuration.addAndConvertAddon(addon.get());
                }catch(MissingConfigurationEntryException ex){
                    throw new RuntimeException(ex);
                }
                refresh();
            });
        }
    }
}