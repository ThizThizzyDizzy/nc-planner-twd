package simplelibrary.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
public class Config extends ConfigBase implements Cloneable{
    private static final int TYPE_ALONE = 1;
    private static final int TYPE_STREAM = 4;
    private int type = TYPE_ALONE;
    private String path;
    private InputStream stream;
    private final ArrayList<String> keys = new ArrayList<>();
    private final HashMap<String, ConfigBase> data = new HashMap<>();
    /**
     * The format version that this Config2 system will write.
     * Any Config2 file, of this version or lower, can be parsed successfully.  However,
     * ALL save operations will produce a file of this specific version.
     * The original Config2 format, written by all versions of SimpleLibrary before the introduction of this field, is format version 0.
     * NOTE:  Version 2 is exclusive to ConfigMulti.
     */
    public static final short CONFIG_VERSION = 1;
    public static Config newConfig(InputStream in){
        Config config = new Config();
        config.type = TYPE_STREAM;
        config.stream = in;
        return config;
    }
    public static Config newConfig(){
        return new Config();
    }
    public Config(){}
    @Override
    void read(DataInputStream in, short version) throws IOException{
        int index;
        ConfigBase base;
        if(version==0){//Version 0 compatability
            String key;
            while((index = in.read())>0){
                base = newConfig(index);
                key = in.readUTF();
                base.read(in, version);
                dataput(key, base);
            }
            return;
        }
        while((index = in.read())>0){
            base = newConfig(index);
            base.read(in, version);
            dataput(in.readUTF(), base);
        }
    }
    @Override
    void write(DataOutputStream out) throws IOException{
        ConfigBase base;
        for(String str : keys){
            base = data.get(str);
            out.write(base.getIndex());
            base.write(out);
            out.writeUTF(str);//Save key AFTER the data, because that's how it's being read
        }
        out.write(0);
    }
    @Override
    Config getData(){
        return this;
    }
    public Config load(){
        switch(type){
            case TYPE_ALONE:
                throw new IllegalArgumentException("Cannot load a config with nothing to load from!");
            case TYPE_STREAM:
                return load(stream);
            default:
                throw new AssertionError(type);
        }
    }
    public boolean save(){
        switch(type){
            case TYPE_ALONE:
                throw new IllegalArgumentException("Cannot save a config with nothing to save to!");
            case TYPE_STREAM:
                throw new IllegalArgumentException("Cannot save a config with nothing to save to!");
            default:
                throw new AssertionError(type);
        }
    }
    public Config load(InputStream in){
        if(in==null){
            throw new IllegalArgumentException("Input stream cannot be null!");
        }
        data.clear();
        keys.clear();
        DataInputStream dataIn = new DataInputStream(in);
        boolean fail = false;
        try{
            short version = dataIn.readShort();
            if(version==2){
                //Version 2 is exclusive to ConfigMulti
                fail = true;
                throw new IllegalArgumentException("File is not a Config file!  Open it with ConfigMulti!");
            }
            if(version>CONFIG_VERSION){
                fail = true;
                throw new IllegalArgumentException("File is a newer version of format!");
            }
            read(dataIn, version);
        }catch(Throwable ex){
            if(ex instanceof IllegalArgumentException&&((IllegalArgumentException)ex).getMessage().equals("File is a newer version of format!")) throw (IllegalArgumentException)ex;
            throw new RuntimeException("Could not load config!", ex);
        }
        return this;
    }
    public  boolean save(OutputStream out){
        if(out==null){
            throw new IllegalArgumentException("Output stream cannot be null!");
        }
        DataOutputStream dataOut = new DataOutputStream(out);
        try{
            dataOut.writeShort(CONFIG_VERSION);
            write(dataOut);
        }catch(Throwable ex){
            throw new RuntimeException("Could not save config!", ex);
        }
        return true;
    }
    public <V> V get(String key){
        if(!data.containsKey(key)){
            return null;
        }else{
            return (V)data.get(key).getData();
        }
    }
    public <V> V get(String key, V defaultValue){
        if(!data.containsKey(key)){
            set(key, defaultValue);
            return defaultValue;
        }else{
            return (V)data.get(key).getData();
        }
    }
    public Config getConfig(String key){
        return get(key);
    }
    public String getString(String key){
        return get(key);
    }
    public int getInt(String key){
        return get(key);
    }
    public boolean getBoolean(String key){
        return get(key);
    }
    public float getFloat(String key){
        return get(key);
    }
    public long getLong(String key){
        return get(key);
    }
    public double getDouble(String key){
        return get(key);
    }
    public byte getByte(String key){
        return get(key);
    }
    public short getShort(String key){
        return get(key);
    }
    public ConfigNumberList getConfigNumberList(String key){
        return get(key);
    }
    public ConfigList getConfigList(String key){
        return get(key);
    }
    public Config getConfig(String key, Config defaultValue){
        return get(key, defaultValue);
    }
    public String getString(String key, String defaultValue){
        return get(key, defaultValue);
    }
    public int getInt(String key, int defaultValue){
        return get(key, defaultValue);
    }
    public boolean getBoolean(String key, boolean defaultValue){
        return get(key, defaultValue);
    }
    public float getFloat(String key, float defaultValue){
        return get(key, defaultValue);
    }
    public long getLong(String key, long defaultValue){
        return get(key, defaultValue);
    }
    public double getDouble(String key, double defaultValue){
        return get(key, defaultValue);
    }
    public byte getByte(String key, byte defaultValue){
        return get(key, defaultValue);
    }
    public short getShort(String key, short defaultValue){
        return get(key, defaultValue);
    }
    public ConfigNumberList getConfigNumberList(String key, ConfigNumberList defaultValue){
        return get(key, defaultValue);
    }
    public ConfigList getConfigList(String key, ConfigList defaultValue){
        return get(key, defaultValue);
    }
    public String[] properties(){
        return keys.toArray(new String[keys.size()]);
    }
    public <V> V removeProperty(String key){
        V val = get(key);
        data.remove(key);
        keys.remove(key);
        return val;
    }
    public boolean hasProperty(String key){
        return data.containsKey(key);
    }
    public void setProperty(String key, Config value){
        dataput(key, value);
    }
    public void setProperty(String key, String value){
        dataput(key, new ConfigString(value));
    }
    public void setProperty(String key, int value){
        dataput(key, new ConfigInteger(value));
    }
    public void setProperty(String key, boolean value){
        dataput(key, new ConfigBoolean(value));
    }
    public void setProperty(String key, float value){
        dataput(key, new ConfigFloat(value));
    }
    public void setProperty(String key, long value){
        dataput(key, new ConfigLong(value));
    }
    public void setProperty(String key, double value){
        dataput(key, new ConfigDouble(value));
    }
    public void setProperty(String key, byte value){
        dataput(key, new ConfigByte(value));
    }
    public void setProperty(String key, short value){
        dataput(key, new ConfigShort(value));
    }
    public void setProperty(String key, ConfigNumberList value){
        dataput(key, value);
    }
    public void setProperty(String key, ConfigList value){
        dataput(key, value);
    }
    public void set(String key, Config value){
        dataput(key, value);
    }
    public void set(String key, String value){
        dataput(key, new ConfigString(value));
    }
    public void set(String key, int value){
        dataput(key, new ConfigInteger(value));
    }
    public void set(String key, boolean value){
        dataput(key, new ConfigBoolean(value));
    }
    public void set(String key, float value){
        dataput(key, new ConfigFloat(value));
    }
    public void set(String key, long value){
        dataput(key, new ConfigLong(value));
    }
    public void set(String key, double value){
        dataput(key, new ConfigDouble(value));
    }
    public void set(String key, ConfigList value){
        dataput(key, value);
    }
    public void set(String key, ConfigNumberList value){
        dataput(key, value);
    }
    public void set(String key, byte value){
        dataput(key, new ConfigByte(value));
    }
    public void set(String key, short value){
        dataput(key, new ConfigShort(value));
    }
    public void set(String key, Object value){
        if(value==null){
        }else if(value instanceof Config){
            set(key, (Config)value);
        }else if(value instanceof String){
            set(key, (String)value);
        }else if(value instanceof Integer){
            set(key, (int)value);
        }else if(value instanceof Boolean){
            set(key, (boolean)value);
        }else if(value instanceof Float){
            set(key, (float)value);
        }else if(value instanceof Long){
            set(key, (long)value);
        }else if(value instanceof Double){
            set(key, (double)value);
        }else if(value instanceof ConfigList){
            set(key, (ConfigList)value);
        }else if(value instanceof ConfigNumberList){
            set(key, (ConfigNumberList)value);
        }else if(value instanceof Byte){
            set(key, (byte)value);
        }else if(value instanceof Short){
            set(key, (short)value);
        }
    }
    private void dataput(String key, ConfigBase base){
        if(base==null) throw new IllegalArgumentException("Cannot set null values to a config!");
        if(!keys.contains(key)){
            keys.add(key);
        }
        data.put(key, base);
    }
    public Config clone(){
        Config c = newConfig();
        c.type = type;
        c.stream = stream;
        c.keys.addAll(keys);
        for(String s : data.keySet()){
            ConfigBase b = data.get(s);
            if(b instanceof Config){
                c.data.put(s, ((Config)b).clone());
                //TODO make ConfigList and ConfigNumberList deep-clone as well
            }else{
                c.data.put(s, b);
            }
        }
        return c;
    }
    @Override
    public boolean equals(Object obj){
        if(obj==null||obj.getClass()!=getClass()) return false;
        Config c = (Config)obj;
        if(c.keys.size()!=keys.size()) return false;
        for(String k : keys){
            if(!c.keys.contains(k)) return false;
            if(!get(k).equals(c.get(k))) return false;
        }
        return true;
    }
}
