package api.codeparts;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// Represents an import component
public class ImportComponent extends Label{

    private boolean isCompiled;
    private boolean isFullImport;

    public ImportComponent(String importComponent, boolean isCompiled, boolean isFullImport) {
        super();
        setText(importComponent);
        setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
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
        return getText();
    }

    public void setImportComponent(String importComponent) {
        setText(importComponent);
    }

    public boolean isCompiled() {
        return isCompiled;
    }

    public void setCompiled(boolean compiled) {
        isCompiled = compiled;
    }
}
