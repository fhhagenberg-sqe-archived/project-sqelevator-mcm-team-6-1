package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class FloorRequestLabel extends HBox {
    public FloorRequestLabel(ElevatorFloor elevatorFloor) {
        Label label = new Label("No\nRequest");
        label.setBorder(BorderStyle.THIN_BLACK.value());

        label.setPadding(new Insets(5));
        label.setTextAlignment(TextAlignment.CENTER);

        label.textProperty().bind(
                Bindings.when(elevatorFloor.getUpRequest())
                        .then("UP\nRequest")
                        .otherwise(
                                Bindings.when(elevatorFloor.getDownRequest())
                                        .then("DOWN\nRequest")
                                        .otherwise("No\nRequest")));

        label.backgroundProperty().bind(
                Bindings.when(elevatorFloor.getUpRequest().or(elevatorFloor.getDownRequest()))
                        .then(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));
        label.setId("FloorRequestLabel");
        this.setPrefHeight(60.0);
        this.setPadding(new Insets(10));
        this.getChildren().add(label);
    }
}
