package api.codeparts;

import api.display.HUDPane;
import api.display.WorkPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

// Represents a class component
public class FileComponent extends HUDPane<WorkPane>{

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
        super(new WorkPane());
        imports = new VBox();
        classes = new Pane();
        self.setPrefHeight(500);
        self.setPrefWidth(500);
        HUDPane<VBox> ports = new HUDPane<>(imports);
        HUDPane<Pane> school = new HUDPane<>(classes);
        ports.setName("imports");
        school.setName("classes");
        self.getChildren().addAll(ports, school);
        school.setLayoutY(300);
        classes.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        classes.setPrefHeight(500);
        classes.setPrefWidth(500);
        nameProperty().bind(name);
        imports.setSpacing(10);
        imports.setBackground(new Background(new BackgroundFill(Color.CRIMSON, null, null)));
        imports.setPadding(new Insets(10, 30, 30, 30));
        self.setBackground(new Background(new BackgroundFill(Color.PLUM, null, null)));
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
