package api.codeparts;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Represents a variable component
public class VariableComponent extends Rectangle implements ContentComponent {

    private String name;
    private String type;
    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public VariableComponent(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
