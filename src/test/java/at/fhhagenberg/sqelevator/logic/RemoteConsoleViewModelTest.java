package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoteConsoleViewModelTest {

    @Test
    void testSetTarget() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);
        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        viewModel.targetFloor(mock(Elevator.class), mock(Floor.class));

        verify(elevatorClient).setTarget(any(Elevator.class), any(Floor.class));
    }

    @Test
    void testSetTarget_ElevatorNull() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        assertThrows(IllegalArgumentException.class, () -> viewModel.targetFloor(null, mock(Floor.class)));
    }

    @Test
    void testSetTarget_FloorNull() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        assertThrows(IllegalArgumentException.class, () -> viewModel.targetFloor(mock(Elevator.class), null));
    }

    @Test
    void testGetMode_InitialMode() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        assertEquals(Mode.AUTOMATIC, viewModel.getModeProperty().get());
    }

    @Test
    void testIsConnected_InitialValue() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        assertTrue(viewModel.getIsConnectedProperty().get());
    }

    @Test
    void testGetElevatorListProperty() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        assertEquals(1, viewModel.getElevatorListProperty().get().size());
        assertEquals(elevator.getElevatorNumber(), viewModel.getElevatorListProperty().get().get(0).getElevatorNumber());
    }
}
