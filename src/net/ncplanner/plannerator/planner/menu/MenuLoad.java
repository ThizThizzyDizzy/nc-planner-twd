package net.ncplanner.plannerator.planner.menu;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import java.io.IOException;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.exception.MissingConfigurationEntryException;
import net.ncplanner.plannerator.planner.file.FileReader;
import net.ncplanner.plannerator.planner.file.NCPFFile;
public class MenuLoad extends Form{
    public MenuLoad(){
        super(new BorderLayout());
        Container footer = new Container(new GridLayout(FileChooser.isAvailable()?2:1));
        Button cancel = new Button("Cancel");
        cancel.addActionListener((evt) -> {
            new MenuMain().showBack();
        });
        footer.add(cancel);
        if(FileChooser.isAvailable()){
            Button useFileChooser = new Button("System file chooser");
            footer.add(useFileChooser);
            useFileChooser.addActionListener((ee) -> {
                FileChooser.showOpenDialog( ".ncpf,.json", (e) -> {
                    if(e!=null&&e.getSource()!=null){
                        String file = (String)e.getSource();
                        open(file);
                        new MenuMain().showBack();
                    }
                });
            });
        }
        add(BOTTOM, footer);
        Container fileList = new Container(BoxLayout.y());
        fileList.setScrollableY(true);
        add(CENTER, fileList);
        FileSystemStorage fs = FileSystemStorage.getInstance();
        String home = fs.getAppHomePath();
        try{
            for(String filename : fs.listFiles(home)){
                if(filename.endsWith(".ncpf")||filename.endsWith(".json")){
                    String file = home+filename;
                    Container fileContainer = new Container(new BorderLayout());
                    fileList.add(fileContainer);
                    Label name = new Label(filename);
                    fileContainer.add(CENTER, name);
                    Container buttonHolder = new Container(new GridLayout(2));
                    fileContainer.add(RIGHT, buttonHolder);
                    Button open = new Button("Load");
                    open.addActionListener((evt) -> {
                        open(file);
                        new MenuMain().showBack();
                    });
                    buttonHolder.add(open);
                    Button delete = new Button("Del");
                    delete.addActionListener((evt) -> {
                        Dialog.show("Delete file", "Delete "+filename+"?", new Command("Cancel"), new Command("Delete"){
                            @Override
                            public void actionPerformed(ActionEvent evt){
                                fs.delete(file);
                                fileContainer.remove();
                                MenuLoad.this.revalidate();
                            }
                        });
                    });
                    buttonHolder.add(delete);
                }
            }
        }catch(IOException ex){
            Log.e(ex);
        }
    }
    private void open(String file){
        NCPFFile ncpf = FileReader.read(file);
        if(ncpf==null)return;
        Core.multiblocks.clear();
        Core.metadata.clear();
        Core.metadata.putAll(ncpf.metadata);
        if(ncpf.configuration==null||ncpf.configuration.isPartial()){
            if(ncpf.configuration!=null&&!ncpf.configuration.name.equals(Core.configuration.name)){
                Core.showOKDialog("Configuration mismatch", "File configuration '"+ncpf.configuration.name+"' does not match currently loaded configuration '"+Core.configuration.name+"'!");
            }
        }else{
            Core.configuration = ncpf.configuration;
        }
        for(Multiblock mb : ncpf.multiblocks){
            try{
                mb.convertTo(Core.configuration);
            }catch(MissingConfigurationEntryException ex){
                Core.showOKDialog("Failed to import multiblock", ex.getMessage()+"\nAre you missing an addon?");
                continue;
            }
            Core.multiblocks.add(mb);
        }
    }
}