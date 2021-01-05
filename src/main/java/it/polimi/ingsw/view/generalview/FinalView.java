package it.polimi.ingsw.view.generalview;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.view.PointsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class FinalView extends Parent {
    private static final int SIZE_W = 1280;
    private static final int SIZE_H = 700;
    private static final int LEADERBOARD_SPACING = 20;
    private static final int VSPACING=50;
    private static final int PADDING_W = 300;
    private static final int PADDING_H = 40;

    private StackPane spane;


    public FinalView(ArrayList<Player> players){
        spane=new StackPane();

        ImageView wallpaper = new ImageView(new Image("view/wallpaper.png", SIZE_W, SIZE_H,false,true));
        wallpaper.fitWidthProperty().bind(spane.widthProperty());
        wallpaper.fitHeightProperty().bind(spane.heightProperty());

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(PADDING_H,PADDING_W,PADDING_H,PADDING_W));
        vbox.setSpacing(VSPACING);

        Label winner_is = new Label("Il vincitore è:");
        winner_is.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 40px");

        Label user_winner = new Label(players.get(0).getUsername());
        user_winner.setStyle("-fx-text-fill:#a67c00; -fx-font-family: Palatino ; -fx-font-size: 70px");

        Label leaderboard = new Label("Classifica Generale");
        leaderboard.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 30px");

        VBox leaderboardbox = new VBox();
        leaderboardbox.setAlignment(Pos.CENTER);
        leaderboardbox.setSpacing(LEADERBOARD_SPACING);

        int i = 1;
        for(Player player : players){

            PointsView points_view = new PointsView(i,player.getUsername(), player.getPoints());
            leaderboardbox.getChildren().add(points_view);
            i++;
        }

        vbox.getChildren().addAll(winner_is,user_winner,leaderboard,leaderboardbox);

        spane.getChildren().addAll(wallpaper,vbox);

        getChildren().add(spane);
    }

    /**
     *
     * @return StackPane with wallpaper
     */
    public StackPane getSpane() {
        return spane;
    }
}
