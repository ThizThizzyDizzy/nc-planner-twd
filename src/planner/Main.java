package planner;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import multiblock.configuration.Configuration;
import multiblock.configuration.TextureManager;
import planner.menu.MenuMain;
import planner.module.Module;
import planner.theme.Theme;
import planner.theme.ThemeCategory;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public class Main {
    private Form current;
    public Resources theme;
    public void init(Object context){
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initNamedTheme("/theme", "Light");

        // Enable Toolbar on all Forms by default
//        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        System.out.println("Loading fonts");
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
        Core.refreshModules();
        for(Configuration configuration : Configuration.configurations){
            if(configuration.overhaul!=null&&configuration.overhaul.fissionMSR!=null){
                for(multiblock.configuration.overhaul.fissionmsr.Block b : configuration.overhaul.fissionMSR.allBlocks){
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
        Configuration.configurations.get(0).impose(Core.configuration);
        if(current != null){
            current.show();
            return;
        }
        System.out.println("Beginning theme dump");
        for(ThemeCategory category : Theme.themes){
            for(Theme theme : category){
                theme.printXML();
            }
        }
        new MenuMain().show();
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
        save();
    }
    
    public void destroy() {
        save();
    }
    public void save(){
        FileSystemStorage fs = FileSystemStorage.getInstance();
        String f = fs.getAppHomePath()+"/settings.dat";
        Config settings = Config.newConfig();
        settings.set("theme", Core.theme.name);
        Config modules = Config.newConfig();
        for(Module m : Core.modules){
            modules.set(m.name, m.isActive());
        }
        settings.set("modules", modules);
        settings.set("tutorialShown", Core.tutorialShown);
        settings.set("autoBuildCasing", Core.autoBuildCasing);
        ConfigList pins = new ConfigList();
        for(String s : Core.pinnedStrs)pins.add(s);
        settings.set("pins", pins);
        try(OutputStream stream = fs.openOutputStream(f)){
            settings.save(stream);
        }catch(IOException ex){
            Log.e(ex);
        }
    }
}
