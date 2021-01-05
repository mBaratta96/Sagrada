package it.polimi.ingsw.view.toolcard;

import com.google.gson.Gson;
import it.polimi.ingsw.model.ToolCardInfo;


import it.polimi.ingsw.net.client.socket.ClientSocket;
import it.polimi.ingsw.view.dice.DiceView;
import it.polimi.ingsw.view.schemacard.SquareUser;
import javafx.application.Platform;

import java.util.ArrayList;


public class ToolCardInfoView {

    private ClientSocket connectionInterface;
    private boolean myTurn = false;
    private boolean faceChosen;
    private boolean twoDices;
    private boolean firstStep=false;
    private ArrayList<SquareUser> squareUsers;
    private ArrayList<DiceView> diceViews;
    private int id;
    private int pos;
    private boolean upDownSetted = false;
    private ToolCardInfo toolCardInfo;
    private int placedTokens;
    private ToolCardView toolCardView;


    public ToolCardInfoView(ClientSocket connectionInterface){
        id=0;
        this.connectionInterface=connectionInterface;
        toolCardInfo= new ToolCardInfo();
        diceViews = new ArrayList<>();
        squareUsers = new ArrayList<>();
    }
    /**
     * Sets the prohibition of the view to execute a tool card
     */
    public synchronized void notMyTurn(){

        myTurn=false;
        if(id!=0){
            Platform.runLater(() -> toolCardView.endTool());
            id=0;
        }
    }
    /**
     * Sets the possibility of the view to execute a placement
     */
    public synchronized void myTurn(){
        myTurn = true;
        if(id!=0){
            Platform.runLater(() -> toolCardView.endTool());
            id=0;
        }
    }
    /**
     *
     * @return a flag the allows or not the view to execute a placement
     */
    public synchronized boolean isMyTurn() {
        return myTurn;
    }

    /**
     * This method is invoked when a tool card is selected. The class becomes ready to get all the information of the move of the player
     * @param id the id of the card
     * @param pos the position of the card in the game
     * @param toolCardView the view's representation of the card
     */
    public void activate(int id, int pos, ToolCardView toolCardView){
        this.id=id;
        this.pos=pos;
        this.toolCardView=toolCardView;
        System.out.println("carta tool "+id+" attivata");
        if(id==7){
            toolCardInfo.setIndex_i(1);
            sendToServer();
        }
        if (id == 4 || id==11) {
            firstStep=true;
        }
    }

    /**
     *
     * @return a flag that notifies whether or not a dice has been selected
     */
    public boolean isDiceViewActivated() {
        return id != 0;
    }

    /**
     *
     * @return a flag that notifies whether or not a grid's cell has been selected
     */
    public boolean isSquareUserActivated(){
        return id!=0 || firstStep;
    }

    /**
     * This method add the grid's cell where some operation needs to take place (the addition of a dice, for example)
     * @param squareUser  the view's representation of the grid's cell
     */
    public void addSquareUser(SquareUser squareUser){
        if(toolCardInfo.getIndex_i()==-1){
            squareUser.deselection();
        }else if(id==11 && faceChosen){
            toolCardInfo.setSecondStep11(true);
            toolCardInfo.setIndex_f(squareUser.getIndex());
            sendToServer();
            squareUsers.add(squareUser);
            id=0;
            faceChosen=false;
        }else {
            squareUsers.add(squareUser);
            addIndex(squareUser.getIndex());
            System.out.println("addedSquareUser " + squareUser.getIndex());
            send();
        }
    }
    /**
     * This method add the dice on which some operation needs to take place
     * @param diceView  the view's representation of the dice
     */
    public void addDiceView(DiceView diceView) {
        diceViews.add(diceView);
        if (id == 5 && diceView.isFromRoundTrack() && toolCardInfo.getIndex_i()==-1) {
                toolCardInfo.setIndex_i(0);
                addIndex(diceView.getPos());
                toolCardInfo.setIndex_i(-1);
        } else
            addIndex(diceView.getPos());
        System.out.println("addedDiceView "+diceView.getPos());
        send();
    }



