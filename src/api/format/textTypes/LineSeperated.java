package api.format.textTypes;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A utility for separating lines of text
public interface LineSeperated {

    static ArrayList<String> seperate(String seperator, String text){
        ArrayList<String> lines = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?s)(.*?)(?:(?<!\\)" + seperator + ")").matcher(text);
        while(matcher.find()) lines.add(matcher.group(1));
        return lines;
    }

    static ArrayList<String> seperate(String text){
        return seperate(";", text);
    }
}