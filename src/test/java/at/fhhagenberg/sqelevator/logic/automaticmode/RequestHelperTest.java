package at.fhhagenberg.sqelevator.logic.automaticmode;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RequestHelperTest {

    @Mock
    private IElevatorClient elevatorClient;

    @ParameterizedTest
    @MethodSource("calculateDirectionSource")
    public void testCalculateDirection(int currentFloor, int nearestFloor, Direction expected) {
        RequestHelper requestHelper = new RequestHelper(elevatorClient);
        assertEquals(expected, requestHelper.calculateDirection(currentFloor, nearestFloor));
    }

    private static Stream<Arguments> calculateDirectionSource() {
        return Stream.of(
                Arguments.of(1, 2, Direction.UP),
                Arguments.of(4, -4, Direction.DOWN),
                Arguments.of(-3, -3, Direction.DOWN));
    }

    @ParameterizedTest
    @MethodSource("areAllElevatorsInitializedSource")
    public void testAreAllElevatorsInitialized(boolean initialized, List<Elevator> elevators, boolean expected) {
        RequestHelper requestHelper = new RequestHelper(elevatorClient);
        assertEquals(expected, requestHelper.areAllElevatorsInitialized(initialized, elevators));
    }

    private static Stream<Arguments> areAllElevatorsInitializedSource() {
        return Stream.of(
                Arguments.of(true, allElevatorsInitialized(), true),
                Arguments.of(false, allElevatorsInitialized(), false),
                Arguments.of(true, notAllElevatorsInitialized(), true),
                Arguments.of(false, notAllElevatorsInitialized(), false));
    }

    private static List<Elevator> allElevatorsInitialized() {
        Elevator elevator = new Elevator();
        Elevator elevator2 = new Elevator();
        var list = new ArrayList<Elevator>();
        list.add(elevator);
        list.add(elevator2);
        return list;
    }

    private static List<Elevator> notAllElevatorsInitialized() {
        Elevator elevator = new Elevator();
        elevator.setCurrentElevatorFloor(null);
        Elevator elevator2 = new Elevator();
        var list = new ArrayList<Elevator>();
        list.add(elevator);
        list.add(elevator2);
        return list;
    }

    @ParameterizedTest
    @MethodSource("checkElevatorAddressedSource")
    public void testCheckElevatorAlreadyAddressed(int toFloor, List<Elevator> elevators, boolean expected) throws RemoteException {
        RequestHelper requestHelper = new RequestHelper(elevatorClient);

        doAnswer(invocationOnMock -> {
            return new ElevatorFloor(new Floor(1));
        }).when(elevatorClient).getCurrentFloor(any(Elevator.class));
        doAnswer(invocationOnMock -> {
            return new ElevatorFloor(new Floor(toFloor));
        }).when(elevatorClient).getTargetedFloor(any(Elevator.class));
        assertEquals(expected, requestHelper.checkElevatorAlreadyAddressed(toFloor, elevators) == null);
    }

    private static Stream<Arguments> checkElevatorAddressedSource() {
        return Stream.of(
                Arguments.of(1, elevators(1, Direction.UNCOMMITED), false),
                Arguments.of(2, elevators(2, Direction.UNCOMMITED), true),
                Arguments.of(3, elevators(3, Direction.UNCOMMITED), true));
    }

    @Test
    public void testCheckElevatorAlreadyAddressed_ElevatorNull() throws RemoteException {
        RequestHelper requestHelper = new RequestHelper(elevatorClient);

        var elevator = requestHelper.checkElevatorAlreadyAddressed(1, null);

        assertNull(elevator);
    }

    @Test
    public void testCheckElevatorAlreadyAddressed_CurrentElevatorFloorNull_DirectionUncommitted() throws RemoteException {
        RequestHelper requestHelper = new RequestHelper(elevatorClient);

        Elevator elevator = new Elevator();
        elevator.setCurrentElevatorFloor(null);
        elevator.setDirection(Direction.UNCOMMITED);

        var result = requestHelper.checkElevatorAlreadyAddressed(1, List.of(elevator));

        assertNull(result);
    }

    @Test
    public void testCheckElevatorAlreadyAddressed_CurrentElevatorFloorNull_DirectionOtherThanUncommitted() throws RemoteException {

        Elevator elevator = new Elevator();
        elevator.setCurrentElevatorFloor(null);
        elevator.setDirection(Direction.UP);

        var elevatorClient = mock(IElevatorClient.class);
        when(elevatorClient.getTargetedFloor(elevator)).thenReturn(Optional.of(new ElevatorFloor(new Floor(1))));

        RequestHelper requestHelper = new RequestHelper(elevatorClient);

        var result = requestHelper.checkElevatorAlreadyAddressed(1, List.of(elevator));

        assertEquals(elevator, result);
    }

    private static List<Elevator> elevators(int floor, Direction direction) {
        Elevator elevator = new Elevator();

        elevator.setCurrentElevatorFloor(new ElevatorFloor(new Floor(floor)));
        elevator.setDirection(direction);
        var list = new ArrayList<Elevator>();
        list.add(elevator);
        return list;
    }

    @Test
    public void testFindAllAvailableFloors() throws RemoteException {
        var requestHelper = new RequestHelper(elevatorClient);
        assertEquals(0, requestHelper.findAvailableElevators(List.of()).size());
    }

    @Test
    public void testFindAllAvailableFloors_AllFloorsUncommitted() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var requestHelper = new RequestHelper(elevatorClient);

        var elevator = mock(Elevator.class);

        when(elevatorClient.getDirection(elevator)).thenReturn(Direction.UNCOMMITED);

        var result = requestHelper.findAvailableElevators(List.of(elevator));

        assertEquals(1,  result.size());
        assertEquals(elevator, result.get(0));
    }

    @Test
    public void testFindAllAvailableFloors_AllFloorsCommitted() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var requestHelper = new RequestHelper(elevatorClient);

        var elevator = mock(Elevator.class);

        when(elevatorClient.getDirection(elevator)).thenReturn(Direction.DOWN);

        var result = requestHelper.findAvailableElevators(List.of(elevator));

        assertEquals(0,  result.size());
    }
}
