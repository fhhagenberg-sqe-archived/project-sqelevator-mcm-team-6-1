package at.fhhagenberg.sqelevator.view.control;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.view.controls.PayloadControl;
import at.fhhagenberg.sqelevator.view.controls.SpeedControl;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class SpeedControlTest {
    private Elevator elevator = new Elevator();

    @Start
    public void start(Stage stage) {
        var pane = new SpeedControl(elevator);
        var scene = new Scene(pane, 250, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var label = (Label)robot.lookup("#SpeedControlLabel").queryAll().iterator().next();
        assertEquals("0.0 km/h", label.getText());
    }

    @Test
    public void testChangeSpeed(FxRobot robot) {
        var label = (Label)robot.lookup("#SpeedControlLabel").queryAll().iterator().next();
        Platform.runLater(() -> elevator.setVelocity(12.9));
        robot.interrupt();
        assertEquals("12.9 km/h", label.textProperty().get());

    }

    @Test
    public void testChangeNegativeSpeed(FxRobot robot) {
        var label = (Label)robot.lookup("#SpeedControlLabel").queryAll().iterator().next();
        Platform.runLater(() -> elevator.setVelocity(-12.9));
        robot.interrupt();
        assertEquals("-12.9 km/h", label.textProperty().get());
    }
}
