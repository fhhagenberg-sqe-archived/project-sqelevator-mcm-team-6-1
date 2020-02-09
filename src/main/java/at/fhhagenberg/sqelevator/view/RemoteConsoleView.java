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

            masterPane.getChildren().addAll(statusControlPane, this.getAlarmListPane(elevator));
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
        elevatorStatusPane.addColumn(1, this.getFloorsStatusPane(elevator));

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

        elevatorStatusControlPane.getChildren().addAll(this.getModeControl(),
                this.getDoorsControl(elevator),
                this.getSpeedAndPayloadControl(elevator),
                this.getCurrentTargetFloorPane(),
                this.getElevatorButtonPane(elevator),
                this.getIsConnectedPane());

        var elevatorStatusControlPaneText = new Text("Elevator Control");
        elevatorStatusControlPaneText.setStyle("-fx-font-size: 22;");

        var box = new VBox(10.0);
        box.getChildren().addAll(elevatorStatusControlPaneText, elevatorStatusControlPane);

        return box;
    }

    private Pane getModeControl() {
        return new ModeControl(viewModel.modeProperty);
    }

    private Pane getDoorsControl(Elevator elevator) {
        return new DoorsControl(elevator);
    }

    private Pane getSpeedAndPayloadControl(Elevator elevator) {
        return new SpeedPayloadPane(elevator);
    }

    private Pane getCurrentTargetFloorPane() {
        Label label = new Label("Current Target Floor: ");
        label.setStyle("-fx-font-size: 20;");

        TextField value = new TextField("1");
        value.editableProperty().bind(viewModel.modeProperty.isEqualTo(Mode.MANUAL));

        value.setStyle("-fx-font-size: 20;");

        label.setPadding(new Insets(20, 5, 20, 5));
        value.setMaxWidth(80.0);
        value.setAlignment(Pos.CENTER_RIGHT);

        VBox valueWrapper = new VBox(value);
        valueWrapper.setPadding(new Insets(15, 5, 15, 5));

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(0, 5, 0, 5));
        pane.setLeft(label);
        pane.setRight(valueWrapper);
        pane.setBorder(BorderStyle.THIN_BLACK.value());

        return pane;
    }

    private Pane getFloorsStatusPane(Elevator elevator) {
        return new FloorStatusPane(elevator, viewModel);
    }

    private Pane getAlarmListPane(Elevator elevator) {
        return new AlarmListPane(elevator);
    }

    private Pane getIsConnectedPane() {
        return new IsConnectedPane(viewModel.isConnectedProperty);
    }

    private Pane getElevatorButtonPane(Elevator elevator) {
        return new ElevatorButtonPane(elevator);
    }
}
