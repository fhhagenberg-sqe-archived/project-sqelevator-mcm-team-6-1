package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.ElevatorFloorButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({
            "1,false, false",
            "1,true"
    })
    public void testElevatorFloorButtonPressed() {
        var button = new ElevatorFloorButton(1);
        assertFalse(button.hasBeenPressed());
        button.setHasBeenPressed(true);

        assertTrue(button.hasBeenPressed());
    }
}
