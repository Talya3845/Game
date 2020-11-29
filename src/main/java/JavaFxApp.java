import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

import java.awt.*;
//import java.lang.StackTraceElement;
//import java.lang.StackTraceElement.moduleVersion;
//import java.lang.String;

public class JavaFxApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        //String text="hello world"+" !!";
        String resource = "/Go.fxml";
//        Label label = new Label(text);

        Parent fxmlLoader =  FXMLLoader.load((getClass().getResource(resource)));
    //    StackPane stackPane = new StackPane();
  //      stackPane.getChildren().add(label);
        primaryStage.setScene(new Scene(fxmlLoader,500,250));
        primaryStage.show();

    }
}
