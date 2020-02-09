package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.view.controls.*;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.logic.RemoteConsoleViewModel;
import at.fhhagenberg.sqelevator.view.style.ControlLabel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
        elevatorStatusPane.addColumn(0, this.getElevatorPositionArrowPane(elevator));
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
        HBox box = new HBox();

        Label modeLabel = new Label("Mode");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label automatic = new ControlLabel("Automatic");
        automatic.setOnMouseClicked(handler -> viewModel.modeProperty.set(Mode.AUTOMATIC));
        automatic.backgroundProperty().bind(
                Bindings.when(viewModel.modeProperty.isEqualTo(Mode.AUTOMATIC))
                        .then(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        Label manual = new ControlLabel("Manual");
        manual.setOnMouseClicked(handler -> viewModel.modeProperty.set(Mode.MANUAL));
        manual.backgroundProperty().bind(
                Bindings.when(viewModel.modeProperty.isEqualTo(Mode.MANUAL))
                        .then(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        box.getChildren().addAll(modeLabel, automatic, manual);
        box.setPadding(new Insets(10));
        box.setBorder(BorderStyle.THIN_BLACK.value());
        return box;
    }

    private Pane getDoorsControl(Elevator elevator) {
        return new DoorsControl(elevator);
    }

    private Pane getSpeedAndPayloadControl(Elevator elevator) {
        return new SpeedPayloadPane(elevator);
    }

    private Pane getSpeedControl(Elevator elevator) {
        return new SpeedControl(elevator);
    }

    private Pane getPayloadControl(Elevator elevator) {
        return new PayloadControl(elevator);
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

    private Pane getElevatorPositionArrowPane(Elevator elevator) {
        Label positionLabel = new Label("Position");
        positionLabel.setStyle("-fx-font-size: 20;");
        positionLabel.setPadding(new Insets(5));

        Polygon upArrow = new Polygon();
        upArrow.getPoints().addAll(30.0, 20.0,
                10.0, 45.0,
                23.0, 45.0,
                23.0, 120.0,
                37.0, 120.0,
                37.0, 45.0,
                50.0, 45.0,
                30.0, 20.0);

        upArrow.setFill(Color.TRANSPARENT);
        upArrow.setStroke(Color.BLACK);

        VBox upArrowWrapper = new VBox(upArrow);
        upArrowWrapper.setPadding(new Insets(45, 5, 5, 15));

        Polygon uncomittedSquare = new Polygon();
        uncomittedSquare.getPoints().addAll(23.0, 20.0,
                37.0, 20.0,
                37.0, 30.0,
                23.0, 30.0,
                23.0, 20.0);

        uncomittedSquare.setFill(Color.LIGHTGRAY);
        uncomittedSquare.setStroke(Color.BLACK);

        VBox uncomittedSquareWrapper = new VBox(uncomittedSquare);
        uncomittedSquareWrapper.setPadding(new Insets(0, 5, 0, 29));

        Polygon downArrow = new Polygon();
        downArrow.getPoints().addAll(23.0, 20.0,
                23.0, 95.0,
                10.0, 95.0,
                30.0, 120.0,
                50.0, 95.0,
                37.0, 95.0,
                37.0, 20.0,
                23.0, 20.0);

        downArrow.setFill(Color.TRANSPARENT);
        downArrow.setStroke(Color.BLACK);
        VBox downArrowWrapper = new VBox(downArrow);
        downArrowWrapper.setPadding(new Insets(5, 5, 15, 15));

        upArrow.fillProperty().bind(Bindings.when(elevator.getDirection().isEqualTo(Direction.UP))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT));

        downArrow.fillProperty().bind(Bindings.when(elevator.getDirection().isEqualTo(Direction.DOWN))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT));

        uncomittedSquare.fillProperty().bind(Bindings.when(elevator.getDirection().isEqualTo(Direction.UNCOMMITED))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT));

        return new VBox(positionLabel, upArrowWrapper, uncomittedSquareWrapper, downArrowWrapper);
    }

    private Pane getFloorsStatusPane(Elevator elevator) {
        VBox floorsStatusPane = new VBox();

        elevator.getElevatorFloors().forEach(elevatorFloor ->
                floorsStatusPane.getChildren().add(this.getElevatorFloorStatusPane(elevator, elevatorFloor))
        );

        floorsStatusPane.setSpacing(5);
        return floorsStatusPane;
    }

    private Pane getElevatorFloorStatusPane(Elevator elevator, ElevatorFloor elevatorFloor) {
        GridPane elevatorFloorStatusPane = new GridPane();

        // column 0 - door sign
        elevatorFloorStatusPane.addColumn(0, this.getDoorSignForFloorPane(elevator, elevatorFloor));

        // column 1 - floor label
        elevatorFloorStatusPane.addColumn(1, this.getFloorNameLabel(elevatorFloor));

        // column 2 - service enabled
        elevatorFloorStatusPane.addColumn(2, this.getServiceEnabledLabel(elevatorFloor));

        // column 3 - up/down request
        elevatorFloorStatusPane.addColumn(3, this.getFloorUpAndDownPane(elevatorFloor));

        elevatorFloorStatusPane.setBorder(BorderStyle.THIN_BLACK.value());

        elevatorFloorStatusPane.setOnMouseClicked(handler -> viewModel.targetFloor(elevator, elevatorFloor.getFloor()));

        elevatorFloorStatusPane.backgroundProperty().bind(
                Bindings.when(elevator.getTargetedElevatorFloor().isEqualTo(elevatorFloor))
                        .then(new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        return elevatorFloorStatusPane;
    }

    private Pane getDoorSignForFloorPane(Elevator elevator, ElevatorFloor elevatorFloor) {

        return new DoorSignPane(elevator, elevatorFloor);
    }

    private Pane getFloorRequestLabel(ElevatorFloor elevatorFloor) {
        return new FloorRequestLabel(elevatorFloor);
    }

    private Pane getFloorNameLabel(ElevatorFloor elevatorFloor) {
        return new FloorNameLabel(elevatorFloor.getFloor().getFloorNumber());
    }

    private Label getServiceEnabledLabel(ElevatorFloor elevatorFloor) {
        return new ServiceEnabledLabel(elevatorFloor.getServiceEnabled());
    }

    private Pane getFloorUpAndDownPane(ElevatorFloor elevatorFloor) {
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

        pane.addColumn(0, upLabel);
        pane.addColumn(1, downLabel);
        HBox wrapper = new HBox(pane);
        wrapper.setPadding(new Insets(10));
        return wrapper;
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
