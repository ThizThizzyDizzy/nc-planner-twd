package net.ncplanner.plannerator.planner.menu;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.FocusListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.PartialConfiguration;
import net.ncplanner.plannerator.multiblock.overhaul.turbine.OverhaulTurbine;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.file.FileFormat;
import net.ncplanner.plannerator.planner.file.FileWriter;
import net.ncplanner.plannerator.planner.file.FormatWriter;
import net.ncplanner.plannerator.planner.file.NCPFFile;
import net.ncplanner.plannerator.planner.file.writer.ImageFormatWriter;
import net.ncplanner.plannerator.planner.menu.component.SquareButton;
public class MenuMain extends Form{
    private final Button editMetadata;
    private final Container multiblocksList;
    private Multiblock selectedMultiblock = null;
    public MenuMain(){
        super(new BorderLayout());
        Container leftPane = new Container(new BorderLayout());
        add(LEFT, leftPane);
        Container leftButtons = new Container(new GridLayout(1, 4));
        leftPane.add(TOP, leftButtons);
        //<editor-fold defaultstate="collapsed" desc="Import">
        Container importContainer = new Container(BoxLayout.y());
        Button imprt = new Button("Import");
        leftButtons.add(importContainer);
        importContainer.add(imprt);
        imprt.addActionListener((evt) -> {
            new MenuImport().show();
        });
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Export">
        Container exportContainer = new Container(BoxLayout.y());
        Button export = new Button("Export");
        exportContainer.add(export);
        ArrayList<Button> specificExports = new ArrayList<>();
        for(FormatWriter writer : FileWriter.formats){
            FileFormat format = writer.getFileFormat();
            Button b = new Button(format.name);
            specificExports.add(b);
            b.addActionListener((evt) -> {
                //remove export buttons
                for(Component c : specificExports)exportContainer.removeComponent(c);
                revalidate();
                //actually export
                NCPFFile ncpf = new NCPFFile();
                ncpf.multiblocks.add(selectedMultiblock);
                ncpf.configuration = PartialConfiguration.generate(Core.configuration, ncpf.multiblocks);
                String name = (String)selectedMultiblock.metadata.get("name");
                if(name==null||name.isEmpty())name = "unnamed";
                FileSystemStorage fs = FileSystemStorage.getInstance();
                String file = fs.getAppHomePath()+"/"+name+"."+format.extensions[0];
                int i = 0;
                String nam = null;
                while(fs.exists(file)){
                    nam = name+"_"+i;
                    file = fs.getAppHomePath()+"/"+nam+"."+format.extensions[0];
                    i++;
                }
                name = nam;
                Container exportTextContainer = new Container(BoxLayout.y());
                TextField exportText = new TextField(name, "filename");
                exportTextContainer.add(exportText);
                Dialog.show("Export "+format.name, exportTextContainer, new Command("Cancel"), new Command("Export"){
                    @Override
                    public void actionPerformed(ActionEvent evt){
                        String filename = exportText.getText();
                        if(filename==null||filename.isEmpty()){
                            Core.showOKDialog("Export failed", "Invalid filename: "+filename+"."+format.extensions[0]);
                        }else{
                            String file = fs.getAppHomePath()+"/"+filename+"."+format.extensions[0];
                            if(fs.exists(file)){
                                Dialog.show("Confirm overwrite", "File "+filename+"."+format.extensions[0]+" already exists!\nOverwrite?", new Command("Cancel"), new Command("Export"){
                                    @Override
                                    public void actionPerformed(ActionEvent evt){
                                        export(file, filename+"."+format.extensions[0]);
                                    }
                                });
                            }else export(file, filename+"."+format.extensions[0]);
                        }
                    }
                    private void export(String file, String filename){
                        try(OutputStream stream = fs.openOutputStream(file)){
                            FileWriter.write(ncpf, stream, writer);
                            if(CN.isNativeShareSupported()&&writer instanceof ImageFormatWriter){
                                Display.getInstance().share(null, file, ((ImageFormatWriter)writer).getMimeType());
                                return;
                            }
                        }catch(IOException ex){
                            Log.e(ex);
                        }
                        Core.showOKDialog("Export complete", "Exported as "+filename);
                    }
                });
            });
        }
        export.setFocusable(false);
        export.setEnabled(false);
        export.addActionListener((evt) -> {
            Component focused = getFocused();
            if(focused instanceof MultiblockContainer){
                if(!exportContainer.contains(specificExports.get(0))){
                    for(Button b : specificExports)exportContainer.add(b);
                    revalidate();
                }
            }
        });
//</editor-fold>
        Button deleteButton = new Button("Delete Multiblock");
        deleteButton.getStyle().setFgColor(Core.theme.getDeleteButtonTextColor().getRGB());
        deleteButton.getSelectedStyle().setFgColor(Core.theme.getDeleteButtonTextColor().getRGB());
        deleteButton.getPressedStyle().setFgColor(Core.theme.getDeleteButtonTextColor().getRGB());
        deleteButton.getDisabledStyle().setFgColor(Core.theme.getDeleteButtonTextColor().getRGB());
        deleteButton.addActionListener((evt) -> {
            if(selectedMultiblock!=null){
                Dialog.show("Confirm Delete", "Delete "+(selectedMultiblock.getName().isEmpty()?selectedMultiblock.getDefinitionName():selectedMultiblock.getName())+"?", new Command("Delete"){
                    @Override
                    public void actionPerformed(ActionEvent evt){
                        for(Multiblock m : Core.multiblocks){
                            if(m instanceof OverhaulTurbine){
                                ((OverhaulTurbine)m).inputs.remove(selectedMultiblock);
                            }
                        }
                        Core.multiblocks.remove(selectedMultiblock);
                        refresh();
                    }
                }, new Command("Cancel"));
            }
        });
        leftButtons.add(exportContainer);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(Component cmp){
                if(cmp instanceof MultiblockContainer){
                    export.setEnabled(true);
                    if(!leftPane.contains(deleteButton))leftPane.add(BOTTOM, deleteButton);
                    selectedMultiblock = ((MultiblockContainer)cmp).multi;
                }
                if(!(cmp instanceof Button)||!specificExports.contains(cmp)){
                    for(Component c : specificExports)exportContainer.removeComponent(c);
                    revalidate();
                }
            }
            @Override
            public void focusLost(Component cmp){
                leftPane.removeComponent(deleteButton);
                export.setEnabled(false);
            }
        });
        //<editor-fold defaultstate="collapsed" desc="Save">
        Container saveContainer = new Container(BoxLayout.y());
        Button save = new Button("Save");
        saveContainer.add(save);
        leftButtons.add(saveContainer);
        save.addActionListener((evt) -> {
            NCPFFile ncpf = new NCPFFile();
            ncpf.configuration = PartialConfiguration.generate(Core.configuration, Core.multiblocks);
            ncpf.multiblocks.addAll(Core.multiblocks);
            ncpf.metadata.putAll(Core.metadata);
            FileSystemStorage fs = FileSystemStorage.getInstance();
            String name = Core.filename;
            if(name==null) name = ncpf.metadata.get("name");
            if(name==null||name.isEmpty()){
                name = "unnamed";
                String file = fs.getAppHomePath()+"/"+name+".ncpf";
                int i = 0;
                while(fs.exists(file)){
                    name = "unnamed_"+i;
                    file = fs.getAppHomePath()+"/"+name+".ncpf";
                    i++;
                }
            }
            Container saveTextContainer = new Container(BoxLayout.y());
            TextField saveText = new TextField(name, "filename");
            saveTextContainer.add(saveText);
            Dialog.show("Save NCPF", saveTextContainer, new Command("Cancel"), new Command("Save"){
                @Override
                public void actionPerformed(ActionEvent evt){
                    String filename = saveText.getText();
                    if(filename==null||filename.isEmpty()){
                        Core.showOKDialog("Save failed", "Invalid filename: "+filename+".ncpf");
                    }else{
                        Core.filename = filename;
                        String file = fs.getAppHomePath()+"/"+filename+".ncpf";
                        if(fs.exists(file)){
                            Dialog.show("Confirm overwrite", "File "+filename+".ncpf already exists!\nOverwrite?", new Command("Cancel"), new Command("Save"){
                                @Override
                                public void actionPerformed(ActionEvent evt){
                                    save(file, filename+".ncpf");
                                }
                            });
                        }else save(file, filename+".ncpf");
                    }
                }
                private void save(String file, String filename){
                    try(OutputStream stream = fs.openOutputStream(file)){
                        FileWriter.write(ncpf, stream, FileWriter.NCPF);
                    }catch(IOException ex){
                        Log.e(ex);
                    }
                    Core.showOKDialog("Save complete", "Saved as "+filename);
                }
            });
        });
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Load">
        Container loadContainer = new Container(BoxLayout.y());
        Button load = new Button("Load");
        loadContainer.add(load);
        leftButtons.add(loadContainer);
        load.addActionListener((evt) -> {
            new MenuLoad().show();
        });
