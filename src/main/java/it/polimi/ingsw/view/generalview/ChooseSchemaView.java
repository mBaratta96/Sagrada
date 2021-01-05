package it.polimi.ingsw.view.generalview;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.model.cards.PrivateTargetCard;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.view.schemacard.SchemaUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class ChooseSchemaView extends Parent {
    private static final int SIZE_W = 1280;
    private static final int SIZE_H = 700;
    private static final int PADDING = 10;
    private static final int PRIVATE_VSPACING = 50;
    private static final int PRIVATE_H = 300;
    private static final int SCHEMA_VSPACING = 30;
    private static final int SCHEMA_HSPACING = 20;
    private static final int CENTRAL_HSPACING = 150;
    private static final int LABEL_WMIN = 200;


    private StackPane spane;
    private BorderPane bpane;
    private ToggleGroup group_schema;
    private JFXButton enter;
    private String selection;
    private ImageView privatecard;
    private Label warning_text;
    private ArrayList<RadioButton> radioButtons;


    public ChooseSchemaView(PrivateTargetCard ptc, List<SchemaCard> possilblecards){
        spane=new StackPane();
        bpane= new BorderPane();
        radioButtons = new ArrayList <>();

        ImageView wallpaper = new ImageView(new Image("view/wallpaper.png", SIZE_W, SIZE_H,false,true));
        wallpaper.fitWidthProperty().bind(spane.widthProperty());
        wallpaper.fitHeightProperty().bind(spane.heightProperty());

        bpane.setPadding(new Insets(20,50,20,50));

        HBox box_title = new HBox();
        box_title.setPadding(new Insets(PADDING));
        box_title.setAlignment(Pos.CENTER);
        Label title = new Label("Scegli la tua carta schema prima di iniziare.");
        title.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 30px");
        box_title.getChildren().add(title);
        bpane.setTop(box_title);

        HBox central_hbox = new HBox();
        central_hbox.setSpacing(CENTRAL_HSPACING);
        central_hbox.setAlignment(Pos.CENTER);
        central_hbox.setStyle("-fx-background-color:#00000080 ");

        VBox selection_schema = new VBox();
        selection_schema.setSpacing(SCHEMA_VSPACING);
        selection_schema.setAlignment(Pos.CENTER);


        VBox privateobj = new VBox();
        Label private_text= new Label ("Questo è il tuo obiettivo privato");
        warning_text= new Label("");


        int id = ptc.getID();
        switch (id){
            case (0):
                privatecard= new ImageView( new Image("view/privatecard/0.png"));
                break;
            case(1):
                privatecard= new ImageView( new Image("view/privatecard/1.png"));
                break;
            case(2):
                privatecard= new ImageView( new Image("view/privatecard/2.png"));
                break;
            case(3):
                privatecard= new ImageView( new Image("view/privatecard/3.png"));
                break;
            case(4):
                privatecard= new ImageView( new Image("view/privatecard/4.png"));
                break;
        }

        privatecard.setPreserveRatio(true);
        privatecard.setFitHeight(PRIVATE_H);

        enter = new JFXButton("Avanti");
        enter.setOnMouseClicked(t -> System.out.println(getSelection()));

        privateobj.setSpacing(PRIVATE_VSPACING);
        privateobj.setAlignment(Pos.CENTER);
        privateobj.getChildren().addAll(private_text,privatecard,enter,warning_text);

        central_hbox.getChildren().addAll(selection_schema,privateobj);
        central_hbox.setPadding(new Insets(PADDING));
        bpane.setCenter(central_hbox);

        group_schema = new ToggleGroup();

        HBox hrb1 = new HBox();
        hrb1.setSpacing(SCHEMA_HSPACING);
        RadioButton rb1 = new RadioButton();
        rb1.setToggleGroup(group_schema);
        rb1.setUserData("0");
        rb1.setGraphic(new SchemaUser(possilblecards.get(0)));
        Label labrb1 = new Label(possilblecards.get(0).getName()+" (Token: "+possilblecards.get(0).getInfo()+")");
        labrb1.setMinWidth(LABEL_WMIN);
        hrb1.getChildren().addAll(rb1,labrb1);


        HBox hrb2 = new HBox();
        hrb2.setSpacing(SCHEMA_HSPACING);
        RadioButton rb2 = new RadioButton();
        rb2.setToggleGroup(group_schema);
        rb2.setUserData("1");
        rb2.setGraphic(new SchemaUser(possilblecards.get(1)));
        Label labrb2 = new Label(possilblecards.get(1).getName()+" (Token: "+possilblecards.get(1).getInfo()+")");
        labrb2.setMinWidth(LABEL_WMIN);
        hrb2.getChildren().addAll(rb2,labrb2);

        HBox hrb3 = new HBox();
        hrb3.setSpacing(SCHEMA_HSPACING);
        RadioButton rb3 = new RadioButton();
        rb3.setToggleGroup(group_schema);
        rb3.setUserData("2");
        rb3.setGraphic(new SchemaUser(possilblecards.get(2)));
        Label labrb3 = new Label(possilblecards.get(2).getName()+" (Token: "+possilblecards.get(2).getInfo()+")");
        labrb3.setMinWidth(LABEL_WMIN);
        hrb3.getChildren().addAll(rb3,labrb3);

        HBox hrb4 = new HBox();
        hrb4.setSpacing(SCHEMA_HSPACING);
        RadioButton rb4 = new RadioButton();
        rb4.setToggleGroup(group_schema);
        rb4.setUserData("3");
        rb4.setGraphic(new SchemaUser(possilblecards.get(3)));
        Label labrb4 = new Label(possilblecards.get(3).getName()+" (Token: "+possilblecards.get(3).getInfo()+")");
        labrb4.setMinWidth(LABEL_WMIN);
        hrb4.getChildren().addAll(rb4,labrb4);

        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);
        radioButtons.add(rb4);

        selection_schema.getChildren().add(hrb1);
        selection_schema.getChildren().add(hrb2);
        selection_schema.getChildren().add(hrb3);
        selection_schema.getChildren().add(hrb4);

        group_schema.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (group_schema.getSelectedToggle() != null) {
                selection = group_schema.getSelectedToggle().getUserData().toString();
            }
        });

        spane.getChildren().addAll(wallpaper,bpane);

        getChildren().add(spane);

    }

    /**
     *
     * @return String with info of SchemaCard choosen on the radio button
     */
    public String getSelection() {
        if(selection != null)
            return selection;
        else
            return "*";
    }

    /**
     *
     * @return EnterButton
     */
    public Button getEnter() {
        return enter;
    }

    /**
     *
     * @return stackpane with wallpaper
     */
    public StackPane getSpane() {
        return spane;
    }

    /**
     *
     * @param s string is a warning that appears if player does something wrong
     */
    public void setWarning_text(String s) {
        warning_text.setText(s);
    }

    public RadioButton getRadioButton(int i) {
        return radioButtons.get(i);
    }
}
