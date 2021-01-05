package it.polimi.ingsw.model.cards;

public abstract class Card {

    protected String name;
    protected int id;
    protected String info;

    /**
     *
     * @return the name of the card
     */
    public abstract String getName();

    /**
     *
     * @return the identifier of the card. used by the controller to select the right operation to do
     */
    public abstract int getID();

    /**
     *
     * @return the description of the card according to the documentation/instruction that has been given bt the developer of the game
     */
    public abstract String getInfo();

    public Card(String name, int id, String info) {
        this.name=name;
        this.id=id;
        this.info= info;
    }
}
