import javafx.application.*;
import javafx.scene.*;
import java.util.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.event.*;
import javafx.util.Duration;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
public class Group_Breakout extends Application {
    public void start(Stage PrimaryStage) {
 
        // creating pane for secondary stage to display nodes i.e., buttons and title of the Game, on the 
       main menu;
        Pane main_menu=new Pane();
        main_menu.setStyle("-fx-background-color:NAVAJOWHITE");
        main_menu.setPrefSize(610,400);
        //Game Title stored in text
        Text title = new Text("Breakout Game\n WELCOME!");
        title.setX(115);
        title.setY(100);
        title.setFill(Color.BLACK);// setting color of the text to black 
        title.setStrokeWidth(3);
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,55)); 
        //Creating buttons 
        Button strt = new Button("Start Game");
        strt.setMaxWidth(165);
        strt.setStyle("-fx-font-size: 1.9em; ");
        strt. setLayoutX(240);
        strt.setLayoutY(240);
        Button ex = new Button("Exit Game ");
        ex.setMaxWidth(165);
        ex. setLayoutX(245);
        ex.setLayoutY(300);
        ex.setStyle("-fx-font-size: 1.9em; ");
        main_menu.getChildren().addAll(strt,ex,title);
        Scene display= new Scene(main_menu);
        
        //secondary stage for the menu 
        Stage second = new Stage();
        second.setTitle("Main Menu");
        second.setScene(display);
        second.show();
        
        //when start button is pressed following actions will be performed
        strt.setOnAction(e->{
        
        //creating pane for the main layout of the game
        Pane align= new Pane();
        align.setStyle("-fx-background-color: BISQUE");
        align.setPrefSize(610,400); 
        
        Scene scn= new Scene(align);
        
        //creating bricks and adding them to the align 
        
        ArrayList<Rectangle> BrickBlocks= new ArrayList<>();
        for(int x=0; x<10; x++) {
        for(int y=0; y<8; y++) {
        Rectangle brk=new Rectangle(60,15);
        brk.setFill(Color.GREEN);
        brk.setLayoutX(x*62);
        brk.setLayoutY((16*y)+35);
        align.getChildren().add(brk);
        BrickBlocks.add(brk);
        } } 
        //creating ball 
        Circle ball= new Circle(18,18,9,Color.PURPLE);
        ball.setStrokeWidth(5);
        ball.relocate(250, 365); //relocating the ball
        
        //adding animation of StrokeTransition to the ball
        StrokeTransition st=new StrokeTransition();
        st.setAutoReverse(true);
        st.setCycleCount(500);
        st.setDuration(Duration.millis(1000));
        st.setFromValue(Color.PURPLE);
        st.setToValue(Color.ORANGE);
        st.setShape(ball);
        st.play();
        
        //creating rectangular paddle
        Rectangle pad= new Rectangle(130,15, Color.RED);
        pad.relocate(210, 385);
        
        Text sB = new Text(){{//storing score and lives in text
        setFill(Color.BLACK);
        setX(150);
        setY(25);
        setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,30)); 
        setText("SCORE = "+ 0 + " LIVES = "+ 3);
    }};
    Text txt=new Text() {{
    setX(150);
    setY(270);
    setFont(Font.font("Robotto", FontWeight.BOLD,FontPosture.REGULAR, 50));
    setFill(Color.RED);
    setStroke(Color.BLACK);
    setStrokeWidth(5);
    }};
    align.getChildren().addAll(pad, ball,sB);// displaying ball, paddle and text on the scene 
    
    //creating infinite bouncing of the ball 
    Timeline bounce = new Timeline(new 
    KeyFrame(Duration.millis(33), 
    new EventHandler<ActionEvent>() {
    double Dx = 6; 
    double Dy = 4; 
    int scr=0 , life=3;
    public void handle(ActionEvent t) {
    
    //controlling the ball movement
    ball.setLayoutX(ball.getLayoutX() + Dx);
    ball.setLayoutY(ball.getLayoutY() + Dy); 
    boolean left_Wall = ball.getLayoutX() <= -5; 
    boolean top_Wall = ball.getLayoutY() < 35;
    boolean right_Wall = ball.getLayoutX() >= 575;
    boolean bottom_Wall = ball.getLayoutY() >= 370;
    if (touching(pad)) {
        Dy = -Dy;
    }
    
    //when ball hits the top wall it will reverse the direction of the ball
    if (top_Wall) {
    Dy = Dy * -1;
    }
    //when ball hits the left or right wall it will reverse the direction of the ball
    if (left_Wall || right_Wall) {
    Dx = Dx * -1;
    }
    //when the ball hits the bottom wall the user loses a life 
    if(bottom_Wall) {
    ball.relocate(270, 260);
    pad.relocate(210, 385);
    
    sB.setText("Score = "+ scr+ " lives = "+ life);
    if(life>0){
    life--;
    sB.setText("SCORE = "+ scr + " LIVES = "+ life);
    }
    //when user loses all the lives GAME OVER message is displayed, paddle and ball disappear 
    else if (life<=0){
    align.getChildren().removeAll(ball,pad);
    sB.setText("SCORE = "+ scr + " LIVES = "+ life);
    txt.setText("GAME OVER :(");
    align.getChildren().addAll(txt);
    }
}
//when ball collides with brick, health of the brick reduces and eventually brick is removed
Rectangle rem=null;
for(Rectangle brick:BrickBlocks) { 
if(touching (brick)) { //green is the highest health level and the color is changing from green to yellow
if (brick.getFill().equals(Color.GREEN)) {
brick.setFill(Color.YELLOW);
Dy=-Dy;
}
//yellow is the half health and the color is changing from yellow to red
else if (brick.getFill().equals(Color.YELLOW)) {
brick.setFill(Color.RED);
Dy=-Dy;
}
//when ball collides with brick for the third time the brick is removed and score is updated (user gets a point)
else if (brick.getFill().equals(Color.RED)) {
scr++; //adding score
sB.setText("SCORE = "+ scr + " LIVES = "+ life);
align.getChildren().remove(brick);
rem=brick;
Dy=-Dy;}
} } 
if(!(rem==null)){
BrickBlocks.remove(rem);//removing bricks
rem=null;} 
//condition is checked, when all the bricks are broken by the player Winning message is displayed
boolean check=BrickBlocks.isEmpty();
 if (check == true) {
 txt.setText("YOU WON! :)");
 align.getChildren().removeAll(ball,pad);
 align.getChildren().add(txt);
 }
 }
 public boolean touching(Rectangle rect) {
 Shape touched_area = Shape.intersect(ball, rect);
 return touched_area.getBoundsInLocal().getWidth() != -1;
 }}));
 
 //controlling the movement of the paddle
 bounce.setCycleCount(Timeline.INDEFINITE);
 scn.setOnKeyPressed(keyEvente-> { 
 int move_pad = 20; 
 scn.setOnKeyPressed(event -> {
 if (event.getCode() == KeyCode.LEFT) {
 if(pad.getLayoutX() < 0) {
 pad.setLayoutX(pad.getLayoutX()+move_pad);
 }
 pad.setLayoutX(pad.getLayoutX()-move_pad);
 }
 if (event.getCode() == KeyCode.RIGHT) {
 if(pad.getLayoutX() > 510) {
 pad.setLayoutX(510);
 } 
 pad.setLayoutX(pad.getLayoutX()+move_pad);
 }});
 bounce.play();
 });
 
 PrimaryStage.setTitle("Breakout! (My First Game) ");
 PrimaryStage.setScene(scn);
 PrimaryStage.show();
 PrimaryStage.setResizable(false);
 });
 //when the exit button is pressed following action will be performed 
 ex.setOnAction(e->{
 System.exit(0);
 }); 
 }
 public static void main(String[] args) {
 launch(args);
 }
}