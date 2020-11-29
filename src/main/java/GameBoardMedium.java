import animatefx.animation.BounceIn;
import animatefx.animation.Flash;
import animatefx.animation.Tada;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class GameBoardMedium {

    public ImageView imagePlayer1;
    public ImageView imagePlayer2;
    public ImageView imagePlayer3;

    public Player player1;
    public Player player2;
    public Player player3;

    public Rectangle overB;

    boolean gameStart=false;
    boolean gameEnd=false;

    private int numPlayer=1;
    private int userPlayer=0;

    public ImageView exit;

    public ImageView sound;
    public ImageView soundMute;

    private MediaPlayer rollDice = new MediaPlayer(new Media(getClass().getResource("/Shake And Roll Dice.mp3").toString()));
    private MediaPlayer jumpPlayer = new MediaPlayer(new Media(getClass().getResource("/Jump-SoundBible.mp3").toString()));
    private MediaPlayer ladderPlayer = new MediaPlayer(new Media(getClass().getResource("/Ta Da.mp3").toString()));
    private MediaPlayer snackPlayer = new MediaPlayer(new Media(getClass().getResource("/Sad_Trombone.mp3").toString()));
    private MediaPlayer croud = new MediaPlayer(new Media(getClass().getResource("/Sports_Crowd-GoGo.mp3").toString()));
    private MediaPlayer backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/Woodcock Courtship.mp3").toString()));



    @FXML
    private void initialize(){

        player1 = new Player(imagePlayer1);
        player2 = new Player(imagePlayer2);
        player3 = new Player(imagePlayer3);

        new Flash(imagePlayer1).setDelay(Duration.millis(2300)).play();
        new Flash(imagePlayer2).setDelay(Duration.millis(2400)).play();
        new Flash(imagePlayer3).setDelay(Duration.millis(2500)).play();

        setSoundClick();
        setExit();
        setPlayer();
    }


    private void setSoundClick() {
        sound.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rollDice.setVolume(0);
                jumpPlayer.setVolume(0);
                ladderPlayer.setVolume(0);
                snackPlayer.setVolume(0);
                croud.setVolume(0);
                backgroundMusic.setVolume(0);
                soundMute.setVisible(true);
                sound.setVisible(false);
            }
        });
        soundMute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rollDice.setVolume(1);
                jumpPlayer.setVolume(1);
                ladderPlayer.setVolume(1);
                snackPlayer.setVolume(1);
                croud.setVolume(1);
                backgroundMusic.setVolume(1);
                soundMute.setVisible(false);
                sound.setVisible(true);
            }
        });
    }


    private void setPlayer() {

        imagePlayer1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if(!gameStart)
                    new BounceIn(imagePlayer1).play();
            }
        });
        imagePlayer2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!gameStart)
                    new BounceIn(imagePlayer2).play();
            }
        });
        imagePlayer3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!gameStart)
                    new BounceIn(imagePlayer3).play();
            }
        });


        //click
        imagePlayer1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                userPlayer=1;
                new Tada(imagePlayer1).play();
                overB.setVisible(false);
                gameStart=true;
                imagePlayer1.setDisable(true);
                imagePlayer2.setDisable(true);
                imagePlayer3.setDisable(true);
                imagePlayer1.setImage(new Image("../../../build/resources/main/iPlayer1.png"));

            }
        });
        imagePlayer2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                userPlayer=2;
                new Tada(imagePlayer2).play();
                overB.setVisible(false);
                gameStart=true;
                imagePlayer1.setDisable(true);
                imagePlayer2.setDisable(true);
                imagePlayer3.setDisable(true);
                imagePlayer2.setImage(new Image("../../../build/resources/main/iPlayer2.png"));

            }
        });
        imagePlayer3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                userPlayer=3;
                new Tada(imagePlayer3).play();
                overB.setVisible(false);
                gameStart=true;
                imagePlayer1.setDisable(true);
                imagePlayer2.setDisable(true);
                imagePlayer3.setDisable(true);
                imagePlayer3.setImage(new Image("../../../build/resources/main/iPlayer3.png"));

            }
        });

    }

    private void setExit() {
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

//                TextArea info=new TextArea("do you want exit?");
                ButtonType yesButtonType = new ButtonType("כן", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButtonType = new ButtonType("לא", ButtonBar.ButtonData.CANCEL_CLOSE);

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().getButtonTypes().add(yesButtonType);
                dialog.getDialogPane().getButtonTypes().add(noButtonType);
                dialog.getDialogPane().setContentText("המשחק ייגמר, אתה בטוח שאתה רוצה לצאת?");
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == yesButtonType) {

                    String resource = "/ChooseLevel.fxml";
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    FXMLLoader hardBoard = new FXMLLoader(getClass().getResource(resource));

                    try {
                        stage.setScene(new Scene((Pane) hardBoard.load(), 500, 250));
                    } catch (IOException e) {
                        System.out.println("error");
                    }
                    stage.show();                //(new EventHandler<ActionEvent>()

                }

            }
        });
    }
}
