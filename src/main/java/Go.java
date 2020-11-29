import com.sun.javafx.stage.EmbeddedWindow;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
//import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Go {

    public ImageView Go;

    public ImageView sound;
    public ImageView soundMute;

    private MediaPlayer backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/Woodcock Courtship.mp3").toString()));

    // public AnchorPane content;

    @FXML
    private void initialize(){
        setSoundClick();
        setGo();
    }


    private void setSoundClick() {
        sound.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                backgroundMusic.setVolume(0);
                soundMute.setVisible(true);
                sound.setVisible(false);
            }
        });
        soundMute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                backgroundMusic.setVolume(1);
                soundMute.setVisible(false);
                sound.setVisible(true);
            }
        });
    }

    @FXML
    private void setGo() {

        Go.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("hard");
                String resource = "/GameBoard.fxml";
                Node node=(Node) event.getSource();
                Stage stage=(Stage)node.getScene().getWindow();
                FXMLLoader hardBoard = new FXMLLoader(getClass().getResource(resource));

                try {
                    stage.setScene(new Scene((Pane)hardBoard.load(),660,550));
                } catch (IOException e) {
                    System.out.println("error");
                    System.out.println(e.getMessage()+"------");
                }
                stage.show();
            }
        });
    }



}
