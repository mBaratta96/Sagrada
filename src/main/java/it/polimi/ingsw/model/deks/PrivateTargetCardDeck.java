package it.polimi.ingsw.model.deks;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.PrivateTargetCard;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PrivateTargetCardDeck {

    private ArrayList<PrivateTargetCard> deck;


    public PrivateTargetCardDeck() {
       deck=new ArrayList<>();
    }

    /**
     *
     * @return the deck as composed deserializing from the respective json file
     */
    public ArrayList<PrivateTargetCard> getDeck() {
        return deck;
    }


}
