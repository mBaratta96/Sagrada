package it.polimi.ingsw.view.toolcard;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.net.client.socket.ClientSocket;
import it.polimi.ingsw.view.dice.AddDiceView;
import it.polimi.ingsw.view.dice.SliderValue;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.util.HashMap;
import java.util.stream.IntStream;


public class ToolCardView extends HBox{
    private SliderValue slider;
    private JFXButton enter;
    private StackPane spane;
    private VBox vboxtool;
    private ImageView imm;
    private Label messageclick;
    private Label price;
    private Label error;
    private Label tutorial1;
    private ClientSocket intconnection;
    private AddDiceView addDiceView;
    private int pos;
    private ToolCardInfoView toolCardInfoView;
    private TokenView token;
    private boolean selected=false;
    private int id;

    private static final int SPACING=5;
    private static final int SPACING_VTOOL=5;
    private static final int HEIGHT =210;
    private static final HashMap<Integer,Image> cards= new HashMap<>();
    static{
        cards.put(1,new Image("view/toolcard/1.png") );
        cards.put(2,new Image("view/toolcard/2.png") );
        cards.put(3,new Image("view/toolcard/3.png") );
        cards.put(4,new Image("view/toolcard/4.png") );
        cards.put(5,new Image("view/toolcard/5.png") );
        cards.put(6,new Image("view/toolcard/6.png") );
        cards.put(7,new Image("view/toolcard/7.png") );
        cards.put(8,new Image("view/toolcard/8.png") );
        cards.put(9,new Image("view/toolcard/9.png") );
        cards.put(10,new Image("view/toolcard/10.png") );
        cards.put(11,new Image("view/toolcard/11.png") );
        cards.put(12,new Image("view/toolcard/12.png") );

    }

    private static final HashMap<Integer,String> tutorials = new HashMap<>();
    static{
        tutorials.put(1,"Scegli dado, poi 0 per diminuire/1 per aumentare.");
        tutorials.put(2,"Scegli dado, poi spostalo.");
        tutorials.put(3,"Scegli dado, poi spostalo.");
        tutorials.put(4,"Scegli dado, poi spostalo.");
        tutorials.put(5,"Scegli il dado dalla riserva, poi quello dal round tracker.");
        tutorials.put(6,"Scegli il dado da rilanciare");
        tutorials.put(7,"");
        tutorials.put(8,"Scegli nuovamente un dado.");
        tutorials.put(9,"Scegli dado, poi spostalo.");
        tutorials.put(10,"Scegli il dado da modificare.");
        tutorials.put(11,"Scegli il dado, la faccia sullo slider e posiziona il dado.");
        tutorials.put(12,"Scegli quanti dadi spostare, muovili sulla griglia.");


    }

    public ToolCardView(ToolCard card, int pos, ClientSocket intconnection, ToolCardInfoView toolCardInfoView, TokenView token, Label tutorial1, AddDiceView addDiceView){

        this.toolCardInfoView=toolCardInfoView;
        this.tutorial1=tutorial1;
        this.intconnection=intconnection;
        this.pos = pos;
        this.token=token;
        this.addDiceView=addDiceView;
        setWidth(400);
        setAlignment(Pos.CENTER_RIGHT);
        id = card.getID();
        if(card.getTokenRequired()==1)
            price = new Label("Costo: 1 token");
        else
            price= new Label("Costo: 2 token");
        messageclick = new Label("");
        error = new Label("");
        vboxtool = new VBox();
        vboxtool.setSpacing(SPACING_VTOOL);
        vboxtool.setAlignment(Pos.TOP_LEFT);
        vboxtool.getChildren().addAll(messageclick,price,error);

        imm=new ImageView(cards.get(id));
        imm.setPreserveRatio(true);
        imm.setFitHeight(HEIGHT);

        spane = new StackPane();
        spane.getChildren().add(imm);

        setSpacing(SPACING);
        getChildren().addAll(vboxtool,spane);

        //change dice face
        if(id==1) {
            addSlider(0,1);
        }
        if(id==11){
            addSlider(1,6);
        }
        if(id==12){
            addSlider(1,2);
        }
        spane.setOnMouseClicked(event -> selectionTool(event));
        spane.setOnMouseEntered(event -> showMessage());
        spane.setOnMouseExited(event -> deleteMessage());
    }

