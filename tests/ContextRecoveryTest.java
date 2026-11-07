import com.thizthizzydizzy.dizzyengine.*;
import com.thizthizzydizzy.dizzyengine.graphics.Shader;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Image;
import com.thizthizzydizzy.dizzyengine.graphics.text.Font;
import com.thizthizzydizzy.dizzyengine.platform.*;
import java.lang.reflect.Proxy;
import java.util.*;

/** Run against the shared engine with a fake GL backend; no display or native libraries required. */
public class ContextRecoveryTest {
    static void check(boolean value,String message){if(!value)throw new AssertionError(message);}
    public static void main(String[] args) throws Exception {
        int[] next={1},deletes={0},arrays={0},inits={0},uniforms={0};
        GL.setBackend((GLBackend)Proxy.newProxyInstance(GLBackend.class.getClassLoader(),new Class<?>[]{GLBackend.class},(proxy,method,values)->{
            String name=method.getName();
            if(name.equals("glUniform4f"))uniforms[0]++;
            if(name.equals("glDeleteTextures"))deletes[0]++;
            if(name.equals("glGenVertexArrays"))arrays[0]++;
            if(name.equals("glGetShaderiv")||name.equals("glGetProgramiv"))((int[])values[2])[0]=1;
            if(method.getReturnType()==int.class)return next[0]++;
            if(method.getReturnType()==boolean.class)return false;
            return null;
        }));
        DizzyEngine.addLayer(new DizzyLayer(){
            public void init(){inits[0]++;}
            public void render(double delta){}
            public void cleanup(){}
        });
        DizzyEngine.addUnmanagedUpdateThread("recovery-test",delta->{},()->{},1);
        Shader shader=new Shader("", "");
        DizzyEngine.initHosted(100,100,shader);
        Image image=new Image(1,1);
        int oldTexture=ResourceManager.getTexture(image);
        int oldMissing=ResourceManager.getMissingTexture();
        var pathCache=ResourceManager.class.getDeclaredField("texturesCache");
        pathCache.setAccessible(true);
        @SuppressWarnings("unchecked") Map<String,Integer> paths=(Map<String,Integer>)pathCache.get(null);
        paths.put("test-path",oldTexture);
        var hooksField=DizzyEngine.class.getDeclaredField("shutdownHooks");
        hooksField.setAccessible(true);
        int hooks=((List<?>)hooksField.get(null)).size();
        HashMap<Character,Font.Glyph> glyphs=new HashMap<>();
        for(char c=' ';c<='~';c++)glyphs.put(c,new Font.Glyph(1,0,0,1,1,0,0,1,1));
        Font font=new Font(oldTexture,1,0,1,glyphs);
        Object oldGlyph=font.getCharacter('A');
        for(int i=0;i<3;i++){
            Renderer.reset();
            int previousArrays=arrays[0];
            shader.restoreContext("", "");
            DizzyEngine.restoreHostedGraphics(shader);
            int oldUniforms=uniforms[0];
            Renderer.reset();
            check(uniforms[0]>=oldUniforms+2,"Replacement shader did not receive color/texture uniforms");
            check(paths.isEmpty(),"Path textures survived context loss");
            check(arrays[0]>previousArrays,"Renderer buffers were not rebuilt");
            int texture=ResourceManager.getTexture(image);
            check(texture!=oldTexture,"Image texture was not uploaded again");
            check(ResourceManager.getTexture(image)==texture,"Image texture no longer cached");
            check(ResourceManager.getMissingTexture()!=oldMissing,"Missing texture was not rebuilt");
            oldTexture=texture;oldMissing=ResourceManager.getMissingTexture();
            font.texture=texture; font.initCharacters();
            check(font.getCharacter('A')!=oldGlyph,"Glyph buffers were not rebuilt");
            oldGlyph=font.getCharacter('A');
            check(inits[0]==1,"Recovery restarted planner layers");
            check(((List<?>)hooksField.get(null)).size()==hooks,"Recovery duplicated cleanup hooks");
        }
        check(deletes[0]==0,"Recovery deleted names belonging to the replacement context");
        DizzyEngine.shutdownHosted();
        System.out.println("Context recovery passed (3 context replacements).");
    }
}
