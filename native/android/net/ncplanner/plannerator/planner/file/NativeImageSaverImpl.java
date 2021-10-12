package net.ncplanner.plannerator.planner.file;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.content.ContentValues;
import android.context.Context; 
public class NativeImageSaverImpl{
    public void saveImage(String filepath){
        ContentValues values = new ContentValues();
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filepath);
        context().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    private static Context context(){
        return com.codename1.impl.android.AndroidNativeUtil.getActivity().getApplicationContext();
    }
    public boolean isSupported(){
        return true;
    }
}