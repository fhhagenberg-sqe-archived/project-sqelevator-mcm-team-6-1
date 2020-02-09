package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class UpAndDownPaneTest {

    private ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));

    @Start
    public void start(Stage stage) {
        var pane = new UpAndDownPane(elevatorFloor);
        var scene = new Scene(pane, 500, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var upLabel = (Label)robot.lookup("#UpLabel").queryAll().iterator().next();
        var downLabel = (Label)robot.lookup("#DownLabel").queryAll().iterator().next();
        assertEquals(BorderStyle.STANDARD_DARKBLUE.value(), upLabel.getBorder());
        assertEquals(BorderStyle.THIN_BLACK.value(), downLabel.getBorder());
    }

    @Test
    public void testChangeRequest(FxRobot robot) {
        var upLabel = (Label) robot.lookup("#UpLabel").queryAll().iterator().next();
        var downLabel = (Label) robot.lookup("#DownLabel").queryAll().iterator().next();

        elevatorFloor.setDownRequest(true);
        elevatorFloor.setUpRequest(false);
        assertEquals(BorderStyle.THIN_BLACK.value(), upLabel.getBorder());
        assertEquals(BorderStyle.STANDARD_DARKBLUE.value(), downLabel.getBorder());
    }
}
