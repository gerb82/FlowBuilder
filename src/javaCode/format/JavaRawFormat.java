package javaCode.format;

import api.codeparts.ClassComponent;
import api.codeparts.FileComponent;
import api.codeparts.ImportComponent;
import api.codeparts.PackageComponent;
import api.format.AbstractCategoryDefinition;
import api.format.AbstractFormatDefinition;

import java.util.LinkedList;
import java.util.Stack;

import static javaCode.format.JavaRawFormat.Type.*;

public class JavaRawFormat extends AbstractFormatDefinition<FileComponent> {

    public static PackageComponent src = new PackageComponent();

    public JavaRawFormat() {
        main = new AbstractCategoryDefinition<FileComponent>() {
            @Override
            public void setContent(String content) throws IllegalArgumentException {

                String pName = "src";
                FileComponent file = new FileComponent();
                char[] text = content.toCharArray();
                LinkedList<CodeComponent> components = new LinkedList<>();
                CodeComponent current = new CodeComponent(0, 0, CODE);

                for (int i = 0; i < text.length; i++) {
                    switch (current.type) {
                        case CODE:
                            switch (text[i]) {
                                case '/':
                                    if (lastChar(text, i) == '/') {
                                        current.length--;
                                        components.add(current);
                                        current = new CodeComponent(i - 1, 2, COMMENT_LINE);
                                    } else {
                                        current.length++;
                                    }
                                    break;
                                case '*':
                                    if (lastChar(text, i) == '/') {
                                        current.length--;
                                        components.add(current);
                                        current = new CodeComponent(i - 1, 2, COMMENT_CLOSED);
                                    } else {
                                        current.length++;
                                    }
                                    break;
                                case '"':
                                    components.add(current);
                                    current = new CodeComponent(i, 1, STRING);
                                    break;
                                case '\'':
                                    components.add(current);
                                    current = new CodeComponent(i, 1, CHAR);
                                    break;
                                default:
                                    current.length++;
                            }
                            break;
                        case CHAR:
                            if (current.length == 1) {
                                current.length++;
                            } else if (current.length == 2) {
                                if (lastChar(text, i) == '\\') {
                                    if ("tbnrf'\"\\".contains(new Character(text[i]).toString())) {
                                        current.length++;
                                    } else {
                                        throw new IllegalArgumentException("Illegal char at: " + i);
                                    }
                                } else {
                                    if (text[i] == '\'') {
                                        current.length++;
                                        components.add(current);
                                        current = new CodeComponent(i + 1, 0, CODE);
                                    } else {
                                        throw new IllegalArgumentException("Illegal char at: " + i);
                                    }
                                }
                            } else if (current.length == 3) {
                                if (text[i] == '\'') {
                                    current.length++;
                                    components.add(current);
                                    current = new CodeComponent(i + 1, 0, CODE);
                                } else {
                                    throw new IllegalArgumentException("Illegal char at: " + i);
                                }
                            }
                            break;
                        case STRING:
                            current.length++;
                            if (text[i] == '"') {
                                if (lastChar(text, i) != '\\') {
                                    components.add(current);
                                    current = new CodeComponent(i + 1, 0, CODE);
                                }
                            }
                            break;
                        case COMMENT_LINE:
                            if (text[i] == '\n') {
                                current.length++;
                                components.add(current);
                                current = new CodeComponent(i + 1, 0, CODE);
                            } else {
                                current.length++;
                            }
                            break;
                        case COMMENT_CLOSED:
                            current.length++;
                            if (text[i] == '/') {
                                if (lastChar(text, i) == '*') {
                                    components.add(current);
                                    current = new CodeComponent(i + 1, 0, CODE);
                                }
                            }
                            break;
                    }
                }
                components.add(current);

                StringBuilder rawCode = new StringBuilder();
                for (CodeComponent component : components) {
                    if (component.type == CODE) {
                        rawCode.append(String.copyValueOf(text, component.start, component.length));
                    } else if (component.type == COMMENT_CLOSED || component.type == COMMENT_LINE) {
                        rawCode.append(" ");
                    }
                }
                String finalCode = rawCode.toString().replaceAll(" ?\\. ?", ".").replaceAll("\\{", " { ").replaceAll("\\}", " } ").replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").replaceAll("\\[", " [ ").replaceAll("\\]", " ] ").replaceAll("<", " < ").replaceAll(">", " > ").replaceAll(";", " ; ").replaceAll("[\\s\\n]+", " ");
                String[] codeParts = finalCode.split(" ");

                Stack<Character> parents = new Stack<>();
                boolean pack = false;
                try {
                    for (int i = 0; i < codeParts.length; i++) {
                        switch (codeParts[i]) {
                            case "package":
                                if (pack) {
                                    throw new IllegalArgumentException("Two package elements found");
                                } else {
                                    pack = true;
                                    i++;
                                    for (String part : codeParts[i].split(".")) {
                                        // TODO check file location
                                        if (!part.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                            throw new IllegalArgumentException("Invalid package name " + part);
                                        }
                                    }
                                    pName = codeParts[i];
                                    i++;
                                }
                                break;
                            case "import":
                                // TODO add import validation
                                i++;
                                boolean last = false;
                                for (String part : codeParts[i].split(".")) {
                                    if (!last && part.matches("\\*")) {
                                        last = true;
                                        continue;
                                    }
                                    assert (!last);
                                    if (!part.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                        throw new IllegalArgumentException("Invalid import name " + part);
                                    }
                                }
                                file.getImports().add(new ImportComponent(codeParts[i], false, codeParts[i].endsWith(".*")));
                                i++;
                                break;
                            case "class":
                                ClassComponent clazz = new ClassComponent();
                                boolean abs = false;
                                boolean pub = false;
                                if (i > 2) {
                                    if (codeParts[i - 1] == "abstract") {
                                        abs = true;
                                        pub = codeParts[i - 2] == "public";
                                    } else {
                                        pub = codeParts[i - 1] == "public";
                                    }
                                } else if (i > 1) {
                                    pub = codeParts[i - 1] == "public";
                                    abs = codeParts[i - 1] == "abstract";
                                }
                                if (pub && file.getMainClass() != null) {
                                    throw new IllegalArgumentException("There are two main classes in the file");
                                }
                                clazz.setAbs(abs);
                                i++;
                                if (!codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                    throw new IllegalArgumentException("Invlaid class name " + codeParts[i]);
                                }
                                clazz.setName(codeParts[i]);
                                i++;
                                if (codeParts[i].equals("extends")) {
                                    i++;
                                    String extension = "";
                                    if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                        extension += codeParts[i];
                                        i++;
                                        //TODO syntax validation
                                        if (codeParts[i] == "<") {
                                            parents.push('<');
                                            extension += codeParts[i];
                                            i++;
                                            while (!parents.isEmpty()) {
                                                extension += codeParts[i];
                                                if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                                    i++;
                                                } else {
                                                    throw new IllegalArgumentException("Malformed extension " + extension);
                                                }
                                                extension += codeParts[i];
                                                while (codeParts[i] == ">") {
                                                    if (parents.pop() == '<') {
                                                        i++;
                                                        extension += codeParts[i];
                                                    }
                                                }
                                                switch (codeParts[i]) {
                                                    case "<":
                                                        parents.push('<');
                                                        break;
                                                    case ",":
                                                        break;
                                                    default:
                                                        throw new IllegalArgumentException("Malformed extension " + extension);
                                                }
                                                i++;
                                            }
                                        }
                                    } else {
                                        throw new IllegalArgumentException("Malformed extension " + codeParts[i]);
                                    }
                                    clazz.setExtension(extension);
                                }
                                if (codeParts[i].equals("implements")) {
                                    i++;
                                    boolean going = true;
                                    while (going) {
                                        String implementation = "";
                                        if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                            implementation += codeParts[i];
                                            i++;
                                            //TODO syntax validation
                                            if (codeParts[i] == "<") {
                                                parents.push('<');
                                                implementation += codeParts[i];
                                                i++;
                                                while (!parents.isEmpty()) {
                                                    implementation += codeParts[i];
                                                    if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                                        i++;
                                                    } else {
                                                        throw new IllegalArgumentException("Malformed implementation " + implementation);
                                                    }
                                                    implementation += codeParts[i];
                                                    while (codeParts[i] == ">") {
                                                        if (parents.pop() == '<') {
                                                            i++;
                                                            implementation += codeParts[i];
                                                        }
                                                    }
                                                    switch (codeParts[i]) {
                                                        case "<":
                                                            parents.push('<');
                                                            break;
                                                        case ",":
                                                            break;
                                                        default:
                                                            throw new IllegalArgumentException("Malformed implementation " + implementation);
                                                    }
                                                    i++;
                                                }
                                            }
                                        } else {
                                            throw new IllegalArgumentException("Malformed implementation " + codeParts[i]);
                                        }
                                        clazz.getImplementations().add(implementation);
                                        if (codeParts[i] == ",") {
                                            i++;
                                        } else {
                                            going = false;
                                        }
                                    }
                                }
                                if (codeParts[i].equals("{")) {
                                    StringBuilder tempContent = new StringBuilder();
                                    int start = i + 1;
                                    do {
                                        switch (codeParts[i]) {
                                            case "{":
                                                parents.push('{');
                                                break;
                                            case "(":
                                                parents.push('(');
                                                break;
                                            case "[":
                                                parents.push('[');
                                                break;
                                            case "<":
                                                parents.push('<');
                                                break;
                                            case ">":
                                                // TODO bigger/smaller, currently unsupported
                                                if(codeParts[i-1].equals("-")) {
                                                    break;
                                                }
                                                if (parents.pop() != '<') {
                                                    System.out.println(i + " " + codeParts[i]);
                                                    throw new IllegalArgumentException("malformed class");
                                                }
                                                break;
                                            case ")":
                                                if (parents.pop() != '(')
                                                    throw new IllegalArgumentException("malformed class");
                                                break;
                                            case "}":
                                                if (parents.pop() != '{')
                                                    throw new IllegalArgumentException("malformed class");
                                                break;
                                            case "]":
                                                if (parents.pop() != '[')
                                                    throw new IllegalArgumentException("malformed class");

                                                break;
                                        }
                                        i++;
                                    }
                                    while (!parents.isEmpty());
                                    while (start < i - 1) {
                                        tempContent.append(codeParts[start++]);
                                    }
                                    clazz.setTempContent(tempContent.toString());
                                } else {
                                    throw new IllegalArgumentException("Malformed class");
                                }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Malformed file exception");
                }
                src.getFiles().add(file);
                src.setName(pName);
            }

            @Override
            public String contentToString() {
                return content.toString();
            }
        };
    }

    private char lastChar(char[] text, int i) {
        if (i == 0) {
            return ' ';
        } else {
            return text[i - 1];
        }
    }

    private class CodeComponent {
        private int start;
        private int length;
        private Type type;

        public CodeComponent(int start, int length, Type type) {
            this.start = start;
            this.length = length;
            this.type = type;
        }
    }

    protected enum Type {
        COMMENT_LINE, COMMENT_CLOSED, STRING, CHAR, CODE
    }
}