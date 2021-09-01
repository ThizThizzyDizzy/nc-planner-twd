package net.ncplanner.plannerator.planner.menu;
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
import java.util.ArrayList;
import java.util.HashMap;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Supplier;
import net.ncplanner.plannerator.planner.Task;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.planner.file.FileReader;
import net.ncplanner.plannerator.planner.file.FormatReader;
import net.ncplanner.plannerator.planner.file.NCPFFile;
import net.ncplanner.plannerator.planner.file.reader.NCPF10Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF11Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF1Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF2Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF3Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF4Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF5Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF6Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF7Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF8Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF9Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR1Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR2Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR3Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR4Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR5Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR6Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR1Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR2Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR3Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR4Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR5Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR6Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulNCConfigReader;
import net.ncplanner.plannerator.planner.file.reader.UnderhaulHellrage1Reader;
import net.ncplanner.plannerator.planner.file.reader.UnderhaulHellrage2Reader;
import net.ncplanner.plannerator.planner.file.reader.UnderhaulNCConfigReader;
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
    HashMap<String, Supplier<FormatReader>> readers = new HashMap<>();
    ArrayList<String> readerNames = new ArrayList<>();
    HashMap<String, Task> readerTasks = new HashMap<>();
    {
        addReader("NCPF11Reader", ()->{return new NCPF11Reader();});// .ncpf version 11
        addReader("NCPF10Reader", ()->{return new NCPF10Reader();});// .ncpf version 10
        addReader("NCPF9Reader", ()->{return new NCPF9Reader();});// .ncpf version 9
        addReader("NCPF8Reader", ()->{return new NCPF8Reader();});// .ncpf version 8
        addReader("NCPF7Reader", ()->{return new NCPF7Reader();});// .ncpf version 7
        addReader("NCPF6Reader", ()->{return new NCPF6Reader();});// .ncpf version 6
        addReader("NCPF5Reader", ()->{return new NCPF5Reader();});// .ncpf version 5
        addReader("NCPF4Reader", ()->{return new NCPF4Reader();});// .ncpf version 4
        addReader("NCPF3Reader", ()->{return new NCPF3Reader();});// .ncpf version 3
        addReader("NCPF2Reader", ()->{return new NCPF2Reader();});// .ncpf version 2
        addReader("NCPF1Reader", ()->{return new NCPF1Reader();});// .ncpf version 1
        addReader("OverhaulHellrageSFR6Reader", ()->{return new OverhaulHellrageSFR6Reader();});// hellrage SFR .json 2.1.1-2.1.7 (present)
        addReader("OverhaulHellrageSFR5Reader", ()->{return new OverhaulHellrageSFR5Reader();});// hellrage SFR .json 2.0.32-2.0.37
        addReader("OverhaulHellrageSFR4Reader", ()->{return new OverhaulHellrageSFR4Reader();});// hellrage SFR .json 2.0.31
        addReader("OverhaulHellrageSFR3Reader", ()->{return new OverhaulHellrageSFR3Reader();});// hellrage SFR .json 2.0.30
        addReader("OverhaulHellrageSFR2Reader", ()->{return new OverhaulHellrageSFR2Reader();});// hellrage SFR .json 2.0.7-2.0.29
        addReader("OverhaulHellrageSFR1Reader", ()->{return new OverhaulHellrageSFR1Reader();});// hellrage SFR .json 2.0.1-2.0.6
        addReader("UnderhaulHellrage2Reader", ()->{return new UnderhaulHellrage2Reader();});// hellrage .json 1.2.23-1.2.25 (present)
        addReader("UnderhaulHellrage1Reader", ()->{return new UnderhaulHellrage1Reader();});// hellrage .json 1.2.5-1.2.22
        addReader("OverhaulHellrageMSR6Reader", ()->{return new OverhaulHellrageMSR6Reader();});// hellrage MSR .json 2.1.1-2.1.7 (present)
        addReader("OverhaulHellrageMSR5Reader", ()->{return new OverhaulHellrageMSR5Reader();});// hellrage MSR .json 2.0.32-2.0.37
        addReader("OverhaulHellrageMSR4Reader", ()->{return new OverhaulHellrageMSR4Reader();});// hellrage MSR .json 2.0.31
        addReader("OverhaulHellrageMSR3Reader", ()->{return new OverhaulHellrageMSR3Reader();});// hellrage MSR .json 2.0.30
        addReader("OverhaulHellrageMSR2Reader", ()->{return new OverhaulHellrageMSR2Reader();});// hellrage MSR .json 2.0.7-2.0.29
        addReader("OverhaulHellrageMSR1Reader", ()->{return new OverhaulHellrageMSR1Reader();});// hellrage MSR .json 2.0.1-2.0.6
        addReader("OverhaulNCConfigReader", ()->{return new OverhaulNCConfigReader();});// OVERHAUL nuclearcraft.cfg
        addReader("UnderhaulNCConfigReader", ()->{return new UnderhaulNCConfigReader();});// UNDERHAUL nuclearcraft.cfg
    }
    private  void addReader(String s, Supplier<FormatReader> reader){
        readerNames.add(s);
        readers.put(s, reader);
    }
    public MenuInit(Form current){
        super(new BorderLayout());
        init = new Task("Initializing...");
        Task t1 = init.addSubtask("Creating form...");
        Task t2 = init.addSubtask("Resetting Metadata");
        Task tf = init.addSubtask("Adding File Readers...");
        for(String s : readerNames){
            readerTasks.put(s, tf.addSubtask("Adding "+s+"..."));
        }
        Task tc = init.addSubtask("Initializing Configurations...");
        Task tc1 = tc.addSubtask("Initializing Nuclearcraft Configuration");
        Task tcc = tc.addSubtask("Initializing Core configuration");
        Task tm = init.addSubtask("Adding modules...");
        Task tm1 = tm.addSubtask("Adding Underhaul Module");
        Task tm2 = tm.addSubtask("Adding Overhaul Module");
        Task tm3 = tm.addSubtask("Adding Fusion Test Module");
        Task tm4 = tm.addSubtask("Adding Rainbow Factor Module");
        Task tm5 = tm.addSubtask("Adding Prime Fuel Module");
        Task ts = init.addSubtask("Loading settings...");
        Task tmr = init.addSubtask("Refreshing modules...");
        Task tct = init.addSubtask("Adjusting MSR block textures...");
        Task tci = init.addSubtask("Imposing Configuration...");
        Task tl = init.addSubtask("Loading previous session...");
        Task tlc = init.addSubtask("Loading configuration...");
        Task tlm = init.addSubtask("Loading multiblocks...");
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
            Core.resetMetadata();
            t2.finish();
            repaint();
            for(String s : readerNames){
                FileReader.formats.add(readers.get(s).get());
                readerTasks.get(s).finish();
                repaint();
            }
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
            String cfgFile = fs.getAppHomePath()+"/config_autosave.ncpf";
            if(fs.exists(cfgFile)){
                NCPFFile ncpf = FileReader.read(cfgFile);
                if(ncpf!=null){
                    Configuration.impose(ncpf.configuration, Core.configuration);
                    //gonna skip the multiblock conversion part. there shouldn't ever be any multiblocks at this point anyway.
                }
            }
            tlc.finish();
            repaint();
            String file = fs.getAppHomePath()+"/autosave.ncpf";
            if(fs.exists(file)){
                NCPFFile ncpf = FileReader.read(file);
                if(ncpf!=null){
                    boolean abort = false;
                    if(ncpf.configuration==null||ncpf.configuration.isPartial()){
                        if(ncpf.configuration!=null&&!ncpf.configuration.name.equals(Core.configuration.name)){
                            //nope, configuration somehow doesn't match. ABORT!
                            abort = true;
                        }
                    }else{
                        Core.configuration = ncpf.configuration;//it's a full configuration for some reason, we can load that
                    }
                    if(!abort){
                        Core.multiblocks.clear();//just in case
                        Core.metadata.clear();//just in case
                        Core.metadata.putAll(ncpf.metadata);
                        Core.configuration = ncpf.configuration;
                        for(Multiblock mb : ncpf.multiblocks){
                            try{
                                mb.convertTo(Core.configuration);
                                Core.multiblocks.add(mb);
                            }catch(MissingConfigurationEntryException ex){
                                Log.e(ex);
                            }
                        }
                    }
                }
            }
            tlm.finish();
            repaint();
//        System.out.println("Beginning theme dump");
//        for(ThemeCategory category : Theme.themes){
//            for(Theme theme : category){
//                theme.printXML();
//            }
//        }
            if(current==null)new MenuMain().show();
            else current.show();
        }).start();
    }
}