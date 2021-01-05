package it.polimi.ingsw.view.generalview;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.cards.PrivateTargetCard;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.cards.PublicTargetCard;
import it.polimi.ingsw.net.client.socket.ClientSocket;
import it.polimi.ingsw.view.OPView;
import it.polimi.ingsw.view.toolcard.TokenView;
import it.polimi.ingsw.view.dice.AddDiceView;
import it.polimi.ingsw.view.dice.DiceView;
import it.polimi.ingsw.view.ReserveView;
import it.polimi.ingsw.view.RoundTrackerView;
import it.polimi.ingsw.view.publiccard.PublicCardView;
import it.polimi.ingsw.view.toolcard.ToolCardInfoView;
import it.polimi.ingsw.view.toolcard.ToolCardView;
import it.polimi.ingsw.view.schemacard.SchemaOP;
import it.polimi.ingsw.view.schemacard.SchemaUser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RoundView extends Parent {
    private static final int SIZE_W = 1280;
    private static final int SIZE_H = 700;
    private static final int PADDING = 20;
    private static final int SPACING_HBOX =100;
    private static final int SPACING_U = 30;
    private static final int SPACING_VRESERVE = 30;
    private static final int SPACING_VCENTRAL = 170;
    private static final int SPACING_OP = 10;
    private static final int H_PRIVATE = 120;

    private static final HashMap<Integer,Image> privatecards= new HashMap<>();
    static{
        privatecards.put(0,new Image("view/privatecard/0.png") );
        privatecards.put(1,new Image("view/privatecard/1.png") );
        privatecards.put(2,new Image("view/privatecard/2.png") );
        privatecards.put(3,new Image("view/privatecard/3.png") );
        privatecards.put(4,new Image("view/privatecard/4.png") );

    }


    private StackPane spane;
    private HBox hbox;
    private HBox hboxuser;
    private VBox vboxcentral;
    private VBox vboxop;
    private SchemaUser userSchema;
    private ReserveView reserve;
    private List<SchemaOP> other_user_schema;
    private List<OPView> other_user;
    private ArrayList<ToolCardView> toolcardview;
    private RoundTrackerView round_tracker;
    private AddDiceView addDiceView;
    private ToolCardInfoView toolCardInfoView;
    private Label turn_text;
    private ClientSocket intconnection;
    private TokenView token;
    private Label tutorialview;
    private Label timerview;

    /**
     * Remove dice from reserveview
     * @param pos index dice to remove
     */
    public void removeReserve(int pos){
        reserve.remove(pos);
    }

    /**
     * Show dice d in position i of user schema
     * @param i index grid
     * @param d dice
     * @param addDiceView
     */
    public void updateUserSchema(int i, Dice d, AddDiceView addDiceView){
        userSchema.add(i, new DiceView(d.getColor(), d.getFace(), i, addDiceView, toolCardInfoView));
    }

    /**
     * Show dice d in position index of  "id other player schema"
     * @param id tells which op schema to modify
     * @param index where in the schema
     * @param d dice to place
     */
    public void updateOtherUserSchema(int id, int index, Dice d) {

    List<SchemaOP> op = other_user_schema.stream()
                                    .filter(o -> o.getId_card()==id)
                                    .collect(Collectors.toList());
    op.get(0).addDice(index, new DiceView(d.getColor(), d.getFace(), index, addDiceView, toolCardInfoView));

    }

    public RoundView(ToolCardInfoView toolCardInfoView, ClientSocket intconnection, AddDiceView addDiceView, List<Player> players, SchemaCard user_schema, List<ToolCard> toolcard, List<PublicTargetCard> publiccard, PrivateTargetCard privatecard, ArrayList<Dice> reserveList, int token) {
        this.intconnection=intconnection;
        ImageView wallpaper = new ImageView(new Image("view/wallpaper.png", SIZE_W, SIZE_H, false, true));
        this.addDiceView=addDiceView;
        this.toolCardInfoView=toolCardInfoView;
        hbox = new HBox();
        hbox.setPadding(new Insets(PADDING));

        spane = new StackPane(wallpaper, hbox);
        spane.setAlignment(Pos.CENTER);

        wallpaper.fitWidthProperty().bind(spane.widthProperty());
        wallpaper.fitHeightProperty().bind(spane.heightProperty());

        tutorialview = new Label("");

        //user schema + privatecard
        userSchema = new SchemaUser(user_schema, addDiceView, toolCardInfoView);

        turn_text = new Label("Non è il tuo turno");
        this.token = new TokenView(token);
        JFXButton skip_turn = new JFXButton("Avanti");
        skip_turn.setOnMouseClicked(t -> {
                                            if(addDiceView.isMyTurn() && !toolCardSelected()) {
                                                intconnection.sendMessage("skip");
                                                tutorialview.setText("");
                                            }
                                            if(toolCardSelected()){
                                                tutorialview.setText("Hai una carta tool selezionata, deselezionala");
                                            }
        });

        timerview=new Label("");

        VBox vboxtoken = new VBox();
        vboxtoken.setAlignment(Pos.CENTER);
        vboxtoken.setSpacing(15);
        vboxtoken.getChildren().addAll(turn_text, this.token,skip_turn,timerview);

        ImageView privatecardview = new ImageView(privatecards.get(privatecard.getID()));
        privatecardview.setPreserveRatio(true);
        privatecardview.setFitHeight(H_PRIVATE);

        vboxcentral = new VBox();
        hboxuser = new HBox();

        hboxuser.setAlignment(Pos.CENTER);
        hboxuser.setSpacing(SPACING_U);
        hboxuser.getChildren().add(privatecardview);
        hboxuser.getChildren().add(userSchema);
        hboxuser.getChildren().add(vboxtoken);
        hboxuser.setAlignment(Pos.BOTTOM_CENTER);

        //op schema
        vboxop = new VBox();
        vboxop.setAlignment(Pos.CENTER);
        vboxop.setSpacing(SPACING_OP);

        other_user_schema = new ArrayList<>();
        other_user = new ArrayList<>();

        players.forEach(c -> {SchemaOP op_schema = new SchemaOP(c);
            Label username = new Label(c.getUsername());
            OPView op = new OPView(op_schema,username);
            other_user_schema.add(op_schema);
            other_user.add(op);

            vboxop.getChildren().add(op);});

        //tool cards
        VBox vboxtool = new VBox();
        vboxtool.setAlignment(Pos.CENTER);
        vboxtool.setSpacing(SPACING_OP);
        toolcardview = new ArrayList<>();
        toolcardview.add(new ToolCardView(toolcard.get(0),0, intconnection, toolCardInfoView, this.token, tutorialview, addDiceView));
        toolcardview.add(new ToolCardView(toolcard.get(1),1, intconnection, toolCardInfoView, this.token, tutorialview, addDiceView));
        toolcardview.add(new ToolCardView(toolcard.get(2),2, intconnection, toolCardInfoView, this.token, tutorialview, addDiceView));
        vboxtool.getChildren().addAll(toolcardview.get(0),toolcardview.get(1),toolcardview.get(2));

        //public target card
        VBox vboxtarget = new VBox();
        vboxtarget.setAlignment(Pos.CENTER);
        vboxtarget.setSpacing(SPACING_OP);
        PublicCardView pub1 = new PublicCardView(publiccard.get(0));
        PublicCardView pub2 = new PublicCardView(publiccard.get(1));
        PublicCardView pub3 = new PublicCardView(publiccard.get(2));
        vboxtarget.getChildren().addAll(pub1,pub2,pub3);

        //cardbox
        HBox cardbox = new HBox();
        cardbox.getChildren().addAll(vboxtool,vboxtarget);
        cardbox.setSpacing(SPACING_U);
        cardbox.setAlignment(Pos.CENTER_RIGHT);

        //round tracker
        round_tracker= new RoundTrackerView();
        round_tracker.setAlignment(Pos.TOP_CENTER);

        //reserve + tutorialview vbox
        VBox v_reserve = new VBox();
        reserve = new ReserveView(reserveList, addDiceView, toolCardInfoView);
        v_reserve.getChildren().addAll(reserve, tutorialview);
        v_reserve.setSpacing(SPACING_VRESERVE);
        v_reserve.setAlignment(Pos.CENTER);

        vboxcentral.getChildren().addAll(round_tracker,v_reserve,hboxuser);
        vboxcentral.setAlignment(Pos.CENTER);
        vboxcentral.setSpacing(SPACING_VCENTRAL);
        vboxcentral.setMinWidth(300);

        hbox.setSpacing(250);
        hbox.setSpacing(SPACING_HBOX);
        hbox.setAlignment(Pos.CENTER);

        hbox.getChildren().addAll( vboxop,vboxcentral,cardbox);


        getChildren().add(spane);

    }

    /**
     * Getter
     * @return Stackpane with wallpaper
     */
    public StackPane getSpane() {
        return spane;
    }

    /**
     * Show message that tells if is player's turn
     * @param s message to show
     */
    public void setTurn_text(String s){
        Platform.runLater(() -> turn_text.setText(s));
    }

    /**
     * Add tokens of the player
     * @param i number of tokens
     */
    public void addTokenMessage(int i) { toolcardview.get(i).addToken(); }

    /**
     * Sets reserve full of random dice
     * @param reserve to full
     */
    public void setReserve(ArrayList<Dice> reserve) {
        this.reserve.updateReserve(reserve);
    }

    /**
     * Shows dice in roundtrack
     * @param dice to place in roundtrack
     */
    public void addRoundTrack(Dice dice) {
        round_tracker.addDice(new DiceView(dice.getColor(), dice.getFace(), round_tracker.getCurrent_index()+1, addDiceView, toolCardInfoView));
    }

    /**
     * Switch dice in pos i of the roundtrack with a new dice
     * @param i switch position
     * @param dice new dice in roundtrack
     */
    public void updateRoundTrack(int i, Dice dice){
        round_tracker.updateDice(i, new DiceView(dice.getColor(), dice.getFace(), i+1, addDiceView, toolCardInfoView));
    }

    /**
     * Remove diceview from UserSchema
     * @param i position of dice to remove in schema
     */
    public void removeUserSchema(int i) {
        userSchema.removeSquareUser(i);
    }

    /**
     * Remove diceview from OPSchema
     * @param id of OPSchema to modify
     * @param i index position where remove dice in schema
     */
    public void removeOtherUserSchema(int id, int i) {
        List<SchemaOP> op = other_user_schema.stream()
                .filter(o -> o.getId_card()==id)
                .collect(Collectors.toList());
        op.get(0).removeSchemaOP(i);
    }

    /**
     * Shows timer
     * @param i seconds rest
     */
    public void setTimerview(int i){
        timerview.setText(Integer.toString(i));
    }

    /**updates the view if some player has disconnected
     * @param user the name of the disconnected user
     */
    public void getNewDisconnected(String user) {
        for(OPView opv: other_user){
            if(opv.getUsername().equals(user)){
                System.out.println("3.New disconnected " + opv.getUsername());
                opv.offlineFlag();
            }
        }
    }

    /**
     * updates the view if some player has reconnected
     * @param user the name of the reconnected user
     */
    public void newReconnected(String user) {
        for(OPView opv: other_user){
            if(opv.getUsername().equals(user)){
                opv.onlineFlag();
            }
        }
    }

    /**
     * Removes some tokens after a confirmation of the possibility to select a tool card
     * @param i the index of the tool card
     * @param i1 the number of tokens
     */
    public void tokenConfirmed(int i, int i1) {
        Platform.runLater(() -> toolcardview.get(i).tokenConfirmed(i1));
    }

    /**
     * Updates the view in the case a player hasn't enoght tokens to play a tool card
     * @param i the tool card index
     */
    public void notEnoughTokens(int i) {
        Platform.runLater(() -> toolcardview.get(i).notEnoughToken());
    }

    /**
     * Selects toolcard clicked
     * @return if toolcard is selected or not
     */
    private boolean toolCardSelected(){
       return toolcardview.stream().anyMatch(ToolCardView::isSelected);
    }

    /**
     * Removes tokens to player
     * @param i index of toolcard in vbox
     * @param i1 number of tokens to remove
     */
    public void removeTokens(int i, int i1) {
        Platform.runLater(() -> toolcardview.get(i).removeTokens(i1));

    }

    /**
     * Updates the view in the case a tool card can be selected using only one token
     * @param i the tool card index
     */
    public void setToolOneToken(int i) {
       toolcardview.get(i).setToolOneToken();
    }
}

