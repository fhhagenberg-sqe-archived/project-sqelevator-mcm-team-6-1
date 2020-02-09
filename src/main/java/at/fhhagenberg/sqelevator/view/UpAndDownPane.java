package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

public class UpAndDownPane extends HBox {
    public UpAndDownPane(ElevatorFloor elevatorFloor) {
        elevatorFloor.setUpRequest(true);
        GridPane pane = new GridPane();
        Label upLabel = new Label();
        upLabel.setText("Up");
        upLabel.setStyle("-fx-font-size: 20;");

        Label downLabel = new Label();
        downLabel.setText("Down");
        downLabel.setStyle("-fx-font-size: 20;");

        // bind border color to up property
        upLabel.borderProperty().bind(
                Bindings.when(elevatorFloor.getUpRequest())
                        .then(BorderStyle.STANDARD_DARKBLUE.value())
                        .otherwise(BorderStyle.THIN_BLACK.value())

        );

        // bind border color to down property
        downLabel.borderProperty().bind(
                Bindings.when(elevatorFloor.getDownRequest())
                        .then(BorderStyle.STANDARD_DARKBLUE.value())
                        .otherwise(BorderStyle.THIN_BLACK.value())

        );

        upLabel.setPrefHeight(60.0);
        downLabel.setPrefHeight(60.0);
        upLabel.setPrefWidth(60.0);
        downLabel.setPrefWidth(60.0);

        upLabel.setTextAlignment(TextAlignment.CENTER);
        downLabel.setTextAlignment(TextAlignment.CENTER);
        upLabel.setAlignment(Pos.CENTER);
        downLabel.setAlignment(Pos.CENTER);

        downLabel.setId("DownLabel");
        upLabel.setId("UpLabel");

        pane.addColumn(0, upLabel);
        pane.addColumn(1, downLabel);
        this.setPadding(new Insets(10));
        this.getChildren().add(pane);
    }
}
