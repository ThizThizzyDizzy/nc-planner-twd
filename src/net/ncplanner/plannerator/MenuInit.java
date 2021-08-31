package net.ncplanner.plannerator;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Supplier;
import net.ncplanner.plannerator.planner.Task;
import net.ncplanner.plannerator.planner.menu.MenuMain;
import net.ncplanner.plannerator.planner.menu.component.ProgressBar;
import net.ncplanner.plannerator.planner.module.FusionTestModule;
import net.ncplanner.plannerator.planner.module.Module;
import net.ncplanner.plannerator.planner.module.OverhaulModule;
import net.ncplanner.plannerator.planner.module.PrimeFuelModule;
import net.ncplanner.plannerator.planner.module.RainbowFactorModule;
import net.ncplanner.plannerator.planner.module.UnderhaulModule;
import net.ncplanner.plannerator.planner.theme.Theme;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
import net.ncplanner.plannerator.simplelibrary.config2.ConfigList;
public class MenuInit extends Form{
    private final Task init;
    public MenuInit(Supplier<Form> formToShow){
        super(new BorderLayout());
        init = new Task("Initializing...");
        Task t1 = init.addSubtask("Creating form...");
        Task ts = init.addSubtask("Loading settings...");
        Task t2 = init.addSubtask("Resetting Metadata");
        Task tc = init.addSubtask("Initializing Configurations...");
        Task tc1 = tc.addSubtask("Initializing Nuclearcraft Configuration");
        Task tcc = tc.addSubtask("Initializing Core configuration");
        Task tm = init.addSubtask("Adding modules...");
        Task tm1 = tm.addSubtask("Adding Underhaul Module");
        Task tm2 = tm.addSubtask("Adding Overhaul Module");
        Task tm3 = tm.addSubtask("Adding Fusion Test Module");
        Task tm4 = tm.addSubtask("Adding Rainbow Factor Module");
        Task tm5 = tm.addSubtask("Adding Prime Fuel Module");
        Task tmr = init.addSubtask("Refreshing modules...");
        Task tct = init.addSubtask("Adjusting MSR block textures...");
        Task tci = init.addSubtask("Imposing Configuration...");
        Container loading = new Container(BoxLayout.y());
        add(CENTER, loading);
        Label lbl;
        loading.add(lbl = new Label("Loading..."));
        int pad = lbl.getPreferredH();
        loading.getStyle().setPadding(pad, pad, pad, pad);
        loading.add(new ProgressBar(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE).getHeight()/20){
            @Override
            public Task getTask(){
                return init;
            }
        });
        new Thread(() -> {
            t1.finish();
            repaint();
            FileSystemStorage fs = FileSystemStorage.getInstance();
            String f = fs.getAppHomePath()+"/settings.dat";
            if(fs.exists(f)){
                try(InputStream stream = fs.openInputStream(f)){
                    Config settings = Config.newConfig(stream);
                    settings.load();
                    System.out.println("Loading theme");
                    Object o = settings.get("theme");
                    if(o instanceof String){
                        Core.setTheme(Theme.getByName((String)o));
                    }else Core.setTheme(Theme.getByLegacyID((int)o));
                    try{
                        Config modules = settings.get("modules", Config.newConfig());
                        HashMap<Module, Boolean> moduleStates = new HashMap<>();
                        for(String key : modules.properties()){
                            for(Module m : Core.modules){
                                if(m.name.equals(key))moduleStates.put(m, modules.getBoolean(key));
                            }
                        }
                        for(Module m : Core.modules){
                            if(!moduleStates.containsKey(m))continue;
                            if(m.isActive()){
                                if(!moduleStates.get(m))m.deactivate();
                            }else{
                                if(moduleStates.get(m))m.activate();
                            }
                        }
                    }catch(Exception ex){}
                    Core.tutorialShown = settings.get("tutorialShown", false);
                    Core.autoBuildCasing = settings.get("autoBuildCasing", true);
                    ConfigList lst = settings.getConfigList("pins", new ConfigList());
                    for(int i = 0; i<lst.size(); i++){
                        Core.pinnedStrs.add(lst.getString(i));
                    }
                }catch(IOException ex){
                    Log.e(ex);
                }
            }
            ts.finish();
            repaint();
            Core.resetMetadata();
            t2.finish();
            repaint();
            Configuration.initNuclearcraftConfiguration();
            tc1.finish();
            repaint();
            Core.configuration = new Configuration(null, null, null);
            tcc.finish();
            repaint();
            Core.modules.add(new UnderhaulModule());
            tm1.finish();
            repaint();
            Core.modules.add(new OverhaulModule());
            tm2.finish();
            repaint();
            Core.modules.add(new FusionTestModule());
            tm3.finish();
            repaint();
            Core.modules.add(new RainbowFactorModule());
            tm4.finish();
            repaint();
            Core.modules.add(new PrimeFuelModule());
            tm5.finish();
            repaint();
            Core.refreshModules();
            tmr.finish();
            repaint();
            for(Configuration configuration : Configuration.configurations){
                if(configuration.overhaul!=null&&configuration.overhaul.fissionMSR!=null){
                    for(net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionmsr.Block b : configuration.overhaul.fissionMSR.allBlocks){
                        if(b.heater&&!b.getDisplayName().contains("Standard")){
                            try{
                                b.setInternalTexture(TextureManager.fromCN1(TextureManager.getImage("overhaul/"+Core.superRemove(b.getDisplayName().toLowerCase(), " coolant heater", "liquid "))));
                            }catch(Exception ex){
                                Core.showOKDialog("Unable to load texture", "Failed to load internal texture for MSR block: "+b.name);
                                Log.e(ex);
                            }
                        }
                    }
                }
            }
            tct.finish();
            repaint();
            Configuration.configurations.get(0).impose(Core.configuration);
            tci.finish();
            repaint();
//        System.out.println("Beginning theme dump");
//        for(ThemeCategory category : Theme.themes){
//            for(Theme theme : category){
//                theme.printXML();
//            }
//        }
            formToShow.get().show();
        }).start();
    }
}