    /**
     * Selects ToolCard
     * @param event click
     */
    public void selectionTool(Event event) {
        event.consume();
        if(selected){
            System.out.println("tool card già selezionata");
        }else {
            if(toolCardInfoView.isMyTurn() && addDiceView.getPos_reserve_selected()==-1) {
                intconnection.sendMessage("token");
                intconnection.sendMessage(Integer.toString(pos));
                selected = true;
                Rectangle rec = new Rectangle();
                rec.setHeight(imm.getFitHeight());
                rec.setWidth(150.606);
                rec.setStroke(Color.web("#FFFFFF"));
                rec.setStrokeWidth(5);
                rec.setFill(Color.TRANSPARENT);
                spane.getChildren().add(rec);
                messageclick.setText("");
                spane.setOnMouseClicked(e -> deselectionTool(e));
            }
        }
    }

    /**
     * Deselects ToolCard
     * @param event click
     */
    private void deselectionTool(Event event){
        event.consume();
        if (toolCardInfoView.canDeselect()) {
            intconnection.sendMessage("tool deselected");
            intconnection.sendMessage(Integer.toString(pos));
            selected = false;
            endTool();
            spane.setOnMouseClicked(e -> selectionTool(e));
        }

    }

    /**
     *  If the server confirms that a player can play this tool card, it sends to the client the confirm and this method is invoked
     * @param nTokens how many tokens must be removed from the player
     */
    public void tokenConfirmed(int nTokens){
        tutorial1.setText(tutorials.get(id));
        toolCardInfoView.activate(id, pos, this);
        token.removeTokens(nTokens);
        IntStream.range(0, nTokens).forEach(i -> token.getChildren().remove(token.getChildren().size() - 1));
    }

    /**
     * Shows slider + button from value i to value i1
     * @param i start value slider
     * @param i1 end value slider
     */
    private void addSlider(int i, int i1){
        slider = new SliderValue(i, i1);
        enter = new JFXButton("OK");
        vboxtool.getChildren().addAll(slider,enter);
        enter.setOnMouseClicked(event->{
                                        if(toolCardInfoView.isDiceViewActivated()) {
                                            int response = slider.getNumber();
                                            System.out.println("hai selezionato la faccia");
                                            toolCardInfoView.addSliderResponse(response, id);
                                        }
        });
    }

    /**
     * Graphic deselection
     */
    public void endTool(){
        spane.getChildren().remove(getChildren().size()-1);
        tutorial1.setText("");
        selected=false;
        spane.setOnMouseClicked(t->selectionTool(t));
    }

    /**
     *
     * @return if toolcard is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Shows message that appears when touch toolcard without click
     */
    public void showMessage(){
        messageclick.setText("Clicca per usare!");
    }

    /**
     * Hides message that appears when touch toolcard without click
     */
    public void deleteMessage(){
        messageclick.setText("");
    }

    /**
     * Shows message of price toolcard
     */
    public void addToken(){
        Platform.runLater(() -> price.setText("Costo: 2 token"));
    }

    /**
     * Autodeselects toolcard if it's too expensive
     */
    public void notEnoughToken() {
        endTool();
    }

    /**
     * If the server confirms that a player can deselect a tool card, it sends to the client the confirm and this method is invoked
     * @param i1 the number of tokens that a player gets back
     */
    public void removeTokens(int i1) {
        token.addNewTokens(i1);
    }

    /**
     * Set price toolcard price 1 token
     */
    public void setToolOneToken() {
        Platform.runLater(() -> price.setText("Costo: 1 token"));
    }

    /**
     *
     * @return toolcard ID
     */
    public int getID(){
        return id;
    }
}