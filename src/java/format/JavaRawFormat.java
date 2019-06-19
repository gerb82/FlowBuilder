package java.format;

import api.codeparts.FileComponent;
import api.codeparts.ImportComponent;
import api.codeparts.PackageComponent;
import api.format.AbstractCategoryDefinition;
import api.format.AbstractFormatDefinition;
import com.florianingerl.util.regex.Matcher;
import com.florianingerl.util.regex.Pattern;

import java.util.ArrayList;

public class JavaRawFormat extends AbstractFormatDefinition<FileComponent> {

    private PackageComponent src = new PackageComponent();

    public JavaRawFormat() {
        main = new AbstractCategoryDefinition<>() {
            @Override
            public void setContent(String content) throws IllegalArgumentException {

                String p_name;
                FileComponent file = new FileComponent();
                StringBuilder postMappingContent = new StringBuilder();
                ArrayList<ArrayList<String>> commentStrings = new ArrayList<>();
                ArrayList<ArrayList<String>> quotedStrings = new ArrayList<>();


                // comments formatting (regex - " \/\/(.*)|\/\*\*(?s)(.*?)\*\/|\/\*(.*?)\*\/|(\/\*) ")
                String[] commentlessContent = content.split("\\/\\/(.*)|\\/\\*\\*(?s)(.*?)\\*\\/|\\/\\*(.*?)\\*\\/|(\\/\\*)");
                Matcher comments = Pattern.compile("\\/\\/(.*)|\\/\\*\\*(?s)(.*?)\\*\\/|\\/\\*(.*?)\\*\\/|(\\/\\*)").matcher(content);
                postMappingContent.append(commentlessContent[0]);
                for (int i = 1; comments.find(); i++) {
                    String replacement = null;
                    for (int j = 1; j < 4; j++) {
                        if (comments.group(j) != null) {
                            int numberRef;
                            if (commentStrings.get(j - 1).contains(comments.group(j))) {
                                numberRef = commentStrings.get(j - 1).indexOf(comments.group(j));
                            } else {
                                numberRef = commentStrings.get(j - 1).size();
                                commentStrings.get(j - 1).add(comments.group(j));
                            }
                            replacement = comments.group(0).replace(comments.group(j), "@" + numberRef);
                            break;
                        }
                    }
                    if (replacement == null)
                        throw new IllegalArgumentException("There was an unclosed comment in the file");
                    else
                        postMappingContent.append(replacement + (commentlessContent.length < i ? commentlessContent[i] : ""));
                }
                System.out.println(postMappingContent.toString());


                // strings formatting (regex - " \"(.*?(?<!\\))\"|'(\\[btnfr"'\\]|[^'\\\n])'|(\"|') ")
                String[] stringlessContent = postMappingContent.toString().split("\\\"(.*?(?<!\\\\))\\\"|'(\\\\[btnfr\"'\\\\]|[^'\\\\\\n])'|(\\\"|')");
                Matcher strings = Pattern.compile("\\\"(.*?(?<!\\\\))\\\"|'(\\\\[btnfr\"'\\\\]|[^'\\\\\\n])'|(\\\"|')").matcher(postMappingContent.toString());
                postMappingContent = new StringBuilder();
                postMappingContent.append(stringlessContent[0]);
                for (int i = 1; strings.find(); i++) {
                    String replacement = null;
                    for (int j = 1; j < 3; j++) {
                        if (strings.group(j) != null) {
                            int numberRef;
                            if (quotedStrings.get(j - 1).contains(strings.group(j))) {
                                numberRef = quotedStrings.get(j - 1).indexOf(strings.group(j));
                            } else {
                                numberRef = quotedStrings.get(j - 1).size();
                                quotedStrings.get(j - 1).add(strings.group(j));
                            }
                            replacement = strings.group(0).replace(strings.group(j), "@" + numberRef);
                            break;
                        }
                    }
                    if (replacement == null)
                        throw new IllegalArgumentException("There was an unclosed quote in the file");
                    else
                        postMappingContent.append(replacement + (stringlessContent.length < i ? stringlessContent[i] : ""));
                }
                System.out.println(postMappingContent.toString());


                /*
                file scanning (regex - "

                (?x)
                (?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)
                (?'class'(?:(?P>word)\.)*(?P>word))
                (?'extension'(?P>class)(?:<(?P>extension) (?:(?P>spacers)* , (?P>spacers)* (?P>extension))*>)?)
                (?'bounds'\{(?:[^{}]|(?P>bounds)?)*\})
                (?'noise'\S+)
                (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                \G (?P>spacers)* (?<PreNoise>(?P>noise))? (?P>spacers)*
                (?: package (?P>spacers)+ (?<package>(?P>class));)?
                (?<imports>(?: (?P>spacers)* import (?P>spacers)+ (?:(?P>class)(?:\.\*)?);)+)? (?P>spacers)*
                (?<classes>(?:(?:public)? (?P>spacers)+ class (?P>spacers)+ (?P>word) (?: (?P>spacers)+ extends (?P>spacers)+ (?P>extension))? (?: (?P>spacers)+ implements (?P>spacers)+ (?P>extension) (?: (?P>spacers)* , (?P>spacers)* (?P>extension))*)? (?P>spacers)* (?P>bounds) (?P>spacers)*)+) (?P>spacers)*
                (?<PostNoise>(?P>noise))?

                ")
                 */
                String mainClass = null;
                ArrayList<String> classes = new ArrayList<>();
                Matcher matcher = Pattern.compile(
                        "(?x)\n" +
                                "(?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                "(?'class'(?:(?P>word)\\.)*(?P>word))\n" +
                                "(?'extension'(?P>class)(?:<(?P>extension) (?:(?P>spacers)* , (?P>spacers)* (?P>extension))*>)?)\n" +
                                "(?'bounds'\\{(?:[^{}]|(?P>bounds)?)*\\})\n" +
                                "(?'noise'\\S+)\n" +
                                "(?'spacers'\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s*))\n" +
                                "\\G (?P>spacers)* (?<PreNoise>(?P>noise))? (?P>spacers)*\n" +
                                "(?: package (?P>spacers)+ (?<package>(?P>class));)?\n" +
                                "(?<imports>(?: (?P>spacers)* import (?P>spacers)+ (?:(?P>class)(?:\\.\\*)?);)+)? (?P>spacers)*\n" +
                                "(?<classes>(?:(?:public)? (?P>spacers)+ class (?P>spacers)+ (?P>word) (?: (?P>spacers)+ extends (?P>spacers)+ (?P>extension))? (?: (?P>spacers)+ implements (?P>spacers)+ (?P>extension) (?: (?P>spacers)* , (?P>spacers)* (?P>extension))*)? (?P>spacers)* (?P>bounds) (?P>spacers)*)+) (?P>spacers)*\n" +
                                "(?<PostNoise>(?P>noise))?").matcher(postMappingContent.toString());
                if (matcher.find()) {
                    if (matcher.group("PreNoise") != null) {
                        throw new IllegalArgumentException("There was noise before the file content");
                    }
                    if (matcher.group("PostNoise") != null) {
                        throw new IllegalArgumentException("There was noise after the file content");
                    }
                    p_name = matcher.group("package") == null ? "src" : matcher.group("package");
                    if (matcher.group("imports") != null) {

                            /* imports separator (regex - "

                            (?x)
                            (?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)
                            (?'class'(?:(?P>word)\.)*(?P>word))
                            (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                            (?P>spacers)* import (?P>spacers)+ (?<import>(?P>class)(?:\.\*)?);

                            ")
                             */

                        Matcher ports = Pattern.compile("(?x)\n" +
                                "(?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                "(?'class'(?:(?P>word)\\.)*(?P>word))\n" +
                                "(?'spacers'\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s*))\n" +
                                "(?P>spacers)* import (?P>spacers)+ (?<import>(?P>class)(?:\\.\\*)?);").matcher(matcher.group("imports"));
                        while (ports.find()) {
                            String port = ports.group("import");
                            if (port.contains("*")) {
                                file.getImports().add(new ImportComponent(port.replace(".*", ""), false, false));
                            } else {
                                try {
                                    Class.forName(port);
                                    file.getImports().add(new ImportComponent(port, true, true));
                                } catch (ClassNotFoundException e) {
                                    file.getImports().add(new ImportComponent(port, false, true));
                                }
                            }
                        }
                    }
                    if (matcher.group("classes") != null) {

                            /* classes separator (regex - "

                            (?x)
                            (?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)
                            (?'class'(?:(?P>word)\.)*(?P>word))
                            (?'extension'(?P>class)(?:<(?P>extension) (?:(?P>spacers)* , (?P>spacers)* (?P>extension))*>)?)
                            (?'bounds'\{(?:[^{}]|(?P>bounds)?)*\})
                            (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                            (?<public>public)? (?P>spacers)+ class (?P>spacers)+ (?<name>(?P>word)) (?: (?P>spacers)+ extends (?P>spacers)+ (?<extends>(?P>extension)))? (?: (?P>spacers)+ implements (?<implements> (?P>spacers)+ (?P>extension) (?: (?P>spacers)* , (?P>spacers)* (?P>extension))*))? (?P>spacers)* (?P>bounds)

                            ")
                             */
                    }
                } else {
                    throw new IllegalArgumentException("The file could not be parsed");
                }
                if(matcher.find()){
                    throw new IllegalArgumentException("The file contained two successive valid files");
                }

                src.setName(p_name);
                src.getFiles().add(file);
            }

            @Override
            public String contentToString() {
                return content.toString();
            }
        };
    }
}
