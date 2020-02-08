package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.BorderStyle;
import at.fhhagenberg.sqelevator.domain.Elevator;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PayloadControl extends BorderPane {
    public PayloadControl(Elevator elevator) {
        Label payloadLabel = new Label("Payload: ");
        Label currentPayloadLabel = new Label("0 kg");
        currentPayloadLabel.textProperty().bind(elevator.getPayload().asString().concat(" kg"));
        currentPayloadLabel.setPadding(new Insets(0, 5, 0, 15));

        currentPayloadLabel.setId("PayloadControlLabel");
        payloadLabel.setStyle("-fx-font-size: 20;");
        currentPayloadLabel.setStyle("-fx-font-size: 20;");

        this.setLeft(payloadLabel);
        this.setRight(currentPayloadLabel);
        this.setBorder(BorderStyle.THIN_BLACK.value());
        this.setPadding(new Insets(15, 5, 15, 5));

    }
}
