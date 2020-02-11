package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ElevatorFloorStatusTest {

    @Test
    void testBuild() {
        var elevatorFloor = mock(ElevatorFloor.class);
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build(elevatorFloor);

        assertNotNull(elevatorFloorStatusBuilder);

        var elevatorFloorStatus = elevatorFloorStatusBuilder.get();

        assertEquals(elevatorFloor, elevatorFloorStatusBuilder.get().getElevatorFloor());
        assertFalse(elevatorFloorStatus.isUpRequested());
        assertFalse(elevatorFloorStatus.isDownRequested());
        assertFalse(elevatorFloorStatus.isServiced());
    }

    @Test
    void testSetIsUpRequested() {
        var elevatorFloor = mock(ElevatorFloor.class);
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build(elevatorFloor);

        assertFalse(elevatorFloorStatusBuilder.get().isUpRequested());

        elevatorFloorStatusBuilder.upRequested(true);

        assertTrue(elevatorFloorStatusBuilder.get().isUpRequested());
    }

    @Test
    void testSetIsDownRequested() {
        var elevatorFloor = mock(ElevatorFloor.class);
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build(elevatorFloor);

        assertFalse(elevatorFloorStatusBuilder.get().isDownRequested());

        elevatorFloorStatusBuilder.downRequested(true);

        assertTrue(elevatorFloorStatusBuilder.get().isDownRequested());
    }

    @Test
    void testSetIsServiced() {
        var elevatorFloor = mock(ElevatorFloor.class);
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build(elevatorFloor);

        assertFalse(elevatorFloorStatusBuilder.get().isServiced());

        elevatorFloorStatusBuilder.serviced(true);

        assertTrue(elevatorFloorStatusBuilder.get().isServiced());
    }
}
