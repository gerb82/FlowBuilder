package api.codeparts;

import api.display.HUDPane;
import api.display.WorkPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PackageComponent extends HUDPane<SplitPane>{

    private WorkPane files;
    private WorkPane packages;
    private SimpleStringProperty name = new SimpleStringProperty();

    public PackageComponent(boolean source) {
        super(new SplitPane(), !source);
        if(source) {
            self.setPrefHeight(USE_COMPUTED_SIZE);
            self.setPrefWidth(USE_COMPUTED_SIZE);
        } else {
            self.setPrefHeight(500);
            self.setPrefWidth(500);
        }
        packages = new WorkPane();
        files = new WorkPane();
        self.getItems().addAll(packages, files);
        nameProperty().bind(name);
        files.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        packages.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
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
