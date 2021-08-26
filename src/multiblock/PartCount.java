package multiblock;
import simplelibrary.image.Image;
public class PartCount implements Comparable<PartCount>{
    private final Image image;
    public final String name;
    public final int count;
    public PartCount(Image image, String name, int count){
        this.image = image;
        this.name = name;
        this.count = count;
    }
    public Image getImage(){
        return image;
    }
    @Override
    public int compareTo(PartCount o){
        if(o==null)return 0-count;
        if(o.count==count){
            return name.compareTo(o.name);
        }
        return o.count-count;
    }
}