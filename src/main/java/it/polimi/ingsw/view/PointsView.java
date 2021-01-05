package it.polimi.ingsw.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;



public class PointsView extends HBox{
    private static final int SPACING = 20;

    private Label pos;
    private Label username;
    private Label points;

    public PointsView(int pos, String username, int points){

        this.pos = new Label(Integer.toString(pos));
        this.pos.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 20px");

        this.username = new Label(username);
        this.username.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 20px");

        this.points = new Label(Integer.toString(points)+" punti");
        this.points.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 20px");
        this.points.setAlignment(Pos.CENTER_RIGHT);

        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
        getChildren().addAll(this.pos, this.username, this.points);
    }
}
