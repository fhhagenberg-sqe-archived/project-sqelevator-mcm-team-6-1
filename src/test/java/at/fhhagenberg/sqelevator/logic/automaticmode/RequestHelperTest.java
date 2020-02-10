package at.fhhagenberg.sqelevator.logic.automaticmode;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Direction;
import at.fhhagenberg.sqelevator.domain.Elevator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RequestHelperTest {

    @Mock
    private IElevatorClient elevatorClient;

    @Mock
    private IAutomaticModeStrategy automaticModeStrategy;

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
                Arguments.of(false, allElevatorsInitialized(), true),
                Arguments.of(true, notAllElevatorsInitialized(), true),
                Arguments.of(false, notAllElevatorsInitialized(), true));
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
        //elevator2.setCurrentElevatorFloor(null);
        var list = new ArrayList<Elevator>();
        list.add(elevator);
        list.add(elevator2);
        return list;
    }
}
