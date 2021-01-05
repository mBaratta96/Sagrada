package it.polimi.ingsw.view.toolcard;

import it.polimi.ingsw.model.cards.SchemaCard;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.stream.IntStream;

public class TokenView extends GridPane{
    private static int RADIUS_TOK = 5;
    private int token;

    public TokenView(int token){
        this.token=token;
        IntStream.range(0, token)
                .forEach(i -> {
                    Circle circle = new Circle(RADIUS_TOK);
                    circle.setFill(Color.web("#CDF2EE"));
                    add(circle, i-(3*(i/3)), i/3);
                });

        setHgap(3);
        setVgap(3);
    }

    /**
     * Add tokens
     * @param i1 number of tokens added
     */
    public void addNewTokens(int i1) {
        IntStream.range(0, i1)
                .forEach(i -> {
                    Circle circle = new Circle(RADIUS_TOK);
                    circle.setFill(Color.web("#CDF2EE"));
                    add(circle, (token+i)-(3*((token+i)/3)), (token+i)/3);
                });
        token+=i1;
    }

    /**
     * remove tokens
     * @param nTokens number of tokens removed
     */
    public void removeTokens(int nTokens) {
        token-=nTokens;
    }
}

