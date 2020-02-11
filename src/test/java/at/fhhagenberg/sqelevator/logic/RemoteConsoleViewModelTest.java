package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.logic.automaticmode.IAutomaticModeStrategy;
import at.fhhagenberg.sqelevator.view.controls.ElevatorFloorButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

        viewModel.getModeProperty().set(Mode.MANUAL);
        viewModel.targetFloor(mock(Elevator.class), mock(Floor.class));

        verify(elevatorClient).setTarget(any(Elevator.class), any(Floor.class));
    }

    @Test
    void testSetTarget_ThrowRemoteException() throws RemoteException {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        doThrow(RemoteException.class).when(elevatorClient).setTarget(any(Elevator.class), any(Floor.class));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);
        viewModel.getModeProperty().set(Mode.MANUAL);

        viewModel.targetFloor(mock(Elevator.class), mock(Floor.class));

        assertFalse(viewModel.getIsConnectedProperty().get());
    }

    @Test
    void testSetTarget_ElevatorNull() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);
        viewModel.getModeProperty().set(Mode.MANUAL);

        assertThrows(IllegalArgumentException.class, () -> viewModel.targetFloor(null, mock(Floor.class)));
    }

    @Test
    void testSetTarget_ManualMode() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);
        viewModel.getModeProperty().set(Mode.AUTOMATIC);

        assertDoesNotThrow(() -> viewModel.targetFloor(null, null));
    }

    @Test
    void testSetTarget_FloorNull() {
        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);
        viewModel.getModeProperty().set(Mode.MANUAL);

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

    @Test
    void testUpdate_NotConnected() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);
        elevator.setVelocity(0.0);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var elevatorStatus = ElevatorStatus.build(elevator)
                .notConnected();

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);
        assertTrue(viewModel.getIsConnectedProperty().get());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();

        assertFalse(viewModel.getIsConnectedProperty().get());
    }

    @Test
    void testUpdate_SetVelocity() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);
        elevator.setVelocity(0.0);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorStatus = ElevatorStatus.build(elevator)
                .velocity(5.0)
                .get();

        assertEquals(0.0, viewModel.getElevatorListProperty().get(0).getVelocity());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();

        assertEquals(5.0, viewModel.getElevatorListProperty().get(0).getVelocity());
    }

    @Test
    void testUpdate_SetPayload() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);
        elevator.setPayload(10.0);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorStatus = ElevatorStatus.build(elevator)
                .payload(150.0)
                .get();

        assertEquals(10.0, viewModel.getElevatorListProperty().get(0).getPayload());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();

        assertEquals(150.0, viewModel.getElevatorListProperty().get(0).getPayload());
    }

    @Test
    void testUpdate_SetButtonStatuses() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);
        elevator.setFloorButtons(FXCollections.observableList(List.of(new ElevatorFloorButton(0), new ElevatorFloorButton(1), new ElevatorFloorButton(2))));

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorStatus = ElevatorStatus.build(elevator)
                .buttonStatus(new boolean[]{true, false, true})
                .get();

        var floorButtons = viewModel.getElevatorListProperty().get(0).getElevatorFloorButtons();

        assertFalse(floorButtons[0].hasBeenPressed());
        assertFalse(floorButtons[1].hasBeenPressed());
        assertFalse(floorButtons[2].hasBeenPressed());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();


        var updatedFloorButtons = viewModel.getElevatorListProperty().get(0).getElevatorFloorButtons();

        assertEquals(3, floorButtons.length);
        assertTrue(updatedFloorButtons[0].hasBeenPressed());
        assertFalse(updatedFloorButtons[1].hasBeenPressed());
        assertTrue(updatedFloorButtons[2].hasBeenPressed());
    }

    @Test
    void testUpdate_SetTargetedFloor() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var targetedFloor = mock(ElevatorFloor.class);
        var elevatorStatus = ElevatorStatus.build(elevator)
                .targetedFloor(targetedFloor)
                .get();

        assertNull(viewModel.getElevatorListProperty().get(0).getTargetedElevatorFloor());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();

        assertEquals(targetedFloor, viewModel.getElevatorListProperty().get(0).getTargetedElevatorFloor());
    }

    @Test
    void testUpdate_SetCurrentFloor() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var currentFloor = mock(ElevatorFloor.class);
        var elevatorStatus = ElevatorStatus.build(elevator)
                .currentFloor(currentFloor)
                .get();

        viewModel.update(List.of(elevatorStatus));

        waitForRunLater();

        assertEquals(currentFloor, viewModel.getElevatorListProperty().get(0).getCurrentElevatorFloor());
    }

    @Test
    void testUpdate_UpdateElevatorFloor_SetUpRequest() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);

        var elevatorFloor = new ElevatorFloor(new Floor(0));
        elevatorFloor.setUpRequest(false);
        elevator.setElevatorFloors(FXCollections.observableList(List.of(elevatorFloor)));

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorFloorStatus = ElevatorFloorStatus.build(elevatorFloor)
                .upRequested(true)
                .get();

        var elevatorStatus = ElevatorStatus.build(elevator)
                .elevatorFloorStatus(new ElevatorFloorStatus[]{elevatorFloorStatus})
                .get();

        viewModel.update(List.of(elevatorStatus));

        waitForRunLater();

        var elevatorFloors = viewModel.getElevatorListProperty().get(0).getElevatorFloors();

        assertEquals(1, elevatorFloors.size());
        assertTrue(elevatorFloors.get(0).getUpRequest().get());
    }

    @Test
    void testUpdate_UpdateElevatorFloor_SetDownRequest() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);

        var elevatorFloor = new ElevatorFloor(new Floor(0));
        elevatorFloor.setDownRequest(false);
        elevator.setElevatorFloors(FXCollections.observableList(List.of(elevatorFloor)));

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorFloorStatus = ElevatorFloorStatus.build(elevatorFloor)
                .downRequested(true)
                .get();

        var elevatorStatus = ElevatorStatus.build(elevator)
                .elevatorFloorStatus(new ElevatorFloorStatus[]{elevatorFloorStatus})
                .get();

        viewModel.update(List.of(elevatorStatus));

        waitForRunLater();

        var elevatorFloors = viewModel.getElevatorListProperty().get(0).getElevatorFloors();

        assertEquals(1, elevatorFloors.size());
        assertTrue(elevatorFloors.get(0).getDownRequest().get());
    }

    @Test
    void testUpdate_UpdateElevatorFloor_SetServiceEnabled() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);

        var elevatorFloor = new ElevatorFloor(new Floor(0));
        elevatorFloor.setServiceEnabled(false);
        elevator.setElevatorFloors(FXCollections.observableList(List.of(elevatorFloor)));

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorFloorStatus = ElevatorFloorStatus.build(elevatorFloor)
                .serviced(true)
                .get();

        var elevatorStatus = ElevatorStatus.build(elevator)
                .elevatorFloorStatus(new ElevatorFloorStatus[]{elevatorFloorStatus})
                .get();

        viewModel.update(List.of(elevatorStatus));

        waitForRunLater();

        var elevatorFloors = viewModel.getElevatorListProperty().get(0).getElevatorFloors();

        assertEquals(1, elevatorFloors.size());
        assertTrue(elevatorFloors.get(0).getServiceEnabled().get());
    }

    @Test
    void testUpdate_SetDirection() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);
        elevator.setDirection(Direction.UNCOMMITED);
        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var elevatorStatus = ElevatorStatus.build(elevator)
                .direction(Direction.DOWN)
                .get();

        assertEquals(Direction.UNCOMMITED, viewModel.getElevatorListProperty().get(0).getDirection());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();

        assertEquals(Direction.DOWN, viewModel.getElevatorListProperty().get(0).getDirection());
    }

    @Test
    void testUpdate_SetDoorStatus() throws InterruptedException {
        // workaround for Platform.runLater(...) in view model
        var panel = new JFXPanel();

        var elevatorClient = mock(IElevatorClient.class);
        var automaticModeStrategy = mock(IAutomaticModeStrategy.class);

        var elevator = new Elevator();
        elevator.setElevatorNumber(1);
        elevator.setDoorStatus(DoorStatus.OPEN);

        when(elevatorClient.getElevators()).thenReturn(List.of(elevator));

        var viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);
        var elevatorStatus = ElevatorStatus.build(elevator)
                .doorStatus(DoorStatus.CLOSED)
                .get();

        assertEquals(DoorStatus.OPEN, viewModel.getElevatorListProperty().get(0).getDoorStatus());

        viewModel.update(List.of(elevatorStatus));
        waitForRunLater();

        assertEquals(DoorStatus.CLOSED, viewModel.getElevatorListProperty().get(0).getDoorStatus());
    }

    private void waitForRunLater() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(countDownLatch::countDown);
        countDownLatch.await();
    }
}
