package api.display;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import java.util.Optional;

public class HUDPane<Content extends Region> extends BorderPane {

    protected Content self;
    private Text name;
    private static SimpleStringProperty current = new SimpleStringProperty();

    public HUDPane(Content center) {
        this(center, true);
    }

    public HUDPane(Content center, boolean moveAble){
        super(center);
        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
        self = center;
        name = new Text();
        setTop(name);
        BorderPane.setAlignment(name, Pos.CENTER);
        setStyle("-fx-border-color : gray; -fx-border-width : 2 2 ");
        Rectangle outClip = new Rectangle();
        setClip(outClip);
        layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outClip.setWidth(newValue.getWidth());
            outClip.setHeight(newValue.getHeight());
        });
        if(moveAble) {
            SimpleObjectProperty<Point2D> last = new SimpleObjectProperty<>();
            setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown()) {
                    last.set(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            });
            setOnMouseDragged(event -> {
                if (event.isPrimaryButtonDown()) {
                    setLayoutX(getLayoutX() + event.getSceneX() - last.get().getX());
                    setLayoutY(getLayoutY() + event.getSceneY() - last.get().getY());
                    last.set(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            });
            setOnMouseMoved(event -> {
                current.set(name.getText());
                event.consume();
            });
            setOnContextMenuRequested(event -> {
                Dialog<Point2D> resize = new Dialog<>();
                GridPane fields = new GridPane();
                fields.setVgap(20);
                fields.setHgap(40);
                resize.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                TextField width = new TextField(String.valueOf(String.valueOf((int)(self.getWidth()))));
                width.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        width.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });
                TextField height = new TextField(String.valueOf((int)(self.getHeight())));
                height.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        height.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });
                fields.addRow(0, new Label("Width:"), width);
                fields.addRow(1, new Label("Height:"), height);
                resize.getDialogPane().setContent(fields);
                resize.setHeaderText("Resize panel: " + name.getText());
                resize.setResultConverter(param -> {
                    if(param == ButtonType.OK){
                        return new Point2D(Integer.valueOf(width.getText()), Integer.valueOf(height.getText()));
                    }
                    return null;
                });
                Optional<Point2D> out = resize.showAndWait();
                out.ifPresent(point2D -> {
                    self.setPrefWidth(point2D.getX());
                    self.setPrefHeight(point2D.getY());
                });
                event.consume();
            });
        } else {
            Text description = new Text();
            description.textProperty().bind(current);
            setBottom(description);
            BorderPane.setAlignment(description, Pos.BOTTOM_RIGHT);
            BorderPane.setMargin(description, new Insets(10, 20, 10, 0));
        }
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
