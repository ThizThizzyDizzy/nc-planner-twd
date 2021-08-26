package planner.file;
import java.io.OutputStream;
import java.util.ArrayList;
import planner.file.writer.HellrageFormatWriter;
import planner.file.writer.NCPFFormatWriter;
public class FileWriter{
    public static final ArrayList<FormatWriter> formats = new ArrayList<>();
    public static boolean botRunning;
    public static FormatWriter NCPF,HELLRAGE;
    static{
        formats.add(HELLRAGE = new HellrageFormatWriter());
        formats.add(NCPF = new NCPFFormatWriter());
    }
    public static void write(NCPFFile ncpf, OutputStream stream, FormatWriter format){
        format.write(ncpf, stream);
    }
}