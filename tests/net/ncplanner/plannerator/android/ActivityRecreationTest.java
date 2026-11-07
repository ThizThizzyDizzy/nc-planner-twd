package net.ncplanner.plannerator.android;

import com.thizthizzydizzy.dizzyengine.*;
import com.thizthizzydizzy.dizzyengine.platform.*;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import net.ncplanner.plannerator.planner.Core;

/** Simulates replacement hosts sharing the process session, without Android framework stubs. */
public class ActivityRecreationTest {
    private static final HostedSession session=new HostedSession();
    private static int plannerInitializations,fontRestores;
    private static class Host {
        void createSurface(){
            Core.bindHostedUIThread();
            session.surfaceCreated("","",()->fontRestores++,()->{
                plannerInitializations++;
                DizzyEngine.addLayer(new TestLayer());
            });
        }
    }
    private static class TestLayer extends DizzyLayer {
        final Object project=new Object();
        public void init(){}
        public void render(double delta){}
        public void cleanup(){}
    }
    private static void replaceHost() throws Exception {
        AtomicReference<Throwable> failure=new AtomicReference<>();
        Thread thread=new Thread(()->{
            try{
                new Host().createSurface();
                if(!Core.isUIThread())throw new AssertionError("Core retained the old UI thread");
                boolean[] completed={false};
                Thread producer=new Thread(()->Core.runOnUIThread(()->{
                    if(!Core.isUIThread())throw new AssertionError("UI action dispatched to the wrong thread");
                    completed[0]=true;
                }));
                producer.start();producer.join();
                Core.processUITasks();
                if(!completed[0])throw new AssertionError("Queued UI action did not complete");
            }catch(Throwable ex){failure.set(ex);}
        });
        thread.start();thread.join();
        if(failure.get()!=null)throw new AssertionError("Replacement host failed",failure.get());
    }
    public static void main(String[] args) throws Exception {
        int[] next={1};
        GL.setBackend((GLBackend)Proxy.newProxyInstance(GLBackend.class.getClassLoader(),new Class<?>[]{GLBackend.class},(proxy,method,values)->{
            if(method.getName().equals("glGetShaderiv")||method.getName().equals("glGetProgramiv"))((int[])values[2])[0]=1;
            if(method.getReturnType()==int.class)return next[0]++;
            if(method.getReturnType()==boolean.class)return false;
            return null;
        }));
        replaceHost();
        TestLayer layer=DizzyEngine.getLayer(TestLayer.class);
        Object project=layer.project;
        for(int i=0;i<3;i++){
            replaceHost();
            if(DizzyEngine.getLayer(TestLayer.class)!=layer || layer.project!=project)throw new AssertionError("Replacement host lost the project/layer");
        }
        if(plannerInitializations!=1 || fontRestores!=4 || !session.isInitialized())throw new AssertionError("Incorrect session initialization/recovery counts");

        System.out.println("Activity session recovery passed (3 replacement hosts).");
    }
}
