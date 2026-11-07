package net.ncplanner.plannerator.android;

import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Shader;

/** Process lifetime graphics/session state; accessed on the active surface's GL thread. */
final class HostedSession {
    private boolean initialized;
    private Shader shader;

    boolean isInitialized(){return initialized;}

    void surfaceCreated(String vertex,String fragment,Runnable restoreFonts,Runnable initializePlanner){
        if(!initialized){
            shader=new Shader(vertex,fragment);
            DizzyEngine.initHosted(1,1,shader);
            restoreFonts.run();
            initializePlanner.run();
            initialized=true;
        }else{
            shader.restoreContext(vertex,fragment);
            DizzyEngine.restoreHostedGraphics(shader);
            restoreFonts.run();
        }
    }
}
