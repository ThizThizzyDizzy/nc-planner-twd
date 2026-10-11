package net.ncplanner.plannerator.android;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.thizthizzydizzy.dizzyengine.graphics.image.Image;
import com.thizthizzydizzy.dizzyengine.io.ImageIO;
import java.io.*;
import java.util.*;
/** Framework image codecs, with bounded caches for Plannerator's repeated texture serialization. */
final class AndroidImages implements ImageIO.Backend {
    private static final int CACHE_SIZE=256;
    private final Map<Bytes,Image> decoded=boundedCache();
    private final Map<Pixels,byte[]> encoded=boundedCache();
    private static <K,V> Map<K,V> boundedCache(){
        return new LinkedHashMap<K,V>(CACHE_SIZE,0.75f,true){
            @Override protected boolean removeEldestEntry(Map.Entry<K,V> entry){return size()>CACHE_SIZE;}
        };
    }
    private record Bytes(byte[] data){
        @Override public int hashCode(){return Arrays.hashCode(data);}
        @Override public boolean equals(Object other){return other instanceof Bytes bytes && Arrays.equals(data,bytes.data);}
    }
    private record Pixels(int width,int height,int[] data){
        @Override public int hashCode(){return 31*(31*width+height)+Arrays.hashCode(data);}
        @Override public boolean equals(Object other){return other instanceof Pixels pixels && width==pixels.width && height==pixels.height && Arrays.equals(data,pixels.data);}
    }
    @Override public synchronized Image read(InputStream input) throws IOException {
        if(input==null)return null;
        byte[] bytes;try(input){bytes=input.readAllBytes();}
        Bytes key=new Bytes(bytes);Image cached=decoded.get(key);
        if(cached!=null)return cached.copy();
        Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        if(bitmap==null)throw new IOException("Unsupported image");
        Image image;try{image=fromBitmap(bitmap);}finally{bitmap.recycle();}
        decoded.put(key,image);
        // Only PNG input can be reused by the PNG writer.
        if(bytes.length>=8 && bytes[0]==(byte)137 && bytes[1]==80 && bytes[2]==78 && bytes[3]==71 && bytes[4]==13 && bytes[5]==10 && bytes[6]==26 && bytes[7]==10)encoded.put(pixels(image),bytes);
        return image.copy();
    }
    private static Pixels pixels(Image image){return new Pixels(image.getWidth(),image.getHeight(),image.getRGB(0,0,image.getWidth(),image.getHeight(),null,0,image.getWidth()));}
    static Image fromBitmap(Bitmap bitmap){
        int w=bitmap.getWidth(),h=bitmap.getHeight();int[] pixels=new int[w*h];bitmap.getPixels(pixels,0,w,0,0,w,h);
        Image image=new Image(w,h);image.setRGB(0,0,w,h,pixels,0,w);return image;
    }
    @Override public synchronized void write(Image image,OutputStream output) throws IOException {
        Pixels key=pixels(image);byte[] bytes=encoded.get(key);
        if(bytes==null){
            Bitmap bitmap=Bitmap.createBitmap(key.data,key.width,key.height,Bitmap.Config.ARGB_8888);
            ByteArrayOutputStream buffer=new ByteArrayOutputStream();
            try{if(!bitmap.compress(Bitmap.CompressFormat.PNG,100,buffer))throw new IOException("PNG encoding failed");}finally{bitmap.recycle();}
            bytes=buffer.toByteArray();encoded.put(key,bytes);
        }
        output.write(bytes);
    }
}
