package javaCode.format;

import api.codeparts.*;
import api.format.AbstractCategoryDefinition;
import api.format.AbstractFormatDefinition;
import com.florianingerl.util.regex.Matcher;
import com.florianingerl.util.regex.Pattern;

import java.util.ArrayList;

public class JavaRawFormat extends AbstractFormatDefinition<FileComponent> {

    public static PackageComponent src = new PackageComponent();

    public JavaRawFormat() {
        main = new AbstractCategoryDefinition<FileComponent>() {
            @Override
            public void setContent(String content) throws IllegalArgumentException {

                String p_name;
                FileComponent file = new FileComponent();
                StringBuilder postMappingContent = new StringBuilder();
                ArrayList<ArrayList<String>> commentStrings = new ArrayList<ArrayList<String>>(){{this.add(new ArrayList<>()); this.add(new ArrayList<>()); this.add(new ArrayList<>());}};
                ArrayList<ArrayList<String>> quotedStrings = new ArrayList<ArrayList<String>>(){{this.add(new ArrayList<>()); this.add(new ArrayList<>());}};


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
                        postMappingContent.append(replacement + (commentlessContent.length < i ? "" : commentlessContent[i]));
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
                        postMappingContent.append(replacement + (stringlessContent.length < i ? "" : stringlessContent[i]));
                }
                System.out.println(postMappingContent.toString());


                /*
                file scanning (regex - "

                (?x)
                (?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)
                (?'class'(?:(?P>word)\.)*(?P>word))
                (?'arr'(?:(?P>spacers)* \[\])*)
                (?'extension'(?P>class)(?:<(?P>extension) (?P>arr) (?:(?P>spacers)* , (?P>spacers)* (?P>extension) (?P>arr))*>)?)
                (?'bounds'\{(?:[^{}]|(?P>bounds)?)*\})
                (?'noise'\S+)
                (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                \G (?P>spacers)* (?: (?<PreNoise>(?P>noise))? (?P>spacers))*? (?P>spacers)*
                (?: package (?P>spacers)+ (?<package>(?P>class));)?
                (?<imports>(?: (?P>spacers)* import (?P>spacers)+ (?:(?P>class)(?:\.\*)?);)+)? (?P>spacers)*
                (?<classes>(?:(?:public)? (?P>spacers)+ class (?P>spacers)+ (?P>word) (?: (?P>spacers)+ extends (?P>spacers)+ (?P>extension))? (?: (?P>spacers)+ implements (?P>spacers)+ (?P>extension) (?: (?P>spacers)* , (?P>spacers)* (?P>extension))*)? (?P>spacers)* (?P>bounds) (?P>spacers)*)+) (?P>spacers)*
                (?: (?<PostNoise>(?P>noise))? (?P>spacers))*

                ")
                 */
                String mainClass = null;
                ArrayList<String> classes = new ArrayList<>();
                Matcher matcher = Pattern.compile(
                        "(?x)\n" +
                                "(?(DEFINE)(?<word>[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                "(?<class>(?:(?'word')\\.)*(?'word'))\n" +
                                "(?<arr>(?:(?'spacers')* \\[\\])*)\n" +
                                "(?<extension>(?'class')(?:<(?'extension') (?'arr') (?:(?'spacers')* , (?'spacers')* (?'extension') (?'arr'))*>)?)\n" +
                                "(?<bounds>\\{(?:[^{}]|(?'bounds')?)*\\})\n" +
                                "(?<noise>\\S+)\n" +
                                "(?<spacers>\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s+))\n" +
                                "\\G (?'spacers')* (?: (?<PreNoise>(?'noise'))? (?'spacers'))*? (?'spacers')*\n" +
                                "(?: package (?'spacers')+ (?<package>(?'class'));)?\n" +
                                "(?<imports>(?: (?'spacers')* import (?'spacers')+ (?:(?'class')(?:\\.\\*)?);)+)? (?'spacers')*\n" +
                                "(?<classes>(?:(?:public)? (?'spacers')+ class (?'spacers')+ (?'word') (?: (?'spacers')+ extends (?'spacers')+ (?'extension'))? (?: (?'spacers')+ implements (?'spacers')+ (?'extension') (?: (?'spacers')* , (?'spacers')* (?'extension'))*)? (?'spacers')* (?'bounds') (?'spacers')*)+) (?'spacers')*\n" +
                                "(?: (?<PostNoise>(?'noise'))? (?'spacers'))*").matcher(postMappingContent.toString());
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
                                "(?(DEFINE)(?<word>[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                "(?<class>(?:(?'word')\\.)*(?'word'))\n" +
                                "(?<spacers>\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s+))\n" +
                                "(?'spacers')* import (?'spacers')+ (?<import>(?'class')(?:\\.\\*)?);").matcher(matcher.group("imports"));
                        while (ports.find()) {
                            // TODO fix import validation
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
                        (?'arr'(?:(?P>spacers)* \\[\\])*)
                        (?'extension'(?P>class)(?:<(?P>extension) (?P>arr) (?:(?P>spacers)* , (?P>spacers)* (?P>extension) (?P>arr))*>)?)
                        (?'bounds'\{(?:[^{}]|(?P>bounds)?)*\})
                        (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                        (?<public>public)? (?P>spacers)+ class (?P>spacers)+ (?<name>(?P>word)) (?: (?P>spacers)+ extends (?P>spacers)+ (?<extends>(?P>extension)))? (?: (?P>spacers)+ implements (?<implements> (?P>spacers)+ (?P>extension) (?: (?P>spacers)* , (?P>spacers)* (?P>extension))*))? (?P>spacers)* (?<content>(?P>bounds))

                        ")
                         */

                        Matcher school = Pattern.compile("(?x)\n" +
                                "(?(DEFINE)(?<word>[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                "(?<class>(?:(?'word')\\.)*(?'word'))\n" +
                                "(?<arr>(?:(?'spacers')* \\[\\])*)\n" +
                                "(?<extension>(?'class')(?:<(?'extension') (?'arr') (?:(?'spacers')* , (?'spacers')* (?'extension') (?'arr'))*>)?)\n" +
                                "(?<bounds>\\{(?:[^{}]|(?'bounds')?)*\\})\n" +
                                "(?<spacers>\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s+))\n" +
                                "(?<public>public)? (?'spacers')+ class (?'spacers')+ (?<name>(?'word')) (?: (?'spacers')+ extends (?'spacers')+ (?<extends>(?'extension')))? (?: (?'spacers')+ implements (?<implements> (?'spacers')+ (?'extension') (?: (?'spacers')* , (?'spacers')* (?'extension'))*))? (?'spacers')* (?<content>(?'bounds'))").matcher(matcher.group("classes"));

                        while(school.find()){
                            ClassComponent current = new ClassComponent();
                            if(school.group("public") != null){
                                if(file.getMainClass() == null){
                                    file.setMainClass(current);
                                } else {
                                    throw new IllegalArgumentException("The file contains two public classes");
                                }
                            } else {
                                file.getClasses().add(current);
                            }
                            current.setExtension(school.group("extends"));
                            if(school.group("implements") != null){
                                for(String string : school.group("implements").replaceAll("\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s+", "").split(",")){
                                    current.getImplementation().add(string);
                                }
                            }
                            current.setName(school.group("name"));

                            /* functions seperator (regex - "

                            (?x)
                            (?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)
                            (?'class'(?:(?P>word)\.)*(?P>word))
                            (?'arr'(?:(?P>spacers)* \[\])*)
                            (?'extension'(?P>class)(?:<(?P>extension) (?P>arr) (?:(?P>spacers)* , (?P>spacers)* (?P>extension) (?P>arr))*>)?)
                            (?'bounds'\{(?:[^{}]|(?P>bounds)?)*\})
                            (?'parameter'(?P>extension) (?P>arr) (?P>spacers)+ (?P>word))
                            (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                            (?P>spacers)* (?<public>public|private|protected)? (?P>spacers)+ (?<static>static (?P>spacers)+)? (?<return>(?P>extension)|void) (?P>spacers)+ (?<name>(?P>word)) (?P>spacers)* \((?<parameters> (?P>parameter) (?:(?P>spacers)* , (?P>spacers)* (?P>parameter))*)?\) (?P>spacers)* (?<throws>throws (?P>spacers)* (?P>extension) (?: (?P>spacers)* , (?P>spacers)* (?P>extension))*)? (?P>spacers)* (?<content>(?P>bounds))

                            ")
                             */

                            Matcher funk = Pattern.compile("(?x)\n" +
                                    "(?(DEFINE)(?<word>[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                    "(?<class>(?:(?'word')\\.)*(?'word'))\n" +
                                    "(?<arr>(?:(?'spacers')* \\[\\])*)\n" +
                                    "(?<extension>(?'class')(?:<(?'extension') (?'arr') (?:(?'spacers')* , (?'spacers')* (?'extension') (?'arr'))*>)?)\n" +
                                    "(?<bounds>\\{(?:[^{}]|(?'bounds')?)*\\})\n" +
                                    "(?<parameter>(?'extension') (?'arr') (?'spacers')+ (?'word'))\n" +
                                    "(?<spacers>\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s+))\n" +
                                    "(?'spacers')* (?<public>public|private|protected)? (?'spacers')+ (?<static>static (?'spacers')+)? (?<return>(?'extension')|void) (?'spacers')+ (?<name>(?'word')) (?'spacers')* \\((?<parameters> (?'parameter') (?:(?'spacers')* , (?'spacers')* (?'parameter'))*)?\\) (?'spacers')* (?<throws>throws (?'spacers')* (?'extension') (?: (?'spacers')* , (?'spacers')* (?'extension'))*)? (?'spacers')* (?<content>(?'bounds'))").matcher(school.group("content"));

                            while(funk.find()){
                                FunctionComponent curFunk = new FunctionComponent();
                                if(funk.group("public") != null){
                                    switch(funk.group("public")){
                                        case "public":
                                            curFunk.setAccessLevel(FunctionComponent.Protection.PUBLIC);
                                            break;
                                        case "private":
                                            curFunk.setAccessLevel(FunctionComponent.Protection.PRIVATE);
                                            break;
                                        case "protected":
                                            curFunk.setAccessLevel(FunctionComponent.Protection.PROTECTED);
                                            break;
                                    }
                                } else {
                                    curFunk.setAccessLevel(FunctionComponent.Protection.PACKAGE_PROTECTED);
                                }
                                curFunk.setStatic(funk.group("static") != null);
                                curFunk.setName(funk.group("name"));
                                curFunk.setReturnType(funk.group("return"));
                                if(funk.group("parameters") != null){

                                    /* parameter seperator (regex - "

                                    (?x)
                                    (?(DEFINE)(?'word'[A-Za-z_$][A-Za-z0-9_$]*)
                                    (?'class'(?:(?P>word)\.)*(?P>word))
                                    (?'arr'(?:(?P>spacers)* \[\])*)
                                    (?'extension'(?P>class)(?:<(?P>extension) (?P>arr) (?:(?P>spacers)* , (?P>spacers)* (?P>extension) (?P>arr))*>)?)
                                    (?'spacers'\/\/@\d+\s*|\/\*@\d+\s*\*\/|\/\*\*@\d+\s*\*\/|\s*))
                                    (?<type>(?P>extension) (?P>arr)) (?P>spacers)+ (?<name>(?P>word))

                                    ")
                                     */

                                    for(Matcher paramater = Pattern.compile("(?x)\n" +
                                            "(?(DEFINE)(?<word>[A-Za-z_$][A-Za-z0-9_$]*)\n" +
                                            "(?<class>(?:(?'word')\\.)*(?'word'))\n" +
                                            "(?<arr>(?:(?'spacers')* \\[\\])*)\n" +
                                            "(?<extension>(?'class')(?:<(?'extension') (?'arr') (?:(?'spacers')* , (?'spacers')* (?'extension') (?'arr'))*>)?)\n" +
                                            "(?<spacers>\\/\\/@\\d+\\s*|\\/\\*@\\d+\\s*\\*\\/|\\/\\*\\*@\\d+\\s*\\*\\/|\\s+))\n" +
                                            "(?<type>(?'extension') (?'arr')) (?'spacers')+ (?<name>(?'word'))").matcher(funk.group("parameters")); paramater.find();){
                                        VariableComponent var = new VariableComponent();
                                        var.setName(paramater.group("name"));
                                        var.setType(paramater.group("type"));
                                        curFunk.getParameters().add(var);
                                    }
                                }

                                //TODO decipher function content
                                curFunk.setTempContent(funk.group("content"));
                                (curFunk.getStatic() ? current.getStaticFunctions() : current.getFunctions()).add(curFunk);
                            }
                        }

                    }
                } else {
                    throw new IllegalArgumentException("The file could not be parsed");
                }
                if (matcher.find()) {
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
