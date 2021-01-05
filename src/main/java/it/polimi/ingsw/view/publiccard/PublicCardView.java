package it.polimi.ingsw.view.publiccard;

import it.polimi.ingsw.model.cards.PublicTargetCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

public class PublicCardView extends StackPane {
    private ImageView imm;

    private static final int HEIGHT =210;
    private static final HashMap<Integer,Image> cards= new HashMap<>();
    static{
        cards.put(0,new Image("view/publiccard/0.png") );
        cards.put(1,new Image("view/publiccard/1.png") );
        cards.put(2,new Image("view/publiccard/2.png") );
        cards.put(3,new Image("view/publiccard/3.png") );
        cards.put(4,new Image("view/publiccard/4.png") );
        cards.put(5,new Image("view/publiccard/5.png") );
        cards.put(6,new Image("view/publiccard/6.png") );
        cards.put(7,new Image("view/publiccard/7.png") );
        cards.put(8,new Image("view/publiccard/8.png") );
        cards.put(9,new Image("view/publiccard/9.png") );
    }

    public PublicCardView (PublicTargetCard card){
        int n = card.getID();
        imm=new ImageView(cards.get(n));
        imm.setPreserveRatio(true);
        imm.setFitHeight(HEIGHT);
        getChildren().add(imm);
    }

}
