package javaCode.format;

import api.codeparts.*;
import api.format.AbstractCategoryDefinition;
import api.format.AbstractFormatDefinition;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

import static javaCode.format.JavaRawFormat.Type.*;

public class JavaRawFormat extends AbstractFormatDefinition<FileComponent> {

    public static PackageComponent src = new PackageComponent();

    private ArrayList<String> stringList;
    private ArrayList<String> characterList;

    public JavaRawFormat() {
        main = new AbstractCategoryDefinition<FileComponent>() {

            @Override
            public void setContent(String content) throws IllegalArgumentException {

                String pName = "src";
                FileComponent file = new FileComponent();
                char[] text = content.toCharArray();
                LinkedList<CodeComponent> components = new LinkedList<>();
                CodeComponent current = new CodeComponent(0, 0, CODE);
                ArrayList<Scannable> toScan = new ArrayList<>();
                stringList = new ArrayList<>();
                characterList = new ArrayList<>();

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
                                    if ("tbnrf'\"\\".contains(Character.toString(text[i]))) {
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
                int strings = 0;
                int chars = 0;
                for (CodeComponent component : components) {
                    switch (component.type) {
                        case CODE:
                            rawCode.append(String.copyValueOf(text, component.start, component.length));
                            break;
                        case COMMENT_CLOSED:
                        case COMMENT_LINE:
                            rawCode.append(" ");
                            break;
                        case STRING:
                            rawCode.append("\"@" + strings++ + "\"");
                            stringList.add(String.copyValueOf(text, component.start + 1, component.length - 1));
                            break;
                        case CHAR:
                            rawCode.append("'@" + chars++ + "'");
                            characterList.add(String.copyValueOf(text, component.start + 1, component.length - 1));
                            break;
                    }
                }
                String finalCode = rawCode.toString().replaceAll(" ?\\. ?", ".").replaceAll("=", " = ").replaceAll("=\\s*=", " == ").replaceAll(";", " ; ").replaceAll(",", " , ").replaceAll("\\{", " { ").replaceAll("\\}", " } ").replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").replaceAll("\\[", " [ ").replaceAll("\\]", " ] ").replaceAll("<", " < ").replaceAll(">", " > ").replaceAll(";", " ; ").replaceAll("[\\s\\n]+", " ");
                String[] codeParts = finalCode.split(" ");

                boolean pack = false;
                try {
                    for (int i = 0; i < codeParts.length; ) {
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
                                i++;
                                break;
                            case "class":
                                ClassComponent clazz = new ClassComponent();
                                boolean abs = false;
                                boolean pub = false;
                                if (i > 2) {
                                    if (codeParts[i - 1].equals("abstract")) {
                                        abs = true;
                                        pub = codeParts[i - 2].equals("public");
                                    } else {
                                        pub = codeParts[i - 1].equals("public");
                                    }
                                } else if (i > 1) {
                                    pub = codeParts[i - 1].equals("public");
                                    abs = codeParts[i - 1].equals("abstract");
                                }
                                if (pub && file.getMainClass() != null) {
                                    throw new IllegalArgumentException("There are two main classes in the file");
                                }
                                clazz.setAbs(abs);
                                i++;
                                if (!codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                    throw new IllegalArgumentException("Invalid class name " + codeParts[i]);
                                }
                                clazz.setName(codeParts[i]);
                                i++;
                                i = classValidation(codeParts, i, clazz);
                                file.getClasses().add(clazz);
                                toScan.add(clazz);
                                break;
                            case "public":
                                if (codeParts[i + 1].equals("class") || codeParts[i + 2].equals("class")) {
                                    i++;
                                    break;
                                } else {
                                    throw new IllegalArgumentException("Malformed file");
                                }
                            case "abstract":
                                if (codeParts[i + 1].equals("class")) {
                                    i++;
                                    break;
                                } else {
                                    throw new IllegalArgumentException("Malformed file");
                                }
                            default:
                                throw new IllegalArgumentException("Malformed file");
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Malformed file exception");
                }
                for (Scannable scannable : toScan) {
                    recursiveScanner(scannable);
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

    private ContentComponent recursiveScanner(Scannable scannable) {
        String[] text = scannable.getTempContent();
        if (scannable instanceof ClassComponent) {
            int privacy = 0;
            boolean stat = false;
            for (int i = 0; i < text.length; ) {
                stat = false;
                privacy = 0;
                switch (text[i]) {
                    case "public":
                        privacy = 1;
                        break;
                    case "protected":
                        privacy = 2;
                        break;
                    case "private":
                        privacy = 3;
                        break;
                }
                if (privacy != 0) {
                    i++;
                }
                if (text[i].equals("static")) {
                    stat = true;
                    i++;
                }
                if (text[i].equals("class")) {
                    // class
                    // TODO add inside class support
                    throw new IllegalArgumentException("Inside classes are currently unsupported");
                } else if (text[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                    Pair<VariableComponent, Integer> var = variableDecleration(text, i);
                    if (var.getKey().getType() != null && var.getKey().getName() != null) {
                        i = var.getValue();
                        if (text[i].equals("(")) {
                            // function
                            i++;
                            FunctionComponent funk = new FunctionComponent();
                            funk.setReturnType(var.getKey().getType());
                            funk.setName(var.getKey().getName());
                            while (!text[i].equals(")")) {
                                Pair<VariableComponent, Integer> param = variableDecleration(text, i);
                                if (param.getKey().getType() != null && param.getKey().getName() != null) {
                                    funk.getParameters().add(param.getKey());
                                    i = param.getValue();
                                } else {
                                    throw new IllegalArgumentException("Invalid function signature for function: " + funk.getName());
                                }
                                if (text[i].equals(",")) {
                                    i++;
                                } else if (!text[i].equals(")")) {
                                    throw new IllegalArgumentException("Invalid function signature for function: " + funk.getName());
                                }
                            }
                            i++;
                            int start = i + 1;
                            if (text[i].equals("throws")) {
                                i++;
                                while (text[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                                    StringBuilder first = new StringBuilder(text[i++]);
                                    if (text[i].equals("<")) {
                                        Pair<String, Integer> out = notBigger(text, i);
                                        if (out.getValue() == 0) throw new IllegalArgumentException("Invalid <>");
                                        first.append(out.getKey());
                                    }
                                    if (text[i].equals(",")) {
                                        funk.getThrowing().add(first.toString());
                                        i++;
                                    } else if (text[i].equals("{")) {
                                        funk.getThrowing().add(first.toString());
                                    } else {
                                        throw new IllegalArgumentException("Invalid throwable in function: " + funk.getName());
                                    }
                                }
                            }
                            i = parentValidator(text, i);
                            funk.setTempContent(Arrays.copyOfRange(text, start, i - 1));
                            (stat ? ((ClassComponent) scannable).getStaticFunctions() : ((ClassComponent) scannable).getFunctions()).add(funk);
                            recursiveScanner(funk);
                        } else if (text[i].equals("=")) {
                            // variable
                            i++;
                            StringBuilder value = new StringBuilder();
                            i = valueDefinition(text, i, value);
                            if (text[i].equals(";")) {
                                var.getKey().setVal(value.toString());
                                i++;
                                (stat ? ((ClassComponent) scannable).getStaticVariables() : ((ClassComponent) scannable).getVariables()).add(var.getKey());
                            } else {
                                throw new IllegalArgumentException("No ; found on variable: " + var.getKey().getName());
                            }
                        } else {
                            throw new IllegalArgumentException("Unexpected token");
                        }
                    } else {
                        throw new IllegalArgumentException("Incorrect variable/function declaration");
                    }
                } else if (text[i].equals("@Override")) {
                    i++;
                } else {
                    throw new IllegalArgumentException("Unexpected token");
                }
            }
        } else {
            for (int i = 0; i < text.length; ) {
                int nextSemicolon = i;
                boolean equals = false;
                try {
                    while (!text[nextSemicolon].equals(";")) {
                        if (text[nextSemicolon].equals("=")) equals = true;
                        nextSemicolon++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Missing ; at end of function");
                }
                if (equals) {
                    Pair<VariableComponent, Integer> result = variableDecleration(text, i);
                    i = result.getValue();
                    if (result.getKey().getName() != null && result.getKey().getType() != null) {
                        if (text[i].equals("=")) {
                            StringBuilder builder = new StringBuilder(text[i++]);
                            i = valueDefinition(text, i, builder);
                            result.getKey().setVal(builder.toString());
                            ((FunctionComponent) scannable).getContent().add(result.getKey());
                            if (text[i++].equals(";")) continue;
                            else {
                                throw new IllegalArgumentException("Missing ;");
                            }
                        } else {
                            throw new IllegalArgumentException("Extra characters found in code line");
                        }
                    } else if (result.getKey().getName() != null) {
                        if (text[i].equals("=")) {
                            StringBuilder builder = new StringBuilder(text[i++]);
                            i = valueDefinition(text, i, builder);
                            result.getKey().setVal(builder.toString());
                            ((FunctionComponent) scannable).getContent().add(result.getKey());
                            if (text[i++].equals(";")) continue;
                            else {
                                throw new IllegalArgumentException("Missing ;");
                            }
                        } else {
                            throw new IllegalArgumentException("Extra characters found in code line");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid =");
                    }
                } else {
                    StringBuilder builder = new StringBuilder();
                    i = valueDefinition(text, i, builder);
                    MethodComponent method = new MethodComponent();
                    method.setMethod(builder.toString());
                    ((FunctionComponent) scannable).getContent().add(method);
                    if (text[i++].equals(";")) continue;
                    else {
                        throw new IllegalArgumentException("Missing ;");
                    }
                }
            }
        }
        return scannable;
    }

    private Pair<VariableComponent, Integer> variableDecleration(String[] text, int i) {
        if (text[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
            StringBuilder first = new StringBuilder(text[i++]);
            String pure = first.toString();
            if (text[i].equals("<")) {
                Pair<String, Integer> out = notBigger(text, i);
                if (out.getValue() == 0) throw new IllegalArgumentException("Invalid <>");
                first.append(out.getKey());
            }

            i = arrayEnclosure(text, i, first);
            String second = null;
            if (text[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                second = text[i++];
            }
            VariableComponent var = new VariableComponent();
            if (second == null && first.toString().equals(pure)) {
                var.setName(pure);
            } else if (second != null) {
                var.setType(first.toString());
                var.setName(second);
            } else {
                throw new IllegalArgumentException("Type declaration found but no variable was found with it");
            }
            return new Pair<>(var, i);
        }
        return new Pair<>(new VariableComponent(), i);
    }

    private int valueDefinition(String[] text, int i, StringBuilder val) {
        if (text[i].equals("new")) {
            val.append(text[i++]);
            if (text[i].matches("^(?:[a-zA-Z_$][a-zA-Z0-9_$]*)(?:\\.(?:[a-zA-Z_$][a-zA-Z0-9_$]*))*$")) {
                val.append(text[i++]);
                if (text[i].equals("<")) {
                    Pair<String, Integer> out = notBigger(text, i);
                    if (out.getValue() == 0) throw new IllegalArgumentException("Invalid <> " + val);
                    val.append(out.getKey());
                }
                i = filledArrayEnclosure(text, i, val);
                if (text[i].equals("(")) {
                    val.append(text[i++]);
                    i = valueDefinition(text, i, val);
                    if (text[i].equals(")")) {
                        val.append(text[i++]);
                        return i;
                    }
                } else {
                    throw new IllegalArgumentException("Missing () " + val);
                }
            } else {
                throw new IllegalArgumentException("Illegal constructor name " + text[i]);
            }
        } else if (text[i].matches("^\\d*(?:\\.\\d*)?$")) {
            val.append(text[i++]);
        } else if (text[i].matches("^true|false$")) {
            val.append(text[i++]);
        } else if (text[i].matches("^\"@\\d*\"$")) {
            val.append(text[i].replaceAll("@\\d*", stringList.get(Integer.valueOf(text[i++].replaceAll("[\"@]", "")))));
        } else if (text[i].matches("^'@\\d*'$")) {
            val.append(text[i].replaceAll("@\\d*", characterList.get(Integer.valueOf(text[i++].replaceAll("['@]", "")))));
        } else if (text[i].equals("(")) {
            val.append(text[i++]);
            i = valueDefinition(text, i, val);
        } else if (text[i].equals(")")) {
            val.append(text[i++]);
            return i;
        } else {
            while (text[i].matches("^(?:[a-zA-Z_$][a-zA-Z0-9_$]*)(?:\\.(?:[a-zA-Z_$][a-zA-Z0-9_$]*))*$")) {
                val.append(text[i++]);
                i = filledArrayEnclosure(text, i, val);
                if (text[i].equals("(")) {
                    i = valueDefinition(text, i, val);
                }
                if (text[i].equals(",")) val.append(text[i++]);
                else if (text[i].equals(")")) {
                    val.append(text[i++]);
                    return i;
                } else if (text[i].equals(".")) {
                    val.append(text[i++]);
                } else {
                    throw new IllegalArgumentException("Unexpected token");
                }
            }
        }
        return i;
    }

    private char lastChar(char[] text, int i) {
        if (i == 0) {
            return ' ';
        } else {
            return text[i - 1];
        }
    }

    private int arrayEnclosure(String[] text, int i, StringBuilder buf) {
        int out = 0;
        while (text[i].equals("[")) {
            out++;
            i++;
            if (text[i].equals("]")) {
                i++;
            } else {
                throw new IllegalArgumentException("Poorly formatted []");
            }
        }
        buf.append(new String(new char[out]).replace("\0", "[]"));
        return i;
    }

    private int filledArrayEnclosure(String[] text, int i, StringBuilder buf) {
        Stack<Character> parents = new Stack<>();
        if (text[i].equals("[")) {
            do {
                switch (text[i]) {
                    case "(":
                        parents.push('(');
                        break;
                    case "[":
                        parents.push('[');
                        break;
                    case ")":
                        if (parents.pop() != '(')
                            throw new IllegalArgumentException("malformed class");
                        break;
                    case "]":
                        if (parents.pop() != '[')
                            throw new IllegalArgumentException("malformed class");
                        break;
                }
                buf.append(text[i++]);
            }
            while (!parents.isEmpty() && !text[i].equals("["));
        }
        return i;
    }

    private Pair<String, Integer> notBigger(String[] codeParts, int i) {
        StringBuilder out = new StringBuilder();
        Stack<Character> parents = new Stack<>();
        boolean going = true;
        parents.push('<');
        out.append(codeParts[i]);
        i++;
        if (codeParts[i].equals(">")) {
            i++;
            return new Pair<>("<>", i);
        }
        while (!parents.isEmpty() && going) {
            out.append(codeParts[i]);
            if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                i++;
            } else {
                return new Pair<>("", 0);
            }
            i = arrayEnclosure(codeParts, i, out);
            out.append(codeParts[i]);
            while (codeParts[i].equals(">")) {
                if (parents.pop() == '<') {
                    i++;
                    i = arrayEnclosure(codeParts, i, out);
                    out.append(codeParts[i]);
                } else {
                    return new Pair<>("", 0);
                }
            }
            i = arrayEnclosure(codeParts, i, out);
            switch (codeParts[i]) {
                case "<":
                    parents.push('<');
                    break;
                case ",":
                    break;
                default:
                    going = false;
            }
            i++;
        }
        if (going) {
            return new Pair<>(out.toString(), i);
        } else {
            return new Pair<>(out.toString(), 0);
        }
    }

    private int classValidation(String[] codeParts, int i, ClassComponent clazz) {
        if (codeParts[i].equals("extends")) {
            i++;
            StringBuilder extension = new StringBuilder();
            if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                extension.append(codeParts[i]);
                i++;
                //TODO syntax validation
                if (codeParts[i].equals("<")) {
                    Pair<String, Integer> bigger = notBigger(codeParts, i);
                    extension.append(bigger.getKey());
                    if (bigger.getValue() == 0) {
                        throw new IllegalArgumentException("Malformed extension " + extension);
                    }
                    i = bigger.getValue();
                }
            } else {
                throw new IllegalArgumentException("Malformed extension " + codeParts[i]);
            }
            clazz.setExtension(extension.toString());
        }
        if (codeParts[i].equals("implements")) {
            i++;
            boolean going = true;
            while (going) {
                StringBuilder implementation = new StringBuilder();
                if (codeParts[i].matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
                    implementation.append(codeParts[i]);
                    i++;
                    //TODO syntax validation
                    if (codeParts[i].equals("<")) {
                        Pair<String, Integer> bigger = notBigger(codeParts, i);
                        implementation.append(bigger.getKey());
                        if (bigger.getValue() == 0) {
                            throw new IllegalArgumentException("Malformed implementation " + implementation);
                        }
                        i = bigger.getValue();
                    }
                } else {
                    throw new IllegalArgumentException("Malformed implementation " + codeParts[i]);
                }
                clazz.getImplementations().add(String.valueOf(implementation));
                if (codeParts[i].equals(",")) {
                    i++;
                } else {
                    going = false;
                }
            }
        }
        int start = i + 1;
        i = parentValidator(codeParts, i);
        clazz.setTempContent(Arrays.copyOfRange(codeParts, start, i - 1));
        return i;
    }

    private int parentValidator(String[] codeParts, int i) {
        Stack<Character> parents = new Stack<>();
        if (codeParts[i].equals("{")) {
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
                        int bigger = notBigger(codeParts, i).getValue();
                        i = bigger > i ? bigger : i + 1;
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
        } else {
            throw new IllegalArgumentException("Missing {}");
        }
        return i;
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