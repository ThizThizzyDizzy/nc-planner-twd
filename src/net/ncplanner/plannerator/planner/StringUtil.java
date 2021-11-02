package net.ncplanner.plannerator.planner;
import com.codename1.util.regex.RE;
/**
 * An abstraction layer for string operations between java and other platforms.
 * Also provides extra functionality used within the planner
 * @author Thiz
 */
public class StringUtil{
    public static String toLowerCase(String str){
        return str.toLowerCase();
    }
    public static String superRemove(String str, String... patterns){
        for(String s : patterns)str = com.codename1.util.StringUtil.replaceAll(str, s, "");
        return str;
    }
    public static String superReplace(String str, String... strs){
        for(int i = 0; i<strs.length; i+=2){
            str = com.codename1.util.StringUtil.replaceAll(str, strs[i], strs[i+1]);
        }
        return str;
    }
    public static String replace(String str, String str1, String str2){
        return com.codename1.util.StringUtil.replaceAll(str, str1, str2);
    }
    public static String replaceFirst(String str, String str1, String str2){
        return com.codename1.util.StringUtil.replaceFirst(str, str1, str2);
    }
    public static String substring(StringBuilder sb, int min){
        for(int i = 0; i<min; i++)sb.deleteCharAt(0);
        return sb.toString();
    }
    public static String[] split(String str, String regex){
        return new RE(regex).split(str);
    }
    public static boolean matches(String str, String regex){
        return new RE(regex).match(str);
    }
}