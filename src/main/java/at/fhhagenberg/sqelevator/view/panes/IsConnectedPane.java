package at.fhhagenberg.sqelevator.view.panes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class IsConnectedPane extends HBox {
    public IsConnectedPane(BooleanProperty isConnectedProperty) {
        Circle isConnectedStatus = new Circle(8.0);

        isConnectedStatus.fillProperty().bind(
                Bindings.when(isConnectedProperty)
                        .then(Color.GREEN)
                        .otherwise(Color.RED));

        isConnectedStatus.setId("IsConnectedStatus");
        VBox connectedStatusBox = new VBox(isConnectedStatus);
        connectedStatusBox.setPadding(new Insets(6, 5, 0, 0));

        Label isConnectedLabel = new Label(" connected to system");
        isConnectedLabel.setStyle("-fx-font-size: 20;");

        this.getChildren().addAll(connectedStatusBox, isConnectedLabel);
    }
}
