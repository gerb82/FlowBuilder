package javaCode;

import api.format.Reader;
import javaCode.format.JavaRawFormat;
import javaCode.format.JavaReader;
import javafx.application.Application;
import javafx.scene.Scene;
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

        stage.setScene(new Scene(JavaRawFormat.src, 600, 400));
        stage.show();

    }
}