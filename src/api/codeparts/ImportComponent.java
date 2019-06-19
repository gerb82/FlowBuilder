package api.codeparts;

import java.util.ArrayList;

// Represents an import component
public class ImportComponent {

    private String importComponent;
    private boolean isCompiled;
    private boolean isFullImport;

    public ImportComponent(String importComponent, boolean isCompiled, boolean isFullImport) {
        this.importComponent = importComponent;
        this.isCompiled = isCompiled;
        this.isFullImport = isFullImport;
    }

    public boolean isFullImport() {
        return isFullImport;
    }

    public void setFullImport(boolean fullImport) {
        isFullImport = fullImport;
    }

    public String getImportComponent() {
        return importComponent;
    }

    public void setImportComponent(String importComponent) {
        this.importComponent = importComponent;
    }

    public boolean isCompiled() {
        return isCompiled;
    }

    public void setCompiled(boolean compiled) {
        isCompiled = compiled;
    }
}
