package api.codeparts;

import api.display.HUDPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;

// Represents a function component
public class FunctionComponent extends HUDPane<ScrollPane> implements Scannable{

    private LinkedList<ContentComponent> content = new LinkedList<>();
    private ArrayList<VariableComponent> parameters = new ArrayList<>();
    private ArrayList<String> throwing = new ArrayList<>();
    private String[] tempContent;
    private boolean electrical;
    private SimpleStringProperty name = new SimpleStringProperty();
    private String returnType;

    public FunctionComponent() {
        super(new ScrollPane());
        nameProperty().bind(name);
        self.setBackground(new Background(new BackgroundFill(Color.PURPLE, null, null)));
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

    public LinkedList<ContentComponent> getContent() {
        return content;
    }

    public ArrayList<VariableComponent> getParameters() {
        return parameters;
    }
}
