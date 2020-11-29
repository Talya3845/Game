import animatefx.animation.BounceIn;
import animatefx.animation.FadeOut;
import animatefx.animation.Flash;
import animatefx.animation.Tada;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class GameBoardEasy {

    public GridPane mapGrid;

    public ImageView exit;
    public Rectangle overB;

    public ImageView sound;
    public ImageView soundMute;

    public ImageView snack1;
    public ImageView snack2;
    public ImageView ladder1;
    public ImageView ladder2;

    public ImageView imagePlayer1;
    public ImageView imagePlayer2;
    public ImageView imagePlayer3;

    public ImageView dice11;
    public ImageView dice21;
    public ImageView dice31;
    public ImageView dice41;
    public ImageView dice51;
    public ImageView dice61;


    private Timeline timelineDice1 = new Timeline();

    public Player player1;
    public Player player2;
    public Player player3;

    private boolean sleepPlayer1;
    private int countSleep1=0;
    private boolean sleepPlayer2;
    private int countSleep2=0;
    private boolean sleepPlayer3;
    private int countSleep3=0;

    private MediaPlayer rollDice = new MediaPlayer(new Media(getClass().getResource("/Shake And Roll Dice.mp3").toString()));
    private MediaPlayer jumpPlayer = new MediaPlayer(new Media(getClass().getResource("/Jump-SoundBible.mp3").toString()));
    private MediaPlayer ladderPlayer = new MediaPlayer(new Media(getClass().getResource("/Ta Da.mp3").toString()));
    private MediaPlayer snackPlayer = new MediaPlayer(new Media(getClass().getResource("/Sad_Trombone.mp3").toString()));
    private MediaPlayer croud = new MediaPlayer(new Media(getClass().getResource("/Sports_Crowd-GoGo.mp3").toString()));
    private MediaPlayer backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/Woodcock Courtship.mp3").toString()));

    Random rand = new Random();

    private boolean gameStart=false;
    private int numCard=0;
    private int n = 0;
    private int userPlayer=0;
    private int numPlayer=1;
    private int players=3;

    @FXML
    private void initialize(){

        sleepPlayer1=false;
        sleepPlayer2=false;
        sleepPlayer3=false;

        player1 = new Player(imagePlayer1);
        player2 = new Player(imagePlayer2);
        player3 = new Player(imagePlayer3);


        new Flash(imagePlayer1).setDelay(Duration.millis(2300)).play();
        new Flash(imagePlayer2).setDelay(Duration.millis(2400)).play();
        new Flash(imagePlayer3).setDelay(Duration.millis(2500)).play();

        createBoard();

        setSoundClick();
        setPlayer();
        setTimeLineDice();
        setDice();
        setExit();

    }



    private void createBoard() {
        int rowNum = 5;
        int colNum =5;
        int startCount =21;

        int count = startCount ;
        for (int col = 0; col < colNum; col++) {
            for (int row = 0; row < rowNum; row++) {
                Random rand = new Random();
                int n = rand.nextInt(4);
                Rectangle rec = new Rectangle();


                String lableNum = String.valueOf( count);
                count = count - colNum;

                Label label = new Label(lableNum);

                rec.setWidth(85);
                rec.setHeight(84);
                if(row%2==0){
                    if (col%2==0){
                        rec.setFill(javafx.scene.paint.Color.valueOf("#ffffff"));
                        rec.setEffect(new InnerShadow());
                    } else {
                        rec.setFill(Paint.valueOf("#B9B9B9"));
                        rec.setEffect(new InnerShadow());
                    }

                } else {
                    if (col%2==0){
                        rec.setFill(Paint.valueOf("#B9B9B9"));
                        rec.setEffect(new InnerShadow());
                    } else {
                        rec.setFill(Paint.valueOf("#ffffff"));
                        rec.setEffect(new InnerShadow());
                    }

                }
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                GridPane.setRowIndex(label, row);
                GridPane.setColumnIndex(label,
                        col);
                GridPane.setHalignment(label, HPos.LEFT);
                GridPane.setValignment(label, VPos.TOP);
                GridPane.setMargin(label,new Insets(4,0,0,7));
                mapGrid.getChildren().add(mapGrid.getChildren().size(),label);
                mapGrid.getChildren().add(0,rec);

            }
            startCount++;
            count = startCount ;
        }
        insertSnaLadSup();
    }

    private void insertSnaLadSup() {
        //create 3 array with number for indexes and random between them.
        //add supprize.
        System.out.println(mapGrid.getRowCount()+" "+mapGrid.getColumnCount());
        snack1.setLayoutX(4*84+115);        //0-4
        snack1.setLayoutY(3*85+33);            //0-3
        System.out.println(snack1.getLayoutX()+ " "+ snack1.getLayoutY());

        snack2.setLayoutX(1*84+126);        //0-2
        snack2.setLayoutY(1*85+42);         //0-3
        System.out.println(snack2.getLayoutX()+ " "+ snack2.getLayoutY());

        ladder1.setLayoutX(0*84+121);       //0-4
        ladder1.setLayoutY(3*85+40);        //0-3
        System.out.println(ladder1.getLayoutX()+ " "+ ladder1.getLayoutY());

        ladder2.setLayoutX(3*84+135);       //0-4
        ladder2.setLayoutY(1*85+40);        //0-3
        System.out.println(ladder2.getLayoutX()+ " "+ ladder2.getLayoutY());

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
                backgroundMusic.setVolume(0.5);
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
                backgroundMusic.setAutoPlay(true);
                backgroundMusic.setVolume(0.5);
                backgroundMusic.setCycleCount(1000000);
                userPlayer=1;
                numPlayer=userPlayer;
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
                backgroundMusic.setAutoPlay(true);
                backgroundMusic.setVolume(0.5);
                backgroundMusic.setCycleCount(1000000);
                userPlayer=2;
                numPlayer=userPlayer;
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
                backgroundMusic.setAutoPlay(true);
                backgroundMusic.setVolume(0.5);
                backgroundMusic.setCycleCount(1000000);
                userPlayer=2;
                numPlayer=userPlayer;
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

    private void setTimeLineDice() {
        ObservableList<KeyFrame> keyFrames1 = timelineDice1.getKeyFrames();
        keyFrames1.add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                dice31.setDisable(true);

                new FadeOut(dice61).play();
                new BounceIn(dice41).play();

                rollDice.play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(150), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice41).play();
                new BounceIn(dice21).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice21).play();
                new BounceIn(dice51).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(350), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice51).play();
                new BounceIn(dice31).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(450), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice31).play();
                new BounceIn(dice11).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(550), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice11).play();
                new BounceIn(dice61).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(800), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice61).play();
                rollDice.stop();
                n = rand.nextInt(6);
                n = n + 1;
                System.out.println(n + dice11.getOpacity());

                if (n == 1)
                    new BounceIn(dice11).play();
                else if (n == 2)
                    new BounceIn(dice21).play();
                else if (n == 3)
                    new BounceIn(dice31).play();
                else if (n == 4)
                    new BounceIn(dice41).play();
                else if (n == 5)
                    new BounceIn(dice51).play();
                else if (n == 6)
                    new BounceIn(dice61).play();

                trans(player1);
            }

        }));

    }
    @FXML
    private void trans(Player player){
        PathTransition pathTransition=new PathTransition();
        pathTransition.setDuration(Duration.millis(2000));
        pathTransition.setNode(player.getImagePlayer());
        pathTransition.setCycleCount(1);
        //pathTransition.setAutoReverse(true);
        TranslateTransition translateTransition;
        System.out.println("before trans: x:"+player.getImagePlayer().getLayoutX()+" y:"+player.getImagePlayer().getLayoutY());
//        player.setCullom(3);
  //      player.setRow(7);
        Path path=new Path();


        MoveTo moveTo=new MoveTo(100,10);
        //ystem.out.println("x: "+moveTo.getX()+"moveTo");
        //MoveTo moveTo=new MoveTo(cellBoundsFrom.getMinX(),cellBoundsFrom.getMinY());
        LineTo lineTo=new LineTo(100,-85);
        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        pathTransition.setPath(path);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("after trans: x:"+player.getImagePlayer().getTranslateX()+" y:"+player.getImagePlayer().getTranslateY());
            //player.getImagePlayer().setLayoutX(player.getImagePlayer().getLayoutX()+player.getImagePlayer().getTranslateX());
                player.getImagePlayer().setLayoutX(player.getImagePlayer().getTranslateX());
                player.getImagePlayer().setLayoutY(6*85+33+player.getImagePlayer().getTranslateY());
                System.out.println("after trans: x:"+player.getImagePlayer().getLayoutX()+" y:"+player.getImagePlayer().getLayoutY());


            }
        });
    }
    private void setDice() {

        dice11.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                timelineDice1.play();
                //(new EventHandler<ActionEvent>()
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
