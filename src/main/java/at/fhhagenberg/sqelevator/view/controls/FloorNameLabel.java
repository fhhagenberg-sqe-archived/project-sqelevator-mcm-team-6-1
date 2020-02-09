package at.fhhagenberg.sqelevator.view.controls;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FloorNameLabel extends HBox {
    public FloorNameLabel(int floorNumber) {
        Label floorLabel = new Label(String.format("Floor %d", floorNumber));

        floorLabel.setStyle("-fx-font-size: 20;");
        this.getChildren().add(floorLabel);

        this.setPadding(new Insets(25, 50, 25, 50));
    }
}
