package net.ncplanner.plannerator.planner.file.writer;
import com.codename1.ui.Image;
import com.codename1.ui.util.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import net.ncplanner.plannerator.planner.file.FormatWriter;
import net.ncplanner.plannerator.planner.file.NCPFFile;
public abstract class ImageFormatWriter extends FormatWriter{
    @Override
    public void write(NCPFFile ncpf, OutputStream stream){
        try{
            Image image = writeImage(ncpf);
            ImageIO.getImageIO().save(image, stream, getIIOFormat(), 1);
            stream.close();
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
    public abstract Image writeImage(NCPFFile ncpf);
    public abstract String getIIOFormat();
}