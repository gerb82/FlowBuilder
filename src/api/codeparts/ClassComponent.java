package api.codeparts;

import api.display.HUDPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ClassComponent extends HUDPane<ScrollPane> implements ContentComponent{

    private Pane functions;
    private Pane staticFunk;
    private Pane variables;
    private String extension;
    private ArrayList<String> implementation = new ArrayList<>();
    private SimpleStringProperty name = new SimpleStringProperty();

    public ClassComponent() {
        super(new ScrollPane());
        functions = new Pane();
        staticFunk = new Pane();
        variables = new Pane();
        Pane inside = new Pane();
        HUDPane<ScrollPane> funkHUD = new HUDPane<>(new ScrollPane(functions){{setBackground(new Background(new BackgroundFill(Color.CYAN, null, null)));}});
        funkHUD.setName("functions");
        HUDPane<ScrollPane> staticFunkHUD = new HUDPane<>(new ScrollPane(staticFunk){{setBackground(new Background(new BackgroundFill(Color.BROWN, null, null)));}});
        staticFunkHUD.setName("static functions");
        HUDPane<ScrollPane> variablesHUD = new HUDPane<>(new ScrollPane(variables){{setBackground(new Background(new BackgroundFill(Color.OLIVE, null, null)));}});
        variablesHUD.setName("variables");
        inside.getChildren().addAll(funkHUD, staticFunkHUD, variablesHUD);
        staticFunkHUD.setLayoutX(100);
        variablesHUD.setLayoutY(100);
        self.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        self.setContent(inside);
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

    public ArrayList<String> getImplementation() {
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
}
