package net.ncplanner.plannerator.planner.file.writer;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.util.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.Block;
import net.ncplanner.plannerator.multiblock.BoundingBox;
import net.ncplanner.plannerator.multiblock.Multiblock;
import net.ncplanner.plannerator.multiblock.PartCount;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.multiblock.overhaul.fusion.OverhaulFusionReactor;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.FormattedText;
import net.ncplanner.plannerator.planner.file.FileFormat;
import net.ncplanner.plannerator.planner.file.FormatWriter;
import net.ncplanner.plannerator.planner.file.NCPFFile;
public class PNGFormatWriter extends FormatWriter{
    private final double borderSizeMod = 16/20d;//based on text height
    @Override
    public FileFormat getFileFormat(){
        return FileFormat.PNG;
    }
    @Override
    public void write(NCPFFile ncpf, OutputStream stream){
        try{
            Image image = writeImage(ncpf);
            ImageIO.getImageIO().save(image, stream, ImageIO.FORMAT_PNG, 1);
            stream.close();
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
    @Override
    public boolean isMultiblockSupported(Multiblock multi){
        return true;
    }
    private Image writeImage(NCPFFile ncpf){
        if(!ncpf.multiblocks.isEmpty()){
            if(ncpf.multiblocks.size()>1)throw new IllegalArgumentException("Multible multiblocks are not supported by PNG!");
            final Multiblock multi = ncpf.multiblocks.get(0);
            multi.recalculate();
            int blSiz = 32;
            ArrayList<Block> blox = multi.getBlocks();
            for(Block b : blox){
                if(b.getTexture()==null)continue;
                blSiz = Math.max(b.getTexture().getWidth(), blSiz);
            }
            Font font = Font.getDefaultFont();
            int textHeight = font.getHeight();
            final int blockSize = blSiz;
            ArrayList<PartCount> parts = multi.getPartsList();
            FormattedText s = multi.getSaveTooltip();
            ArrayList<FormattedText> strs = s.split("\n");
            int totalTextHeight = Math.max(textHeight*strs.size(),textHeight*parts.size());
            double textWidth = 0;
            for(int i = 0; i<strs.size(); i++){
                FormattedText str = strs.get(i);
                textWidth = Math.max(textWidth, font.stringWidth(str.text));
            }
            double partsWidth = 0;
            for(PartCount c : parts){
                partsWidth = Math.max(partsWidth, textHeight+font.stringWidth(c.count+"x "+c.name));
            }
            int borderSize = (int)(this.borderSizeMod*textHeight);
            final double tW = textWidth+borderSize;
            final double pW = partsWidth+borderSize;
            BoundingBox bbox = multi.getBoundingBox();
            int width = (int) Math.max(tW+pW,bbox.getWidth()*blockSize+borderSize);
            int multisPerRow = Math.max(1, (int)(width/(bbox.getWidth()*blockSize+borderSize)));
            int rowCount = (bbox.getHeight()+multisPerRow-1)/multisPerRow;
            int height = totalTextHeight+rowCount*(bbox.getDepth()*blockSize+borderSize)+borderSize/2;
            while(rowCount>1&&height>width){
                width++;
                multisPerRow = Math.max(1, (int)(width/(bbox.getWidth()*blockSize+borderSize)));
                rowCount = (bbox.getHeight()+multisPerRow-1)/multisPerRow;
                height = totalTextHeight+rowCount*(bbox.getDepth()*blockSize+borderSize);
            }
            int mpr = multisPerRow;
            Image image = Image.createImage(width, height);
            Graphics g = image.getGraphics();
            g.setFont(font);
            Renderer renderer = new Renderer(g);
            g.setColor(Core.theme.getImageExportBackgroundColor().getRGB());
            renderer.fillRect(0, 0, width, height);
            g.setColor(Core.theme.getImageExportTextColor().getRGB());
            for(int i = 0; i<strs.size(); i++){
                FormattedText str = strs.get(i);
                renderer.drawFormattedText(borderSize/2, i*textHeight+borderSize/2, str);
            }
            g.setColor(Core.theme.getImageExportTextColor().getRGB());
            for(int i = 0; i<parts.size(); i++){
                PartCount c = parts.get(i);
                g.drawString(c.count+"x "+c.name, (int)(tW+textHeight+borderSize/2), i*textHeight+borderSize/2);
            }
            g.setColor(Core.theme.getWhiteColor().getRGB());
            for(int i = 0; i<parts.size(); i++){
                PartCount c = parts.get(i);
                Image tex = TextureManager.toCN1(c.getImage());
                if(tex!=null)renderer.drawImage(tex, (int)tW, i*textHeight+borderSize/2, (int)(tW+textHeight), (i+1)*textHeight+borderSize/2);
            }
            for(int y = 0; y<bbox.getHeight(); y++){
                int column = y%mpr;
                int row = y/mpr;
                int layerWidth = bbox.getWidth()*blockSize+borderSize;
                int layerHeight = bbox.getDepth()*blockSize+borderSize;
                for(int x = 0; x<bbox.getWidth(); x++){
                    for(int z = 0; z<bbox.getDepth(); z++){
                        Block b = multi.getBlock(x+bbox.x1, y+bbox.y1, z+bbox.z1);
                        if(b!=null)b.render(g, column*layerWidth+borderSize/2+x*blockSize, row*layerHeight+borderSize+z*blockSize+totalTextHeight, blockSize, blockSize, true, multi);
                        if(multi instanceof OverhaulFusionReactor&&((OverhaulFusionReactor)multi).getLocationCategory(x, y, z)==OverhaulFusionReactor.LocationCategory.PLASMA){
                            renderer.drawImage(TextureManager.getImage("/textures/overhaul/fusion/plasma.png"), column*layerWidth+borderSize/2+x*blockSize, row*layerHeight+borderSize+z*blockSize+totalTextHeight, column*layerWidth+borderSize/2+x*blockSize+blockSize, row*layerHeight+borderSize+z*blockSize+totalTextHeight+blockSize);
                        }
                    }
                }
            }
            return image;
        }else{
            throw new IllegalArgumentException("Cannot export configuration to image!");
        }
    }
}