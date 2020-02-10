package at.fhhagenberg.sqelevator.view.panes;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.logic.IRemoteConsoleViewModel;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ElevatorStatusPane extends VBox {
    public ElevatorStatusPane(Elevator elevator, IRemoteConsoleViewModel viewModel) {
        GridPane elevatorStatusPane = new GridPane();
        elevatorStatusPane.setPadding(new Insets(10));
        elevatorStatusPane.addColumn(1, new FloorStatusPane(elevator, viewModel));

        elevatorStatusPane.setBorder(BorderStyle.THIN_BLACK.value());
        elevatorStatusPane.setPadding(new Insets(5));

        var elevatorStatusPaneText = new Text("Elevator Status");
        elevatorStatusPaneText.setStyle("-fx-font-size: 22;");

        this.setSpacing(10.0);
        this.getChildren().addAll(elevatorStatusPaneText, elevatorStatusPane);
    }
}
