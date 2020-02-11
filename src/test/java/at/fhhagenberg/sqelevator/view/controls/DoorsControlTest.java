package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.domain.DoorStatus;
import at.fhhagenberg.sqelevator.domain.Elevator;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(ApplicationExtension.class)
public class DoorsControlTest {
    private Elevator elevator = new Elevator();

    @Start
    public void start(Stage stage) {
        var pane = new DoorsControl(elevator);
        var scene = new Scene(pane, 250, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var open = (Label)robot.lookup("#Open").queryAll().iterator().next();
        var closed = (Label)robot.lookup("#Closed").queryAll().iterator().next();

        assertNotEquals(Background.EMPTY, open.backgroundProperty().get());
        assertEquals(Background.EMPTY, closed.backgroundProperty().get());
    }

    @Test
    public void testCloseDoor(FxRobot robot) {
        var open = (Label)robot.lookup("#Open").queryAll().iterator().next();
        var closed = (Label)robot.lookup("#Closed").queryAll().iterator().next();

        elevator.setDoorStatus(DoorStatus.CLOSED);
        assertEquals(Background.EMPTY, open.backgroundProperty().get());
        assertNotEquals(Background.EMPTY, closed.backgroundProperty().get());
    }

    @Test
    public void testOpenDoorAfterClose(FxRobot robot) {
        var open = (Label)robot.lookup("#Open").queryAll().iterator().next();
        var closed = (Label)robot.lookup("#Closed").queryAll().iterator().next();

        elevator.setDoorStatus(DoorStatus.CLOSED);
        assertEquals(Background.EMPTY, open.backgroundProperty().get());
        assertNotEquals(Background.EMPTY, closed.backgroundProperty().get());
        elevator.setDoorStatus(DoorStatus.OPEN);
        assertNotEquals(Background.EMPTY, open.backgroundProperty().get());
        assertEquals(Background.EMPTY, closed.backgroundProperty().get());
    }
}
