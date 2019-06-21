package api.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The abstract reader class
public abstract class Reader {

    // Category opener (for example, '{')
    private String opener;
    // Category closer (for example, '}')
    private String closer;
    // List of possible categories inside of the file
    protected ArrayList<String> categories;
    // Are there in fact any categories, or is this file a raw main category?
    protected boolean raw = false;

    // Opener setter
    protected void setOpener(String opener) {
        this.opener = opener;
    }

    // Closer setter
    protected void setCloser(String closer) {
        this.closer = closer;
    }

    // File reader, throws an IOException if the file could not be read
    public AbstractFormatDefinition readFile(File file) throws IOException {
        // File content
        String fileText = new String(Files.readAllBytes(file.toPath()));
        // The format that will be used to decipher the contents of the file
        AbstractFormatDefinition format = formatSetup(file);
        // If it's a raw file, just set the main content on the format
        if (raw) format.setMainContent(fileText);
        else {
            // If not, then first read all of the side categories, and fill them in
            for (int i = 0; i <= categories.size(); i++) {
                /* Regex explanation
                (?s) - The . character will now reference ALL characters, even new-line characters
                (?:categoryName+opener) - The matched pattern will start with categoryName+opener (for example: "test{"), but it will not be a part of
                the actual string given back
                (.*?) - Capture group one, which can be referenced as group 1 in the result, contains the following:
                . (matches ANY character), times *? (matches the previous pattern zero to an infinite amount of times, eating up more characters as
                needed in order for the pattern to match
                (?:closer) - The matched pattern will end with closer (for example: "}"), but it will not be a part of the actual string given back.
                Example result of the regex:
                category1{
                hi there i'm doing all sorts of interesting stuff
                }
                group 1: "hi there i'm doing all sorts of interesting stuff"
                 */
                Matcher matcher = Pattern.compile("(?s)(?:" + categories.get(i) + opener + ")(.*?)" + "(?:" + closer + ")").matcher(fileText);
                if (matcher.find()) {
                    format.setCategory(i, matcher.group(1));
                } else {
                    throw new IOException("Couldn't find the " + categories.get(i) + " category in the file with the appropriate opener and closer");
                }
            }
            // And then read the rest of the file, that contains the main category, and add it into the format
            /* Regex explanation
            The exact regex as the previous one, except this one will not take more characters as needed, but instead, give up on characters as needed
            for the pattern to match.
            The main difference is the following result:

            category1{
            hello world
            }
            }

            While the first pattern will only match:
            "category1{
            hello world
            }"

            The second pattern would match:
            "category1{
            hello world
            }
            }"

            This way, if this block of text contains the closer by any chance, there won't be any problems.
            That being said, this can only be done because it is the last category, as if it wasn't, it would have eaten up all of the other
            categories, closing itself on the last category's closer regardless of anything else.
             */
            Matcher matcher = Pattern.compile("(?s)(?:" + "main" + opener + ")(.*)" + "(?:" + closer + ")").matcher(fileText);
            if (matcher.find()) {
                format.setMainContent(matcher.group(1));
            } else {
                throw new IOException("Couldn't find the main category in the file with the appropriate opener and closer");
            }
        }
        // Return the finished format
        return format;
    }

    // Format setup that needs to be done in order to properly read the file
    protected abstract AbstractFormatDefinition formatSetup(File file) throws IOException;
}
