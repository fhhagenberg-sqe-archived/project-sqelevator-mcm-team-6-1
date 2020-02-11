package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import at.fhhagenberg.sqelevator.domain.Elevator;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SpeedControl extends BorderPane {
    public SpeedControl(Elevator elevator) {
        Label speedLabel = new Label("Speed: ");
        Label currentSpeedLabel = new Label("0 km/h");
        currentSpeedLabel.textProperty().bind(elevator.velocityProperty().asString().concat(" km/h"));
        currentSpeedLabel.setPadding(new Insets(0, 5, 0, 15));

        speedLabel.setStyle("-fx-font-size: 20;");
        currentSpeedLabel.setStyle("-fx-font-size: 20;");
        currentSpeedLabel.setId("SpeedControlLabel");
        this.setLeft(speedLabel);
        this.setRight(currentSpeedLabel);
        this.setBorder(BorderStyle.THIN_BLACK.value());
        this.setPadding(new Insets(15, 5, 15, 5));
    }
}
