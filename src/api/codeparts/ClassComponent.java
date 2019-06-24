package api.codeparts;

import api.display.HUDPane;
import api.display.WorkPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ClassComponent extends HUDPane<WorkPane> implements Scannable{

    private VBox functions;
    private VBox staticFunk;
    private VBox variables;
    private VBox staticVariables;
    private String extension;
    private String[] tempContent;

    public String[] getTempContent() {
        return tempContent;
    }

    public void setTempContent(String[] tempContent) {
        this.tempContent = tempContent;
    }

    private boolean abs;

    public boolean isAbs() {
        return abs;
    }

    public void setAbs(boolean abs) {
        this.abs = abs;
    }

    private ArrayList<String> implementation = new ArrayList<>();
    private SimpleStringProperty name = new SimpleStringProperty();

    public ClassComponent() {
        super(new WorkPane());
        functions = new VBox();
        staticFunk = new VBox();
        variables = new VBox();
        staticVariables = new VBox();
        self.setPrefHeight(300);
        self.setPrefWidth(300);
        HUDPane<VBox> funkHUD = new HUDPane<>(functions);
        functions.setBackground(new Background(new BackgroundFill(Color.YELLOWGREEN, null, null)));
        funkHUD.setName("functions");
        functions.setSpacing(10);
        functions.setPadding(new Insets(10, 20, 20, 20));
        HUDPane<VBox> staticFunkHUD = new HUDPane<>(staticFunk);
        staticFunk.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, null, null)));
        staticFunkHUD.setName("static functions");
        staticFunk.setSpacing(10);
        staticFunk.setPadding(new Insets(10, 20, 20, 20));
        HUDPane<VBox> variablesHUD = new HUDPane<>(variables);
        variables.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
        variablesHUD.setName("variables");
        variables.setSpacing(10);
        variables.setPadding(new Insets(10, 20, 20, 20));
        HUDPane<VBox> staticVariablesHUD = new HUDPane<>(staticVariables);
        staticVariables.setBackground(new Background(new BackgroundFill(Color.DARKORANGE, null, null)));
        staticVariablesHUD.setName("static variables");
        staticVariables.setSpacing(10);
        staticVariables.setPadding(new Insets(10, 20, 20, 20));
        self.getChildren().addAll(funkHUD, staticFunkHUD, variablesHUD, staticVariablesHUD);
        self.setBackground(new Background(new BackgroundFill(Color.OLIVE, null, null)));
        nameProperty().bind(name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {

        return extension;
    }

    public ArrayList<String> getImplementations() {
        return implementation;
    }

    public ObservableList<Node> getFunctions() {
        return functions.getChildren();
    }

    public ObservableList<Node> getStaticFunctions() {
        return staticFunk.getChildren();
    }

    public ObservableList<Node> getVariables() {
        return variables.getChildren();
    }

    public ObservableList<Node> getStaticVariables() {
        return staticVariables.getChildren();
    }
}
