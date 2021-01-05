package it.polimi.ingsw.model.cards;



import it.polimi.ingsw.model.SchemaCell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SchemaCard extends Card {

    private ArrayList<SchemaCell> cells;

    public SchemaCard(String name, int id, String info, String schemaCellString){
        super(name, id, info);
        cells = new ArrayList<>();
        for(char c : schemaCellString.toCharArray()){
            try {
                cells.add(new SchemaCell(c));
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @return all the cells (white or with restriction) that compose the schema card
     */
    public List<SchemaCell> getSchema(){
        return cells;
    }

    @Override
    public String toString() {
        String objectDescription = String.format("Nome: %s%nId: %d%nInfo: %s%n", name, id, info);
        objectDescription += cells.stream()
                                    .map(c -> (cells.indexOf(c)+1)%5==0 ? c.toString()+'\n' : c.toString())
                                    .collect(Collectors.joining());
        return objectDescription;
    }


}
