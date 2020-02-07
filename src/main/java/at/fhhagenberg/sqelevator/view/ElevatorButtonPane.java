package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.Elevator;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.Arrays;
import java.util.Collections;

public class ElevatorButtonPane extends VBox {

    public ElevatorButtonPane(Elevator elevator) {
        var floorButtons = Arrays.asList(elevator.getElevatorFloorButtons());
        Collections.reverse(floorButtons);

        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setVgap(5.0);

        for (var floorButton : floorButtons) {
            Text text = new Text(String.format("%d", floorButton.getFloorNumber()));
            text.setBoundsType(TextBoundsType.VISUAL);

            Circle circle = new Circle();
            circle.setRadius(12.0);
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.BLACK);
            circle.fillProperty().bind(Bindings
                    .when(floorButton.hasBeenPressedProperty)
                    .then(Color.YELLOW)
                    .otherwise(Color.TRANSPARENT));

            StackPane stack = new StackPane();
            stack.getChildren().addAll(circle, text);
            flowPane.getChildren().add(stack);
        }

        this.setPadding(new Insets(10.0));
        this.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                        null, new BorderWidths(1))));

        var elevatorButtonPaneText = new Text("Elevator Buttons");
        elevatorButtonPaneText.setStyle("-fx-font-size: 20;");

        this.getChildren().addAll(elevatorButtonPaneText, flowPane);
    }
}
