package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.model.*;
import at.fhhagenberg.sqelevator.viewmodel.RemoteConsoleViewModel;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sqelevator.IElevator;

import java.lang.Comparable;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.stream.Collectors;

public class RemoteConsoleView {

    private RemoteConsoleViewModel viewModel;

    public RemoteConsoleView(RemoteConsoleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ScrollPane createView() {
        VBox masterPane = new VBox();

        HBox statusControlPane = new HBox();
        statusControlPane.getChildren().addAll(this.getElevatorStatusPane(), this.getElevatorStatusControlPane());
        statusControlPane.setSpacing(20);

        HBox elevatorAndAlarmListPane = new HBox();
        elevatorAndAlarmListPane.getChildren().addAll(this.getElevatorListPane(), this.getAlarmListPane());
        elevatorAndAlarmListPane.setSpacing(20);

        masterPane.getChildren().addAll(statusControlPane, elevatorAndAlarmListPane);
        masterPane.setSpacing(20);
        masterPane.setPadding(new Insets(20));

        return new ScrollPane(masterPane);
    }

    private Pane getElevatorStatusPane() {
        GridPane elevatorStatusPane = new GridPane();
        elevatorStatusPane.setPadding(new Insets(10));
        elevatorStatusPane.addColumn(0, this.getElevatorPositionArrowPane());
        elevatorStatusPane.addColumn(1, this.getFloorsStatusPane());

        elevatorStatusPane.setBorder(this.getThinBlackBorder());
        elevatorStatusPane.setPadding(new Insets(5));

        return elevatorStatusPane;
    }

    private Pane getElevatorStatusControlPane() {
        VBox pane = new VBox();
        pane.setSpacing(10);

        pane.getChildren().addAll(this.getModeControl(),
                this.getDoorsControl(),
                this.getSpeedAndPayloadControl(),
                this.getCurrentTargetFloorPane(),
                this.getIsConectedPane());
        return pane;
    }

    private Pane getModeControl() {
        HBox box = new HBox();

        Label modeLabel = new Label("Mode");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label automatic = this.getControlLabelStyled("Automatic");
        Label manual = this.getControlLabelStyled("Manual");

        box.getChildren().addAll(modeLabel, automatic, manual);
        box.setPadding(new Insets(10));
        box.setBorder(this.getThinBlackBorder());
        return box;
    }

    private Pane getDoorsControl() {
        HBox box = new HBox();

        Label modeLabel = new Label("Doors");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label automatic = this.getControlLabelStyled("Open");
        Label manual = this.getControlLabelStyled("Closed");

        box.getChildren().addAll(modeLabel, automatic, manual);
        box.setPadding(new Insets(10));
        box.setBorder(this.getThinBlackBorder());
        return box;
    }

    private Pane getSpeedAndPayloadControl() {
        BorderPane speedAndPayloadControl = new BorderPane();
        speedAndPayloadControl.setLeft(this.getSpeedControl());
        speedAndPayloadControl.setRight(this.getPayloadControl());
        return speedAndPayloadControl;
    }

    private Pane getSpeedControl() {
        Label speedLabel = new Label("Speed: ");
        Label currentSpeedLabel = new Label("0 km/h");

        currentSpeedLabel.textProperty().bind(viewModel.velocityProperty.asString().concat(" km/h"));

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

    private Pane getPayloadControl() {
        Label payloadLabel = new Label("Payload: ");
        Label currentPayloadLabel = new Label("0 kg");
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

        // TODO: make value field not-editable when in automatic mode
        TextField value = new TextField("1");
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

    private Pane getElevatorPositionArrowPane() {
        Label positionLabel = new Label("Position");
        positionLabel.setStyle("-fx-font-size: 20;");
        positionLabel.setPadding(new Insets(5));

        Polygon arrow1 = new Polygon();
        arrow1.getPoints().addAll(new Double[]{
                30.0, 20.0,
                10.0, 45.0,
                23.0, 45.0,
                23.0, 120.0,
                37.0, 120.0,
                37.0, 45.0,
                50.0, 45.0,
                30.0, 20.0
        });

        arrow1.setFill(Color.TRANSPARENT);
        arrow1.setStroke(Color.BLACK);

        VBox arrow1Wrapper = new VBox(arrow1);
        arrow1Wrapper.setPadding(new Insets(45, 5, 5, 15));

        Polygon rectangle = new Polygon();
        rectangle.getPoints().addAll(new Double[]{
                23.0, 20.0,
                37.0, 20.0,
                37.0, 30.0,
                23.0, 30.0,
                23.0, 20.0
        });

        rectangle.setFill(Color.LIGHTGRAY);
        rectangle.setStroke(Color.BLACK);

        VBox rectangleWrapper = new VBox(rectangle);
        rectangleWrapper.setPadding(new Insets(0, 5, 0, 29));

        Polygon arrow2 = new Polygon();
        arrow2.getPoints().addAll(new Double[]{
                23.0, 20.0,
                23.0, 95.0,
                10.0, 95.0,
                30.0, 120.0,
                50.0, 95.0,
                37.0, 95.0,
                37.0, 20.0,
                23.0, 20.0
        });

        arrow2.setFill((Color.TRANSPARENT));
        arrow2.setStroke(Color.BLACK);

        VBox arrow2Wrapper = new VBox(arrow2);
        arrow2Wrapper.setPadding(new Insets(5, 5, 15, 15));

        return new VBox(positionLabel, arrow1Wrapper, rectangleWrapper, arrow2Wrapper);
    }

    private Pane getFloorsStatusPane() {
        VBox floorsStatusPane = new VBox();

        viewModel.selectedElevatorProperty.get().getElevatorFloors().forEach(elevatorFloor ->
                floorsStatusPane.getChildren().add(this.getElevatorFloorStatusPane(elevatorFloor))
        );

        floorsStatusPane.setSpacing(5);
        return floorsStatusPane;
    }

    private Pane getElevatorFloorStatusPane(ElevatorFloor elevatorFloor) {
        GridPane elevatorFloorStatusPane = new GridPane();

        // column 0 - door sign
        elevatorFloorStatusPane.addColumn(0, this.getDoorSignForFloorPane(elevatorFloor));

        // column 1 - floor request
        elevatorFloorStatusPane.addColumn(1, this.getFloorRequestLabel(elevatorFloor));

        // column 2 - floor label
        elevatorFloorStatusPane.addColumn(2, this.getFloorNameLabel(elevatorFloor.getFloor()));

        // column 3 - service enabled
        elevatorFloorStatusPane.addColumn(3, this.getServiceEnabledLabel(elevatorFloor));

        // column 4 - up/down request
        elevatorFloorStatusPane.addColumn(4, this.getFloorUpAndDownPane(elevatorFloor));

        elevatorFloorStatusPane.setBorder(this.getThinBlackBorder());
        return elevatorFloorStatusPane;
    }

    private Border getThinBlackBorder() {
        return new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                        null, new BorderWidths(1)));
    }

    private Pane getDoorSignForFloorPane(ElevatorFloor elevatorFloor) {
        int dummyPosition = 2;

        Rectangle doorOutline = new Rectangle(60, 60);
        doorOutline.setFill(Color.TRANSPARENT);
        doorOutline.setStroke(Color.GRAY);
        doorOutline.getStrokeDashArray().addAll(2.0, 2.0);

        Rectangle door1 = new Rectangle(5, 10, 24, 40);
        door1.setFill(Color.TRANSPARENT);

        Rectangle door2 = new Rectangle(31, 10, 24, 40);
        door2.setFill(Color.TRANSPARENT);

        // TODO: bind stroke color
        if (dummyPosition == elevatorFloor.getFloor().getFloorNumber()) {
            door1.setStroke(Color.GRAY);
            door2.setStroke(Color.GRAY);
        }

        Group group = new Group(doorOutline, door1, door2);
        HBox groupWrapper = new HBox(group);
        groupWrapper.setPadding(new Insets(10));
        return new VBox(groupWrapper);
    }

    private Pane getFloorRequestLabel(ElevatorFloor elevatorFloor) {
        Label label = new Label("Floor\nRequest\n#");
        label.setBorder(this.getThinBlackBorder());

        label.setPadding(new Insets(5));
        label.setTextAlignment(TextAlignment.CENTER);
        // TODO think about how to realize floorRequests best
        // floorRequest = floor requested INSIDE elevator
        // elevator.getCurrentFloorRequests needed for that

        HBox wrapper = new HBox(label);
        wrapper.setPrefHeight(60.0);
        wrapper.setPadding(new Insets(10));
        return wrapper;
    }

    private Pane getFloorNameLabel(Floor floor) {
        Label floorLabel = new Label("Floor " + floor.getFloorNumber());
        floorLabel.setStyle("-fx-font-size: 20;");

        HBox wrapper = new HBox(floorLabel);
        wrapper.setPadding(new Insets(25, 50, 25, 50));
        return wrapper;
    }

    private Label getServiceEnabledLabel(ElevatorFloor elevatorFloor) {
        Label label = new Label("Service\nenabled");
        label.setPadding(new Insets(5));
        label.setTextAlignment(TextAlignment.CENTER);

        // bind border color to serviceEnabledProperty
        label.borderProperty().bind(
                Bindings.when(
                        elevatorFloor.serviceEnabledProperty())
                        .then(new Border(
                                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))
                        .otherwise(new Border(
                                new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))

        );

        // bind textfill color to serviceEnabledProperty
        label.textFillProperty().bind(
                Bindings.createObjectBinding(
                        () -> {
                            if (elevatorFloor.serviceEnabledProperty().getValue()) {
                                return Color.BLACK;
                            } else {
                                return Color.GRAY;
                            }
                        }, elevatorFloor.serviceEnabledProperty()
                )
        );

        return label;
    }

    // TODO: display that there is no up/down
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
                Bindings.when(
                        elevatorFloor.upRequestProperty())
                        .then(new Border(
                                new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(2))))
                        .otherwise(new Border(
                                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                                        null, new BorderWidths(1))))

        );

        // bind border color to down property
        downLabel.borderProperty().bind(
                Bindings.when(
                        elevatorFloor.downRequestProperty())
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

    private Pane getElevatorListPane() {
        VBox pane = new VBox();

        Label elevatorListLabel = new Label("Elevator List");
        elevatorListLabel.setStyle("-fx-font-size: 22;");

        ListView<Elevator> elevatorList = new ListView<>();
        elevatorList.setBorder(this.getThinBlackBorder());
        elevatorList.itemsProperty().bind(viewModel.elevatorListProperty);
        VBox.setVgrow(elevatorList, Priority.SOMETIMES);
        viewModel.selectedElevatorProperty.bind(elevatorList.getSelectionModel().selectedItemProperty());
        elevatorList.getSelectionModel().select(0);

        elevatorList.setCellFactory((cell) -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Elevator elevator, boolean b) {
                    super.updateItem(elevator, b);

                    if (elevator != null) {
                        setText(elevator.toString());
                        setFont(Font.font(20.0));
                    }
                }
            };
        });

        pane.getChildren().addAll(elevatorListLabel, elevatorList);
        return pane;
    }

    private Pane getAlarmListPane() {
        VBox pane = new VBox();

        Label alarmListLabel = new Label("Alarm List");
        alarmListLabel.setStyle("-fx-font-size: 22;");

        ListView<Alarm> alarmList = new ListView<>();
        alarmList.setBorder(this.getThinBlackBorder());
//        alarmList.setItems(remoteConsole.getAlarms());
        VBox.setVgrow(alarmList, Priority.SOMETIMES);

        alarmList.setCellFactory(cell -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Alarm alarm, boolean b) {
                    super.updateItem(alarm, b);

                    if (alarm != null) {
                        setText(alarm.getTimestamp() + " " + alarm.getMessage());
                        setFont(Font.font(14.0));
                    }
                }
            };
        });

        pane.getChildren().addAll(alarmListLabel, alarmList);
        return pane;
    }

    private Pane getIsConectedPane() {
        Circle isConnectedStatus = new Circle(8.0);
        isConnectedStatus.setFill(Color.GREEN);

        VBox connectedStatusBox = new VBox(isConnectedStatus);
        connectedStatusBox.setPadding(new Insets(6, 5, 0, 0));

        Label isConnectedLabel = new Label(" connected to system");
        isConnectedLabel.setStyle("-fx-font-size: 20;");

        HBox pane = new HBox(connectedStatusBox, isConnectedLabel);
        return pane;
    }
}
