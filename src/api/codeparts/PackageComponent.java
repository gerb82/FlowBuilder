package api.codeparts;

import api.display.HUDPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

public class PackageComponent extends HUDPane<ScrollPane>{

    private Pane files;
    private Pane packages;
    private SimpleStringProperty name = new SimpleStringProperty();

    public PackageComponent() {
        super(new ScrollPane());
        SplitPane inside = new SplitPane();
        inside.setPrefHeight(1000);
        inside.setPrefWidth(1000);
        self.setContent(inside);
        packages = new Pane();
        files = new Pane();
        inside.getItems().addAll(packages, files);
        nameProperty().bind(name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableList<Node> getFiles() {
        return files.getChildren();
    }

    public ObservableList<Node> getPackages() {
        return packages.getChildren();
    }
}
