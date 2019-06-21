package api.codeparts;

import api.display.HUDPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

// Represents a class component
public class FileComponent extends HUDPane<ScrollPane>{

    private VBox imports;
    private Pane classes;
    private ClassComponent mainClass;
    private SimpleStringProperty name = new SimpleStringProperty("file");

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public FileComponent() {
        super(new ScrollPane());
        Pane inside = new Pane();
        self.setContent(inside);
        self.setPrefHeight(500);
        self.setPrefWidth(500);
        imports = new VBox();
        classes = new Pane();
        HUDPane<VBox> ports = new HUDPane<>(imports);
        ports.setName("imports");
        inside.getChildren().addAll(ports, classes);
        classes.setLayoutY(300);
        classes.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        nameProperty().bind(name);
        imports.setPadding(new Insets(10, 20, 10, 20));
        imports.setSpacing(10);
        imports.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
    }

    public ClassComponent getMainClass() {
        return mainClass;
    }

    public void setMainClass(ClassComponent mainClass) {
        this.mainClass = mainClass;
        classes.getChildren().add(mainClass);
    }

    public ObservableList<Node> getClasses() {
        return classes.getChildren();
    }

    public ObservableList<Node> getImports() {
        return imports.getChildren();
    }
}