//</editor-fold>
        Container centerPane = new Container(new BorderLayout());
        add(CENTER, centerPane);
        Container header = new Container(new BorderLayout());
        centerPane.add(TOP, header);
        Container footer = new Container(new BorderLayout());
        centerPane.add(BOTTOM, footer);
        footer.add(RIGHT, new Button(new Command("Credits"){
            @Override
            public void actionPerformed(ActionEvent evt){
                new MenuCredits().show();
            }
        }));
        editMetadata = new Button("Edit Metadata");
        header.add(CENTER, editMetadata);
        editMetadata.addActionListener((evt) -> {
            new MenuEditMetadata(Core.metadata, Core::resetMetadata, MenuMain::new).show();
        });
        //<editor-fold defaultstate="collapsed" desc="Settings">
        Button settings = new Button();
        settings.addActionListener((evt) -> {
            new MenuSettings().show();
        });
        int h = editMetadata.getPreferredH()-1;
        settings.setIcon(genGear(h, settings.getStyle().getBgColor(), settings.getStyle().getFgColor()));
        settings.setPressedIcon(genGear(h, settings.getPressedStyle().getBgColor(), settings.getPressedStyle().getFgColor()));
        settings.getStyle().setPadding(0, 0, 0, 0);
        settings.getSelectedStyle().setPadding(0, 0, 0, 0);
        settings.getPressedStyle().setPadding(0, 0, 0, 0);
        settings.setText("");
        header.add(RIGHT, settings);
