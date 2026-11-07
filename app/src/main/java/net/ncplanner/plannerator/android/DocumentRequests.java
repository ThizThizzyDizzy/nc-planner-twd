package net.ncplanner.plannerator.android;

import java.io.File;
import java.util.function.Consumer;

/** Process lifetime picker callback; accessed only on the Android main thread. */
final class DocumentRequests {
    record Request(Consumer<File> accepted,boolean save,String[] extensions) {}
    private Request pending;

    void begin(Consumer<File> accepted,boolean save,String[] extensions){
        if(pending!=null)throw new IllegalStateException("A document picker is already open");
        pending=new Request(accepted,save,extensions.clone());
    }

    boolean isPending(){return pending!=null;}

    Request take(){
        Request request=pending;
        pending=null;
        return request;
    }
}
