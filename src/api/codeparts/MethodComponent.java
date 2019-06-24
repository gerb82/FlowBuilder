package api.codeparts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

// Represents a method component (System.out.println(text)) for example
public class MethodComponent extends Label implements ContentComponent {

    public MethodComponent() {
        super();
        textProperty().bind(method);
        setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
    }

    private SimpleStringProperty method = new SimpleStringProperty();

    public String getMethod() {
        return method.get();
    }

    public void setMethod(String method) {
        this.method.set(method);
    }
}
