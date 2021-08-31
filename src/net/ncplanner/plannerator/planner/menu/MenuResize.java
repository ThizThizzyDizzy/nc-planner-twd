package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.CuboidalMultiblock;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.menu.component.SquareButton;
import net.ncplanner.plannerator.simplelibrary.image.Color;
public class MenuResize extends Form{
    private final Container mainPanel;
    private final CuboidalMultiblock multiblock;
    private final Label dimensionsLabel;
    public MenuResize(CuboidalMultiblock multiblock){
        super(new BorderLayout());
        this.multiblock = multiblock;
        Container rightSidebar = new Container(BoxLayout.y());
        Button back = new Button("Back");
        back.addActionListener((evt) -> {
            new MenuEdit(multiblock).showBack();
        });
        rightSidebar.add(back);
        rightSidebar.add(dimensionsLabel = new Label(multiblock.getDimensionsStr()));
        Container mainParentArea = new Container(new BorderLayout());//to make it not stretch
        add(CENTER, mainParentArea);
        mainPanel = new Container(BoxLayout.y());
        mainPanel.setScrollableY(true);
        mainPanel.setScrollableX(true);
        mainParentArea.add(CENTER, mainPanel);
        refresh();
        add(RIGHT, rightSidebar);
    }
    private void refresh(){
        dimensionsLabel.setText(multiblock.getDimensionsStr());
        mainPanel.removeAll();
        for(int y = 0; y<multiblock.getInternalHeight(); y++){
            final int layer = y;
            Container insertLayerHolder = new Container(BoxLayout.x());
            mainPanel.add(insertLayerHolder);
            Button insertLayer = new Button("+"){
                @Override
                protected Dimension calcPreferredSize(){
                    Dimension d = super.calcPreferredSize();
                    d.setWidth(d.getHeight()*(multiblock.getInternalWidth()+4));
                    return d;
                }
            };
            insertLayer.setEnabled(multiblock.getInternalHeight()<multiblock.getMaxY());
            insertLayerHolder.add(insertLayer);
            margin(insertLayer, insertLayer.getPreferredH()/2, insertLayer.getPreferredH()/2, 0, 0);
            fg(insertLayer, Core.theme.getAddButtonTextColor());
            Container sectionHeader = new Container(BoxLayout.x());
            Button del = new Button("-"){
                @Override
                protected Dimension calcPreferredSize(){
                    Dimension d = super.calcPreferredSize();
                    d.setWidth(d.getHeight()*2);//double width button
                    return d;
                }
            };
            del.setEnabled(multiblock.getInternalHeight()>multiblock.getMinY());
            sectionHeader.add(del);
            mainPanel.add(sectionHeader);
            fg(del, Core.theme.getDeleteButtonTextColor());
            Container sectionHeader2 = new Container(BoxLayout.y());
            sectionHeader.add(sectionHeader2);
            Button top = new Button("+");
            top.setEnabled(multiblock.getInternalDepth()<multiblock.getMaxZ());;
            sectionHeader2.add(top);
            fg(top, Core.theme.getAddButtonTextColor());
            Container sectionHeaderButtons = new Container(BoxLayout.x());
            sectionHeader2.add(sectionHeaderButtons);
            for(int x = 0; x<multiblock.getInternalWidth(); x++){
                final int column = x;
                Button delColumn = new SquareButton("-");
                delColumn.setEnabled(multiblock.getInternalWidth()>multiblock.getMinX());
                sectionHeaderButtons.add(delColumn);
                fg(delColumn, Core.theme.getDeleteButtonTextColor());
                delColumn.addActionListener((evt) -> {
                    deleteX(column);
                });
            }
            Container sectionMainArea = new Container(BoxLayout.x());
            mainPanel.add(sectionMainArea);
            Button left = new SquareButton("+");
            left.setEnabled(multiblock.getInternalWidth()<multiblock.getMaxX());
            sectionMainArea.add(left);
            fg(left, Core.theme.getAddButtonTextColor());
            Container firstColumnContainer = new Container(BoxLayout.y());
            sectionMainArea.add(firstColumnContainer);
            for(int z = 0; z<multiblock.getInternalDepth(); z++){
                final int row = z;
                Button delRow = new SquareButton("-");
                delRow.setEnabled(multiblock.getInternalDepth()>multiblock.getMinZ());
                firstColumnContainer.add(delRow);
                fg(delRow, Core.theme.getDeleteButtonTextColor());
                delRow.addActionListener((e) -> {
                    deleteZ(row);
                });
            }
            for(int x = 0; x<multiblock.getInternalWidth(); x++){
                final int column = x;
                Container columnContainer = new Container(BoxLayout.y());
                sectionMainArea.add(columnContainer);
                for(int z = 0; z<multiblock.getInternalDepth(); z++){
                    final int row = z;
                    Container blok = new Container(){
                        @Override
                        protected Dimension calcPreferredSize(){
                            Dimension d = super.calcPreferredSize();
                            d.setHeight(del.getPreferredH());//same as button height
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
                            Block block = multiblock.getBlock(column+1, layer+1, row+1);
                            if(block!=null)block.render(g, getX(), getY(), getWidth(), getHeight(), false, multiblock);
                        }
                    };
                    columnContainer.add(blok);
                }
            }
            Button right = new SquareButton("+");
            right.setEnabled(multiblock.getInternalWidth()<multiblock.getMaxX());
            sectionMainArea.add(right);
            fg(right, Core.theme.getAddButtonTextColor());
            Container bottomHolder = new Container(BoxLayout.x());
            mainPanel.add(bottomHolder);
            Button bottom = new Button("+"){
                @Override
                protected Dimension calcPreferredSize(){
                    Dimension d = super.calcPreferredSize();
                    d.setWidth(d.getHeight()*multiblock.getInternalWidth());
                    return d;
                }
            };
            bottom.setEnabled(multiblock.getInternalDepth()<multiblock.getMaxZ());;
            bottomHolder.add(bottom);
            fg(bottom, Core.theme.getAddButtonTextColor());
            margin(bottom, 0, 0, del.getPreferredW(), 0);
            insertLayer.addActionListener((e) -> {
                if(layer==0)expand(0, -1, 0);
                else insertY(layer);
            });
            del.addActionListener((e) -> {
                deleteY(layer);
            });
            top.addActionListener((e) -> {
                expand(0,0,-1);
            });
            bottom.addActionListener((e) -> {
                expand(0,0,1);
            });
            left.addActionListener((e) -> {
                expand(-1,0,0);
            });
            right.addActionListener((e) -> {
                expand(1,0,0);
            });
        }
        Container layerTopHolder = new Container(BoxLayout.x());
        Button layerTop = new Button("+"){
            @Override
            protected Dimension calcPreferredSize(){
                Dimension d = super.calcPreferredSize();
                d.setWidth(d.getHeight()*(multiblock.getInternalWidth()+4));
                return d;
            }
        };
        layerTopHolder.add(layerTop);
        layerTop.setEnabled(multiblock.getInternalHeight()<multiblock.getMaxY());
        mainPanel.add(layerTopHolder);
        margin(layerTop, layerTop.getPreferredH()/2, layerTop.getPreferredH()/2, 0, 0);
        fg(layerTop, Core.theme.getAddButtonTextColor());
        layerTop.addActionListener((e) -> {
            expand(0,1,0);
        });
        revalidate();
    }
    private void margin(Button button, int top, int bottom, int left, int right){
        button.getStyle().setMargin(top, bottom, left, right);
        button.getSelectedStyle().setMargin(top, bottom, left, right);
        button.getPressedStyle().setMargin(top, bottom, left, right);
        button.getDisabledStyle().setMargin(top, bottom, left, right);
    }
    private void fg(Button button, Color color){
        button.getStyle().setFgColor(color.getRGB());
        button.getSelectedStyle().setFgColor(color.getRGB());
        button.getPressedStyle().setFgColor(color.getRGB());
        button.getDisabledStyle().setFgColor(color.getRGB());
    }
    public void expand(int x, int y, int z){
        if(x>0)multiblock.expandRight(x);
        if(x<0)multiblock.expandLeft(-x);
        if(y>0)multiblock.expandUp(y);
        if(y<0)multiblock.exandDown(-y);
        if(z>0)multiblock.expandToward(z);
        if(z<0)multiblock.expandAway(-z);
        refresh();
    }
    private void deleteX(int x){
        multiblock.deleteX(x);
        refresh();
    }
    private void deleteY(int y){
        multiblock.deleteY(y);
        refresh();
    }
    private void deleteZ(int z){
        multiblock.deleteZ(z);
        refresh();
    }
    private void insertX(int x){
        multiblock.insertX(x);
        refresh();
    }
    private void insertY(int y){
        multiblock.insertY(y);
        refresh();
    }
    private void insertZ(int z){
        multiblock.insertZ(z);
        refresh();
    }
}