package it.polimi.ingsw.view.generalview;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginView extends Parent {
    private static final int SIZE_W = 1280;
    private static final int SIZE_H = 700;
    private static final int PADDING = 20;
    private static final int VGAP = 5;
    private static final int HGAP = 5;
    private static final String DIVISOR = ":";

    private StackPane spane;
    private BorderPane bpane;
    private JFXButton login_but;
    private TextField user_field;
    private PasswordField pass_field;
    private GridPane gridpane;
    private Label error;

    public LoginView(){

        spane=new StackPane();
        bpane= new BorderPane();


        ImageView wallpaper = new ImageView(new Image("view/wallpaper.png", SIZE_W, SIZE_H,false,true));
        wallpaper.fitWidthProperty().bind(spane.widthProperty());
        wallpaper.fitHeightProperty().bind(spane.heightProperty());

        bpane.setPadding(new Insets(20,50,20,50));

        HBox box_title = new HBox();
        box_title.setPadding(new Insets(PADDING));
        box_title.setAlignment(Pos.CENTER);

        ImageView title = new ImageView(new Image("view/sagradalogo.png"));
        box_title.getChildren().add(title);
        bpane.setTop(box_title);

        gridpane = new GridPane();
        gridpane.setMaxSize(500,200);
        gridpane.setPadding(new Insets(PADDING));
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setVgap(VGAP);
        gridpane.setHgap(HGAP);
        gridpane.setStyle("-fx-background-color:#00000080 ");

        Label user_lab= new Label("Username");
        user_field = new TextField();
        Label pass_lab= new Label("Password");
        pass_field = new PasswordField();
        login_but = new JFXButton("Login");
        //login_but.defaultButtonProperty().bind(login_but.focusedProperty());
        error= new Label("");


        gridpane.add(user_lab,0,0);
        gridpane.add(user_field,1,0);
        gridpane.add(pass_lab,0,1);
        gridpane.add(pass_field,1,1);
        gridpane.add(login_but,2,1);
        gridpane.add(error,1,2);
        bpane.setCenter(gridpane);
        getChildren().add(spane);
        spane.getChildren().addAll(wallpaper,bpane);


    }

    /**
     *
     * @param error string is a warning that appears if player does something wrong
     */
    public void printError(String error){
        user_field.clear();
        pass_field.clear();
        this.error.setText(error);
    }

    /**
     *
     * @return StackPane with wallpaper
     */
    public StackPane getSpane() {
        return spane;
    }

    /**
     *
     * @return login Button
     */
    public Button getLogin_but() {
        return login_but;
    }

    /**
     *
     * @return contents user textfield and password textfield
     */
    public String getUserPass_field(){
        return user_field.getText() + DIVISOR + pass_field.getText();
    }

    /**
     *
     * @return TextField for username
     */
    public TextField getUser_field() { return user_field;}

    /**
     *
     * @return PasswordField for password
     */
    public PasswordField getPass_field() {
        return pass_field;
    }
}


