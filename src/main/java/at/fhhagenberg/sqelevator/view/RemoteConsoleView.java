package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.view.controls.*;
import at.fhhagenberg.sqelevator.view.panes.*;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.logic.RemoteConsoleViewModel;
import at.fhhagenberg.sqelevator.view.style.ControlLabel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class RemoteConsoleView {

    private RemoteConsoleViewModel viewModel;

    public RemoteConsoleView(RemoteConsoleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public Control createView() {

        TabPane tabPane = new TabPane();

        for (Elevator elevator : viewModel.elevatorListProperty) {
            Tab tab = new Tab();
            tab.setText(String.format("Elevator %d", elevator.getElevatorNumber()));
            tab.setClosable(false);

            var masterPane = new HBox();

            HBox statusControlPane = new HBox();
            statusControlPane.getChildren().addAll(this.getElevatorStatusPane(elevator), this.getElevatorStatusControlPane(elevator));
            statusControlPane.setSpacing(20);

            masterPane.getChildren().addAll(statusControlPane, new AlarmListPane(elevator));
            masterPane.setSpacing(20);
            masterPane.setPadding(new Insets(20));

            tab.setContent(new ScrollPane(masterPane));
            tabPane.getTabs().add(tab);
        }

        return tabPane;
    }

    private Pane getElevatorStatusPane(Elevator elevator) {

        GridPane elevatorStatusPane = new GridPane();
        elevatorStatusPane.setPadding(new Insets(10));
        elevatorStatusPane.addColumn(1, new FloorStatusPane(elevator, viewModel));

        elevatorStatusPane.setBorder(BorderStyle.THIN_BLACK.value());
        elevatorStatusPane.setPadding(new Insets(5));

        var elevatorStatusPaneText = new Text("Elevator Status");
        elevatorStatusPaneText.setStyle("-fx-font-size: 22;");

        var box = new VBox(10.0);
        box.getChildren().addAll(elevatorStatusPaneText, elevatorStatusPane);

        return box;
    }

    private Pane getElevatorStatusControlPane(Elevator elevator) {
        VBox elevatorStatusControlPane = new VBox();
        elevatorStatusControlPane.setSpacing(10);

        elevatorStatusControlPane.getChildren().addAll(
                new ModeControl(viewModel.modeProperty),
                new DoorsControl(elevator),
                new SpeedPayloadPane(elevator),
                new ElevatorButtonPane(elevator),
                new IsConnectedPane(viewModel.isConnectedProperty)
        );

        var elevatorStatusControlPaneText = new Text("Elevator Control");
        elevatorStatusControlPaneText.setStyle("-fx-font-size: 22;");

        var box = new VBox(10.0);
        box.getChildren().addAll(elevatorStatusControlPaneText, elevatorStatusControlPane);

        return box;
    }
}
