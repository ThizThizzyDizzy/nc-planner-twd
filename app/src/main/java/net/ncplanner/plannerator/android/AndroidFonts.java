package net.ncplanner.plannerator.android;
import android.graphics.*;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.text.Font;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
final class AndroidFonts implements Font.Backend {
    // Weak keys retain the CPU atlas only while a theme/menu still uses its font.
    // Static so fonts surviving activity recreation are also repaired.
    private static final Map<Font,Atlas> atlases=new WeakHashMap<>();
    private record Atlas(int size,ByteBuffer pixels) {}
    static void restoreContext(){
        for(var entry:atlases.entrySet()){
            Atlas atlas=entry.getValue();
            Font font=entry.getKey();
            font.texture=ResourceManager.loadGLTexture(atlas.size(),atlas.size(),atlas.pixels().duplicate());
            font.initCharacters();
        }
    }
    private final File cache;
    AndroidFonts(File cache){this.cache=cache;}
    @Override public Font load(ByteBuffer data, float yOff, float requestedHeight, int requestedSize){
        File file=null;
        Bitmap bitmap=null;
        try{
            file=File.createTempFile("plannerator-font", ".ttf", cache);
            byte[] bytes=new byte[data.remaining()];data.duplicate().get(bytes);
            try(OutputStream output=new FileOutputStream(file)){output.write(bytes);}
            Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTypeface(Typeface.createFromFile(file));paint.setTextSize(48);paint.setColor(Color.WHITE);
            int size=1024,cell=64;
            bitmap=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bitmap);
            HashMap<Character,Font.Glyph> glyphs=new HashMap<>();
            for(char c=' ';c<='~';c++){
                int index=c-' ',x=(index%16)*cell,y=(index/16)*cell;
                canvas.drawText(String.valueOf(c),x+4,y+48,paint);
                glyphs.put(c,new Font.Glyph(paint.measureText(String.valueOf(c)), -4,-48,cell-4,cell-48,(float)x/size,(float)y/size,(float)(x+cell)/size,(float)(y+cell)/size));
            }
            ByteBuffer pixels=AndroidImages.fromBitmap(bitmap).getGLData();
            int texture=ResourceManager.loadGLTexture(size,size,pixels.duplicate());
            Font font=new Font(texture,48,yOff,size,glyphs);
            atlases.put(font,new Atlas(size,pixels));
            return font;
        }catch(IOException ex){throw new RuntimeException("Could not load font",ex);}
        finally{if(bitmap!=null)bitmap.recycle();if(file!=null)file.delete();}
    }
}
