package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.model.actions.toolCardActions.PreliminaryCheck;
import it.polimi.ingsw.model.actions.toolCardActions.ToolCardAction;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.exceptions.NotValidException;

public class ToolCard extends Card {

    private int n_token;
    private ToolCardAction action;
    private int tokenRequired;




    @Override
    public String getName() {
        return super.name;
    }

    @Override
    public int getID() {
        return super.id;
    }

    @Override
    public String getInfo() {
        return super.info;
    }

    /**
     * When a tool card is selected, one or two tokens are added to it. According to the number, the required tokens for future usage may increment
     * @param n_tokens the number of added tokens
     */
    public void addToken(int n_tokens){
        if(tokenRequired==1){
            tokenRequired=2;
        }
        n_token+=n_tokens;
    }

    /**
     * This method is invoked when a player asks the server if he can play a tool card.
     * @param game the game in its current state
     * @return confirm/denial of the possibility of using a tool card.
     */
    public boolean getPreCheck(Game game){
        return PreliminaryCheck.getPreCheck(id).test(game);
    }

    /**
     *
     * @return the number of required tokens to play the tool card
     */
    public int getTokenRequired(){
        return tokenRequired;
    }

    /**
     * This method set the right succession of ToolCardAction that must take place in order to execute the tool card
     * @param game the game in its current state
     */
    public void setAction(Game game) {
        this.action = ToolCardAction.getInstance(id, game);
    }

    /**
     * When a tool card has done its operation, the reference to the action is canceled
     */
    public void resetAction(){
        action = null;
    }

    /**
     *
     * @return the number of tokens placed on the tool card
     */
    public int showToken(){
        return n_token;
    }

    public ToolCard(String name, int id, String info){
        super(name, id, info);
        this.n_token=0;

    }

    /**
     *
     * @param n the number of tokens that needs to be removed from the tool card
     */
    public void removeTokens(int n){
        n_token-=n;
        if(n_token==0){
            tokenRequired=1;
        }
    }

    /**
     * This method is invoked in order to exceture the tool card functionality
     * @param toolCardInfo an object containing all the necessary information to execute the tool card
     * @throws NotValidException if some of the information in not valid or doesn't respect the restrictions imposed by the game
     */
    public void activate(ToolCardInfo toolCardInfo) throws NotValidException {
        action.perform(id, toolCardInfo, false);

    }

    /**
     *
     * @param i How many tokens are needed to selecte the tool card the first time
     */
    public void setTokenRequired(int i) {
        tokenRequired=i;
    }
}
