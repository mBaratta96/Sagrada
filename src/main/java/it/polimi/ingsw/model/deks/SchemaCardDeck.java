package it.polimi.ingsw.model.deks;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.model.cards.SchemaCardBackFront;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaCardDeck {
    private ArrayList<SchemaCardBackFront> deck;


    private SchemaCardDeck() {
        deck = new ArrayList<>();
    }
    /**
     *
     * @return the deck as composed deserializing from the respective json file
     */
    public ArrayList<SchemaCardBackFront> getDeck() {
        return deck;
    }

    @Override
    public String toString() {
       return deck.stream()
                    .map(card -> card.toString())
                    .collect(Collectors.joining());
    }


}
