package api.codeparts;

import api.display.HUDPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PackageComponent extends HUDPane<SplitPane>{

    private Pane files;
    private Pane packages;
    private SimpleStringProperty name = new SimpleStringProperty();

    public PackageComponent() {
        super(new SplitPane());
        self.setPrefHeight(1000);
        self.setPrefWidth(1000);
        packages = new Pane();
        files = new Pane();
        self.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
        self.getItems().addAll(packages, files);
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
