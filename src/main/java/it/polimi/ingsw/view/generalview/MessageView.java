package it.polimi.ingsw.view.generalview;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MessageView extends Parent {
    private static final int SIZE_W = 1280;
    private static final int SIZE_H = 700;
    private StackPane spane;
    private BorderPane bpane;

    public MessageView(String message){
        spane = new StackPane();
        bpane= new BorderPane();

        ImageView wallpaper = new ImageView(new Image("view/wallpaper.png", SIZE_W, SIZE_H,false,true));
        wallpaper.fitWidthProperty().bind(spane.widthProperty());
        wallpaper.fitHeightProperty().bind(spane.heightProperty());

        Label text_waiting = new Label(message);
        text_waiting.setStyle("-fx-text-fill:#FFFFFF; -fx-font-family: Palatino ; -fx-font-size: 40px");
        bpane.setCenter(text_waiting);
        spane.getChildren().addAll(wallpaper, bpane);


        getChildren().add(spane);
    }

    /**
     *
     * @return general StackPane with wallpaper
     */
    public StackPane getSpane() {
        return spane;
    }
}