//</editor-fold>
        Container multiblocksPane = new Container(new BorderLayout());
        leftPane.add(CENTER, multiblocksPane);
        Container multiblocksListHeader = new Container(new BorderLayout());
        multiblocksPane.add(TOP, multiblocksListHeader);
        multiblocksList = new Container(BoxLayout.y());
        multiblocksList.setScrollableY(true);
        multiblocksPane.add(CENTER, multiblocksList);
        Label multiblocksTitle = new Label("Multiblocks");
        multiblocksListHeader.add(CENTER, multiblocksTitle);
        Button addMultiblock = new SquareButton("+");
        multiblocksListHeader.add(RIGHT, addMultiblock);
        addMultiblock.addActionListener((evt) -> {
            new MenuAddMultiblock().show();
        });
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
        multiblocksList.removeAll();
        for(Multiblock multi : Core.multiblocks){
            Container multiblock = new MultiblockContainer(multi);
            multiblocksList.add(multiblock);
        }
        String name = Core.metadata.containsKey("Name")?Core.metadata.get("Name"):"";
        editMetadata.setText(name.isEmpty()?"Edit Metadata":(name+" | Edit Metadata"));
        revalidate();
    }
    private static class MultiblockContainer extends Container{
        private final Multiblock multi;
        public MultiblockContainer(Multiblock multi){
            super(new BorderLayout());
            this.multi = multi;
            setFocusable(true);
            getStyle().setBgColor(Core.theme.getComponentColor().getRGB());
            getStyle().setBgTransparency(255);
            getSelectedStyle().setBgColor(Core.theme.getSelectedComponentColor().getRGB());
            getSelectedStyle().setBgTransparency(255);
            Button edit = new SquareButton(" "){
                @Override
                public void paint(Graphics g){
                    super.paint(g);
                    g.setColor(getStyle().getFgColor());
                    int x = getX();
                    int y = getY();
                    int w = getWidth();
                    int h = getHeight();
                    g.fillPolygon(new int[]{x+w/4, x+w*3/8, x+w/4}, new int[]{y+h*3/4, y+h*3/4, y+h*5/8}, 3);
                    g.fillPolygon(new int[]{x+w*2/5, x+w*11/40, x+w/2, x+w*5/8}, new int[]{y+h*29/40, y+h*3/5, y+h*3/8, y+h/2}, 4);
                    g.fillPolygon(new int[]{x+w*21/40, x+w*13/20, x+w*3/4, x+w*5/8}, new int[]{y+h*7/20, y+h*19/40, y+h*3/8, y+h/4}, 4);
                }
            };
            edit.setEnabled(multi.exists());
            edit.addActionListener((evt) -> {
                new MenuEdit(multi).show();
            });
            edit.getStyle().setBgColor(Core.theme.getSecondaryComponentColor().getRGB());
            edit.getSelectedStyle().setBgColor(Core.theme.getSecondaryComponentColor().getRGB());
            edit.getPressedStyle().setBgColor(Core.theme.getSecondaryComponentPressedColor().getRGB());
            edit.getDisabledStyle().setBgColor(Core.theme.getSecondaryComponentDisabledColor().getRGB());
            Container labelHolder = new Container(new GridLayout(2, 1));
            Label label;
            if(!multi.getName().isEmpty()){
                labelHolder.add(label = new Label(multi.getName()));
                label.getStyle().setBgTransparency(0);
            }
            labelHolder.add(label = new Label(multi.getDefinitionName()));
            label.getStyle().setBgTransparency(0);
            add(CENTER, labelHolder);
            int mg = edit.getPreferredH()/2;
            edit.getStyle().setMargin(mg, mg, mg, mg);
            edit.getSelectedStyle().setMargin(mg, mg, mg, mg);
            edit.getPressedStyle().setMargin(mg, mg, mg, mg);
            add(RIGHT, edit);
        }
    }
}