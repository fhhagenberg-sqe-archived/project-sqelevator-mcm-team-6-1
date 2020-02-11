package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorFloorTest {

    @Test
    public void testFloor() {
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));
        assertEquals(1, elevatorFloor.getFloor().getFloorNumber());
        var floor = new Floor(2);
        elevatorFloor.setFloor(floor);
        assertEquals(2, elevatorFloor.getFloor().getFloorNumber());
    }

    @Test
    public void testUpRequest() {
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));
        assertFalse(elevatorFloor.getUpRequest().get());
        elevatorFloor.setUpRequest(true);
        assertTrue(elevatorFloor.getUpRequest().get());
        elevatorFloor.setUpRequest(false);
        assertFalse(elevatorFloor.getUpRequest().get());
    }

    @Test
    public void testDownRequest() {
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));
        assertFalse(elevatorFloor.getDownRequest().get());
        elevatorFloor.setDownRequest(true);
        assertTrue(elevatorFloor.getDownRequest().get());
        elevatorFloor.setDownRequest(false);
        assertFalse(elevatorFloor.getDownRequest().get());
    }

    @Test
    public void testServiceEnabled() {
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));
        assertFalse(elevatorFloor.getServiceEnabled().get());
        elevatorFloor.setServiceEnabled(true);
        assertTrue(elevatorFloor.getServiceEnabled().get());
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,0",
            "3,1,1",
            "0,3,-1"
    })
    public void testCompareElevatorFloors(int floor1, int floor2, int expected) {
        ElevatorFloor elevatorFloor1 = new ElevatorFloor(new Floor(floor1));
        ElevatorFloor elevatorFloor2 = new ElevatorFloor(new Floor(floor2));

        assertEquals(expected, elevatorFloor1.compareTo(elevatorFloor2));
    }
}
