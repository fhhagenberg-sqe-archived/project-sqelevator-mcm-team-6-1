package at.fhhagenberg.sqelevator.view;


import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

@ExtendWith(ApplicationExtension.class)
public class ElevatorButtonPaneTest {

    private static Elevator elevator;
    @BeforeAll
    public static void setup() {
        elevator = new Elevator();
        var elevatorFloor = new ElevatorFloor(new Floor());
        ArrayList<ElevatorFloor> list = new ArrayList<ElevatorFloor>();
        list.add(elevatorFloor);
        elevator.setElevatorFloors(list);
    }

    @Start
    public void start(Stage stage) {
        var scene = new Scene(new ElevatorButtonPane(this.elevator), 250, 600);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testEmptyPane(FxRobot robot) {

        var props = robot.targetWindow().getScene().getProperties();
        verifyThat(".text.0", hasText("0"));

        assertTrue(true);
        assertFalse(false);
    }
}
