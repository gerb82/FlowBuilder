package api.codeparts;

import api.display.HUDPane;
import api.display.WorkPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

// Represents a function component
public class FunctionComponent extends HUDPane<WorkPane> implements Scannable{

    private VBox content = new VBox();
    private ArrayList<VariableComponent> parameters = new ArrayList<>();
    private ArrayList<String> throwing = new ArrayList<>();
    private String[] tempContent;
    private boolean electrical;
    private SimpleStringProperty name = new SimpleStringProperty();
    private String returnType;

    public ArrayList<String> getThrowing() {
        return throwing;
    }
    public FunctionComponent() {
        super(new WorkPane());
        nameProperty().bind(name);
        self.setBackground(new Background(new BackgroundFill(Color.PURPLE, null, null)));
        content.setSpacing(5);
        self.getChildren().add(content);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getStatic(){
        return electrical;
    }

    public void setStatic(boolean electrical){
        this.electrical = electrical;
    }

    public String[] getTempContent() {
        return tempContent;
    }

    public void setTempContent(String[] tempContent) {
        this.tempContent = tempContent;
    }

    public ObservableList<Node> getContent() {
        return content.getChildren();
    }

    public ArrayList<VariableComponent> getParameters() {
        return parameters;
    }
}
