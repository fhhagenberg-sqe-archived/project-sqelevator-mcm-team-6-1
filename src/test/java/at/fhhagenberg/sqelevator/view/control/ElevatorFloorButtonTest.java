package at.fhhagenberg.sqelevator.view.control;

import at.fhhagenberg.sqelevator.view.controls.ElevatorFloorButton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElevatorFloorButtonTest {

    @Test
    public void testElevatorFloorButtonInit() {
        var button = new ElevatorFloorButton(1);

        assertFalse(button.hasBeenPressed());
    }

    @Test
    public void testElevatorFloorButtonInitWithValue() {
        var button = new ElevatorFloorButton(1, false);
        assertFalse(button.hasBeenPressed());
    }

    @Test
    public void testElevatorFloorButtonPressed() {
        var button = new ElevatorFloorButton(1);
        assertFalse(button.hasBeenPressed());
        button.setHasBeenPressed(true);

        assertTrue(button.hasBeenPressed());
        button.setHasBeenPressed(false);
        assertFalse(button.hasBeenPressed());
    }
}
