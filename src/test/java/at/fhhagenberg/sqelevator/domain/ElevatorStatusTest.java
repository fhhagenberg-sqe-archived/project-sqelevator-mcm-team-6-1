package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorStatusTest {

    @Test
    void testBuild() {
        var elevatorStatusBuilder = ElevatorStatus.build();

        assertNotNull(elevatorStatusBuilder);

        var elevatorStatus = elevatorStatusBuilder.get();

        assertTrue(elevatorStatus.isConnected());
        assertEquals(0, elevatorStatus.getPosition());
        assertEquals(0.0, elevatorStatus.getVelocity());
        assertEquals(0.0, elevatorStatus.getPayload());
        assertNull(elevatorStatus.getCurrentFloor());
        assertFalse(elevatorStatus.getTargetedFloor().isPresent());
        assertNull(elevatorStatus.getElevatorButtonStatuses());
        assertNull(elevatorStatus.getElevatorFloorStatuses());
        assertNull(elevatorStatus.getDirection());
        assertNull(elevatorStatus.getDoorStatus());
    }

    @Test
    void testSetPosition() {
        var elevatorStatusBuilder = ElevatorStatus.build();

        assertEquals(0, elevatorStatusBuilder.get().getPosition());

        elevatorStatusBuilder.position(5);

        assertEquals(5, elevatorStatusBuilder.get().getPosition());
    }

    @Test
    void testSetVelocity() {
        var elevatorStatusBuilder = ElevatorStatus.build();

        assertEquals(0.0, elevatorStatusBuilder.get().getVelocity());

        elevatorStatusBuilder.velocity(5.0);

        assertEquals(5.0, elevatorStatusBuilder.get().getVelocity());
    }

    @Test
    void testSetPayload() {
        var elevatorStatusBuilder = ElevatorStatus.build();

        assertEquals(0.0, elevatorStatusBuilder.get().getPayload());

        elevatorStatusBuilder.payload(5.0);

        assertEquals(5.0, elevatorStatusBuilder.get().getPayload());
    }

    @Test
    void testSetElevatorButtonStatuses() {
        var elevatorStatusBuilder = ElevatorStatus.build();
        var elevatorButtonStatuses = new boolean[] { true, false };

        assertNull(elevatorStatusBuilder.get().getElevatorButtonStatuses());

        elevatorStatusBuilder.buttonStatus(elevatorButtonStatuses);

        assertEquals(elevatorButtonStatuses, elevatorStatusBuilder.get().getElevatorButtonStatuses());
    }

    @Test
    void testSetTargetedFloor() {
        var elevatorStatusBuilder = ElevatorStatus.build();
        var elevatorFloor = new ElevatorFloor(new Floor(1));

        assertNotNull(elevatorStatusBuilder.get().getTargetedFloor());
        assertFalse(elevatorStatusBuilder.get().getTargetedFloor().isPresent());

        elevatorStatusBuilder.targetedFloor(elevatorFloor);

        assertTrue(elevatorStatusBuilder.get().getTargetedFloor().isPresent());
        assertEquals(elevatorFloor, elevatorStatusBuilder.get().getTargetedFloor().get());
    }

    @Test
    void testSetCurrentFloor() {
        var elevatorStatusBuilder = ElevatorStatus.build();
        var elevatorFloor = new ElevatorFloor(new Floor(1));

        assertNull(elevatorStatusBuilder.get().getCurrentFloor());

        elevatorStatusBuilder.currentFloor(elevatorFloor);

        assertEquals(elevatorFloor, elevatorStatusBuilder.get().getCurrentFloor());
    }

    @Test
    void testSetElevatorFloorStatus() {
        var elevatorStatusBuilder = ElevatorStatus.build();
        var elevatorFloorStatus = ElevatorFloorStatus.build()
                .upRequested(true)
                .downRequested(true)
                .serviced(true)
                .get();

        var elevatorFloorStatuses = new ElevatorFloorStatus[] { elevatorFloorStatus };

        assertNull(elevatorStatusBuilder.get().getElevatorFloorStatuses());

        elevatorStatusBuilder.elevatorFloorStatus(elevatorFloorStatuses);

        assertEquals(1, elevatorStatusBuilder.get().getElevatorFloorStatuses().length);
        assertEquals(elevatorFloorStatuses, elevatorStatusBuilder.get().getElevatorFloorStatuses());
        assertEquals(elevatorFloorStatus, elevatorStatusBuilder.get().getElevatorFloorStatuses()[0]);
    }

    @Test
    void testSetDirection() {
        var elevatorStatusBuilder = ElevatorStatus.build();

        assertNull(elevatorStatusBuilder.get().getDirection());

        elevatorStatusBuilder.direction(Direction.DOWN);

        assertEquals(Direction.DOWN, elevatorStatusBuilder.get().getDirection());
    }

    @Test
    void testSetDoorStatus() {
        var elevatorStatusBuilder = ElevatorStatus.build();

        assertNull(elevatorStatusBuilder.get().getDoorStatus());

        elevatorStatusBuilder.doorStatus(DoorStatus.OPEN);

        assertEquals(DoorStatus.OPEN, elevatorStatusBuilder.get().getDoorStatus());
    }
}
