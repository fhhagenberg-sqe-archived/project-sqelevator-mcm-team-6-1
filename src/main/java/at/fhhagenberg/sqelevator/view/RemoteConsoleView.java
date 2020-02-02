package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.*;
import sqelevator.RemoteConsoleViewModel;
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
import javafx.scene.text.Font;
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

            VBox masterPane = new VBox();

            HBox statusControlPane = new HBox();
            statusControlPane.getChildren().addAll(this.getElevatorStatusPane(elevator), this.getElevatorStatusControlPane(elevator));
            statusControlPane.setSpacing(20);

            HBox elevatorAndAlarmListPane = new HBox();
            elevatorAndAlarmListPane.getChildren().addAll(this.getAlarmListPane(elevator));
            elevatorAndAlarmListPane.setSpacing(20);

            masterPane.getChildren().addAll(statusControlPane, elevatorAndAlarmListPane);
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

        elevatorStatusPane.setBorder(this.getThinBlackBorder());
        elevatorStatusPane.setPadding(new Insets(5));

        return elevatorStatusPane;
    }

    private Pane getElevatorStatusControlPane(Elevator elevator) {
        VBox pane = new VBox();
        pane.setSpacing(10);

        pane.getChildren().addAll(this.getModeControl(),
                this.getDoorsControl(elevator),
                this.getSpeedAndPayloadControl(elevator),
                this.getCurrentTargetFloorPane(),
                this.getIsConnectedPane());
        return pane;
    }

    private Pane getModeControl() {
        HBox box = new HBox();

        Label modeLabel = new Label("Mode");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label automatic = this.getControlLabelStyled("Automatic");
        automatic.setOnMouseClicked(handler -> viewModel.modeProperty.set(Mode.AUTOMATIC));
        automatic.backgroundProperty().bind(
                Bindings.when(viewModel.modeProperty.isEqualTo(Mode.AUTOMATIC))
                        .then(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        Label manual = this.getControlLabelStyled("Manual");
        manual.setOnMouseClicked(handler -> viewModel.modeProperty.set(Mode.MANUAL));
        manual.backgroundProperty().bind(
                Bindings.when(viewModel.modeProperty.isEqualTo(Mode.MANUAL))
                        .then(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        box.getChildren().addAll(modeLabel, automatic, manual);
        box.setPadding(new Insets(10));
        box.setBorder(this.getThinBlackBorder());
        return box;
    }

    private Pane getDoorsControl(Elevator elevator) {
        HBox box = new HBox();

        Label modeLabel = new Label("Doors");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label open = this.getControlLabelStyled("Open");
        Label closed = this.getControlLabelStyled("Closed");

        open.backgroundProperty().bind(
                Bindings.when(elevator.doorsStatusProperty.isEqualTo(DoorStatus.OPEN))
                        .then(new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        closed.backgroundProperty().bind(
                Bindings.when(elevator.doorsStatusProperty.isEqualTo(DoorStatus.CLOSED))
                        .then(new Background(new BackgroundFill(Color.rgb(255, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        box.getChildren().addAll(modeLabel, open, closed);
        box.setPadding(new Insets(10));
        box.setBorder(this.getThinBlackBorder());
        return box;
    }

    private Pane getSpeedAndPayloadControl(Elevator elevator) {
        BorderPane speedAndPayloadControl = new BorderPane();
        speedAndPayloadControl.setLeft(this.getSpeedControl(elevator));
        speedAndPayloadControl.setRight(this.getPayloadControl(elevator));
        return speedAndPayloadControl;
    }

    private Pane getSpeedControl(Elevator elevator) {
        Label speedLabel = new Label("Speed: ");
        Label currentSpeedLabel = new Label("0 km/h");
        currentSpeedLabel.textProperty().bind(elevator.velocityProperty.asString().concat(" km/h"));
        currentSpeedLabel.setPadding(new Insets(0, 5, 0, 15));

        speedLabel.setStyle("-fx-font-size: 20;");
        currentSpeedLabel.setStyle("-fx-font-size: 20;");

        BorderPane pane = new BorderPane();
        pane.setLeft(speedLabel);
        pane.setRight(currentSpeedLabel);
        pane.setBorder(this.getThinBlackBorder());
        pane.setPadding(new Insets(15, 5, 15, 5));
        return pane;
    }

    private Pane getPayloadControl(Elevator elevator) {
        Label payloadLabel = new Label("Payload: ");
        Label currentPayloadLabel = new Label("0 kg");
        currentPayloadLabel.textProperty().bind(elevator.payloadProperty.asString().concat(" kg"));
        currentPayloadLabel.setPadding(new Insets(0, 5, 0, 15));

        payloadLabel.setStyle("-fx-font-size: 20;");
        currentPayloadLabel.setStyle("-fx-font-size: 20;");

        BorderPane pane = new BorderPane();
        pane.setLeft(payloadLabel);
        pane.setRight(currentPayloadLabel);
        pane.setBorder(this.getThinBlackBorder());
        pane.setPadding(new Insets(15, 5, 15, 5));
        return pane;
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
        pane.setBorder(this.getThinBlackBorder());

        return pane;
    }

    private Label getControlLabelStyled(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setBorder(this.getThinBlackBorder());
        label.setStyle("-fx-font-size: 20;");
        label.setPrefWidth(120.0);
        label.setPadding(new Insets(20, 0, 20, 0));
        return label;
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

        upArrow.fillProperty().bind(Bindings.when(elevator.directionProperty.isEqualTo(0))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT));

        downArrow.fillProperty().bind(Bindings.when(elevator.directionProperty.isEqualTo(1))
                .then(Color.BLUE)
                .otherwise(Color.TRANSPARENT));

        uncomittedSquare.fillProperty().bind(Bindings.when(elevator.directionProperty.isEqualTo(2))
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

        // column 1 - floor request
        elevatorFloorStatusPane.addColumn(1, this.getFloorRequestLabel(elevatorFloor));

        // column 2 - floor label
        elevatorFloorStatusPane.addColumn(2, this.getFloorNameLabel(elevatorFloor));

        // column 3 - service enabled
        elevatorFloorStatusPane.addColumn(3, this.getServiceEnabledLabel(elevatorFloor));

        // column 4 - up/down request
        elevatorFloorStatusPane.addColumn(4, this.getFloorUpAndDownPane(elevatorFloor));

        elevatorFloorStatusPane.setBorder(this.getThinBlackBorder());

        elevatorFloorStatusPane.setOnMouseClicked(handler -> viewModel.targetFloor(elevator, elevatorFloor.floorProperty.get().getFloorNumber()));

        elevatorFloorStatusPane.backgroundProperty().bind(
                Bindings.when(elevator.targetedElevatorFloorProperty.isEqualTo(elevatorFloor))
                        .then(new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        return elevatorFloorStatusPane;
    }

    private Border getThinBlackBorder() {
        return new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                        null, new BorderWidths(1)));
    }

    private Pane getDoorSignForFloorPane(Elevator elevator, ElevatorFloor elevatorFloor) {
        Rectangle doorOutline = new Rectangle(60, 60);
        doorOutline.setFill(Color.TRANSPARENT);
        doorOutline.setStroke(Color.GRAY);
        doorOutline.getStrokeDashArray().addAll(2.0, 2.0);

        Rectangle leftDoorPart = new Rectangle(5, 10, 24, 40);
        leftDoorPart.setFill(Color.TRANSPARENT);

        Rectangle rightDoorPart = new Rectangle(31, 10, 24, 40);
        rightDoorPart.setFill(Color.TRANSPARENT);

        leftDoorPart.strokeProperty().bind(
                Bindings.when(elevator.currentElevatorFloorProperty.isEqualTo(elevatorFloor))
                        .then(Color.GRAY)
                        .otherwise(Color.TRANSPARENT));

        rightDoorPart.strokeProperty().bind(
                Bindings.when(elevator.currentElevatorFloorProperty.isEqualTo(elevatorFloor))
                        .then(Color.GRAY)
                        .otherwise(Color.TRANSPARENT));

        Group group = new Group(doorOutline, leftDoorPart, rightDoorPart);
        HBox groupWrapper = new HBox(group);
        groupWrapper.setPadding(new Insets(10));
        return new VBox(groupWrapper);
    }

    private Pane getFloorRequestLabel(ElevatorFloor elevatorFloor) {
        Label label = new Label("No\nRequest");
        label.setBorder(this.getThinBlackBorder());

        label.setPadding(new Insets(5));
        label.setTextAlignment(TextAlignment.CENTER);

        label.textProperty().bind(
                Bindings.when(elevatorFloor.upRequestProperty)
                        .then("UP\nRequest")
                        .otherwise(
                                Bindings.when(elevatorFloor.downRequestProperty)
                                        .then("DOWN\nRequest")
                                        .otherwise("No\nRequest")));

        label.backgroundProperty().bind(
                Bindings.when(elevatorFloor.upRequestProperty.or(elevatorFloor.downRequestProperty))
                        .then(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        HBox wrapper = new HBox(label);
        wrapper.setPrefHeight(60.0);
        wrapper.setPadding(new Insets(10));
        return wrapper;
    }

    private Pane getFloorNameLabel(ElevatorFloor elevatorFloor) {
        Label floorLabel = new Label(String.format("Floor %d", elevatorFloor.floorProperty.get().getFloorNumber()));
        floorLabel.setStyle("-fx-font-size: 20;");

        HBox wrapper = new HBox(floorLabel);
        wrapper.setPadding(new Insets(25, 50, 25, 50));
        return wrapper;
    }

    private Label getServiceEnabledLabel(ElevatorFloor elevatorFloor) {
        Label label = new Label("Service\nenabled");
        label.setPadding(new Insets(5));
        label.setTextAlignment(TextAlignment.CENTER);

        label.borderProperty().bind(
                Bindings.when(elevatorFloor.serviceEnabled)
                        .then(new Border(
                                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))
                        .otherwise(new Border(
                                new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))

        );

        label.textFillProperty().bind(
                Bindings.when(elevatorFloor.serviceEnabled)
                        .then(Color.BLACK)
                        .otherwise(Color.GRAY));

        return label;
    }

    private Pane getFloorUpAndDownPane(ElevatorFloor elevatorFloor) {
        elevatorFloor.setUpRequest();
        GridPane pane = new GridPane();
        Label upLabel = new Label();
        upLabel.setText("Up");
        upLabel.setStyle("-fx-font-size: 20;");

        Label downLabel = new Label();
        downLabel.setText("Down");
        downLabel.setStyle("-fx-font-size: 20;");

        // bind border color to up property
        upLabel.borderProperty().bind(
                Bindings.when(elevatorFloor.upRequestProperty)
                        .then(new Border(
                                new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(2))))
                        .otherwise(new Border(
                                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))

        );

        // bind border color to down property
        downLabel.borderProperty().bind(
                Bindings.when(elevatorFloor.downRequestProperty)
                        .then(new Border(
                                new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(2))))
                        .otherwise(new Border(
                                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))

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
        VBox pane = new VBox();

        Label alarmListLabel = new Label("Alarm List");
        alarmListLabel.setStyle("-fx-font-size: 22;");

        ListView<Alarm> alarmList = new ListView<>();
        alarmList.setBorder(this.getThinBlackBorder());
        alarmList.itemsProperty().bind(elevator.alarmListProperty);
//        alarmList.setItems(remoteConsole.getAlarms());
        VBox.setVgrow(alarmList, Priority.SOMETIMES);

        alarmList.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(Alarm alarm, boolean b) {
                super.updateItem(alarm, b);

                if (alarm != null) {
                    setText(alarm.getTimestamp() + " " + alarm.getMessage());
                    setFont(Font.font(14.0));
                }
            }
        });

        pane.getChildren().addAll(alarmListLabel, alarmList);
        return pane;
    }

    private Pane getIsConnectedPane() {
        Circle isConnectedStatus = new Circle(8.0);
//        isConnectedStatus.setFill(Color.GREEN);

        isConnectedStatus.fillProperty().bind(
                Bindings.when(viewModel.isConnectedProperty)
                        .then(Color.GREEN)
                        .otherwise(Color.RED));

        VBox connectedStatusBox = new VBox(isConnectedStatus);
        connectedStatusBox.setPadding(new Insets(6, 5, 0, 0));

        Label isConnectedLabel = new Label(" connected to system");
        isConnectedLabel.setStyle("-fx-font-size: 20;");

        return new HBox(connectedStatusBox, isConnectedLabel);
    }
}
