package it.polimi.ingsw.model.deks;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.PublicTargetCard;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PublicTargetCardDeck {

    private ArrayList<PublicTargetCard> deck;

    public PublicTargetCardDeck() {
       deck = new ArrayList<>();
    }
    /**
     *
     * @return the deck as composed deserializing from the respective json file
     */
    public ArrayList<PublicTargetCard> getDeck() {
        return deck;
    }


}
