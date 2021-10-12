package net.ncplanner.plannerator.planner.file;
import java.io.OutputStream;
import java.util.ArrayList;
import net.ncplanner.plannerator.planner.file.writer.HellrageFormatWriter;
import net.ncplanner.plannerator.planner.file.writer.NCPFFormatWriter;
import net.ncplanner.plannerator.planner.file.writer.PNGFormatWriter;
public class FileWriter{
    public static final ArrayList<FormatWriter> formats = new ArrayList<>();
    public static boolean botRunning;
    public static FormatWriter NCPF,HELLRAGE;
    public static PNGFormatWriter IMAGE;
    static{
        formats.add(HELLRAGE = new HellrageFormatWriter());
        formats.add(NCPF = new NCPFFormatWriter());
        formats.add(IMAGE = new PNGFormatWriter());
    }
    public static void write(NCPFFile ncpf, OutputStream stream, FormatWriter format){
        format.write(ncpf, stream);
    }
}