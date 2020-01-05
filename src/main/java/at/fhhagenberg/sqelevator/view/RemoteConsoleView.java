package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.model.ElevatorFloor;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class RemoteConsoleView extends Application {
    // private RemoteConsole object

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Elevator Remote Console");


        Scene scene = new Scene(this.getElevatorStatusPane(), 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane getElevatorStatusPane() {
        GridPane elevatorStatusPane = new GridPane();
        elevatorStatusPane.setPadding(new Insets(10));
        elevatorStatusPane.addColumn(0, this.getElevatorPositionArrowPane());
        elevatorStatusPane.addColumn(1, this.getElevatorFloorStatusPane());

        return elevatorStatusPane;
    }

    private Pane getElevatorPositionArrowPane() {
        // TODO implement
        return new GridPane();
    }

    private Pane getElevatorFloorStatusPane() {
        ElevatorFloor elevatorFloor = new ElevatorFloor(); // TODO: switch to actual object
        GridPane elevatorFloorStatusPane = new GridPane();

        // column 1 - floor request
        elevatorFloorStatusPane.addColumn(1, this.getFloorRequestLabel(elevatorFloor));

        // column 2 - floor label
        elevatorFloorStatusPane.addColumn(2, this.getFloorNameLabel());

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

    private Pane getFloorRequestLabel (ElevatorFloor elevatorFloor) {
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

    private Pane getFloorNameLabel() {
        Label floorLabel = new Label("Floor");
        floorLabel.setStyle("-fx-font-size: 20;");

        HBox wrapper = new HBox(floorLabel);
        wrapper.setPadding(new Insets(25, 50, 25, 50));
        return wrapper;
    }

    private Label getServiceEnabledLabel (ElevatorFloor elevatorFloor) {
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
                            }
                            else {
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
}
