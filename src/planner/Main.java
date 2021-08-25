package planner;
import com.codename1.io.Log;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import planner.menu.MenuMain;
public class Main {
    private Form current;
    public Resources theme;
    public void init(Object context){
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

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
        if(current != null){
            current.show();
            return;
        }
        new MenuMain().show();
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
