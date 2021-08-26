package simplelibrary.image;
public class Image{
    private final int width;
    private final int height;
    private final int[][] data;
    public Image(int width, int height){
        this.width = width;
        this.height = height;
        data = new int[width][height];
    }
    public void setRGB(int x, int y, int width, int height, int[] rgbArray, int offset, int scansize){
        int yoff  = offset;
        int off;
        for(int Y = y; Y<y+height; Y++, yoff+=scansize){
            off = yoff;
            for(int X = x; X<x+width; X++){
                data[X][Y] = rgbArray[off++];
            }
        }
    }
    public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize){
        int yoff = offset;
        int off;
        if(rgbArray==null){
            rgbArray = new int[offset+h*scansize];
        }
        for(int y = startY; y<startY+h; y++, yoff+=scansize){
            off = yoff;
            for(int x = startX; x<startX+w; x++){
                rgbArray[off++] = getRGB(x, y);
            }
        }
        return rgbArray;
    }
    public void setRGB(int x, int y, int rgb){
        data[x][y] = rgb;
    }
    public void setColor(int x, int y, Color color){
        setRGB(x, y, color.getRGB());
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getRGB(int x, int y){
        return data[x][y];
    }
    public Color getColor(int x, int y){
        return new Color(getRGB(x, y));
    }
    public int getRed(int x, int y){
        return Color.getRed(getRGB(x, y));
    }
    public int getGreen(int x, int y){
        return Color.getGreen(getRGB(x, y));
    }
    public int getBlue(int x, int y){
        return Color.getBlue(getRGB(x, y));
    }
    public int getAlpha(int x, int y){
        return Color.getAlpha(getRGB(x, y));
    }
    public Image getSubimage(int x, int y, int width, int height){
        Image sub = new Image(width, height);
        for(int X = 0; X<width; X++){
            for(int Y = 0; Y<height; Y++){
                sub.setRGB(X, Y, getRGB(x+X, y+Y));
            }
        }
        return sub;
    }
}