package at.fhhagenberg.sqelevator.view.panes;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.logic.IRemoteConsoleViewModel;
import at.fhhagenberg.sqelevator.view.controls.DoorsControl;
import at.fhhagenberg.sqelevator.view.controls.ModeControl;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ElevatorStatusControlPane extends VBox {
    public ElevatorStatusControlPane(Elevator elevator, IRemoteConsoleViewModel viewModel) {
        VBox elevatorStatusControlPane = new VBox();
        elevatorStatusControlPane.setSpacing(10);

        elevatorStatusControlPane.getChildren().addAll(
                new ModeControl(viewModel.getModeProperty()),
                new DoorsControl(elevator),
                new SpeedPayloadPane(elevator),
                new ElevatorButtonPane(elevator),
                new IsConnectedPane(viewModel.getIsConnectedProperty())
        );

        var elevatorStatusControlPaneText = new Text("Elevator Control");
        elevatorStatusControlPaneText.setStyle("-fx-font-size: 22;");

        this.setSpacing(10.0);
        this.getChildren().addAll(elevatorStatusControlPaneText, elevatorStatusControlPane);
    }
}
