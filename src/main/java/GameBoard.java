
import animatefx.animation.*;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class GameBoard {

    Random rand = new Random();

    public ImageView exit;

    //image for players
    public ImageView imagePlayer1;
    public ImageView imagePlayer2;
    public ImageView imagePlayer3;
    public ImageView imagePlayer4;

    //image for choose player when get supprize.
    public ImageView choosePlayer1;
    public ImageView choosePlayer2;
    public ImageView choosePlayer3;
    public ImageView choosePlayer4;


    //players of game.
    public Player player1;
    public Player player2;
    public Player player3;
    public Player player4;

    //to block the game board.
    public Rectangle overB;
    //the image of the next player that gaming.
    public ImageView turn;
    //to say game over in end game
    public ImageView gameOver;
    //save the player that is winner.
    public ImageView winner;

    //all card in suprpize
    public ImageView card1;
    public ImageView card2;
    public ImageView card3;
    public ImageView card4;

    //first dice.
    public ImageView dice11;
    public ImageView dice21;
    public ImageView dice31;
    public ImageView dice41;
    public ImageView dice51;
    public ImageView dice61;
    //second dice
    public ImageView dice12;
    public ImageView dice22;
    public ImageView dice32;
    public ImageView dice42;
    public ImageView dice52;
    public ImageView dice62;

    //time line for play dice.
    private Timeline diceTimeLine = new Timeline();
    //time line for jump player on the board.
    private Timeline jumpTimeLine=new Timeline();
    //time line for display card in supprize.
    private Timeline cardTimeLine=new Timeline();

    //button for mute music or not.
    public ImageView sound;
    public ImageView soundMute;
    //media for roll dices.
    private MediaPlayer rollDice = new MediaPlayer(new Media(getClass().getResource("/ShakeAndRollDice.mp3").toString()));
    //media for player jump.
    private MediaPlayer jumpPlayer = new MediaPlayer(new Media(getClass().getResource("/Jump-SoundBible.mp3").toString()));
    //media for player who climbs a ladder
    private MediaPlayer ladderPlayer = new MediaPlayer(new Media(getClass().getResource("/TaDa.mp3").toString()));
    //media for player who down in a snake
    private MediaPlayer snackPlayer = new MediaPlayer(new Media(getClass().getResource("/Sad_Trombone.mp3").toString()));
    //media of afflause when a player wins.
    private MediaPlayer croud = new MediaPlayer(new Media(getClass().getResource("/Sports_Crowd-GoGo.mp3").toString()));
    //media for background.
    private MediaPlayer backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/rainforest.mp3").toString()));

    //animation for user player when that his turn.
    public Flash flash=new Flash();
    //slider for change speed.
    public Slider sliderSpeed;
//    DataOutputStream dataOutputStream;

    //boolean for know when start game.
    private boolean gameStart=false;
    //flag for know when end game.
    private boolean gameEnd=false;
    //flag for know if the user player press on the dice.
    private boolean noWait=false;
    //flag for know if player finish his turn.
    private boolean finishStep=true;

    //number of winner per player.
    public Label countWinnerLabel1;
    public Label countWinnerLabel2;
    public Label countWinnerLabel3;
    public Label countWinnerLabel4;

    public GridPane mapGrid;

    //speed of game.
    private int speed=400;
    private int afterSpeed=(speed/5)*4;

    //save last step.
    private Square copySquare;

    //variable for game.
    private int numCard=0;
    private int dice1 = 0;
    private int dice2 = 0;
    private int userPlayer=0;
    private int numPlayer=1;
    private int players=4;

    //space of board in the scene.
    private static final int ADDED_X_TO_GRID =121;
    private static final int ADDED_Y_TO_GRID =36;
    @FXML
    private void initialize() throws IOException {
        //create players
        player1 = new Player(imagePlayer1);
        player2 = new Player(imagePlayer2);
        player3 = new Player(imagePlayer3);
        player4 = new Player(imagePlayer4);

        //create square for last step.
        copySquare=new Square();
        //init animation for user player
        flash=new Flash(player1.getImagePlayer());

        new Flash(imagePlayer1).setDelay(Duration.millis(2300)).play();
        new Flash(imagePlayer2).setDelay(Duration.millis(2400)).play();
        new Flash(imagePlayer3).setDelay(Duration.millis(2500)).play();
        new Flash(imagePlayer4).setDelay(Duration.millis(2600)).play();

        //initFile();
        setSliderChange();
        setGameEnd();
        setSoundClick();
        setPlayer();
        setClickCard();
        setExit();
        setTimeLineDice();
        setDice();
        System.out.println(player1.getColumn());

    }

    //the function reminds to user that your turn
    private void waitForUser() {

        System.out.println(finishStep);
        int cycle=1;
        //check who is user.
        switch (userPlayer)
        {
            case 1:
                turn.setImage(player1.getImagePlayer().getImage());
                break;
            case 2:
                turn.setImage(player2.getImagePlayer().getImage());
                break;
            case 3:
                turn.setImage(player3.getImagePlayer().getImage());
                break;
            case 4:
                turn.setImage(player4.getImagePlayer().getImage());
                break;
            default:
                break;
        }
        //animation for wait user.
        flash=new Flash(turn);
        flash.setDelay(Duration.millis(2000)).setCycleCount(5).setDelay(Duration.millis(1000*cycle++)).play();

    }
    //the funnction setting slider for change speed's player.
    private void setSliderChange() {
        //the max speed.
        sliderSpeed.setMin(45);
        //listener to change of slider.
        sliderSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,Number oldValue, Number newValue) {
                //applying change.
                sliderChange();
            }
        });

    }
    private void sliderChange() {

        //update value from slider change.
        speed=(int)((sliderSpeed.getValue()/100)*1000);
        afterSpeed=(speed/5)*4;
        //stop in the old value speed.
        jumpTimeLine.stop();
        //next in the new value speed.
        if(gameStart) {
            switch (numPlayer - 1) {
                case 1:
                    jump(copySquare.getColumn(), copySquare.getRow(), player1);
                    break;
                case 2:
                    jump(copySquare.getColumn(), copySquare.getRow(), player2);
                    break;
                case 3:
                    jump(copySquare.getColumn(), copySquare.getRow(), player3);
                    break;
                case 0:
                    jump(copySquare.getColumn(), copySquare.getRow(), player4);
                    break;
            }
        }
    }

    //save data of count winner in file.
    private void initFile() throws IOException {
//        dataOutputStream =
//                new DataOutputStream(new FileOutputStream("fileWinner.txt"));
//        dataOutputStream.writeChars("player 1: 0\n");
//        dataOutputStream.writeChars("player 2: 0\n");
//        dataOutputStream.writeChars("player 3: 0\n");
//        dataOutputStream.writeChars("player 4: 0\n");

    }

    //init action for game over.
    private void setGameEnd() {
        gameOver.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //change variable for the next game.
                gameEnd=false;
                gameOver.setVisible(false);
                winner.setVisible(false);
                overB.setVisible(false);

                dice31.setDisable(false);
                dice62.setDisable(false);

                imagePlayer1.setOpacity(1);
                imagePlayer2.setOpacity(1);
                imagePlayer3.setOpacity(1);
                imagePlayer4.setOpacity(1);

                imagePlayer1.setVisible(true);
                imagePlayer2.setVisible(true);
                imagePlayer3.setVisible(true);
                imagePlayer4.setVisible(true);

                //to place players in start board.
                imagePlayer4.setLayoutX(6);
                imagePlayer4.setLayoutY(446);

                imagePlayer1.setLayoutX(35);
                imagePlayer1.setLayoutY(446);

                imagePlayer2.setLayoutX(60);
                imagePlayer2.setLayoutY(446);

                imagePlayer3.setLayoutX(85);
                imagePlayer3.setLayoutY(446);

                player1.setColumn(0);
                player1.setRow(9);
                player1.setSleep(false);
                player1.setCountSleep(0);

                player2.setColumn(0);
                player2.setRow(9);
                player2.setSleep(false);
                player2.setCountSleep(0);

                player3.setColumn(0);
                player3.setRow(9);
                player3.setSleep(false);
                player3.setCountSleep(0);

                player4.setColumn(0);
                player4.setRow(9);
                player4.setSleep(false);
                player4.setCountSleep(0);

                //update player winner
                countWinnerLabel1.setText(String.valueOf(player1.getCountWinner()));
                countWinnerLabel2.setText(String.valueOf(player2.getCountWinner()));
                countWinnerLabel3.setText(String.valueOf(player3.getCountWinner()));
                countWinnerLabel4.setText(String.valueOf(player4.getCountWinner()));

                //reset the player choose
                choosePlayer1.setVisible(false);
                choosePlayer2.setVisible(false);
                choosePlayer3.setVisible(false);
                choosePlayer4.setVisible(false);

                //next game
                if(numPlayer!=userPlayer)
                    diceTimeLine.play();
            }
        });
    }

    //change volume to music 0/1
    private void setSoundClick() {
        sound.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //less volume for all music
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
                //more volume for all music
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

    private void setClickCard() {
        //when user player get supprize, need to press on the card
        card1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { clickCard1(); }
        });
        card2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clickCard2();
                }
        });
        card3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clickCard3();
            }
        });
        card4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               clickCard4();
            }
        });
    }

    //roll the dice one more
    private void clickCard1() {
        //back to game
        overB.setVisible(false);
        //unVisible card.
        card1.setVisible(false);

        //roll again
        if(numPlayer>1)
            numPlayer--;
        else
            numPlayer=players;
        finishStep=true;
        if(finishStep&&(numPlayer!=userPlayer))
        {
            diceTimeLine.play();
            System.out.println("click card 1, auto play");
        }
    }

    //wait turn
    private void clickCard2() {

        overB.setVisible(false);
        //check which player need to wait.
        switch(numPlayer-1)
        {
            case 1:
                //display the player wait
                choosePlayer1.setVisible(true);
                //symbol of the player wait
                player1.setSleep(true);
                //when player waiting more than once.
                player1.setCountSleep(player1.getCountSleep()+1);
                System.out.println("sleep player 1");
                break;
            case 2:
                //display the player wait
                choosePlayer2.setVisible(true);
                //symbol of the player wait
                player2.setSleep(true);
                //when player waiting more than once.
                player2.setCountSleep(player2.getCountSleep()+1);
                System.out.println("sleep player 2");
                break;
            case 3:
                //display the player wait
                choosePlayer3.setVisible(true);
                //symbol of the player wait
                player3.setSleep(true);
                //when player waiting more than once.
                player3.setCountSleep(player3.getCountSleep()+1);
                System.out.println("sleep player 3");
                break;
            case 0:         // player4
                //display the player wait
                choosePlayer4.setVisible(true);
                //symbol of the player wait
                player4.setSleep(true);
                //when player waiting more than once.
                player4.setCountSleep(player4.getCountSleep()+1);
                System.out.println("sleep player 4");
                break;
            default:
                break;
        }
        //reset cards
        card1.setVisible(false);
        card2.setVisible(false);
        card3.setVisible(false);
        card4.setVisible(false);
        finishStep=true;
        //check if the next player is computer
        if(nextPlayer()!=userPlayer&&finishStep) {
            //auto play in the next player.
            diceTimeLine.play();
            System.out.println("click card 2, auto play");
        }
        //or user
        else if(nextPlayer()==userPlayer)
        {
            //update the user that  his turn arrive.
            waitForUser();
        }

    }

    //choose player that wait turn
    private void clickCard3() {
        card1.setVisible(false);
        card2.setVisible(false);
        card3.setVisible(false);
        card4.setVisible(false);

        //check if the player is computer
        if(numPlayer-1!=userPlayer&&!(numPlayer-1==0&&userPlayer==players)) {
            //choose for computer whitch player to delay
            switch (checkMax()) {
                case 1:
                    //symbol of the player wait
                    player1.setSleep(true);
                    //when player waiting more than once.
                    player1.setCountSleep(player1.getCountSleep()+1);
                    //display the player wait
                    choosePlayer1.setVisible(true);
                    break;
                case 2:
                    //symbol of the player wait
                    player2.setSleep(true);
                    //when player waiting more than once.
                    player2.setCountSleep(player2.getCountSleep()+1);
                    //display the player wait
                    choosePlayer2.setVisible(true);
                    break;
                case 3:
                    //symbol of the player wait
                    player3.setSleep(true);
                    //when player waiting more than once.
                    player3.setCountSleep(player3.getCountSleep()+1);
                    //display the player wait
                    choosePlayer3.setVisible(true);
                    break;
                case 4:
                    //symbol of the player wait
                    player4.setSleep(true);
                    //when player waiting more than once.
                    player4.setCountSleep(player4.getCountSleep()+1);
                    //display the player wait
                    choosePlayer4.setVisible(true);
                    break;
                default:
                    break;
            }
        overB.setVisible(false);
        }
        //the player is user and display for choose whitch player to delay.
        else {
            if(numPlayer!=1) {
                choosePlayer4.setVisible(true);
            }if(numPlayer!=2) {
                choosePlayer1.setVisible(true);
            }if(numPlayer!=3) {
                choosePlayer2.setVisible(true);
            }if(numPlayer!=4) {
                choosePlayer3.setVisible(true);
            }
            new Flash(choosePlayer1).setDelay(Duration.millis(2300)).play();
            new Flash(choosePlayer2).setDelay(Duration.millis(2400)).play();
            new Flash(choosePlayer3).setDelay(Duration.millis(2500)).play();
            new Flash(choosePlayer4).setDelay(Duration.millis(2600)).play();
            setClickChoosePlayer();
        }
    }
    //check player that more advantage to delay.
    private int checkMax() {
        //need change algorithm.
        int max=0;

        if(player1.getRow()>player2.getRow()) {
            if (player1.getRow() < player3.getRow()) {
                if (player1.getRow() < player4.getRow())
                    max = 1;
                else if (player1.getRow() == player4.getRow()) {
                    if (player1.getColumn() > player4.getColumn())
                        max = 1;
                    else
                        max = 4;
                }
                else
                    max=4;
            }
            else if(player3.getRow()<player4.getRow())
            {
                max=3;
            }
            else if(player1.getRow()==player3.getRow())
            {
                if(player1.getColumn()<player3.getColumn())
                    max=1;
                else
                    max=3;
            }
        }
        else if(player2.getRow()<player3.getRow())
        {
            if(player2.getRow()<player4.getRow())
                max=2;
            else if(player2.getRow()==player4.getRow())
            {
                if(player2.getColumn()>player4.getColumn())
                    max=2;
                else
                    max=4;
            }
            else
                max=4;
        }
        else if(player3.getRow()<player4.getRow())
            max=3;
        else
            max=4;
        if(max==numPlayer-1||(numPlayer-1==0&&max==4)) {
            max--;
            if(max==0)
                max=4;
        }
        return max;
    }


    private void clickCard4() {
        //unvisible the card
        card1.setVisible(false);
        card2.setVisible(false);
        card3.setVisible(false);
        card4.setVisible(false);

        //if the player is computer
        if(numPlayer-1!=userPlayer&&!(numPlayer-1==0&&userPlayer==4)) {
            overB.setVisible(false);
            switch (numPlayer) {        //change player before me.
                case 1:
                    changeLess3(player3);
                    break;
                case 2:
                    changeLess3(player4);
                    break;
                case 3:
                    changeLess3(player1);
                    break;
                case 4:
                    changeLess3(player2);
                    break;

            }
        }
        else {
            if((player1.getRow()!=9||player1.getColumn()>3)&&numPlayer!=2) { //if player1 is not player now and he does more than 3 step on board.
            choosePlayer1.setVisible(true);
            new Flash(choosePlayer1).setDelay(Duration.millis(2300)).play();
            }
            if((player2.getRow()!=9||player2.getColumn()>3)&&numPlayer!=3) { //if player2 is not player now and he does more than 3 step on board.
                choosePlayer2.setVisible(true);
                new Flash(choosePlayer2).setDelay(Duration.millis(2400)).play();
            }if((player3.getRow()!=9||player3.getColumn()>3)&&numPlayer!=4) { //if player3 is not player now and he does more than 3 step on board.
                choosePlayer3.setVisible(true);
                new Flash(choosePlayer3).setDelay(Duration.millis(2500)).play();
            }if((player4.getRow()!=9||player4.getColumn()>3)&&numPlayer!=1) { //if player4 is not player now and he does more than 3 step on board.
                choosePlayer4.setVisible(true);
                new Flash(choosePlayer4).setDelay(Duration.millis(2600)).play();
            }
            //if the player is user and less 3 players not start go in board, the turn passes to the next player.
            if (((numPlayer - 1 == 0 && userPlayer == 4) || numPlayer - 1 == userPlayer) && player1.getColumn() == 0 && player2.getColumn() == 0 && player3.getColumn() == 0) {
                overB.setVisible(false);
                if(numPlayer!=userPlayer||isSleep(userPlayer))
                {
                    diceTimeLine.play();
                    System.out.println("click card 4, auto play");
                }
            }else if (((numPlayer - 1 == 0 && userPlayer == 4) || numPlayer - 1 == userPlayer) && player1.getColumn() == 0 && player2.getColumn() == 0 && player4.getColumn() == 0) {
                overB.setVisible(false);
                if(numPlayer!=userPlayer||isSleep(userPlayer))
                {    diceTimeLine.play();
                    System.out.println("click card 4, auto play");
                }
            }else if (((numPlayer - 1 == 0 && userPlayer == 4) || numPlayer - 1 == userPlayer) && player4.getColumn() == 0 && player1.getColumn() == 0 && player3.getColumn() == 0) {
                overB.setVisible(false);
                if(numPlayer!=userPlayer||isSleep(userPlayer))
                {   diceTimeLine.play();
                    System.out.println("click card 4, auto play");
                }
            }else if (((numPlayer - 1 == 0 && userPlayer == 4) || numPlayer - 1 == userPlayer) && player2.getColumn() == 0 && player3.getColumn() == 0 && player4.getColumn() == 0) {
                overB.setVisible(false);
                if(numPlayer!=userPlayer||isSleep(userPlayer))
                {
                    diceTimeLine.play();
                    System.out.println("click card 4, auto play");
                }
            }
            else
            {
                waitForUser();
            }
            //give to user to choose player.
            setClickChoosePlayer();
        }
    }

    private void setClickChoosePlayer() {

        choosePlayer1.setOnMouseClicked(new EventHandler<MouseEvent> (){
                @Override
                public void handle(MouseEvent event1){
                    overB.setVisible(false);
                    if(numCard==3) {
                        player1.setSleep(true);
                        player1.setCountSleep(player1.getCountSleep()+1);
                        }
                    else if(numCard==4)
                    {
                        changeLess3(player1);
                    }
                    //display all the sleep player.
                    if(!player1.getSleep()) {
                        choosePlayer1.setVisible(false);
                    }if(!player2.getSleep()) {
                        choosePlayer2.setVisible(false);
                    }if(!player3.getSleep()) {
                        choosePlayer3.setVisible(false);
                    }if(!player4.getSleep()) {
                        choosePlayer4.setVisible(false);
                    }
                    //go to next player, auto if the player is computer, wait if its user.
                    if(numPlayer!=userPlayer||isSleep(userPlayer))
                    {
                        diceTimeLine.play();
                        System.out.println("choose 1, auto play");
                    }
                    else
                        waitForUser();
                }
            }
        );
        choosePlayer2.setOnMouseClicked(new EventHandler<MouseEvent> (){
            @Override
            public void handle(MouseEvent event1){
                overB.setVisible(false);
                if(numCard==3) {
                    player2.setSleep(true);
                    player2.setCountSleep(player2.getCountSleep()+1);
                }
                else if(numCard==4)
                {
                    changeLess3(player2);
                }
                //display all the sleep player.
                if(!player1.getSleep()) {
                    choosePlayer1.setVisible(false);
                }if(!player2.getSleep()) {
                    choosePlayer2.setVisible(false);
                }if(!player3.getSleep()) {
                    choosePlayer3.setVisible(false);
                }if(!player4.getSleep()) {
                    choosePlayer4.setVisible(false);
                }
                //go to next player, auto if the player is computer, wait if its user.
                if(numPlayer!=userPlayer||isSleep(userPlayer))
                {    diceTimeLine.play();
                    System.out.println("choose 2, auto play");
                }
                else
                    waitForUser();
            }
        });
        choosePlayer3.setOnMouseClicked(new EventHandler<MouseEvent> (){
            @Override
            public void handle(MouseEvent event1){
                overB.setVisible(false);
                if(numCard==3) {
                    player3.setSleep(true);
                    player3.setCountSleep(player3.getCountSleep()+1);
                }
                else if(numCard==4)
                {
                    changeLess3(player3);
                }
                //display all the sleep player.
                if(!player1.getSleep()) {
                    choosePlayer1.setVisible(false);
                }if(!player2.getSleep()) {
                    choosePlayer2.setVisible(false);
                }if(!player3.getSleep()) {
                    choosePlayer3.setVisible(false);
                }if(!player4.getSleep()) {
                    choosePlayer4.setVisible(false);
                }
                //go to next player, auto if the player is computer, wait if its user.
                if(numPlayer!=userPlayer||isSleep(userPlayer))
                {   diceTimeLine.play();
                    System.out.println("choose 3, auto play");
                }
                else
                    waitForUser();
            }
        });
        choosePlayer4.setOnMouseClicked(new EventHandler<MouseEvent> (){
            @Override
            public void handle(MouseEvent event1){
                overB.setVisible(false);
                if(numCard==3) {
                    player4.setSleep(true);
                    player4.setCountSleep(player4.getCountSleep()+1);
                }
                else if(numCard==4)
                {
                    changeLess3(player4);
                }
                //display all the sleep player.
                if(!player1.getSleep()) {
                    choosePlayer1.setVisible(false);
                }
                if(!player2.getSleep()) {
                    choosePlayer2.setVisible(false);
                }if(!player3.getSleep()) {
                    choosePlayer3.setVisible(false);
                }if(!player4.getSleep()) {
                    choosePlayer4.setVisible(false);
                }
                //go to next player, auto if the player is computer, wait if its user.
                if(numPlayer!=userPlayer||isSleep(userPlayer)) {
                    diceTimeLine.play();
                    System.out.println("choose 4, auto play");
                }
                else
                    waitForUser();
            }
        });
    }

    //returns a player 3 steps back
    private void changeLess3(Player player) {
        if(player.getColumn()>2)
            player.setColumn(player.getColumn()-3);
        else
        {
            player.setRow(player.getRow()+1);
            player.setColumn(10-(3-player.getColumn()));

        }
        player.getImagePlayer().setLayoutX(mapGrid.getCellBounds(player.getColumn(),player.getRow()).getMinX()+ ADDED_X_TO_GRID);
        player.getImagePlayer().setLayoutY(mapGrid.getCellBounds(player.getColumn(),player.getRow()).getMinY()+36);

        //check(player);
    }


    private void setPlayer() {
        //in start game, have animation to player for to remind to user, choose player.
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
        imagePlayer4.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!gameStart)
                    new BounceIn(imagePlayer4).play();
            }
        });

        //click
        imagePlayer1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //restart to game.
                backgroundMusic.play();
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
                imagePlayer4.setDisable(true);
                imagePlayer1.setImage(new Image("iPlayer1.png"));
                turn.setImage(imagePlayer1.getImage());

            }
        });
        imagePlayer2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //restart to game.
                backgroundMusic.play();
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
                imagePlayer4.setDisable(true);
                imagePlayer2.setImage(new Image("iPlayer2.png"));
                turn.setImage(imagePlayer2.getImage());

            }
        });
        imagePlayer3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //restart to game.
                backgroundMusic.play();
                backgroundMusic.setVolume(0.5);
                backgroundMusic.setCycleCount(1000000);
                userPlayer=3;
                numPlayer=userPlayer;
                new Tada(imagePlayer3).play();
                overB.setVisible(false);
                gameStart=true;
                imagePlayer1.setDisable(true);
                imagePlayer2.setDisable(true);
                imagePlayer3.setDisable(true);
                imagePlayer4.setDisable(true);
                imagePlayer3.setImage(new Image("iPlayer3.png"));
                turn.setImage(imagePlayer3.getImage());

            }
        });
        imagePlayer4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //restart to game.
                backgroundMusic.play();
                backgroundMusic.setVolume(0.5);
                backgroundMusic.setCycleCount(1000000);
                userPlayer=4;
                numPlayer=userPlayer;
                new Tada(imagePlayer4).play();
                overB.setVisible(false);
                gameStart=true;
                imagePlayer1.setDisable(true);
                imagePlayer2.setDisable(true);
                imagePlayer3.setDisable(true);
                imagePlayer4.setDisable(true);
                imagePlayer4.setImage(new Image("iPlayer4.png"));
                turn.setImage(imagePlayer4.getImage());

            }
        });
    }

    @FXML
    private void play() {

        // choose the player to move
        switch(numPlayer) {
            case 1:
                //check if the player is sleep
                if(!player1.getSleep()) {
                    finishStep=false;
                    imagePlayer1.setOpacity(0);
                    player1.getImagePlayer().setOpacity(0);
                    turn.setImage(player1.getImagePlayer().getImage());
                    move(player1, dice1 + dice2);
                }
                else {                          //the player wait, next to other player.
                    dice31.setDisable(false);
                    dice62.setDisable(false);

                    System.out.println("sleep 1");
                    if (numPlayer == players)
                        numPlayer = 1;
                    else
                        numPlayer++;
                    player1.setCountSleep(player1.getCountSleep()-1);
                    if(player1.getCountSleep()==0) {
                        player1.setSleep(false);
                        choosePlayer1.setVisible(false);
                    }
                    play();
                    return;
                }
                break;
            case 2:
                //check if the player is sleep
                if(!player2.getSleep())
                {
                    finishStep=false;
                    imagePlayer2.setOpacity(0);
                    player2.getImagePlayer().setOpacity(0);
                    turn.setImage(player2.getImagePlayer().getImage());
                    move(player2,dice1+dice2);
                }
                else {                          //the player wait, next to other player.
                    dice31.setDisable(false);
                    dice62.setDisable(false);

                    System.out.println("sleep 2");
                    if (numPlayer == players)
                        numPlayer = 1;
                    else
                        numPlayer++;
                    player2.setCountSleep(player2.getCountSleep()-1);
                    if(player2.getCountSleep()==0) {
                        player2.setSleep(false);
                        choosePlayer2.setVisible(false);
                    }
                    play();
                    return;
                }
                break;
            case 3:
                //check if the player is sleep
                if(!player3.getSleep()){
                    finishStep=false;
//                    mapGrid.getChildren().remove(player3.getImagePlayer());
                    imagePlayer3.setOpacity(0);
                    player3.getImagePlayer().setOpacity(0);
                    turn.setImage(player3.getImagePlayer().getImage());
                    move(player3,dice1+dice2);
                }
                else {                          //the player wait, next to other player.
                    dice31.setDisable(false);
                    dice62.setDisable(false);

                    System.out.println("sleep 3");
                    if (numPlayer == players)
                        numPlayer = 1;
                    else
                        numPlayer++;
                    player3.setCountSleep(player3.getCountSleep()-1);
                    if(player3.getCountSleep()==0) {
                        player3.setSleep(false);
                        choosePlayer3.setVisible(false);
                    }
                    play();
                    return;
                }break;
            case 4:
                //check if the player is sleep
                if(!player4.getSleep()){
                    finishStep=false;
//                    mapGrid.getChildren().remove(player4.getImagePlayer());
                    imagePlayer4.setOpacity(0);
                    player4.getImagePlayer().setOpacity(0);
                    turn.setImage(player4.getImagePlayer().getImage());
                    move(player4,dice1+dice2);
                }
                else {                          //the player wait, next to other player.
                    dice31.setDisable(false);
                    dice62.setDisable(false);

                    System.out.println("sleep 4");
                    if (numPlayer == players)
                        numPlayer = 1;
                    else
                        numPlayer++;
                    player4.setCountSleep(player4.getCountSleep()-1);
                    if(player4.getCountSleep()==0) {
                        player4.setSleep(false);
                        choosePlayer4.setVisible(false);
                    }
                    play();
                    return;
                }break;
            default:
                break;

        }
        System.out.println(numPlayer+"--play");

        //to the next player


        if(numPlayer>=players)
                numPlayer=1;
            else
                numPlayer++;
    }
    @FXML
    public void move(Player player, int dice) {             //change the place of the player acoording to dice.
        System.out.println(player.getColumn() + " move---");
        //  if ((player.getX()>0)&&player.getX()<10&&player.getX()==0) {
        int x=player.getColumn();
        int y=player.getRow();

        if(dice>=10&&player.getColumn()==0&&player.getRow()==9) {
            if (dice == 10) {
                player.setColumn(9);
            }
            else {
                player.setColumn(dice-11);
                player.setRow(8);
            }
        }
        else if ((player.getColumn() + dice) > 9) {
            if (player.getRow() == 0||
                    (player.getRow()==1&&
                            ((player.getColumn()==9&&(dice==11||dice==12))||(player.getColumn()==8&&dice==12))))
            {
                if(player.getColumn()>8)
                    gameEnd=true;
                gameEnd=true;
                System.out.println("END");
                if(player.getRow()==0)
                {
                    dice2=10-x;
                    dice1=1;
                }
                player.setColumn(9);

                player.setRow(0);

            }
            else {
                player.setRow(player.getRow() - 1);
                if(dice==11&&player.getColumn()==9)
                {
                    player.setRow(player.getRow()-1);
                    player.setColumn(dice-10-(9-player.getColumn())-1);
                }
                else if(dice==12&&player.getColumn()>=8)
                {  player.setRow(player.getRow()-1);
                    player.setColumn(dice-10-(9-player.getColumn())-1);
                }
                else
                    player.setColumn(dice - (9 - player.getColumn())-1);
            }
        }
        else
            if(player.getRow()==9&&player.getColumn()==0)
                player.setColumn(player.getColumn() + dice-1);
            else
                player.setColumn(player.getColumn() + dice);
            jump(x,y,player);
        //checkWithOutSupp(player);       //check snack or ladder
    }

    //animation of jump from square to square and pass in all the square between.
    private void jump(int x, int y, Player player) {
        //create time line for new jump
        jumpTimeLine=new Timeline();
        ObservableList<KeyFrame> keyFrames =jumpTimeLine.getKeyFrames();

        int line=0;

        //when the player stay in the same line.
        if(y==player.getRow())
        {
            //System.out.println(y+" "+player.getX());
            int i;
            if(x==0&&y==9) {
                i = x;
            }
            else {
                i = x + 1;
            }
            int count=1;
            for(i=i; i<=player.getColumn(); i++)
            {

                int copyI = i;

                keyFrames.add(new KeyFrame(Duration.millis(speed*count), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        dice31.setDisable(true);
                        dice62.setDisable(true);
                        jumpPlayer.play();

                        //save current place for timeLine when change speed.
                        copySquare.setColumn(copyI);
                        copySquare.setRow(y);

                        player.getImagePlayer().setOpacity(0);
                        player.getImagePlayer().setLayoutX(mapGrid.getCellBounds(copyI,player.getRow()).getMinX()+ ADDED_X_TO_GRID);
                        player.getImagePlayer().setLayoutY(mapGrid.getCellBounds(copyI,player.getRow()).getMinY()+36);

                        new JackInTheBox(player.getImagePlayer()).play();
                    }
                }));
                keyFrames.add(new KeyFrame(Duration.millis(speed*count++ + afterSpeed), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(copyI < player.getColumn()||(copyI==player.getColumn()-1&&y==9)) {
                            jumpPlayer.stop();
                            System.out.println("zoom out 4");
                            new ZoomOut(player.getImagePlayer()).play();
                        }
                        else {
                            Bounds cellBounds = mapGrid.getCellBounds(player.getColumn(), player.getRow());
                            player.getImagePlayer().setOpacity(0);
                            player.getImagePlayer().setLayoutX(cellBounds.getMinX()+ ADDED_X_TO_GRID);
                            player.getImagePlayer().setLayoutY(cellBounds.getMinY()+ ADDED_Y_TO_GRID);
                            player.getImagePlayer().setOpacity(1);
                            jumpPlayer.stop();
                        }
                    }
                }));

            }
        }
        //The player jumps to more than one row in board.
        else{
            line=0;
            for (int j = y; j>=player.getRow(); j--)
            {
                int countLast=10-x;
                int count = 1;
                System.out.println(y+" "+player.getRow());
                //jump in the first line
                if(j==y) {
                    int i;
                    if(x==0&&y==9) {
                        i = x;
                    countLast=countLast+2;
                    }
                    else
                        i=x+1;
                    for (i=i; i <= 9; i++) {
                        final int copyI=i;
                        final int copyJ=j;
                        keyFrames.add(new KeyFrame(Duration.millis(speed * count), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                dice31.setDisable(true);
                                dice62.setDisable(true);
                                jumpPlayer.play();

                                //save current place for timeLine when change speed.
                                copySquare.setColumn(copyI);
                                copySquare.setRow(copyJ);

                                player.getImagePlayer().setOpacity(0);
                                player.getImagePlayer().setLayoutX(mapGrid.getCellBounds(copyI,copyJ).getMinX()+ ADDED_X_TO_GRID);
                                player.getImagePlayer().setLayoutY(mapGrid.getCellBounds(copyI,copyJ).getMinY()+ ADDED_Y_TO_GRID);

                                System.out.println("first line. x: "+ player.getImagePlayer().getLayoutX()+" "+ player.getColumn()+" y:"+player.getImagePlayer().getLayoutY()+" "+player.getRow());
                                new JackInTheBox(player.getImagePlayer()).play();

                            }
                        }));
                        keyFrames.add(new KeyFrame(Duration.millis(speed * count++ + afterSpeed), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                    jumpPlayer.stop();
                                     System.out.println("zoom out 3");
                                    new ZoomOut(player.getImagePlayer()).play();
                            }
                        }));
                    }
                }
                //jump in the last line
                else if(j==player.getRow())
                {
                    if(x==0&&y==9) {
                        countLast++;
                    }
                    //the player jump 2 line in board
                    if(y-2==player.getRow()) {          //when the player jump 2 line.
                        countLast=countLast+10;         //time for time line of jump
                    }
                    for(int i = 0; i<=player.getColumn(); i++)
                    {
//                        if(y-2==player.getRow()) {
//                            System.out.println("yes");
//                        }
                        int copyI = i;      //for using in i in the timeline.
                        //image in
                        keyFrames.add(new KeyFrame(Duration.millis(speed*(countLast)), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                dice31.setDisable(true);
                                dice62.setDisable(true);
                                //save current place for timeLine when change speed.
                                copySquare.setColumn(copyI);
                                copySquare.setRow(player.getRow());
                                //change the layout of image
                                player.getImagePlayer().setOpacity(0);
                                player.getImagePlayer().setLayoutX(mapGrid.getCellBounds(copyI,player.getRow()).getMinX()+ ADDED_X_TO_GRID);
                                player.getImagePlayer().setLayoutY(mapGrid.getCellBounds(copyI,player.getRow()).getMinY()+ ADDED_Y_TO_GRID);

                                //sound of jump
                                jumpPlayer.play();
                                //animation of jump
                                new JackInTheBox(player.getImagePlayer()).play();
                            }
                        }));
                        //image out
                        keyFrames.add(new KeyFrame(Duration.millis(speed*(countLast++)+afterSpeed), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                //if to player have more to move.
                                if(copyI < player.getColumn()) {
                                    jumpPlayer.stop();
                                    new ZoomOut(player.getImagePlayer()).play();
                                }
                                //if this is last step
                                else {
                                    //change the layout of image
                                    player.getImagePlayer().setOpacity(0);
                                    player.getImagePlayer().setLayoutX(mapGrid.getCellBounds(player.getColumn(),player.getRow()).getMinX()+ ADDED_X_TO_GRID);
                                    player.getImagePlayer().setLayoutY(mapGrid.getCellBounds(player.getColumn(),player.getRow()).getMinY()+ ADDED_Y_TO_GRID);
                                    player.getImagePlayer().setOpacity(1);
                                    jumpPlayer.stop();
                                }

                            }
                        }));
                    }
                }
                //jump in the between line
                else
                {
                    if(x==8)            //for time of timeline.
                        line=1;
                    for(int i=0;i<10;i++)
                    {
                        int copyI=i;        //for using in i(of for) in the timeline.
                        int copyJ=j;        //for using in j(of for) in the timeline.
                        keyFrames.add(new KeyFrame(Duration.millis(speed * (9-x+i+line)), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                dice31.setDisable(true);
                                dice62.setDisable(true);
                                jumpPlayer.play();

                                //save current place for timeLine when change speed.
                                copySquare.setColumn(copyI);
                                copySquare.setRow(copyJ);
                                //change the layout of image
                                player.getImagePlayer().setOpacity(0);
                                player.getImagePlayer().setLayoutX(mapGrid.getCellBounds(copyI,copyJ).getMinX()+ ADDED_X_TO_GRID);
                                player.getImagePlayer().setLayoutY(mapGrid.getCellBounds(copyI,copyJ).getMinY()+ ADDED_Y_TO_GRID);
                                new JackInTheBox(player.getImagePlayer()).play();
                            }
                        }));
                        count++;
                        //player have more to move.
                        keyFrames.add(new KeyFrame(Duration.millis(speed *  (9-x+i+line) + afterSpeed), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                jumpPlayer.stop();
                                new ZoomOut(player.getImagePlayer()).play();
                            }
                        }));

                    }
                }
            }
        }
        //rolling the dices.
        keyFrames.add(new KeyFrame(Duration.millis(speed*(dice1+dice2+2.5)), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                check(player);//check if the player find in special square
                if(nextPlayer()!=userPlayer&&finishStep) {   //the next player is computer.
                    diceTimeLine.play();        //auto next player.
                }
                else if(nextPlayer()==userPlayer)   //the next player is user.
                {
                    waitForUser();  //remind to user that is your turn.
                }

            }
        }));
        jumpTimeLine.play();
    }

    //check who is the next player and return his number.
    private int nextPlayer() {
        int nextplayer=numPlayer;       //initialize.
        for (int i=0;i<4;i++)
        {
            switch (nextplayer) {
                case 1:
                    if(player1.getSleep())
                        nextplayer++;
                    else
                        return nextplayer;
                    break;
                case 2:
                    if(player2.getSleep())
                        nextplayer++;
                    else
                        return nextplayer;
                    break;
                case 3:
                    if(player3.getSleep())
                        nextplayer++;
                    else
                        return nextplayer;
                    break;
                case 4:
                    if(player4.getSleep())
                        nextplayer=1;
                    else
                        return nextplayer;
                    break;
                default:
                    return nextplayer;
            }
        }
        return nextplayer;
    }
    //checks by player number if he is asleep.
    private boolean isSleep(int numP) {
        switch (numP) {
            case 1:
                if(player1.getSleep())
                    return true;
                break;
            case 2:
                if(player2.getSleep())
                    return true;
                break;
            case 3:
                if(player3.getSleep())
                    return true;
                break;
            case 4:
                if(player4.getSleep())
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    //click dices.
    private void setDice() {

        dice31.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {      //Checks which numbers of dices are displayed. and bounce in.
                if(finishStep) {
                    if(dice11.getOpacity()==1)
                        new BounceIn(dice11).play();
                    if(dice21.getOpacity()==1)
                        new BounceIn(dice21).play();
                    if(dice31.getOpacity()==1)
                        new BounceIn(dice31).play();
                    if(dice41.getOpacity()==1)
                        new BounceIn(dice41).play();
                    if(dice51.getOpacity()==1)
                        new BounceIn(dice51).play();
                    if(dice61.getOpacity()==1)
                        new BounceIn(dice61).play();
                    if(dice12.getOpacity()==1)
                        new BounceIn(dice12).play();
                    if(dice22.getOpacity()==1)
                        new BounceIn(dice22).play();
                    if(dice32.getOpacity()==1)
                        new BounceIn(dice32).play();
                    if(dice42.getOpacity()==1)
                        new BounceIn(dice42).play();
                    if(dice52.getOpacity()==1)
                        new BounceIn(dice52).play();
                    if(dice62.getOpacity()==1)
                        new BounceIn(dice62).play();
                }
            }
        });

        dice62.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {      //Checks which numbers of dices are displayed. and bounce in.
                if(finishStep)
                {
                    if(dice11.getOpacity()==1)
                        new BounceIn(dice11).play();
                    if(dice21.getOpacity()==1)
                        new BounceIn(dice21).play();
                    if(dice31.getOpacity()==1)
                        new BounceIn(dice31).play();
                    if(dice41.getOpacity()==1)
                        new BounceIn(dice41).play();
                    if(dice51.getOpacity()==1)
                        new BounceIn(dice51).play();
                    if(dice61.getOpacity()==1)
                        new BounceIn(dice61).play();
                    if(dice12.getOpacity()==1)
                        new BounceIn(dice12).play();
                    if(dice22.getOpacity()==1)
                        new BounceIn(dice22).play();
                    if(dice32.getOpacity()==1)
                        new BounceIn(dice32).play();
                    if(dice42.getOpacity()==1)
                        new BounceIn(dice42).play();
                    if(dice52.getOpacity()==1)
                        new BounceIn(dice52).play();
                    if(dice62.getOpacity()==1)
                        new BounceIn(dice62).play();
                }
            }
        });

        dice31.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(noWait)  //if animation its play
                {
                    flash.stop();
                    flash.getNode().setOpacity(1);
                }
                noWait=true;
                diceTimeLine.play();
            }
        });
        dice62.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(noWait)  //if animation its play
                {
                    flash.stop();
                    flash.getNode().setOpacity(1);
                }
                noWait=true;        //init
                diceTimeLine.play();        //go play
            }
        });
    }

    //roll dices- choose dice by random number.
    private void setTimeLineDice() {
        ObservableList<KeyFrame> keyFrames1 = diceTimeLine.getKeyFrames();
        //roll dice 1.
        keyFrames1.add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dice31.setDisable(true);
                dice62.setDisable(true);

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
        //and choose by random number 1.
        keyFrames1.add(new KeyFrame(Duration.millis(800), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //  new FadeOut(summer).play();
                new FadeOut(dice61).play();
                rollDice.stop();
                dice1 = rand.nextInt(6);
                dice1=dice1+1;
               // dice1=6;
                System.out.println(dice1+dice11.getOpacity());

                    if(dice1==1)
                        new BounceIn(dice11).play();
                    else if(dice1== 2)
                        new BounceIn(dice21).play();
                    else if(dice1==3)
                        new BounceIn(dice31).play();
                    else if(dice1==4)
                        new BounceIn(dice41).play();
                    else if(dice1== 5)
                        new BounceIn(dice51).play();
                    else if(dice1==6)
                        new BounceIn(dice61).play();

                }
        }));
        //roll dice 2
        keyFrames1.add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice52).play();
                new BounceIn(dice42).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(150), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice42).play();
                new BounceIn(dice22).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice22).play();
                new BounceIn(dice52).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(350), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice52).play();
                new BounceIn(dice32).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(450), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice32).play();
                new BounceIn(dice62).play();

            }
        }));
        keyFrames1.add(new KeyFrame(Duration.millis(550), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice62).play();
                new BounceIn(dice12).play();

            }
        }));
        //and choose by random number 2.
        keyFrames1.add(new KeyFrame(Duration.millis(800), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FadeOut(dice12).play();
                rollDice.stop();
                dice2 = rand.nextInt(6);
                dice2 = dice2 + 1;
                //dice2=6;
                System.out.println(dice2);
                switch (dice2) {
                    case 1:
                        new BounceIn(dice12).play();
                        break;
                    case 2:
                        new BounceIn(dice22).play();
                        break;
                    case 3:
                        new BounceIn(dice32).play();
                        break;
                    case 4:
                        new BounceIn(dice42).play();
                        break;
                    case 5:
                        new BounceIn(dice52).play();
                        break;
                    case 6:
                        new BounceIn(dice62).play();
                        break;
                    default:
                        break;
                }
            }
        }));
        //go player.
        keyFrames1.add(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                play();
            }
        }));
    }

    private void checkWithOutSupp(Player player) {
        //snack
        if (player.getColumn() == 6 && player.getRow() == 7) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(109,65);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            snackAnimation(player,path,8,8);
        } else if (player.getColumn() == 2 && player.getRow() == 4) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(19,110);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            snackAnimation(player,path,2,6);
        } else if (player.getColumn() == 9 && player.getRow() == 4) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(-20,110);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            snackAnimation(player,path,8,6);
        }
        else if (player.getColumn() == 2 && player.getRow() == 1) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(199,110);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);
            snackAnimation(player,path,6,3);
        }
        //ladder
        else if (player.getColumn() == 3 && player.getRow() == 9) {
            ladderAnimation(player,3,7);
        }
        else if (player.getColumn() == 8 && player.getRow() == 4) {
            ladderAnimation(player,8,2);
        }
        else if (player.getColumn() == 5 && player.getRow() == 6) {
            ladderAnimation(player,5,4);
        }
        else if (player.getColumn() == 5 && player.getRow() == 2) {
            ladderAnimation(player,5,0);
        }
    }

    private void check(Player player) {
        if(gameEnd) //finish game
        {
            player.setCountWinner(player.getCountWinner()+1);
            System.out.println("gameEnd");
            setTimeLineEnd();
            return;
        }
        //snack
        if (player.getColumn() == 6 && player.getRow() == 7) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(109,65);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            snackAnimation(player,path,8,8);
        } else if (player.getColumn() == 2 && player.getRow() == 4) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(19,110);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            snackAnimation(player,path,2,6);
        } else if (player.getColumn() == 9 && player.getRow() == 4) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(-20,110);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            snackAnimation(player,path,8,6);
        }
        else if (player.getColumn() == 2 && player.getRow() == 1) {
            //create path for snack.
            Path path=new Path();
            MoveTo moveTo=new MoveTo(19,23);
            LineTo lineTo=new LineTo(199,110);
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);
            snackAnimation(player,path,6,3);
        }
        //ladder
        else if (player.getColumn() == 3 && player.getRow() == 9) {
            ladderAnimation(player,3,7);
        }
        else if (player.getColumn() == 8 && player.getRow() == 4) {
            ladderAnimation(player,8,2);
        }
        else if (player.getColumn() == 5 && player.getRow() == 6) {
            ladderAnimation(player,5,4);
        }
        else if (player.getColumn() == 5 && player.getRow() == 2) {
            ladderAnimation(player,5,0);
        }
        //supprize
        else if (player.getColumn() ==1  && player.getRow() == 8) {
            supprize();
            System.out.println("supprize");
        }
        else if (player.getColumn() ==6  && player.getRow() == 6) {
            supprize();
            System.out.println("supprize");
        }
        else if (player.getColumn() ==1  && player.getRow() == 3) {
            supprize();
            System.out.println("supprize");
        }
        else if (player.getColumn() ==4  && player.getRow() == 1) {
            supprize();
            System.out.println("supprize");
        }
        else if (player.getColumn() ==4  && player.getRow() == 4) {
            supprize();
            System.out.println("supprize");
        }else if (player.getColumn() ==5  && player.getRow() == 9) {
            supprize();
            System.out.println("supprize");
        }else if (player.getColumn() ==0  && player.getRow() == 6) {
            supprize();
            System.out.println("supprize");
        }else if (player.getColumn() ==7  && player.getRow() == 0) {
            supprize();
            System.out.println("supprize");
        }else if (player.getColumn() ==5  && player.getRow() == 9) {
            supprize();
            System.out.println("supprize");
        }
        else
            finishStep=true;
        dice31.setDisable(false);
        dice62.setDisable(false);
    }
    //snack animation.
    private void snackAnimation(Player player, Path path, int col,int row) {
        System.out.println("--> snack animation ");

        // play music
        snackPlayer.stop();
        snackPlayer.play();

        //animation snack
        Bounds cellBoundsTo = mapGrid.getCellBounds(col, row);
        //init transition
        PathTransition pathTransition=new PathTransition();
        //duration animation.
        pathTransition.setDuration(Duration.millis(2000));
        //image animation.
        pathTransition.setNode(player.getImagePlayer());
        //count animation
        pathTransition.setCycleCount(1);
        pathTransition.setPath(path);
        //play
        pathTransition.play();

        //init value after path transition.
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                player.setColumn(col);
                player.setRow(row);
                System.out.println("snack to x="+col+" y="+row);

                player.getImagePlayer().setTranslateX(0);
                player.getImagePlayer().setTranslateY(0);
                player.getImagePlayer().setLayoutY(cellBoundsTo.getMinY()+ADDED_Y_TO_GRID);
                player.getImagePlayer().setLayoutX(cellBoundsTo.getMinX()+ADDED_X_TO_GRID);

            }
        });
        finishStep=true;

    }
    //lader animation.
    private void ladderAnimation(Player player, int col,int row) {
        System.out.println("--> ladder animation ");

        // play music
        ladderPlayer.stop();
        ladderPlayer.play();

        //animation ladder
        Bounds cellBoundsTo = mapGrid.getCellBounds(col, row);

        //init transition.
        PathTransition pathTransition=new PathTransition();
        //duration animation
        pathTransition.setDuration(Duration.millis(2000));
        //image animation.
        pathTransition.setNode(player.getImagePlayer());
        //count animation
        pathTransition.setCycleCount(1);

        //path for animation.
        Path path=new Path();
        MoveTo moveTo=new MoveTo(19,23);
        LineTo lineTo=new LineTo(19,-73);
        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        pathTransition.setPath(path);
        //play
        pathTransition.play();
        //init value after path transition.
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.setColumn(col);
                player.setRow(row);
                player.getImagePlayer().setTranslateX(0);
                player.getImagePlayer().setTranslateY(0);
                player.getImagePlayer().setLayoutY(cellBoundsTo.getMinY()+ADDED_Y_TO_GRID);
                player.getImagePlayer().setLayoutX(cellBoundsTo.getMinX()+ADDED_X_TO_GRID);
            }
        });
        finishStep=true;
    }
    //choose card by random number.
    private void supprize() {
        numCard=new Random().nextInt(4);
        numCard=numCard+1;
        overB.setVisible(true);
        switch(numCard){
            case 1:                  //throw another dice.
                new Tada(card1).play();
                card1.setVisible(true);
                if((numPlayer-1!=userPlayer)&&(!(numPlayer-1==0&&userPlayer==4))) {
                    cardTimeLine=new Timeline();
                    ObservableList<KeyFrame> keyFrames =cardTimeLine.getKeyFrames();
                    keyFrames.add(new KeyFrame(Duration.millis(2000), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //display card.
                            card1.setVisible(false);
                            clickCard1();
                        }
                    }));
                    cardTimeLine.play();
                 }
                break;
            case 2:         //wait turn.
                card2.setVisible(true);
                new Tada(card2).play();
                System.out.println(numPlayer+" "+userPlayer);
                if((numPlayer-1!=userPlayer)&&(!(numPlayer-1==0&&userPlayer==4))) {
                    //timeLineCard
                    cardTimeLine=new Timeline();
                    ObservableList<KeyFrame> keyFrames =cardTimeLine.getKeyFrames();

                    keyFrames.add(new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //display card.
                            card2.setVisible(false);
                            clickCard2();
                        }
                    }));
                    cardTimeLine.play();

                }
                break;
            case 3:         //choose player for wait turn.
                new Tada(card3).play();
                card3.setVisible(true);
                if((numPlayer-1!=userPlayer)&&(!(numPlayer-1==0&&userPlayer==4))) {
                    //timeLineCard
                    cardTimeLine=new Timeline();
                    ObservableList<KeyFrame> keyFrames =cardTimeLine.getKeyFrames();
                    keyFrames.add(new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //display card.
                            card3.setVisible(false);
                            clickCard3();
                        }
                    }));
                    cardTimeLine.play();
                    //auto play.
                    if(nextPlayer()!=userPlayer||isSleep(userPlayer))
                    {
                        diceTimeLine.play();
                        System.out.println("supp card 3, auto play");
                    }
                    //wait for user
                    else
                    {
                        waitForUser();
                    }
                }
                break;
            case 4:     //choose player for come back 3 step
                new Tada(card4).play();
                card4.setVisible(true);
                System.out.println(numPlayer+" card 4");
                if((numPlayer-1!=userPlayer)&&(!(numPlayer-1==0&&userPlayer==4))) {
                    //timeLineCard
                    cardTimeLine = new Timeline();
                    ObservableList<KeyFrame> keyFrames = cardTimeLine.getKeyFrames();
                    keyFrames.add(new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //display card.
                            card4.setVisible(false);
                            clickCard4();
                        }
                    }));
                    cardTimeLine.play();
                    //auto play.
                    if (nextPlayer() != userPlayer || isSleep(userPlayer))
                    {
                        diceTimeLine.play();
                        System.out.println("supp card 4, auto play");
                    }
                    //wait for user.
                    else
                    {
                        waitForUser();
                    }
                }
                break;
            default:
                break;
        }
    }

    //when finish game.
    private void setTimeLineEnd() {
        Timeline timeLineEnd=new Timeline();
        ObservableList<KeyFrame> keyFrames1 = timeLineEnd.getKeyFrames();
        keyFrames1.add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                croud.play();
                overB.setVisible(true);
                gameOver.setVisible(true);
                System.out.println(numPlayer+" winner!!!");
                numPlayer--;
                //choose the player is winner
                if(numPlayer==0) {
                    winner.setImage(new Image("winner4.png"));
                    numPlayer=4;
                   // player4.setCountWinner(player4.getCountWinner()+1);
                }else if(numPlayer==1) {
                    winner.setImage(new Image("winner1.png"));
                   // player1.setCountWinner(player1.getCountWinner()+1);
                }else if(numPlayer==2) {
                    winner.setImage(new Image("winner2.png"));
                   // player2.setCountWinner(player2.getCountWinner()+1);
                }else if(numPlayer==3) {
                    winner.setImage(new Image("winner3.png"));
                   // player3.setCountWinner(player3.getCountWinner()+1);
                }
                //display winner.
                winner.setVisible(true);
                new FlipInX(winner).play();
            }
        }));

        //animation for finish game.
        keyFrames1.add(new KeyFrame(Duration.millis(2000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FlipOutX(winner).play();
                new Flash(gameOver).play();
            }
        }));
        keyFrames1.add(new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new Flash(gameOver).play();
                croud.stop();
            }
        }));
        timeLineEnd.play();
    }
    //player press on door.
    private void setExit() {
        exit.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new BounceIn(exit).play();
            }
        });
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ButtonType yesButtonType = new ButtonType("כן", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButtonType = new ButtonType("לא", ButtonBar.ButtonData.CANCEL_CLOSE);

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().getButtonTypes().add(yesButtonType);
                dialog.getDialogPane().getButtonTypes().add(noButtonType);
                dialog.getDialogPane().setContentText("המשחק ייגמר, אתה בטוח שאתה רוצה לצאת?");
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == yesButtonType) {

                    String resource = "/Go.fxml";
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    FXMLLoader hardBoard = new FXMLLoader(getClass().getResource(resource));
                    try {
                        stage.setScene(new Scene((Pane) hardBoard.load(), 500, 250));
                    } catch (IOException e) {
                        System.out.println("error");
                    }
                    diceTimeLine.stop();
                    jumpTimeLine.stop();
                    backgroundMusic.stop();
                    stage.show();
                }
            }
        });
    }
}
