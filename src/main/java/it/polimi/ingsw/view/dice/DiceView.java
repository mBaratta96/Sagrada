package it.polimi.ingsw.view.dice;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.view.toolcard.ToolCardInfoView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.util.HashMap;


public class DiceView extends StackPane {

    private Rectangle rec;
    private Rectangle modifiedrec;
    private ImageView face;
    private int pos;
    private AddDiceView adView;
    private ToolCardInfoView toolCardInfoView;
    private boolean placed;
    private boolean fromRoundTrack;
    private boolean fromReserve;
    private boolean fromGrid;
    private char c;
    private int value;

    private static final int SIZE_W = 30;
    private static final int SIZE_H = 30;
    private static final HashMap<Character,Color > colors= new HashMap<>();
    static{
        colors.put('b',Color.web("#2DAABE") );
        colors.put('p',Color.web("#996299") );
        colors.put('g',Color.web("#A1BB51") );
        colors.put('r',Color.web("#E54245") );
        colors.put('y',Color.web("#F8D330") );
        colors.put('+',Color.web("#FFFFFF") );
    }

    private static final HashMap<Integer,Image > faces= new HashMap<>();
    static{
        faces.put(1,new Image("view/dadifacce/dado1.png", SIZE_W, SIZE_H,true,false) );
        faces.put(2,new Image("view/dadifacce/dado2.png", SIZE_W, SIZE_H,true,false) );
        faces.put(3,new Image("view/dadifacce/dado3.png", SIZE_W, SIZE_H,true,false) );
        faces.put(4,new Image("view/dadifacce/dado4.png", SIZE_W, SIZE_H,true,false) );
        faces.put(5,new Image("view/dadifacce/dado5.png", SIZE_W, SIZE_H,true,false) );
        faces.put(6,new Image("view/dadifacce/dado6.png", SIZE_W, SIZE_H,true,false) );
    }

    public DiceView(char c , int value, int pos, AddDiceView adView, ToolCardInfoView toolCardInfoView) {
        this.c=c;
        this.value=value;
        this.toolCardInfoView=toolCardInfoView;
        rec = new Rectangle(SIZE_W, SIZE_H);
        rec.setFill(colors.get(c));
        rec.setStroke(Color.BLACK);
        face = new ImageView(faces.get(value));
        getChildren().add(rec);
        getChildren().add(face);
        this.pos=pos;
        this.adView = adView;
        this.placed=false;
        rec.setOnMouseClicked(event -> selectionDice(event));
        face.setOnMouseClicked(event-> selectionDice(event));
        fromRoundTrack=false;
        fromGrid=false;
        fromReserve=false;
    }

    /**
     *
     * This is a getter which gets pos
     * @return index of the dice on the reserve
     */
    public int getPos() {
        return pos;
    }

    /**
     *
     * @return a flag to check if the dice is from the roundtrack
     */
    public boolean isFromRoundTrack() {
        return fromRoundTrack;
    }

    /**
     * This is a setter which sets if the dice is from the roundtrack
     * @param fromRoundTrack boolean to be set
     */
    public void setFromRoundTrack(boolean fromRoundTrack) {
        this.fromRoundTrack = fromRoundTrack;
    }

    /**
     *
     * @return a flag to check if the dice is from grid
     */
    public boolean isFromGrid() {
        return fromGrid;
    }

    /**
     *
     * @return a flag to check if the dice is from reserve
     */
    public boolean isFromReserve() {
        return fromReserve;
    }

    /**
     * This is a setter which sets if the dice is from the grid
     * @param fromGrid boolean to be set
     */
    public void setFromGrid(boolean fromGrid) {
        this.fromGrid = fromGrid;
    }

    /**
     * This is a setter which sets if the dice is from reserve
     * @param fromReserve boolean to be set
     */
    public void setFromReserve(boolean fromReserve) {
        this.fromReserve = fromReserve;
    }

    /**
     * This method shows a graphic selection on dice clicked and save its info on adView only if it's player's turn
     * @param event click on a dice
     */
    public void selectionDice(MouseEvent event){
        event.consume();
        if(!adView.isMyTurn()) {
            System.out.println("It's not your turn!");
        }else if(toolCardInfoView.isDiceViewActivated()){
            if(!toolCardInfoView.canSelectNewDice(this)){
                return;
            }
            System.out.println("ho selezionato il dado "+c+' '+value);
            modifiedrec = new Rectangle(SIZE_W, SIZE_H);
            modifiedrec.setFill(Color.web("#000000", 0.3));
            getChildren().add(modifiedrec);
            toolCardInfoView.addDiceView(this);
            modifiedrec.setOnMouseClicked(event1 -> deselectionDice(event1));
            face.setOnMouseClicked(event1 -> deselectionDice(event1));
        }else if(!placed && adView.getPos_reserve_selected()==-1){
            System.out.println("ho selezionato il dado "+c+' '+value);
            modifiedrec = new Rectangle(SIZE_W, SIZE_H);
            modifiedrec.setFill(Color.web("#000000", 0.3));
            getChildren().add(modifiedrec);
            if(fromReserve) {
                adView.addDiceReserve(pos, this);
            }
            System.out.println("Dice selected!");
            modifiedrec.setOnMouseClicked(event1 -> deselectionDice(event1));
            face.setOnMouseClicked(event1 -> deselectionDice(event1));
        }   else {
            System.out.println("Please, select dice!");
        }
    }

    /**
     * This method hides a graphic selection on dice clicked
     * @param event click on a dice
     */
    public void deselectionDice(MouseEvent event){
        event.consume();
        if(toolCardInfoView.isDiceViewActivated()){
            toolCardInfoView.deselectDiceView(this);
        }
        else if(adView.getPos_reserve_selected()!=-1){
            adView.removeSelection();
        }
        getChildren().remove(getChildren().size()-1);
        System.out.println("Dice deselected");
        rec.setOnMouseClicked(event2->selectionDice(event2));
        face.setOnMouseClicked(event2->selectionDice(event2));
    }

    /**
     * This method hides a graphic selection on dice
     */
    public void deselectionDice(){
        getChildren().remove(getChildren().size()-1);
        System.out.println("Dice deselected");
        rec.setOnMouseClicked(event2->selectionDice(event2));
        face.setOnMouseClicked(event2->selectionDice(event2));
    }

    public void autoDeselectionDice(){
        Label invisible_lab=new Label("");
        getChildren().remove(getChildren().size()-1);
        getChildren().add(invisible_lab);
    }

    /**
     * This method translates a DiceView object in Dice object
     * @return Dice object with info
     */
    public Dice toDice(){
        Dice d = null;
        try {
            d = new Dice(c);
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        try {
            if (d != null) {
                d.setFace(value);
            }
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        return d;
    }

    public void changeFace(int i) {
        face = new ImageView(faces.get(i));
    }

    /**
     * This is a getter which gets face
     * @return image which represents value of dice
     */
    public ImageView getFace() {
        return face;
    }

    /**
     * This is a getter which gets rec
     * @return Rectangle with color of dice
     */
    public Rectangle getRec() {
        return rec;
    }

    /**
     * This is a setter which sets a new index of dice on the reserve
     * @param pos
     */
    public void updatePos(int pos){
        this.pos=pos;
    }

    public boolean isPlaced(){
        return placed;
    }

    /**
     *
     * @param placed sets if dice is placed on Schema
     */
    public void place(boolean placed){
        this.placed=placed;
    }
}
