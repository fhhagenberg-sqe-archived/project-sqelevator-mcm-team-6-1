package at.fhhagenberg.sqelevator.view.panes;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.logic.IRemoteConsoleViewModel;
import at.fhhagenberg.sqelevator.view.controls.FloorNameLabel;
import at.fhhagenberg.sqelevator.view.controls.ServiceEnabledLabel;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ElevatorFloorStatusPane extends GridPane {
    public ElevatorFloorStatusPane(Elevator elevator, ElevatorFloor elevatorFloor, IRemoteConsoleViewModel viewModel) {
        // column 0 - door sign
        this.addColumn(0, new DoorSignPane(elevator, elevatorFloor));

        // column 1 - floor label
        this.addColumn(1, new FloorNameLabel(elevatorFloor.getFloor().getFloorNumber()));

        // column 2 - service enabled
        this.addColumn(2, new ServiceEnabledLabel(elevatorFloor.getServiceEnabled()));

        // column 3 - up/down request
        this.addColumn(3, new UpAndDownPane(elevatorFloor));

        this.setBorder(BorderStyle.THIN_BLACK.value());

        this.setOnMouseClicked(handler -> viewModel.targetFloor(elevator, elevatorFloor.getFloor()));

        this.backgroundProperty().bind(
                Bindings.when(elevator.getTargetedElevatorFloor().isEqualTo(elevatorFloor))
                        .then(new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));
        this.setId("ElevatorFloorStatus");
    }
}
