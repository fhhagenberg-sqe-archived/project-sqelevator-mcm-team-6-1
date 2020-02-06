package at.fhhagenberg.sqelevator;

import at.fhhagenberg.sqelevator.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import sqelevator.ElevatorClient;
import sqelevator.IElevator;
import sqelevator.IElevatorClient;

import java.rmi.RemoteException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElevatorClientTest {

    private IElevator client;
    private IElevatorClient elevatorClient;

    @BeforeEach
    void setUp() throws RemoteException {
        this.client = mock(IElevator.class);

        when(client.getElevatorNum()).thenReturn(1);
        when(client.getFloorNum()).thenReturn(5);

        this.elevatorClient = new ElevatorClient(client);
    }

    @Test
    void testSetTarget() throws RemoteException {
        final var elevatorNumber = 0;
        final var floorNumber = 2;
        final var elevator = mock(Elevator.class);
        final var floor = mock(Floor.class);

        when(elevator.getElevatorNumber()).thenReturn(elevatorNumber);
        when(floor.getFloorNumber()).thenReturn(floorNumber);

        this.elevatorClient.setTarget(elevator, floor);

        verify(client).setTarget(anyInt(), anyInt());
    }

    @Test
    void testSetTarget_ElevatorIsNull() {
        final var floor = mock(Floor.class);

        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.setTarget(null, floor));
    }

    @Test
    void testSetTarget_FloorIsNull() {
        final var elevator = mock(Elevator.class);

        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.setTarget(elevator, null));
    }

    @Test
    void testGetElevators() {
        var elevators = this.elevatorClient.getElevators();

        assertEquals(1, elevators.size());
        assertTrue(elevators.stream().allMatch(Objects::nonNull));

        var elevator = elevators.get(0);

        assertNotNull(elevator);
        assertEquals(0, elevator.getElevatorNumber());
        assertEquals(5, elevator.getElevatorFloors().size());
        assertTrue(elevator.getElevatorFloors().stream().allMatch(Objects::nonNull));
    }

    @Test
    void testGetElevatorByNumber() {
        var maybeElevator = this.elevatorClient.getElevatorByNumber(0);

        assertTrue(maybeElevator.isPresent());

        var elevator = maybeElevator.get();

        assertNotNull(elevator);
        assertEquals(0, elevator.getElevatorNumber());
    }

    @Test
    void testGetElevatorByNumber_ElevatorNumberNegative() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getElevatorByNumber(-1));
    }

    @Test
    void testGetFloorByNumber() {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);
        final int floorNumber = 2;

        var maybeFloor = this.elevatorClient.getFloorByNumber(elevator, floorNumber);

        assertTrue(maybeFloor.isPresent());

        var floor = maybeFloor.get();

        assertNotNull(floor);
        assertNotNull(floor.getFloor());
        assertEquals(2, floor.getFloor().getFloorNumber());
    }

    @Test
    void testGetFloorByNumber_FloorNumberNegative() {
        final var elevator = mock(Elevator.class);

        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getFloorByNumber(elevator, -1));
    }

    @Test
    void testGetFloorByNumber_ElevatorNull() {
        final var floorNumber = 2;

        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getFloorByNumber(null, floorNumber));
    }

    @Test
    void testGetTargetedFloor() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getTarget(elevatorNumber)).thenReturn(2);

        var maybeTargetedFloor = this.elevatorClient.getTargetedFloor(elevator);

        assertTrue(maybeTargetedFloor.isPresent());

        var targetedFloor = maybeTargetedFloor.get();

        assertNotNull(targetedFloor);
        assertNotNull(targetedFloor.getFloor());
        assertEquals(2, targetedFloor.getFloor().getFloorNumber());
    }

    @Test
    void testGetTargetedFloor_ElevatorNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getTargetedFloor(null));
    }

    @Test
    void testGetCurrentFloor() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getElevatorFloor(elevatorNumber)).thenReturn(2);

        var currentFloor = this.elevatorClient.getCurrentFloor(elevator);

        assertNotNull(currentFloor);
        assertNotNull(currentFloor.getFloor());
        assertEquals(2, currentFloor.getFloor().getFloorNumber());
    }

    @Test
    void testGetCurrentFloor_ElevatorNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getCurrentFloor(null));
    }

    @Test
    void testGetDoorStatus_DoorsAreOpen() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getElevatorDoorStatus(elevatorNumber)).thenReturn(1);

        var doorStatus = this.elevatorClient.getElevatorDoorStatus(elevator);

        assertEquals(DoorStatus.OPEN, doorStatus);
    }

    @Test
    void testGetDoorStatus_DoorsAreClosed() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getElevatorDoorStatus(elevatorNumber)).thenReturn(2);

        var doorStatus = this.elevatorClient.getElevatorDoorStatus(elevator);

        assertEquals(DoorStatus.CLOSED, doorStatus);
    }

    @Test
    void testGetCurrentVelocity() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getElevatorAccel(elevatorNumber)).thenReturn(7);

        var velocity = this.elevatorClient.getCurrentVelocity(elevator);

        assertEquals(7, velocity);
    }

    @Test
    void testGetCurrentVelocity_ElevatorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getCurrentVelocity(null));
    }

    @Test
    void testGetCurrentWeightLoad() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getElevatorWeight(elevatorNumber)).thenReturn(437);

        var payload = this.elevatorClient.getCurrentWeightLoad(elevator);

        assertEquals(437, payload);
    }

    @Test
    void testGetCurrentWeightLoad_ElevatorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getCurrentWeightLoad(null));
    }

    @Test
    void testGetDirection_ElevatorDirectionUp() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getCommittedDirection(elevatorNumber)).thenReturn(0);

        var direction = this.elevatorClient.getDirection(elevator);

        assertEquals(Direction.UP, direction);
    }

    @Test
    void testGetDirection_ElevatorDirectionDown() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getCommittedDirection(elevatorNumber)).thenReturn(1);

        var direction = this.elevatorClient.getDirection(elevator);

        assertEquals(Direction.DOWN, direction);
    }

    @Test
    void testGetDirection_ElevatorDirectionUncommitted() throws RemoteException {
        final int elevatorNumber = 0;
        final var elevator = getMockElevator(elevatorNumber);

        when(client.getCommittedDirection(elevatorNumber)).thenReturn(2);

        var direction = this.elevatorClient.getDirection(elevator);

        assertEquals(Direction.UNCOMMITED, direction);
    }

    @Test
    void testGetDirection_ElevatorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.getDirection(null));
    }

    @Test
    void testHasFloorBeenRequestedUp_RequestUpTrue() throws RemoteException {
        final int floorNumber = 2;
        final var floor = mock(Floor.class);

        when(floor.getFloorNumber()).thenReturn(floorNumber);
        when(this.client.getFloorButtonUp(floorNumber)).thenReturn(true);

        assertTrue(this.elevatorClient.hasFloorBeenRequestedUp(floor));
    }

    @Test
    void testHasFloorBeenRequestedUp_RequestUpFalse() throws RemoteException {
        final int floorNumber = 2;
        final var floor = mock(Floor.class);

        when(floor.getFloorNumber()).thenReturn(floorNumber);
        when(this.client.getFloorButtonUp(floorNumber)).thenReturn(false);

        assertFalse(this.elevatorClient.hasFloorBeenRequestedUp(floor));
    }

    @Test
    void testHasFloorBeenRequestedUp_FloorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.hasFloorBeenRequestedUp(null));
    }

    @Test
    void testHasFloorBeenRequestedDown_RequestDownTrue() throws RemoteException {
        final int floorNumber = 2;

        final var floor = mock(Floor.class);

        when(floor.getFloorNumber()).thenReturn(floorNumber);
        when(this.client.getFloorButtonDown(floorNumber)).thenReturn(true);

        assertTrue(this.elevatorClient.hasFloorBeenRequestedDown(floor));
    }

    @Test
    void testHasFloorBeenRequestedDown_RequestDownFalse() throws RemoteException {
        final int floorNumber = 2;
        final var floor = mock(Floor.class);

        when(floor.getFloorNumber()).thenReturn(floorNumber);
        when(this.client.getFloorButtonDown(floorNumber)).thenReturn(false);

        assertFalse(this.elevatorClient.hasFloorBeenRequestedDown(floor));
    }

    @Test
    void testHasFloorBeenRequestedDown_FloorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.hasFloorBeenRequestedDown(null));
    }

    @Test
    void testIsServiceEnabled_ServiceEnabledTrue() throws RemoteException {
        final int elevatorNumber = 0;
        final int floorNumber = 2;
        final var elevator = getMockElevator(elevatorNumber);
        final var floor = mock(Floor.class);

        when(floor.getFloorNumber()).thenReturn(floorNumber);
        when(this.client.getServicesFloors(elevatorNumber, floorNumber)).thenReturn(true);

        assertTrue(this.elevatorClient.isServiceEnabled(elevator, floor));
    }

    @Test
    void testIsServiceEnabled_ServiceEnabledFalse() throws RemoteException {
        final int elevatorNumber = 0;
        final int floorNumber = 2;
        final var elevator = getMockElevator(elevatorNumber);
        final var floor = mock(Floor.class);

        when(floor.getFloorNumber()).thenReturn(floorNumber);
        when(this.client.getServicesFloors(elevatorNumber, floorNumber)).thenReturn(false);

        assertFalse(this.elevatorClient.isServiceEnabled(elevator, floor));
    }

    @Test
    void testIsServiceEnabled_ElevatorIsNull() {
        final var floor = mock(Floor.class);

        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.isServiceEnabled(null, floor));
    }

    @Test
    void testIsServiceEnabled_FloorIsNull() {
        final var elevator = mock(Elevator.class);

        assertThrows(IllegalArgumentException.class, () -> this.elevatorClient.isServiceEnabled(elevator, null));
    }

    private Elevator getMockElevator(int elevatorNumber) {
        var elevator = mock(Elevator.class);
        var floors = IntStream.range(0, 5)
                .boxed()
                .map(i -> {
                    var floor = getMockFloor(i);

                    var elevatorFloor = mock(ElevatorFloor.class);
                    lenient().when(elevatorFloor.getFloor()).thenReturn(floor);

                    return elevatorFloor;
                })
                .collect(Collectors.toList());

        lenient().when(elevator.getElevatorNumber()).thenReturn(elevatorNumber);
        lenient().when(elevator.getElevatorFloors()).thenReturn(floors);

        return elevator;
    }

    private Floor getMockFloor(int floorNumber) {
        var floor = mock(Floor.class);

        lenient().when(floor.getFloorNumber()).thenReturn(floorNumber);

        return floor;
    }
}
