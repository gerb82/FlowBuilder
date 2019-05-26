package api.format.textTypes;

import com.florianingerl.util.regex.Matcher;
import com.florianingerl.util.regex.Pattern;

import java.util.ArrayList;

public interface LoopSeperated {

    default ArrayList<String> seperate(ArrayList<String> loops, char argumentStarter, char argumentCloser, char loopStarter, char loopCloser, String text){
        ArrayList<String> lines = new ArrayList<>();
        String loopNames = "";
        boolean first = true;
        for(String loop : loops){
            loopNames += (!first ? "|" : "") + loop;
            first = false;
        }
        Matcher matcher = Pattern.compile("(?:(" + loopNames + ")(\\" + argumentStarter + "((?:[^" + argumentStarter + argumentCloser + "]*?|(?2))*?)\\" + argumentCloser + ")(\\" + loopStarter + "((?:[^" + loopStarter + loopCloser + "]*|(?4))*)\\" + loopCloser + "))").matcher(text);
        while(matcher.find()) lines.add(matcher.group(1));
        return lines;
    }
}
