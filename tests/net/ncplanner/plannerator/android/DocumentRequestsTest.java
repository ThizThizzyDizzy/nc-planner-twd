package net.ncplanner.plannerator.android;

import java.io.File;

/** Picker state is shared by replacement hosts, independently of graphics initialization. */
public class DocumentRequestsTest {
    private record Host(DocumentRequests requests) {
        void result(File file){
            DocumentRequests.Request request=requests.take();
            if(request!=null)request.accepted().accept(file);
        }
    }
    public static void main(String[] args){
        for(boolean save:new boolean[]{false,true}){
            DocumentRequests requests=new DocumentRequests();
            File[] received={null};
            String[] extensions={"ncpf.json"};
            new Host(requests).requests().begin(file->received[0]=file,save,extensions);
            extensions[0]="changed";
            if(!requests.isPending())throw new AssertionError("Request lost before replacement");
            DocumentRequests.Request request=requests.take();
            if(request.save()!=save || !request.extensions()[0].equals("ncpf.json"))throw new AssertionError("Request metadata lost");
            requests.begin(request.accepted(),request.save(),request.extensions());
            File selected=new File("selected");
            new Host(requests).result(selected);
            if(received[0]!=selected || requests.isPending())throw new AssertionError("Replacement did not consume request");
            received[0]=null;
            new Host(requests).result(selected);
            if(received[0]!=null)throw new AssertionError("Duplicate callback");
            requests.begin(request.accepted(),save,request.extensions());
            requests.take(); // Picker cancellation clears retained callbacks.
            if(requests.isPending())throw new AssertionError("Cancelled callback retained");
            if(new DocumentRequests().take()!=null)throw new AssertionError("Process restart retained callback");
        }
        System.out.println("Document request replacement/cancellation passed (open and save).");
    }
}
