package api.codeparts;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Represents a variable component
public class VariableComponent extends Rectangle implements ContentComponent {

    private String name;
    private String type;

    public VariableComponent(){
        setWidth(100);
        setHeight(100);
        setFill(Color.AQUAMARINE);
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
