package it.polimi.ingsw.model.deks;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.ToolCard;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolCardDeck {

    private ArrayList<ToolCard> deck;



    public ToolCardDeck(){
        deck = new ArrayList<>();
    }
    /**
     *
     * @return the deck as composed deserializing from the respective json file
     */
    public ArrayList<ToolCard> getDeck() {
        return deck;
    }


}
