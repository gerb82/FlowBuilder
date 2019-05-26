package api.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Reader {

    private String opener;
    private String closer;
    private ArrayList<String> categories;

    protected void setOpener(String opener) {
        this.opener = opener;
    }

    protected void setCloser(String closer) {
        this.closer = closer;
    }

    public AbstractFormatDefinition readFile(File file) throws IOException {
        String fileText = new ObjectInputStream(new FileInputStream(file)).readUTF();
        AbstractFormatDefinition format = formatSetup();
        for (int i = 0; i <= categories.size(); i++) {
            Matcher matcher = Pattern.compile("(?:" + categories.get(i) + opener + ")(.*?)" + "(?:" + closer + ")").matcher(fileText);
            if (matcher.find()) {
                format.setCategory(i, matcher.group(1));
            } else {
                throw new IOException("Couldn't find the " + categories.get(i) + " category in the file with the appropriate opener and closer");
            }
        }
        Matcher matcher = Pattern.compile("(?:" + "main" + opener + ")(.*)" + "(?:" + closer + ")").matcher(fileText);
        if (matcher.find()) {
            format.setMainContent(matcher.group(1));
        } else {
            throw new IOException("Couldn't find the main category in the file with the appropriate opener and closer");
        }
        return format;
    }

    protected abstract AbstractFormatDefinition formatSetup();
}
