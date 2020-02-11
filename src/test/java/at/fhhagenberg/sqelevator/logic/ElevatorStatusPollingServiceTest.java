package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ElevatorStatusPollingServiceTest {

    private ElevatorStatusObserver elevatorStatusObserver;

    @BeforeEach
    void setup() {
        this.elevatorStatusObserver = mock(ElevatorStatusObserver.class);
    }

    @Test
    void testStartPollingService_UpdateCalledWithinTimeout() {
        var elevatorClient = mock(IElevatorClient.class);
        var elevators = List.of(mock(Elevator.class));
        var pollingTime = 100L;

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, elevators, pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_VelocityCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        when(elevatorClient.getCurrentVelocity(elevator)).thenReturn(5.0);

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);

            assertEquals(5.0, elevatorStatus.getVelocity());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_PayloadCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        when(elevatorClient.getCurrentWeightLoad(elevator)).thenReturn(1000.0);

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);

            assertEquals(1000.0, elevatorStatus.getPayload());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_ElevatorButtonStatusCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        when(elevatorClient.getElevatorFloorButtonsStatus(elevator)).thenReturn(new boolean[] { true, false, false });

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);
            var buttonStatuses = elevatorStatus.getElevatorButtonStatuses();

            assertEquals(3, buttonStatuses.length);
            assertTrue(buttonStatuses[0]);
            assertFalse(buttonStatuses[1]);
            assertFalse(buttonStatuses[2]);

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_TargetedFloorCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        var targetedFloor = new ElevatorFloor(new Floor(1));
        targetedFloor.setUpRequest(true);
        targetedFloor.setDownRequest(true);
        targetedFloor.setServiceEnabled(true);

        when(elevatorClient.getTargetedFloor(elevator)).thenReturn(Optional.of(targetedFloor));

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);
            var maybeTargetedFloor = elevatorStatus.getTargetedFloor();

            assertTrue(maybeTargetedFloor.isPresent());

            var floor = maybeTargetedFloor.get();

            assertTrue(floor.getUpRequest().get());
            assertTrue(floor.getDownRequest().get());
            assertTrue(floor.getServiceEnabled().get());
            assertEquals(1, floor.getFloor().getFloorNumber());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_TargetedFloorNull() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        when(elevatorClient.getTargetedFloor(elevator)).thenReturn(Optional.empty());

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);
            var maybeTargetedFloor = elevatorStatus.getTargetedFloor();

            assertFalse(maybeTargetedFloor.isPresent());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_CurrentFloorCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        var targetedFloor = new ElevatorFloor(new Floor(1));
        targetedFloor.setUpRequest(true);
        targetedFloor.setDownRequest(true);
        targetedFloor.setServiceEnabled(true);

        when(elevatorClient.getCurrentFloor(elevator)).thenReturn(targetedFloor);

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);
            var currentFloor = elevatorStatus.getCurrentFloor();

            assertTrue(currentFloor.getUpRequest().get());
            assertTrue(currentFloor.getDownRequest().get());
            assertTrue(currentFloor.getServiceEnabled().get());
            assertEquals(1, currentFloor.getFloor().getFloorNumber());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_ElevatorFloorStatusesCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        var elevatorFloor = new ElevatorFloor(new Floor(1));
        elevatorFloor.setUpRequest(true);
        elevatorFloor.setDownRequest(true);
        elevatorFloor.setServiceEnabled(true);

        when(elevator.getElevatorFloors()).thenReturn(List.of(elevatorFloor));

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);
            var floorStatuses = elevatorStatus.getElevatorFloorStatuses();

            assertEquals(1, floorStatuses.length);

            var floorStatus = floorStatuses[0];

            assertFalse(floorStatus.isUpRequested());
            assertFalse(floorStatus.isDownRequested());
            assertFalse(floorStatus.isServiced());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_DirectionCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        when(elevatorClient.getDirection(elevator)).thenReturn(Direction.DOWN);

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);

            assertEquals(Direction.DOWN, elevatorStatus.getDirection());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }

    @Test
    void testStartPollingService_DoorStatusCorrect() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var elevator = mock(Elevator.class);
        var pollingTime = 100L;

        when(elevatorClient.getElevatorDoorStatus(elevator)).thenReturn(DoorStatus.OPEN);

        var elevatorStatusPollingService = new ElevatorStatusPollingService(elevatorClient, List.of(elevator), pollingTime);

        elevatorStatusPollingService.addObserver(elevatorStatusObserver);
        elevatorStatusPollingService.startPollingService();

        doAnswer(invocationOnMock -> {
            var elevatorStatuses = (List<ElevatorStatus>) invocationOnMock.getArgument(0);

            assertEquals(1, elevatorStatuses.size());

            var elevatorStatus = elevatorStatuses.get(0);

            assertEquals(DoorStatus.OPEN, elevatorStatus.getDoorStatus());

            return null;
        }).when(elevatorStatusObserver).update(anyList());

        verify(elevatorStatusObserver, timeout(1000)).update(anyList());
    }
}
