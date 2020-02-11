package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ElevatorStatusTest {

    @Test
    void testBuild() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertNotNull(elevatorStatusBuilder);

        var elevatorStatus = elevatorStatusBuilder.get();

        assertTrue(elevatorStatus.isConnected());
        assertEquals(elevator, elevatorStatus.getElevator());
        assertEquals(0, elevatorStatus.getPosition());
        assertEquals(0.0, elevatorStatus.getVelocity());
        assertEquals(0.0, elevatorStatus.getPayload());
        assertNull(elevatorStatus.getCurrentFloor());
        assertFalse(elevatorStatus.getTargetedFloor().isPresent());
        assertNotNull(elevatorStatus.getElevatorButtonStatuses());
        assertEquals(elevatorStatus.getElevatorButtonStatuses().length, 0);
        assertNotNull(elevatorStatus.getElevatorFloorStatuses());
        assertEquals(elevatorStatus.getElevatorFloorStatuses().length, 0);
        assertNull(elevatorStatus.getDirection());
        assertNull(elevatorStatus.getDoorStatus());
    }

    @Test
    void testNotConnected() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertNotNull(elevatorStatusBuilder);

        var elevatorStatus = elevatorStatusBuilder.notConnected();

        assertEquals(elevator, elevatorStatus.getElevator());
        assertFalse(elevatorStatus.isConnected());
    }

    @Test
    void testSetPosition() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertEquals(0, elevatorStatusBuilder.get().getPosition());

        elevatorStatusBuilder.position(5);

        assertEquals(5, elevatorStatusBuilder.get().getPosition());
    }

    @Test
    void testSetVelocity() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertEquals(0.0, elevatorStatusBuilder.get().getVelocity());

        elevatorStatusBuilder.velocity(5.0);

        assertEquals(5.0, elevatorStatusBuilder.get().getVelocity());
    }

    @Test
    void testSetPayload() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertEquals(0.0, elevatorStatusBuilder.get().getPayload());

        elevatorStatusBuilder.payload(5.0);

        assertEquals(5.0, elevatorStatusBuilder.get().getPayload());
    }

    @Test
    void testSetElevatorButtonStatuses() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);
        var elevatorButtonStatuses = new boolean[] { true, false };

        assertNotNull(elevatorStatusBuilder.get().getElevatorButtonStatuses());
        assertEquals(elevatorStatusBuilder.get().getElevatorButtonStatuses().length, 0);

        elevatorStatusBuilder.buttonStatus(elevatorButtonStatuses);

        assertEquals(elevatorButtonStatuses, elevatorStatusBuilder.get().getElevatorButtonStatuses());
    }

    @Test
    void testSetTargetedFloor() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);
        var elevatorFloor = new ElevatorFloor(new Floor(1));

        assertNotNull(elevatorStatusBuilder.get().getTargetedFloor());
        assertFalse(elevatorStatusBuilder.get().getTargetedFloor().isPresent());

        elevatorStatusBuilder.targetedFloor(elevatorFloor);

        assertTrue(elevatorStatusBuilder.get().getTargetedFloor().isPresent());
        assertEquals(elevatorFloor, elevatorStatusBuilder.get().getTargetedFloor().get());
    }

    @Test
    void testSetCurrentFloor() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);
        var elevatorFloor = new ElevatorFloor(new Floor(1));

        assertNull(elevatorStatusBuilder.get().getCurrentFloor());

        elevatorStatusBuilder.currentFloor(elevatorFloor);

        assertEquals(elevatorFloor, elevatorStatusBuilder.get().getCurrentFloor());
    }

    @Test
    void testSetElevatorFloorStatus() {
        var elevator = mock(Elevator.class);
        var elevatorFloor = mock(ElevatorFloor.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);
        var elevatorFloorStatus = ElevatorFloorStatus.build(elevatorFloor)
                .upRequested(true)
                .downRequested(true)
                .serviced(true)
                .get();

        var elevatorFloorStatuses = new ElevatorFloorStatus[] { elevatorFloorStatus };

        assertNotNull(elevatorStatusBuilder.get().getElevatorFloorStatuses());
        assertEquals(elevatorStatusBuilder.get().getElevatorFloorStatuses().length, 0);

        elevatorStatusBuilder.elevatorFloorStatus(elevatorFloorStatuses);

        assertEquals(1, elevatorStatusBuilder.get().getElevatorFloorStatuses().length);
        assertEquals(elevatorFloorStatuses, elevatorStatusBuilder.get().getElevatorFloorStatuses());
        assertEquals(elevatorFloorStatus, elevatorStatusBuilder.get().getElevatorFloorStatuses()[0]);
    }

    @Test
    void testSetDirection() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertNull(elevatorStatusBuilder.get().getDirection());

        elevatorStatusBuilder.direction(Direction.DOWN);

        assertEquals(Direction.DOWN, elevatorStatusBuilder.get().getDirection());
    }

    @Test
    void testSetDoorStatus() {
        var elevator = mock(Elevator.class);
        var elevatorStatusBuilder = ElevatorStatus.build(elevator);

        assertNull(elevatorStatusBuilder.get().getDoorStatus());

        elevatorStatusBuilder.doorStatus(DoorStatus.OPEN);

        assertEquals(DoorStatus.OPEN, elevatorStatusBuilder.get().getDoorStatus());
    }
}
