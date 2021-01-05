package it.polimi.ingsw.model.cards;

public class SchemaCardBackFront {
    private SchemaCard front;
    private SchemaCard back;

    public SchemaCardBackFront(SchemaCard front, SchemaCard back) {
        this.back = back;
        this.front = front;
    }

    /**
     *
     * @return the back face of a schema card
     */
    public SchemaCard getBack() {
        return back;
    }

    /**
     *
     * @return the front face of a schema card
     */
    public SchemaCard getFront() {
        return front;
    }
}
