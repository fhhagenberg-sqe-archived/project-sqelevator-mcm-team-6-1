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

import java.lang.reflect.Array;
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
public class OutsideRequestManagerTest {

    @Mock
    private IElevatorClient client;
    @Mock
    private RequestHelper helper;


    @ParameterizedTest
    @MethodSource("outSideRequestsFromClientSource")
    public void testOutSideRequestsFromClient(int numberFloors, boolean up, boolean down, int[] expected) throws RemoteException {
        var outSideRequ = new OutsideRequestManager(client, helper);
        doAnswer(invocationOnMock -> {
            return up;
        }).when(client).getFloorButtonDown(any(Integer.class));
        doAnswer(invocationOnMock -> {
            return down;
        }).when(client).getFloorButtonDown(any(Integer.class));
        doAnswer(invocationOnMock -> {
            return numberFloors;
        }).when(client).getFloorNum();

        assertEquals(expected.length, outSideRequ.getOutsideRequestsFromClient().size());
    }

    private static Stream<Arguments> outSideRequestsFromClientSource() {
        return Stream.of(
                Arguments.of(3, true, false, new int[] {}),
                Arguments.of(4, true, true, new int[] {0, 1, 2, 3}),
                Arguments.of(1, true, false, new int[] {}),
                Arguments.of(2, false, true, new int[] {0, 1}),
                Arguments.of(3, false, false, new int[] {}),
                Arguments.of(4, true, true, new int[] { 0, 1, 2, 3}),
                Arguments.of(8, true, false, new int[] {}),
                Arguments.of(5, false, true,  new int[] { 0, 1, 2, 3, 4})
        );
    }

    @Test
    public void testFindClosestElevator() throws RemoteException {
        var outsideReqMg = new OutsideRequestManager(client, helper);
        doAnswer(invocationOnMock -> {
            return new ElevatorFloor(new Floor(0));
        }).when(client).getCurrentFloor(any(Elevator.class));

        assertEquals(null, outsideReqMg.findClosestElevator(
                elevators(1, Direction.DOWN),
                1,
                elevators(1, Direction.UNCOMMITED)
        ));

        assertEquals(null, outsideReqMg.findClosestElevator(
                elevators(1, Direction.UNCOMMITED),
                2,
                elevators(5, Direction.DOWN)
        ));

    }

    private static List<Elevator> elevators(int floor, Direction direction) {
        Elevator elevator = new Elevator();
        Elevator elevator2 = new Elevator();
        Elevator elevator3 = new Elevator();
        elevator.setCurrentElevatorFloor(new ElevatorFloor(new Floor(floor)));
        elevator.setDirection(direction);

        elevator.setCurrentElevatorFloor(new ElevatorFloor(new Floor(3)));
        elevator2.setDirection(Direction.DOWN);
        var list = new ArrayList<Elevator>();
        list.add(elevator);
        list.add(elevator2);
        list.add(elevator3);

        return list;
    }

    @ParameterizedTest
    @MethodSource("findNearestIntermediateOutsideRequesSourcet")
    public void testFindNearestIntermediateOutsideRequest(Integer nearestFloor, int currentFloor,
                                                          List<Integer> outsideRequests, List<Elevator> elevators, int expected) throws RemoteException {
        var outsideReqMg = new OutsideRequestManager(client, helper);
        doAnswer(invocationOnMock -> {
            return 23;
        }).when(client).getFloorNum();

        doCallRealMethod().when(helper).calculateDirection(any(Integer.class), any(Integer.class));
        assertEquals(expected, outsideReqMg.findNearestIntermediateOutsideRequest(nearestFloor, currentFloor, outsideRequests, elevators));
    }

    private static Stream<Arguments> findNearestIntermediateOutsideRequesSourcet() {
        return Stream.of(
                Arguments.of(3, 1, Arrays.asList(1,2,3), elevators(2, Direction.DOWN), 3),
                Arguments.of(5, 2, Arrays.asList(1,2,3, 4, 5), elevators(3, Direction.UP), 5),
                Arguments.of(3, 3, Arrays.asList(1,2), elevators(2, Direction.DOWN), 3),
                Arguments.of(null, 5, Arrays.asList(1,2,3, 4, 6), elevators(6, Direction.DOWN), 1)
        );
    }
}
