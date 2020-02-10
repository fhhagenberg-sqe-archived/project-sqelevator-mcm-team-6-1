package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import at.fhhagenberg.sqelevator.domain.DoorStatus;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.view.style.ControlLabel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class DoorsControl extends HBox {

    public DoorsControl(Elevator elevator) {
        Label modeLabel = new Label("Doors");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label open = new ControlLabel("Open");
        open.setId("Open");
        Label closed = new ControlLabel("Closed");
        closed.setId("Closed");

        open.backgroundProperty().bind(
                Bindings.when(elevator.doorStatusProperty().isEqualTo(DoorStatus.OPEN))
                        .then(new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        closed.backgroundProperty().bind(
                Bindings.when(elevator.doorStatusProperty().isEqualTo(DoorStatus.CLOSED))
                        .then(new Background(new BackgroundFill(Color.rgb(255, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        this.getChildren().addAll(modeLabel, open, closed);
        this.setPadding(new Insets(10));
        this.setBorder(BorderStyle.THIN_BLACK.value());
        this.setId("DoorControl");
    }
}
