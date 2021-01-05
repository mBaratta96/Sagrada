package it.polimi.ingsw.view;

import it.polimi.ingsw.view.schemacard.SchemaOP;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.xml.validation.Schema;

public class OPView extends VBox{
    private static final int SPACING=5;

    private SchemaOP schema;
    private String username;
    private Label user;

    public OPView(SchemaOP schema, Label user){
        setSpacing(SPACING);
        setAlignment(Pos.CENTER);

        this.schema=schema;
        this.user=user;
        this.username=user.getText();

        getChildren().addAll(schema,user);
    }

    /**
     * Shows to other players that user is offline
     */
    public void offlineFlag(){
        System.out.println("FINE");
        Platform.runLater(
                () -> {
                    user.setText(username+" è offline");
                }
        );
    }

    /**
     * Shows to other players that user in online again
     */
    public void onlineFlag(){
        Platform.runLater(() -> this.user.setText(username));
    }

    /**
     *
     * @return username of player
     */
    public String getUsername() {
        return username;
    }
}
