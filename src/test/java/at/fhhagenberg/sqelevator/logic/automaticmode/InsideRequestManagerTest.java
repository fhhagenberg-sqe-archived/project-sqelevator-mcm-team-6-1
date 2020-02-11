package at.fhhagenberg.sqelevator.logic.automaticmode;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Direction;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class InsideRequestManagerTest {

    @Mock
    private IElevatorClient client;
    @Mock
    private RequestHelper helper;

    @Test
    public void testInitialize() throws RemoteException {
        var inside = new InsideRequestManager(client, helper);
        doAnswer(invocationOnMock -> {
            var elevator = new Elevator();
            var elevator2 = new Elevator();
            var elevator3 = new Elevator();
            var elevator4 = new Elevator();

            return Arrays.asList(elevator, elevator2, elevator3, elevator4);
        }).when(client).getElevators();
        assertEquals(4, inside.initialize().length);
    }

    @ParameterizedTest
    @MethodSource("findNearestInsideRequestsSource")
    public void testFindNearestInsideRequests(int index, Elevator elevator, List<Integer>[] insideRequests, int expected) throws RemoteException {
        var inside = new InsideRequestManager(client, helper);
        doAnswer(invocationOnMock -> {
            return new ElevatorFloor(new Floor(index));
        }).when(client).getCurrentFloor(any(Elevator.class));

        assertEquals(expected, inside.findNearestInsideRequest(index, elevator, insideRequests));
    }

    private static Stream<Arguments> findNearestInsideRequestsSource() {
        return Stream.of(
                Arguments.of(0, new Elevator(), new List[] {Arrays.asList( 1,2,3 )}, 1),
                Arguments.of(0, new Elevator(), new List[] {Arrays.asList(1,2,3, 4, 5)}, 1),
                Arguments.of(0, new Elevator(), new List[] {Arrays.asList(1,2)}, 1),
                Arguments.of(0, new Elevator(), new List[] {Arrays.asList(1,2,3, 4, 6)}, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("findNearestInsideRequestsToCurrentRequestSource")
    public void testFindNearestInsideRequestsToCurrentRequest(int index, Integer nearestFloor,
                                                              int currentFloor, List<Integer>[] insideRequests,
                                                              List<Elevator> elevators, Integer expected) throws RemoteException {
        var inside = new InsideRequestManager(client, helper);
        doCallRealMethod().when(helper).calculateDirection(any(Integer.class), any(Integer.class));
        doAnswer(invocationOnMock -> {
            return new ElevatorFloor(new Floor(index));
        }).when(client).getCurrentFloor(any(Elevator.class));

        assertEquals(expected, inside.findNearestInsideRequestToCurrentRequest(index, nearestFloor, currentFloor, insideRequests, elevators));
    }

    private static Stream<Arguments> findNearestInsideRequestsToCurrentRequestSource() {
        return Stream.of(
                Arguments.of(0, 3, 2, new List[] {Arrays.asList( 1,2,3 )}, elevators(), 3),
                Arguments.of(0, 4, 5, new List[] {Arrays.asList(1,2,3, 4, 5)}, elevators(), 5),
                Arguments.of(0, null, 1, new List[] {Arrays.asList(1,2)}, elevators(), null),
                Arguments.of(0, 6, 4, new List[] {Arrays.asList(1,2,3, 4, 6)}, elevators(), 6)
        );
    }
    private static List<Elevator> elevators() {
        Elevator elevator = new Elevator();
        elevator.setCurrentElevatorFloor(null);
        Elevator elevator2 = new Elevator();
        var list = new ArrayList<Elevator>();
        list.add(elevator);
        list.add(elevator2);
        return list;
    }


    @Test
    public void testInsideRequestsFromClient() throws RemoteException {
        doAnswer(invocationOnMock -> {
            return elevators();
        }).when(client).getElevators();
        doAnswer(invocationOnMock -> {
            return 3;
        }).when(client).getFloorNum();
        doAnswer(invocationOnMock -> {
            return new boolean[] { true, false, true, false};
        }).when(client).getElevatorFloorButtonsStatus(any(Elevator.class));

        var insideRequestManager = new InsideRequestManager(client, helper);
        insideRequestManager.initialize();

        assertEquals(2, insideRequestManager.getInsideRequestsFromClient(elevators()).length);
        var elevators = elevators();
        elevators.remove(elevators.get(0));
        assertEquals(2, insideRequestManager.getInsideRequestsFromClient(elevators).length);
    }
}
