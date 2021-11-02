package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.BoundingBox;
import net.ncplanner.plannerator.multiblock.overhaul.fusion.OverhaulFusionReactor;
import net.ncplanner.plannerator.planner.Core;
public class MenuResizeFusion extends Form{
    private final Container mainPanel;
    private final OverhaulFusionReactor multiblock;
    private final Label dimensionsLabel;
    private final Label dimensionsLabel2;
    public MenuResizeFusion(OverhaulFusionReactor multiblock){
        super(new BorderLayout());
        this.multiblock = multiblock;
        Container rightSidebar = new Container(BoxLayout.y());
        Button back = new Button("Back");
        back.addActionListener((evt) -> {
            new MenuEdit(multiblock).showBack();
        });
        rightSidebar.add(back);
        rightSidebar.add(dimensionsLabel = new Label(multiblock.getDimensionsStr()));
        BoundingBox bbox = multiblock.getBoundingBox();
        rightSidebar.add(dimensionsLabel2 = new Label(bbox.getWidth()+"x"+bbox.getHeight()+"x"+bbox.getDepth()));
        add(RIGHT, rightSidebar);
        rightSidebar.add(new Label("Inner Radius"));
        {
            Container c = new Container(new GridLayout(1, 2));
            Button increase = new Button("+");
            c.add(increase);
            Button decrease = new Button("-");
            c.add(decrease);
            rightSidebar.add(c);
            increase.addActionListener((evt) -> {
                multiblock.increaseInnerRadius();
                refresh();
            });
            decrease.addActionListener((evt) -> {
                multiblock.decreaseInnerRadius();
                refresh();
            });
        }
        rightSidebar.add(new Label("Core Size"));
        {
            Container c = new Container(new GridLayout(1, 2));
            Button increase = new Button("+");
            c.add(increase);
            Button decrease = new Button("-");
            c.add(decrease);
            rightSidebar.add(c);
            increase.addActionListener((evt) -> {
                multiblock.increaseCoreSize();
                refresh();
            });
            decrease.addActionListener((evt) -> {
                multiblock.decreaseCoreSize();
                refresh();
            });
        }
        rightSidebar.add(new Label("Toroid Width"));
        {
            Container c = new Container(new GridLayout(1, 2));
            Button increase = new Button("+");
            c.add(increase);
            Button decrease = new Button("-");
            c.add(decrease);
            rightSidebar.add(c);
            increase.addActionListener((evt) -> {
                multiblock.increaseToroidWidth();
                refresh();
            });
            decrease.addActionListener((evt) -> {
                multiblock.decreaseToroidWidth();
                refresh();
            });
        }
        rightSidebar.add(new Label("Lining Thickness"));
        {
            Container c = new Container(new GridLayout(1, 2));
            Button increase = new Button("+");
            c.add(increase);
            Button decrease = new Button("-");
            c.add(decrease);
            rightSidebar.add(c);
            increase.addActionListener((evt) -> {
                multiblock.increaseLiningThickness();
                refresh();
            });
            decrease.addActionListener((evt) -> {
                multiblock.decreaseLiningThickness();
                refresh();
            });
        }
        Container mainParentArea = new Container(new BorderLayout());//to make it not stretch
        add(CENTER, mainParentArea);
        mainPanel = new Container(BoxLayout.y());
        mainPanel.setScrollableY(true);
        mainPanel.setScrollableX(true);
        mainParentArea.add(CENTER, mainPanel);
        refresh();
    }
    private void refresh(){
        dimensionsLabel.setText(multiblock.getDimensionsStr());
        BoundingBox bbox = multiblock.getBoundingBox();
        dimensionsLabel2.setText(bbox.getWidth()+"x"+bbox.getHeight()+"x"+bbox.getDepth());
        mainPanel.removeAll();
        for(int y = 0; y<bbox.getHeight(); y++){
            final int layer = y;
            Container section = new Container(BoxLayout.x());
            int m = dimensionsLabel.getPreferredH()/2;
            margin(section, m, m, m, m);
            mainPanel.add(section);
            for(int x = 0; x<bbox.getWidth(); x++){
                final int column = x;
                Container columnContainer = new Container(BoxLayout.y());
                section.add(columnContainer);
                for(int z = 0; z<bbox.getDepth(); z++){
                    final int row = z;
                    Container blok = new Container(){
                        @Override
                        protected Dimension calcPreferredSize(){
                            Dimension d = super.calcPreferredSize();
                            d.setHeight(dimensionsLabel.getPreferredH());//same as label height
                            d.setWidth(d.getHeight());//square
                            return d;
                        }
                        @Override
                        public void paint(Graphics g){
                            super.paint(g);
                            g.setColor(Core.theme.getEditorBackgroundColor().getRGB());
                            g.fillRect(getX(), getY(), getWidth(), getHeight());
                            int border = getHeight()/8;
                            g.setColor(Core.theme.getEditorGridColor().getRGB());
                            g.fillRect(getX(), getY(), getWidth(), border/4);
                            g.fillRect(getX(), getY()+getHeight()-border/4, getWidth(), border/4);
                            g.fillRect(getX(), getY()+border/4, border/4, getHeight()-border/2);
                            g.fillRect(getX()+getWidth()-border/4, getY()+border/4, border/4, getHeight()-border/2);
                            Block block = multiblock.getBlock(column+bbox.x1, layer+bbox.y1, row+bbox.z1);
                            if(block!=null)block.render(new Renderer(g), getX(), getY(), getWidth(), getHeight(), false, multiblock);
                        }
                    };
                    columnContainer.add(blok);
                }
            }
        }
        revalidate();
    }
    private void margin(Container c, int top, int bottom, int left, int right){
        c.getStyle().setMargin(top, bottom, left, right);
    }
}