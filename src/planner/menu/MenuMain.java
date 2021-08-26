package planner.menu;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import multiblock.Multiblock;
import planner.Core;
import planner.exception.MissingConfigurationEntryException;
import planner.file.FileReader;
import planner.file.NCPFFile;
public class MenuMain extends Form{
    private final Button editMetadata;
    public MenuMain(){
        super(BoxLayout.y());
        Container header = new Container(new BorderLayout());
        add(header);
        Container leftButtons = new Container(BoxLayout.x());
        header.add(LEFT, leftButtons);
        boolean hasFileChooser = FileChooser.isAvailable();
        if(hasFileChooser){
            Button imprt = new Button("Import");
            leftButtons.add(imprt);
            imprt.addActionListener((evt) -> {
                FileChooser.showOpenDialog(true, ".ncpf,.json", (e) -> {
                    if(e!=null&&e.getSource()!=null){
                        String[] files = (String[])e.getSource();
                        for(String file : files){
                            NCPFFile ncpf = FileReader.read(file);
                            if(ncpf==null)return;
                            if(ncpf.configuration!=null&&!ncpf.configuration.name.equals(Core.configuration.name)){
                                Core.showWarningDialog("Configuration mismatch", "File configuration '"+ncpf.configuration.name+"' does not match currently loaded configuration '"+Core.configuration.name+"'!");
                            }
                            convertAndImportMultiblocks(ncpf.multiblocks);
                            refresh();
                        }
                    }
                });
            });
        }
        Button export = new Button("Export");
        leftButtons.add(export);
        export.addActionListener((evt) -> {
            
        });
        Button save = new Button("Save");
        leftButtons.add(save);
        if(hasFileChooser){
            Button load = new Button("Load");
            leftButtons.add(load);
            load.addActionListener((evt) -> {
                FileChooser.showOpenDialog(".ncpf,.json", (e) -> {
                    if(e!=null&&e.getSource()!=null){
                        String file = (String)e.getSource();
                        NCPFFile ncpf = FileReader.read(file);
                        if(ncpf==null)return;
                        Core.multiblocks.clear();
                        Core.metadata.clear();
                        Core.metadata.putAll(ncpf.metadata);
                        if(ncpf.configuration==null||ncpf.configuration.isPartial()){
                            if(ncpf.configuration!=null&&!ncpf.configuration.name.equals(Core.configuration.name)){
                                Core.showWarningDialog("Configuration mismatch", "File configuration '"+ncpf.configuration.name+"' does not match currently loaded configuration '"+Core.configuration.name+"'!");
                            }
                        }else{
                            Core.configuration = ncpf.configuration;
                        }
                        convertAndImportMultiblocks(ncpf.multiblocks);
                        refresh();
                    }
                });
            });
        }
        editMetadata = new Button("Edit Metadata");
        header.add(CENTER, editMetadata);
        Button settings = new Button();
        int h = editMetadata.getPreferredH()-1;
        settings.setIcon(genGear(h, settings.getStyle().getBgColor(), settings.getStyle().getFgColor()));
        settings.setPressedIcon(genGear(h, settings.getPressedStyle().getBgColor(), settings.getPressedStyle().getFgColor()));
        settings.getStyle().setPadding(0, 0, 0, 0);
        settings.getSelectedStyle().setPadding(0, 0, 0, 0);
        settings.getPressedStyle().setPadding(0, 0, 0, 0);
        settings.setText("");
        header.add(RIGHT, settings);
        refresh();
    }
    private Image genGear(int size, int bgColor, int fgColor){
        Image image = Image.createImage(size, size);
        {
            Graphics g = image.getGraphics();
            g.setAntiAliased(true);
            g.setColor(bgColor);
            g.fillRect(0, 0, size, size);
            g.setColor(fgColor);
            double holeRad = size*.1;
            int teeth = 8;
            double averageRadius = size*.3;
            double toothSize = size*.1;
            double rot = 360/16d;
            int resolution = (int)(2*Math.PI*averageRadius*2/teeth);//an extra *2 to account for wavy surface?
            double angle = rot;
            double radius = averageRadius+toothSize/2;
            int actualRes = teeth*resolution;
            int[] x = new int[actualRes];
            int[] y = new int[actualRes];
            int[] ix = new int[actualRes];
            int[] iy = new int[actualRes];
            for(int i = 0; i<actualRes; i++){
                x[i] = (int)(size/2+Math.cos(Math.toRadians(angle-90))*radius);
                y[i] = (int)(size/2+Math.sin(Math.toRadians(angle-90))*radius);
                ix[i] = (int)(size/2+Math.cos(Math.toRadians(angle-90))*holeRad);
                iy[i] = (int)(size/2+Math.sin(Math.toRadians(angle-90))*holeRad);
                angle+=(360d/(actualRes));
                if(angle>=360)angle-=360;
                radius = averageRadius+(toothSize/2)*Math.cos(Math.toRadians(teeth*(angle-rot)));
            }
            g.fillPolygon(x, y, actualRes);
            g.setColor(bgColor);
            g.fillPolygon(ix, iy, actualRes);
        }
        return image;
    }
    private void refresh(){
//        multiblocks.components.clear();
//        for(Multiblock multi : Core.multiblocks){
//            multiblocks.add(new MenuComponentMultiblock(this, multi));
//        }
        String name = Core.metadata.containsKey("Name")?Core.metadata.get("Name"):"";
        editMetadata.setText(name.isEmpty()?"Edit Metadata":(name+" | Edit Metadata"));
    }
    private void convertAndImportMultiblocks(ArrayList<Multiblock> multiblocks){
        for(Multiblock mb : multiblocks){
            try{
                mb.convertTo(Core.configuration);
            }catch(MissingConfigurationEntryException ex){
                Core.showWarningDialog("Failed to load multiblock", ex.getMessage()+"\nAre you missing an addon?");
                continue;
            }
            Core.multiblocks.add(mb);
        }
    }
}