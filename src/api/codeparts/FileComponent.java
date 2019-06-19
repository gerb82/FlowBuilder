package api.codeparts;

import java.util.ArrayList;

// Represents a class component
public class FileComponent {

    private ArrayList<ImportComponent> imports = new ArrayList<>();
    private ArrayList<ClassComponent> classes = new ArrayList<>();
    private ClassComponent mainClass;

    public ClassComponent getMainClass() {
        return mainClass;
    }

    public void setMainClass(ClassComponent mainClass) {
        this.mainClass = mainClass;
    }

    public ArrayList<ClassComponent> getClasses() {
        return classes;
    }

    public ArrayList<ImportComponent> getImports() {

        return imports;
    }
}
