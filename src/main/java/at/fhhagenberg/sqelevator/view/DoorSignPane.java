package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DoorSignPane extends VBox {
    public DoorSignPane(Elevator elevator, ElevatorFloor elevatorFloor) {
        Rectangle doorOutline = new Rectangle(60, 60);
        doorOutline.setFill(Color.TRANSPARENT);
        doorOutline.setStroke(Color.GRAY);
        doorOutline.getStrokeDashArray().addAll(2.0, 2.0);

        Rectangle leftDoorPart = new Rectangle(5, 10, 24, 40);
        leftDoorPart.setFill(Color.TRANSPARENT);

        Rectangle rightDoorPart = new Rectangle(31, 10, 24, 40);
        rightDoorPart.setFill(Color.TRANSPARENT);

        leftDoorPart.strokeProperty().bind(
                Bindings.when(elevator.getCurrentElevatorFloor().isEqualTo(elevatorFloor))
                        .then(Color.GRAY)
                        .otherwise(Color.TRANSPARENT));

        rightDoorPart.strokeProperty().bind(
                Bindings.when(elevator.getCurrentElevatorFloor().isEqualTo(elevatorFloor))
                        .then(Color.GRAY)
                        .otherwise(Color.TRANSPARENT));
        rightDoorPart.setId("RightDoorPart");
        leftDoorPart.setId("LeftDoorPart");

        Group group = new Group(doorOutline, leftDoorPart, rightDoorPart);
        HBox groupWrapper = new HBox(group);
        groupWrapper.setPadding(new Insets(10));
        this.getChildren().add(groupWrapper);
    }
}
