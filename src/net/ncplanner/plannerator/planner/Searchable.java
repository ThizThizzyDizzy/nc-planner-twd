package net.ncplanner.plannerator.planner;
import com.codename1.util.StringUtil;
import com.codename1.util.regex.RE;
import com.codename1.util.regex.RECharacter;
import java.util.ArrayList;
public interface Searchable{
    public ArrayList<String> getSimpleSearchableNames();//[\s\A](-([\w\d]+)|-\"(.+?)\")[\Z\s]
    public ArrayList<String> getSearchableNames();//[\s\A](-([\w\d]+)|-\"(.+?)\")[\Z\s]
    public static final String excludePattern = "(\\s|^)(-([\\w\\d]+)|-\\\"(.+?)\\\")($|\\s)";
    public static boolean isValidForSearch(Searchable searchable, String searchText){
        String regex = ".*";
        if(searchText.startsWith("regex:")){
            regex = searchText.substring(6);
        }else{
            while(true){
                RE re = new RE(excludePattern);
                if(re.match(searchText)){
                    String exclude = re.getParen(4);
                    if(exclude==null)exclude = re.getParen(3);
                    String excludeRegex = ".*";
                    for(char c : exclude.toCharArray()){
                        if(RECharacter.isLetterOrDigit(c)){
                            excludeRegex+="["+Character.toLowerCase(c)+""+Character.toUpperCase(c)+"]";
                        }else excludeRegex+="\\"+c;
                    }
                    excludeRegex+=".*";
                    RE re2 = new RE(excludeRegex);
                    for(String nam : searchable.getSearchableNames()){
                        if(re2.match(nam))return false;//excluded
                    }
                    
                    searchText = StringUtil.replaceFirst(searchText, re.getParen(0), " ");;
                }else break;
            }
            searchText = searchText.trim();
            //old stuff
            for(char c : searchText.toCharArray()){
                if(RECharacter.isLetterOrDigit(c)){
                    regex+="["+Character.toLowerCase(c)+""+Character.toUpperCase(c)+"].*";
                }else regex+="\\"+c+".*";
            }
        }
        for(String nam : searchable.getSearchableNames()){
            try{
                if(new RE(regex).match(nam))return true;
            }catch(Exception ex){return false;}
        }
        return false;
    }
}