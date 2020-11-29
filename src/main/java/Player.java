import javafx.scene.image.ImageView;

public class Player {
    private ImageView imagePlayer;
    private int column;
    private int row;
    private int countWinner;
    private int countSleep;
    private boolean sleep;

    public Player(ImageView imagePlayer){
        this.imagePlayer=imagePlayer;
        this.column =0;
        this.row =9;
        this.countWinner=0;
        this.countSleep=0;
        this.sleep=false;
        System.out.println("constructor");
    }
    public int getColumn(){
        return column;
    }
    public int getRow(){
        return row;
    }
    public int getCountWinner(){
        return countWinner;
    }
    public ImageView getImagePlayer(){
        return  imagePlayer;
    }
    public boolean getSleep(){
        return  sleep;
    }
    public int getCountSleep(){
        return countSleep;
    }

    public void setColumn(int column){
        this.column = column;
    }
    public void setRow(int row){
        this.row = row;
    }
    public void setCountWinner(int countWinner){
        this.countWinner = countWinner;
    }
    public void setSleep(boolean sleep){
        this.sleep = sleep;
    }
    public void setCountSleep(int countSleep){
        this.countSleep = countSleep;
    }
}
