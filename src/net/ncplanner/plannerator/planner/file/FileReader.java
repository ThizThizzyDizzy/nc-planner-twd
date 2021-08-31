package net.ncplanner.plannerator.planner.file;
import com.codename1.io.FileSystemStorage;
import java.io.IOException;
import java.util.ArrayList;
public class FileReader{
    public static final ArrayList<FormatReader> formats = new ArrayList<>();
    public static NCPFFile read(InputStreamProvider provider){
        for(FormatReader reader : formats){
            boolean matches = false;
            try{
                if(reader.formatMatches(provider.getInputStream()))matches = true;
            }catch(Throwable t){}
            if(matches)return reader.read(provider.getInputStream());
        }
        throw new IllegalArgumentException("Unknown file format!");
    }
    public static NCPFFile read(String file){
        return read(() -> {
            try{
                return FileSystemStorage.getInstance().openInputStream(file);
            }catch(IOException ex){
                return null;
            }
        });
    }
}