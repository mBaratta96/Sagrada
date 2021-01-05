package it.polimi.ingsw.view.schemacard;

import it.polimi.ingsw.view.dice.AddDiceView;
import it.polimi.ingsw.view.dice.DiceView;
import it.polimi.ingsw.view.toolcard.ToolCardInfoView;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class SquareUser extends StackPane {

    private Rectangle modifiedrec;
    private Rectangle rec;
    private ImageView face;
    private AddDiceView adView;
    private int index;
    private ToolCardInfoView toolCardInfoView;
    private char cellConstraint;

    private static final Integer size_w= 30;
    private static final Integer size_h= 30;
    private static final Double opacity_selec=0.3;
    private static final String black = "#000000";

    private static final HashMap<Character,Color > colors= new HashMap<>();
    static{
        colors.put('b',Color.web("#2DAABE") );
        colors.put('p',Color.web("#996299") );
        colors.put('g',Color.web("#A1BB51") );
        colors.put('r',Color.web("#E54245") );
        colors.put('y',Color.web("#F8D330") );
        colors.put('+',Color.web("#FFFFFF") );
    }

    private static final HashMap<Character,Image> faces= new HashMap<>();
    static{
        faces.put('1',new Image("view/dadifacce/dado1.png",size_w,size_h,true,false) );
        faces.put('2',new Image("view/dadifacce/dado2.png",size_w,size_h,true,false) );
        faces.put('3',new Image("view/dadifacce/dado3.png",size_w,size_h,true,false) );
        faces.put('4',new Image("view/dadifacce/dado4.png",size_w,size_h,true,false) );
        faces.put('5',new Image("view/dadifacce/dado5.png",size_w,size_h,true,false) );
        faces.put('6',new Image("view/dadifacce/dado6.png",size_w,size_h,true,false) );
    }

    /**
     *Square with color restriction
     * @param c restriction
     * @param adView a instance of the AddDiceView class. When it's time to place a dice, a selected object of this class sends the information of its index to adView.
     * @param index position in grid
     * @param toolCardInfoView a instance of the ToolCardInfoView class. When it's time to play a tool card, a selected object of this class sends the information of its index to adView.
     */
    public SquareUser(char c,AddDiceView adView, int index, ToolCardInfoView toolCardInfoView) {
        if (colors.keySet().contains(c)) {
            squareColor(c);
        }else {
            squareNumb(c);

        }
        cellConstraint=c;
        this.adView=adView;
        this.index=index;
        this.toolCardInfoView=toolCardInfoView;
    }

    /**
     *
     * @param c restriction
     * @param index position in grid
     */
    public SquareUser(char c, int index) {
        if (colors.keySet().contains(c)) {
            squareColor(c);
        }else {
            squareNumb(c);
        }
        this.index=index;
    }

    /**
     * Method, after a dice removal from schema, shows restriction of square again
     */
    public void resetSchemaUser(){
        Platform.runLater(() -> {
            if (colors.keySet().contains(cellConstraint)) {
                squareColor(cellConstraint);
            }else {
                squareNumb(cellConstraint);
            }
        });
    }

    /**
     *Square with color restriction
     * @param c color
     */
    private void squareColor(char c) {
        rec= new Rectangle(size_w, size_h);
        rec.setFill(colors.get(c));
        rec.setStroke(Color.BLACK);
        face=new ImageView( new Image("view/blank.png",size_w,size_h,false,false));
        getChildren().addAll(rec,face);
        rec.setOnMouseClicked(event -> selection(event));
    }

    /**
     *Square with value restriction
     * @param c value
     */
    private void squareNumb(char c) {
        face = new ImageView(faces.get(c));
        rec= new Rectangle(size_w, size_h);
        rec.setFill(Color.WHITE);
        rec.setStroke(Color.BLACK);
        getChildren().addAll(rec,face);
        rec.setOnMouseClicked(event -> selection(event));
        face.setOnMouseClicked(event -> selection(event));
    }

    /**
     * Getter
     * @return index in grid
     */
    public int getIndex(){
        return index;
    }

    /**
     * Shows graphic selection on schema
     * @param event click
     */
   public void selection(MouseEvent event){
        event.consume();
        if(!adView.isMyTurn()) {
            System.out.println("It's not your turn!");
        }else if(toolCardInfoView.isSquareUserActivated()){
            System.out.println("ho selezionato la cella "+index);
            modifiedrec = new Rectangle(size_w, size_h);
            modifiedrec.setFill(Color.web(black, opacity_selec));
            getChildren().add(modifiedrec);
            toolCardInfoView.addSquareUser(this);
        }else if(adView.getPos_reserve_selected()!=-1){
            System.out.println("ho selezionato la cella "+index);
            modifiedrec = new Rectangle(size_w, size_h);
            modifiedrec.setFill(Color.web(black, opacity_selec));
            getChildren().add(modifiedrec);
            System.out.println("Grid selected!");
            adView.addDiceGrid(index, this);
            modifiedrec.setOnMouseClicked(event1 -> deselection(event1));
            face.setOnMouseClicked(event1 -> deselection(event1));
        }else{
            System.out.println("Please, select the dice!");
        }
    }

    /**
     * Hides graphic selection on schema
     */
    public void deselection(){
        getChildren().remove(getChildren().size()-1);
        System.out.println("Grid auto-deselected");
        rec.setOnMouseClicked(event2->selection(event2));
        face.setOnMouseClicked(event2->selection(event2));
    }

    /**
     * Hides graphic selection on schema
     * @param event click
     */
    private void deselection(MouseEvent event){
        event.consume();
        getChildren().remove(getChildren().size()-1);
        System.out.println("Grid deselected");
        rec.setOnMouseClicked(event2->selection(event2));
        face.setOnMouseClicked(event2->selection(event2));
    }

    /**
     * Adds DiceView on Square
     * @param d dice to add
     */
    public void addDice(DiceView d){
        Platform.runLater(() -> {/*d.autoDeselectionDice();*/
        getChildren().add(d);
        d.place(true);
        });
    }
}
