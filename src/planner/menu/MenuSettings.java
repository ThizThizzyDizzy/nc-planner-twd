package planner.menu;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import java.io.IOException;
import java.io.OutputStream;
import multiblock.Multiblock;
import multiblock.configuration.Configuration;
import planner.Core;
import planner.exception.MissingConfigurationEntryException;
import planner.file.FileReader;
import planner.file.NCPFFile;
import planner.module.Module;
import simplelibrary.config2.Config;
public class MenuSettings extends Form{
    private final Label currentConfigLabel;
    private final CheckBox casings;
    public MenuSettings(){
        super(new BorderLayout());
        Container sidebar = new Container(BoxLayout.y());
        sidebar.getStyle().setBgTransparency(255);
        sidebar.getStyle().setBgColor(Core.theme.getSettingsSidebarColor().getRGB());
        add(LEFT, sidebar);
        Container mainPane = new Container(BoxLayout.y());
        add(CENTER, mainPane);
        Button done = new Button("Done");
        sidebar.add(done);
        casings = new CheckBox("Auto-build Casings");
        done.addActionListener((evt) -> {
            onClosed();
            new MenuMain().showBack();
        });
        sidebar.add(new Label("Settings"));
        casings.setSelected(Core.autoBuildCasing);
        sidebar.add(casings);
        int active = 0;
        for(Module m : Core.modules)if(m.isActive())active++;
        Button modules = new Button("Modules ("+active+"/"+Core.modules.size()+" Active)");
        modules.addActionListener((evt) -> {
            new MenuModules().show();
        });
        sidebar.add(modules);
        Button theme = new Button("Change Theme");
        theme.addActionListener((evt) -> {
            new MenuThemes().show();
        });
        sidebar.add(theme);
        mainPane.add(new Label("Internal Configurations"));
        for(Configuration config : Configuration.configurations){
            Button b = new Button("Load "+config.toString());
            b.addActionListener((e) -> {
                config.impose(Core.configuration);
                for(Multiblock multi : Core.multiblocks){
                    try{
                        multi.convertTo(Core.configuration);
                    }catch(MissingConfigurationEntryException ex){
                        throw new RuntimeException(ex);
                    }
                }
                refresh();
            });
            mainPane.add(b);
        }
        mainPane.add(currentConfigLabel = new Label("Current Configuration: "+Core.configuration.toString()));
        Container configButtons = new Container(new GridLayout(1, 3));
        if(FileChooser.isAvailable()){
            Button load = new Button("Load");
            configButtons.add(load);
            load.addActionListener((evt) -> {
                FileChooser.showOpenDialog(".ncpf", (e) -> {
                    if(e!=null&&e.getSource()!=null){
                        String file = (String)e.getSource();
                        NCPFFile ncpf = FileReader.read(file);
                        if(ncpf==null)return;
                        Configuration.impose(ncpf.configuration, Core.configuration);
                        for(Multiblock multi : Core.multiblocks){
                            try{
                                multi.convertTo(Core.configuration);
                            }catch(MissingConfigurationEntryException ex){
                                throw new RuntimeException(ex);
                            }
                        }
                        refresh();
                    }
                });
            });
        }
        Button save = new Button("Save");
        save.getStyle().setMargin(0, 0, 5, 5);
        save.getPressedStyle().setMargin(0, 0, 5, 5);
        save.getSelectedStyle().setMargin(0, 0, 5, 5);
        configButtons.add(save);
        save.addActionListener((evt) -> {
            String name = Core.configuration.getFullName();
            if(name==null||name.isEmpty())name = "configuration";
            FileSystemStorage fs = FileSystemStorage.getInstance();
            String file = fs.getAppHomePath()+"/"+name+".ncpf";
            int i = 1;
            while(fs.exists(file)){
                file = fs.getAppHomePath()+"/"+name+"_"+i+".ncpf";
                i++;
            }
            try(OutputStream stream = fs.openOutputStream(file)){
                Config header = Config.newConfig();
                header.set("version", NCPFFile.SAVE_VERSION);
                header.set("count", 0);
                header.save(stream);
                Core.configuration.save(null, Config.newConfig()).save(stream);
            }catch(IOException ex){
                Log.e(ex);
            }
            Core.showOKDialog("Save complete", "Saved as "+file);
        });
        Button modify = new Button("Modify");
        configButtons.add(modify);
        modify.addActionListener((evt) -> {
            onClosed();
            new MenuConfiguration(Core.configuration).show();
        });
        mainPane.add(configButtons);
        refresh();
    }
    public void refresh(){
        currentConfigLabel.setText("Current Configuration: "+Core.configuration.toString());
    }
    private void onClosed(){
        Core.autoBuildCasing = casings.isSelected();
    }
}