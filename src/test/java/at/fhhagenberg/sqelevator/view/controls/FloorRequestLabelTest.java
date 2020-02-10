package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(ApplicationExtension.class)
public class FloorRequestLabelTest {

    private ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));

    @Start
    public void start(Stage stage) {
        var pane = new FloorRequestLabel(elevatorFloor);
        var scene = new Scene(pane, 250, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var label = (Label)robot.lookup("#FloorRequestLabel").queryAll().iterator().next();
        assertEquals("No\nRequest", label.getText());
    }

    @Test
    public void testDownRequest(FxRobot robot) {
        var label = (Label) robot.lookup("#FloorRequestLabel").queryAll().iterator().next();

        Platform.runLater(() -> elevatorFloor.setDownRequest(true));
        robot.interrupt();
        assertEquals("DOWN\nRequest", label.getText());
        assertFalse(elevatorFloor.getUpRequest().getValue());
    }

    @Test
    public void testUpRequest(FxRobot robot) {
        var label = (Label) robot.lookup("#FloorRequestLabel").queryAll().iterator().next();

        Platform.runLater(() -> elevatorFloor.setUpRequest(true));
        robot.interrupt();
        assertEquals("UP\nRequest", label.getText());
        assertFalse(elevatorFloor.getDownRequest().getValue());
    }
}
