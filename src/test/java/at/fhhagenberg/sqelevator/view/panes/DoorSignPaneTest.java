package at.fhhagenberg.sqelevator.view.panes;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.view.panes.DoorSignPane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class DoorSignPaneTest {

    private Elevator elevator = new Elevator();

    @Start
    public void start(Stage stage) {
        this.elevator.setCurrentElevatorFloor(new ElevatorFloor(new Floor(2)));
        var pane = new DoorSignPane(this.elevator, new ElevatorFloor(new Floor(1)));

        var scene = new Scene(pane, 250, 600);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var rightRect = (Rectangle) robot.lookup("#RightDoorPart").queryAll().iterator().next();
        var leftRect = (Rectangle) robot.lookup("#LeftDoorPart").queryAll().iterator().next();

        assertEquals(Color.TRANSPARENT, leftRect.getFill());
        assertEquals(Color.TRANSPARENT, rightRect.getFill());
    }

    @Test
    public void testChangeFloor(FxRobot robot) {
        this.elevator.setCurrentElevatorFloor(new ElevatorFloor(new Floor(1)));
        var rightRect = (Rectangle) robot.lookup("#RightDoorPart").queryAll().iterator().next();
        var leftRect = (Rectangle) robot.lookup("#LeftDoorPart").queryAll().iterator().next();

        assertEquals(Color.TRANSPARENT, leftRect.getFill());
        assertEquals(Color.TRANSPARENT, rightRect.getFill());
    }
}
