package javaCode;

import api.display.NodeGestures;
import api.display.PannablePane;
import api.display.SceneGestures;
import api.format.Reader;
import javaCode.format.JavaRawFormat;
import javaCode.format.JavaReader;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
/* test two */
import java.io.File;
import java.io.IOException;
// test
public class Main extends Application {


    public static void main(String[] args) throws IOException {
        Reader reader = new JavaReader();
        reader.readFile(new File("C:\\Users\\User\\IdeaProjects\\FlowBuilder\\src\\javaCode\\Main.java"));
        launch(args);
    }

    @Override
    public void start(Stage stage) {

//        Pane pane = new Pane();
//        pane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
//        pane.setMaxWidth(500);
//        pane.setMaxHeight(500);
//        pane.setLayoutY(200);
//        pane.setLayoutX(200);
//        Group group = new Group();
//        pane.getChildren().add(group);
//
//        final Rectangle outputClip = new Rectangle();
//        pane.setClip(outputClip);
//
//        pane.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
//            outputClip.setWidth(newValue.getWidth());
//            outputClip.setHeight(newValue.getHeight());
//        });
//
//        // create canvas
//        PannablePane canvas = new PannablePane();
//
//        // we don't want the canvas on the top/left in this example => just
//        // translate it a bit
//        canvas.setTranslateX(100);
//        canvas.setTranslateY(100);
//
//        // create sample nodes which can be dragged
//        NodeGestures nodeGestures = new NodeGestures( canvas);
//
//        Label label1 = new Label("Draggable node 1");
//        label1.setTranslateX(10);
//        label1.setTranslateY(10);
//        label1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        label1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
//
//        Label label2 = new Label("Draggable node 2");
//        label2.setTranslateX(100);
//        label2.setTranslateY(100);
//        label2.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        label2.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
//
//        Label label3 = new Label("Draggable node 3");
//        label3.setTranslateX(200);
//        label3.setTranslateY(200);
//        label3.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        label3.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
//
//        Circle circle1 = new Circle( 300, 300, 50);
//        circle1.setStroke(Color.ORANGE);
//        circle1.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
//        circle1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        circle1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
//
//        Rectangle rect1 = new Rectangle(100,100);
//        rect1.setTranslateX(450);
//        rect1.setTranslateY(450);
//        rect1.setStroke(Color.BLUE);
//        rect1.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.5));
//        rect1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        rect1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
//
//        canvas.getChildren().addAll(label1, label2, label3, circle1, rect1);
//
//        group.getChildren().add(canvas);
//
//        // create scene which can be dragged and zoomed
//        Scene scene = new Scene(pane, 1024, 768);
//
//        SceneGestures sceneGestures = new SceneGestures(canvas);
//        group.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
//
//        stage.setScene(scene);
//        stage.show();
//
//        canvas.addGrid();
        stage.setScene(new Scene(JavaRawFormat.src, 600, 400));
        stage.show();

    }
}