    private void addIndex(int i){
        if (toolCardInfo.getIndex_i()== -1) {
            toolCardInfo.setIndex_i(i);
            if(id==6 || id==10){
                toolCardInfo.setIndex_f(0);
            }
            if (id==11 && !faceChosen){
                toolCardInfo.setIndex_f(0);
            }
        } else if(toolCardInfo.getIndex_f()== -1){
            toolCardInfo.setIndex_f(i);
            if(id==5)
                toolCardInfo.setIndex_f(toolCardInfo.getIndex_f()-1);
        }else if(toolCardInfo.getIndex_i2()== -1){
            toolCardInfo.setIndex_i2(i);
        }else if(toolCardInfo.getIndex_f2()== -1){
            toolCardInfo.setIndex_f2(i);
        }
    }

    private void send(){
        if(toolCardInfo.getIndex_i() != -1 && toolCardInfo.getIndex_f() != -1) {
           sendToServer();
        }

    }
    private void sendToServer(){
        if(id==4 && !firstStep || id==12 && !twoDices){
            toolCardInfo.setSecondStep11(true);
        }
        connectionInterface.sendMessage("toolCard");
        connectionInterface.sendMessage(pos+"___"+new Gson().toJson(toolCardInfo));
        System.out.println("sended");
        toolCardInfo.resetIndex();

    }

    void addSliderResponse(int response, int i) {
        if(i ==1){
            if(toolCardInfo.getIndex_i()==-1){
                notValidToolCard();
            } else {
                toolCardInfo.setUpDown(response == 1);
                sendToServer();
            }
        }else if(i ==11 && !firstStep) {
            toolCardInfo.setChosenFace(response);
            faceChosen=true;
            sendToServer();
        }else if(i==12 && response==2){
            twoDices=true;
            send();
        }
    }

    /**
     * This method is invoked when a tool card move is not valid and the deselection of the elements on the view is needed
     */
    public void notValidToolCard() {
        Platform.runLater(() -> {
            squareUsers.forEach(SquareUser::deselection);
            diceViews.forEach(DiceView::deselectionDice);
            diceViews.clear();
            squareUsers.clear();
            if(id==7) {
                id = 0;
                toolCardView.endTool();
            }
        });

    }
    /**
     * This method is invoked when a tool card move is valid and the class becomes ready to get new information.
     * For some tool cards the procedure is taken in multiple steps, so the cards isn't immediately deselected
     */
    public void valid() {
        Platform.runLater(() -> {
            diceViews.clear();
            squareUsers.clear();
        });
        if(!((id==11 && firstStep) || (id==4 && firstStep) || (id==12 && twoDices))) {
            id = 0;
            Platform.runLater(() -> toolCardView.endTool());
        }
        if((id==11 && firstStep) || (id==4 && firstStep)){
            firstStep=false;
        }
        if(id==12 && twoDices){
            twoDices=false;
        }
    }

    /**
     * If a tool card can be deselected, it reset the information of the class
     * @return a flag that checks if a tool card on the view can/can't be deselected
     */
    public boolean canDeselect() {
        if(!diceViews.isEmpty() || !squareUsers.isEmpty())
            return false;
        id=0;
        return true;
    }

    /**
     * If a dice on the view can be deselected, that dice is removed from the information of the class
     * @param diceView the deselected dice
     */
    public void deselectDiceView(DiceView diceView) {
        if(toolCardInfo.getIndex_i()==diceView.getPos()){
            toolCardInfo.setIndex_i(-1);
        }else{
            toolCardInfo.setIndex_f(-1);
        }
        diceViews.remove(diceView);
    }

    /**
     *
     * @param diceView the selected dice
     * @return a flag that notifies whether or not a player can select another dice during the playing of a tool card
     */
    public boolean canSelectNewDice(DiceView diceView) {
        if(id!=5 && id!=7){
            if(!diceViews.isEmpty())
                return false;
        }
        if((id==1||id==6||id==8||id==9||id==10||id==11) && !diceView.isFromReserve())
            return false;
        if(id==5 && diceView.isFromGrid())
            return false;
        if((id==2||id==3||id==4||id==12) && !diceView.isFromGrid())
            return false;
        if(id==11 && !firstStep && !faceChosen)
            return false;
        return true;
    }


}
