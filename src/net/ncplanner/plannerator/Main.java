package net.ncplanner.plannerator;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.io.OutputStream;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.menu.MenuInit;
import net.ncplanner.plannerator.planner.module.Module;
import net.ncplanner.plannerator.simplelibrary.config2.Config;
import net.ncplanner.plannerator.simplelibrary.config2.ConfigList;
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
    public void start(){
        Display.getInstance().lockOrientation(false);//lock to landscape
        new MenuInit(current).show();
    }
    public void stop(){
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
        save();
    }
    public void destroy(){
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
//        Core.autosave();//save multiblocks and configurations too for next session
    }
}
