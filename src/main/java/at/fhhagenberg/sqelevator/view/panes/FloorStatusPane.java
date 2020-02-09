package at.fhhagenberg.sqelevator.view.panes;

        import at.fhhagenberg.sqelevator.domain.Elevator;
        import at.fhhagenberg.sqelevator.logic.RemoteConsoleViewModel;
        import javafx.scene.layout.*;

public class FloorStatusPane extends VBox {
    public FloorStatusPane(Elevator elevator, RemoteConsoleViewModel viewModel) {
        elevator.getElevatorFloors().forEach(elevatorFloor ->
                this.getChildren().add(new ElevatorFloorStatusPane(elevator, elevatorFloor, viewModel))
        );

        this.setSpacing(5);
        //this.setBackground(new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
