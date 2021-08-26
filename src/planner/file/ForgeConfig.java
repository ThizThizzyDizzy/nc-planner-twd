package planner.file;
import com.codename1.util.regex.RE;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public class ForgeConfig{
    public static boolean debug = false;
    private static int ln;
    private static final int TYPE_NONE = -1;
    private static final int TYPE_STRING = 0;
    private static final int TYPE_INT = 1;
    private static final int TYPE_DOUBLE = 2;
    private static final int TYPE_BOOLEAN = 3;
    public static Config parse(InputStream stream){
        try(InputStreamReader reader = new InputStreamReader(stream)){
            Config config = Config.newConfig();
            ConfigList list = null;
            int type = TYPE_NONE;
            if(debug)System.out.print("Reading Forge Config file...");
            Stack<Config> configs = new Stack<>();
            String line;
            ln = 0;
            while((line = readLine(reader))!=null){
                ln++;
                line = line.trim();
                if(line.isEmpty())continue;
                if(line.startsWith("#"))continue;//comment
                if(list!=null){
                    if(line.equals(">")){
                        list = null;
                        type = TYPE_NONE;
                        continue;
                    }
                    switch(type){
                        case TYPE_BOOLEAN:
                            list.add(Boolean.parseBoolean(line));
                            continue;
                        case TYPE_DOUBLE:
                            list.add(Double.parseDouble(line));
                            continue;
                        case TYPE_INT:
                            list.add(Integer.parseInt(line));
                            continue;
                        case TYPE_STRING:
                            list.add(line);
                            continue;
                    }
                    error("Unknown list entry: "+line);
                }
                char[] chars = line.toCharArray();
                if(isValidKeyChar(chars[0])){//subconfig
                    String key = "";
                    for(int i = 0; i<chars.length; i++){
                        char c = chars[i];
                        if(isValidKeyChar(c))key+=c;
                        else break;
                    }
                    String value = line.substring(key.length()).trim();
                    if(!value.equals("{"))error("'{' expected!");
                    configs.push(config);
                    Config cfg = Config.newConfig();
                    config.set(key, cfg);
                    config = cfg;
                    continue;
                }
                if(line.startsWith("D:")){
                    if(line.contains("<")){
                        String key = new RE("\\<").split(line.substring(2))[0].trim();
                        list = new ConfigList();
                        config.set(key, list);
                        type = TYPE_DOUBLE;
                        continue;
                    }
                    if(line.contains("=")){
                        String[] split = new RE("=").split(line.substring(2));
                        if(split.length!=2)error("expected exactly 1 equals, found "+(split.length-1)+"!");
                        config.set(split[0].trim(), Double.parseDouble(split[1].trim()));
                        continue;
                    }
                    error("Unknown double entry: "+line);
                    continue;
                }
                if(line.startsWith("I:")){
                    if(line.contains("<")){
                        String key = new RE("\\<").split(line.substring(2))[0].trim();
                        list = new ConfigList();
                        config.set(key, list);
                        type = TYPE_INT;
                        continue;
                    }
                    if(line.contains("=")){
                        String[] split = new RE("=").split(line.substring(2));
                        if(split.length!=2)error("expected exactly 1 equals, found "+(split.length-1)+"!");
                        config.set(split[0].trim(), Integer.parseInt(split[1].trim()));
                        continue;
                    }
                    error("Unknown int entry: "+line);
                    continue;
                }
                if(line.startsWith("S:")){
                    if(line.contains("<")){
                        String key = new RE("\\<").split(line.substring(2))[0].trim();
                        list = new ConfigList();
                        config.set(key, list);
                        type = TYPE_STRING;
                        continue;
                    }
                    if(line.contains("=")){
                        String[] split = new RE("=").split(line.substring(2));
                        if(split.length!=2)error("expected exactly 1 equals, found "+(split.length-1)+"!");
                        config.set(split[0].trim(), split[1].trim());
                        continue;
                    }
                    error("Unknown string entry: "+line);
                    continue;
                }
                if(line.startsWith("B:")){
                    if(line.contains("<")){
                        String key = new RE("\\<").split(line.substring(2))[0].trim();
                        list = new ConfigList();
                        config.set(key, list);
                        type = TYPE_BOOLEAN;
                        continue;
                    }
                    if(line.contains("=")){
                        String[] split = new RE("=").split(line.substring(2));
                        if(split.length!=2)error("expected exactly 1 equals, found "+(split.length-1)+"!");
                        config.set(split[0].trim(), Boolean.parseBoolean(split[1].trim()));
                        continue;
                    }
                    error("Unknown boolean entry: "+line);
                    continue;
                }
                if(line.equals("}")){
                    config = configs.pop();
                    continue;
                }
                error("Unknown entry: "+line);
            }
            if(debug)System.out.println("Done");
            return config;
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
    private static boolean isValidKeyChar(char c){
        return Character.isLowerCase(c)||c=='_';
    }
    private static void error(String error){
        throw new IllegalArgumentException(error+" (Line "+ln+")");
    }
    private static String readLine(InputStreamReader reader) throws IOException{
        String line = "";
        char character;
        while((character = (char)reader.read())!=-1){
            line+=character;
            if(line.endsWith("\n"))return line;
        }
        return line.isEmpty()?null:line;
    }
}