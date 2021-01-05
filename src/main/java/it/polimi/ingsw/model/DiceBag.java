package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiceBag {

    private ArrayList<Dice> dices;

    public DiceBag(){
        dices = new ArrayList<>();
    }

    /**
     *
     * @param colors a list of colors equivalent to the dices wanted to insert
     */
    public void insertDices(List<Character> colors){
       colors.forEach(c -> {
           try {
               dices.add(new Dice(c));
           } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
               e.printStackTrace();
           }
       });
       shuffle();
    }

    /**
     *
     * @param howManyDices the amount of dices that needs to be extract
     * @return a list of dices selected randomly
     */
    public ArrayList <Dice> extract(int howManyDices) {
        ArrayList<Dice> dicesToSend  = dices.stream().limit(howManyDices).collect(Collectors.toCollection(ArrayList::new));
        dicesToSend.forEach(d -> {dices.remove(d);
                                    d.throwDice();});
        return dicesToSend;
    }

    private void shuffle(){
        Collections.shuffle(dices);
    }


}

