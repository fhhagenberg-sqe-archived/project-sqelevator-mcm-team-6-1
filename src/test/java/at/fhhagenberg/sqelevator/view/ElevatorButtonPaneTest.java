package at.fhhagenberg.sqelevator.view;


import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
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
        list.add(elevatorFloor);
        elevator.setElevatorFloors(list);
    }

    @Start
    public void start(Stage stage) {
        var pane = new ElevatorButtonPane(this.elevator);
        var scene = new Scene(pane, 250, 600);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @ParameterizedTest
    @CsvSource({
        "0,0",
        "1,1"
    })
    public void testElevatorButtons(String id, String text) {
        verifyThat(id, hasText(text));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "1, 0"
    })
    public void testElevatorButtonHighlight(int testId, int verificationId, FxRobot robot) {
        assertEquals(Color.TRANSPARENT, ((Circle)robot.lookup("#" + testId).queryAll().iterator().next()).getFill());
        assertEquals(Color.TRANSPARENT, ((Circle)robot.lookup("#" + verificationId).queryAll().iterator().next()).getFill());

        elevator.getElevatorFloorButtons()[testId].hasBeenPressedProperty.setValue(true);

        assertEquals(Color.YELLOW, ((Circle)robot.lookup("#" + testId).queryAll().iterator().next()).getFill());
        assertEquals(Color.TRANSPARENT, ((Circle)robot.lookup("#" + verificationId).queryAll().iterator().next()).getFill());
        elevator.getElevatorFloorButtons()[testId].hasBeenPressedProperty.setValue(false);
    }
}
