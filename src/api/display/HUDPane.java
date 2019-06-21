package api.display;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class HUDPane<Content extends Region> extends BorderPane {

    protected Content self;
    private Text name;

    public HUDPane(Content center) {
        super(center);
        backgroundProperty().bind(center.backgroundProperty());
        self = center;
        name = new Text();
        setTop(name);
        BorderPane.setAlignment(name, Pos.CENTER);
        setStyle("-fx-border-color : black; -fx-border-width : 3 3 ");
    }

    public void setName(String name){
        this.name.setText(name);
    }

    public void setNameColor(Color fill){
        this.name.setFill(fill);
    }

    public StringProperty nameProperty(){
        return name.textProperty();
    }
}
