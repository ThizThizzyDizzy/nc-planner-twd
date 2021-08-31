package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
public class MenuAddMultiblock extends Form{
    public MenuAddMultiblock(){
        super(new BorderLayout());
        int count = Core.multiblockTypes.size();
        int columns = Math.min(count,(int)Math.ceil(getWidth()/Math.sqrt(getHeight()*getWidth()/(double)count)));
        if(columns<=0)columns = 1;
        Container buttonsPanel = new Container(new GridLayout(columns));
        add(CENTER, buttonsPanel);
        for(Multiblock m : Core.multiblockTypes){
            String tex = m.getPreviewTexture();
            Image image = tex==null?null:TextureManager.getImage(tex);
            Container c = new Container(new BorderLayout());
            Button b = new Button(){
                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    if(image!=null){
                        int size = Math.min(getWidth(), getHeight()-getStyle().getPaddingTop()-getStyle().getPaddingBottom());
                        g.drawImage(image, getX()+getWidth()/2-size/2, getY()+getHeight()/2-size/2, size, size);
                    }
                }
            };
            b.setEnabled(m.exists());
            b.addActionListener((evt) -> {
                Multiblock mb = m.newInstance();
                mb.init();
                Core.multiblocks.add(mb);
                new MenuMain().showBack();
            });
            if(image==null)b.setText(m.getDefinitionName());
            c.add(CENTER, b);
            if(image!=null){
                Label title = new Label(m.getDefinitionName());
                c.add(TOP, title);
            }
            buttonsPanel.add(c);
        }
        Button backButton = new Button("Cancel");
        backButton.addActionListener((evt) -> {
            new MenuMain().show();
        });
        add(BOTTOM, backButton);
    }
}