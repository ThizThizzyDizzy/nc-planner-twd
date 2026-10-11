package net.ncplanner.plannerator.android;
import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.*;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.*;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Shader;
import com.thizthizzydizzy.dizzyengine.graphics.text.Font;
import com.thizthizzydizzy.dizzyengine.io.ImageIO;
import com.thizthizzydizzy.dizzyengine.platform.GL;
import com.thizthizzydizzy.dizzyengine.platform.Input;
import net.ncplanner.plannerator.graphics.PlanneratorRenderer;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.module.Module;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
/** Minimal Android host. All visible planner menus and editor content are rendered by DizzyEngine. */
public final class PlanneratorActivity extends Activity implements Core.Host {
    private GLSurfaceView surface;
    private boolean initialized;
    private int mouseButton;
    private Consumer<File> fileAccepted;
    private boolean saving;
    private static final int DOCUMENT_REQUEST=1;
    private static final String VERTEX="""
        #version 300 es
        precision highp float;
        layout(location=0) in vec3 aPos;
        layout(location=1) in vec3 aNormal;
        layout(location=2) in vec2 aTexCoord;
        out vec3 ourNormal;
        out vec2 texCoord;
        uniform mat4 model;
        uniform mat4 view;
        uniform mat4 projection;
        void main(){gl_Position=projection*view*model*vec4(aPos,1.0);ourNormal=aNormal;texCoord=aTexCoord;}
        """;
    private static final String FRAGMENT="""
        #version 300 es
        precision highp float;
        in vec3 ourNormal;
        in vec2 texCoord;
        out vec4 FragColor;
        uniform sampler2D tex;
        uniform vec4 color;
        uniform vec4 noTex;
        void main(){FragColor=(noTex.x>0.5?vec4(1.0):texture(tex,texCoord))*color;}
        """;
    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        // JOML's optional desktop Unsafe path is unavailable on Android.
        System.setProperty("joml.nounsafe","true");
        GL.setBackend(new AndroidGL());
        ImageIO.setBackend(new AndroidImages());
        Font.setBackend(new AndroidFonts(getCacheDir()));
        Input.setBackend(new Input.Backend(){
            public int getKey(long window,int key){return 0;}
            public int getMouseButton(long window,int button){return button==0?mouseButton:0;}
            public void setClipboardString(long window,CharSequence text){runOnUiThread(() -> ((android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Plannerator",text)));}
        });
        System.setProperty("ncpf.registrationResource", "ncpf-registration.txt");
        Core.host=this;
        Core.setStorageRoot(getFilesDir());
        Core.tutorialShown=true;
        Module.registrationClasses=this::registeredClasses;
        surface=new GLSurfaceView(this);
        surface.setEGLContextClientVersion(3);
        surface.setEGLConfigChooser(8,8,8,8,24,8);
        surface.setPreserveEGLContextOnPause(true);
        surface.setRenderer(new GLSurfaceView.Renderer(){
            long lastFrame;
            public void onSurfaceCreated(GL10 unused,EGLConfig config){
                try{
                    GLES30.glEnable(GLES30.GL_BLEND);
                    GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA,GLES30.GL_ONE_MINUS_SRC_ALPHA);
                    GLES30.glEnable(GLES30.GL_STENCIL_TEST);
                    PlanneratorRenderer.addCustomElements();
                    DizzyEngine.initHosted(1,1,new Shader(VERTEX,FRAGMENT));
                    if(!initialized){Core.initHosted();initialized=true;}
                }catch(Throwable ex){android.util.Log.e("Plannerator","Initialization failed",ex);throw new RuntimeException(ex);}
            }
            public void onSurfaceChanged(GL10 unused,int width,int height){DizzyEngine.resizeHosted(width,height);}
            public void onDrawFrame(GL10 unused){
                long now=System.nanoTime();
                try{DizzyEngine.renderHosted(lastFrame==0?0:Math.min((now-lastFrame)/1e9,0.1));}
                catch(Throwable ex){android.util.Log.e("Plannerator","Render failed",ex);throw new RuntimeException(ex);}
                lastFrame=now;
            }
        });
        surface.setOnTouchListener((view,event) -> {
            float x=event.getX(),y=event.getY();int action=event.getActionMasked();
            surface.queueEvent(() -> {
                DizzyEngine.hostedCursor(0,x,y);
                if(action==MotionEvent.ACTION_DOWN){mouseButton=1;DizzyEngine.hostedMouseButton(0,0,1,0);}
                if(action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL){mouseButton=0;DizzyEngine.hostedMouseButton(0,0,0,0);}
            });
            return true;
        });
        setContentView(surface);
    }
    private List<Class<?>> registeredClasses(){
        ArrayList<Class<?>> classes=new ArrayList<>();
        try(InputStream input=getClassLoader().getResourceAsStream("plannerator-registration.txt");BufferedReader reader=new BufferedReader(new InputStreamReader(input))){
            String line;while((line=reader.readLine())!=null)if(!line.isBlank())classes.add(Class.forName(line));
        }catch(Exception ex){throw new RuntimeException("Cannot load generated module registry",ex);}
        return classes;
    }
    @Override protected void onPause(){
        if(initialized)surface.queueEvent(() -> {try{Core.autosave();}catch(IOException ex){android.util.Log.w("Plannerator","Autosave failed",ex);}});
        surface.onPause();super.onPause();
    }
    @Override protected void onResume(){super.onResume();surface.onResume();}
    @Override public void chooseFile(File selected,Consumer<File> accepted,String[] extensions,String hint,boolean save){
        runOnUiThread(() -> {
            fileAccepted=accepted;saving=save;
            Intent intent=new Intent(save?Intent.ACTION_CREATE_DOCUMENT:Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);intent.setType("*/*");
            if(save)intent.putExtra(Intent.EXTRA_TITLE,selected==null?"reactor.ncpf.json":selected.getName());
            startActivityForResult(intent,DOCUMENT_REQUEST);
        });
    }
    @Override protected void onActivityResult(int request,int result,Intent data){
        super.onActivityResult(request,result,data);
        if(request!=DOCUMENT_REQUEST || result!=RESULT_OK || data==null){fileAccepted=null;return;}
        Uri uri=data.getData();Consumer<File> accepted=fileAccepted;fileAccepted=null;boolean save=saving;
        surface.queueEvent(() -> {
            File file=null;
            try{
                String name="document.ncpf.json";
                try(Cursor cursor=getContentResolver().query(uri,null,null,null,null)){
                    if(cursor!=null && cursor.moveToFirst()){int index=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);if(index>=0)name=cursor.getString(index);}
                }
                file=new File(getCacheDir(),new File(name).getName());
                if(!save)try(InputStream input=getContentResolver().openInputStream(uri)){Files.copy(input,file.toPath(),StandardCopyOption.REPLACE_EXISTING);}
                accepted.accept(file);
                if(save)try(OutputStream output=getContentResolver().openOutputStream(uri)){Files.copy(file.toPath(),output);}
            }catch(Exception ex){Core.error("Could not access document",ex);}
        });
    }
    @Override public boolean openURL(String url){runOnUiThread(() -> startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url))));return true;}
    @Override public boolean loadOptionalConfigurations(){return false;}
    @Override public void close(){runOnUiThread(this::finish);}
}
