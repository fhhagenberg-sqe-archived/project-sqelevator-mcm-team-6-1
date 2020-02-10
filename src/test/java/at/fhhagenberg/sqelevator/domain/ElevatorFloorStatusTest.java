package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorFloorStatusTest {

    @Test
    void testBuild() {
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build();

        assertNotNull(elevatorFloorStatusBuilder);

        var elevatorFloorStatus = elevatorFloorStatusBuilder.get();

        assertFalse(elevatorFloorStatus.isUpRequested());
        assertFalse(elevatorFloorStatus.isDownRequested());
        assertFalse(elevatorFloorStatus.isServiced());
    }

    @Test
    void testSetIsUpRequested() {
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build();

        assertFalse(elevatorFloorStatusBuilder.get().isUpRequested());

        elevatorFloorStatusBuilder.upRequested(true);

        assertTrue(elevatorFloorStatusBuilder.get().isUpRequested());
    }

    @Test
    void testSetIsDownRequested() {
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build();

        assertFalse(elevatorFloorStatusBuilder.get().isDownRequested());

        elevatorFloorStatusBuilder.downRequested(true);

        assertTrue(elevatorFloorStatusBuilder.get().isDownRequested());
    }

    @Test
    void testSetIsServiced() {
        var elevatorFloorStatusBuilder = ElevatorFloorStatus.build();

        assertFalse(elevatorFloorStatusBuilder.get().isServiced());

        elevatorFloorStatusBuilder.serviced(true);

        assertTrue(elevatorFloorStatusBuilder.get().isServiced());
    }
}
