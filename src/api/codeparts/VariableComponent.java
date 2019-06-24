package api.codeparts;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Represents a variable component
public class VariableComponent extends Label implements ContentComponent {

    private String name = null;
    private String type = null;
    private String val = null;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
        updateView();
    }

    public VariableComponent() {
        super();
        setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE, null, null)));
        setPadding(new Insets(5, 5, 5, 5));
    }

    private void updateView() {
        setText(String.format("name: %s%ntype: %s%nvalue: %s", name, type, val));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateView();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        updateView();
    }
}
