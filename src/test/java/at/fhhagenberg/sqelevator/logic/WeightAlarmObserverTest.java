package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorStatus;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeightAlarmObserverTest {

    @Test
    void testUpdate_WeightOverThreshold() {
        var elevator = new Elevator();
        elevator.setAlarms(FXCollections.observableList(new LinkedList<>()));
        elevator.setMaximumPayload(1000.0);

        var elevatorStatus = mock(ElevatorStatus.class);
        when(elevatorStatus.getElevator()).thenReturn(elevator);
        when(elevatorStatus.getPayload()).thenReturn(901.0);

        var weightAlarmObserver = new WeightAlarmObserver();

        weightAlarmObserver.update(List.of(elevatorStatus));

        assertEquals(1, elevator.getAlarms().size());

        var alarm = elevator.getAlarms().get(0);

        assertEquals("Payload approaching maximum payload!", alarm.getMessage());
    }

    @Test
    void testUpdate_WeightUnderThreshold() {
        var elevator = new Elevator();
        elevator.setAlarms(FXCollections.observableList(new LinkedList<>()));
        elevator.setMaximumPayload(1000.0);

        var elevatorStatus = mock(ElevatorStatus.class);
        when(elevatorStatus.getElevator()).thenReturn(elevator);
        when(elevatorStatus.getPayload()).thenReturn(0.0);

        var weightAlarmObserver = new WeightAlarmObserver();

        weightAlarmObserver.update(List.of(elevatorStatus));

        assertTrue(elevator.getAlarms().isEmpty());
    }

    @Test
    void testUpdate_DebounceAlarms() {
        var elevator = new Elevator();
        elevator.setAlarms(FXCollections.observableList(new LinkedList<>()));
        elevator.setMaximumPayload(1000.0);

        var elevatorStatus = mock(ElevatorStatus.class);
        when(elevatorStatus.getElevator()).thenReturn(elevator);
        when(elevatorStatus.getPayload()).thenReturn(901.0);

        var weightAlarmObserver = new WeightAlarmObserver();

        weightAlarmObserver.update(List.of(elevatorStatus));
        weightAlarmObserver.update(List.of(elevatorStatus));

        assertEquals(1, elevator.getAlarms().size());

        var alarm = elevator.getAlarms().get(0);

        assertEquals("Payload approaching maximum payload!", alarm.getMessage());
    }
}
