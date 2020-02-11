package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.Mode;
import at.fhhagenberg.sqelevator.logic.automaticmode.IAutomaticModeStrategy;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import at.fhhagenberg.sqelevator.domain.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RemoteConsoleViewModel implements IRemoteConsoleViewModel {

    private IElevatorClient client;
    private IAutomaticModeStrategy automaticModeStrategy;

    private ListProperty<Elevator> elevatorListProperty;
    private ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.AUTOMATIC);
    private BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);

    public RemoteConsoleViewModel(IElevatorClient client, IAutomaticModeStrategy automaticModeStrategy) {
        this.client = client;
        this.automaticModeStrategy = automaticModeStrategy;

        var elevators = this.client.getElevators();
        elevatorListProperty = new SimpleListProperty<>(FXCollections.observableList(elevators));
    }

    @Override
    public void targetFloor(Elevator elevator, Floor floor) {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        if (floor == null) {
            throw new IllegalArgumentException("Floor must not be null!");
        }

        try {
            client.setTarget(elevator, floor);
        } catch (RemoteException e) {
            isConnectedProperty.set(false);
        }
    }

    @Override
    public void update(List<ElevatorStatus> elevatorStatuses) {
        for (var elevatorStatus : elevatorStatuses) {
            Platform.runLater(() -> {
                if (elevatorStatus.isConnected()) {
                    this.isConnectedProperty.set(true);

                    this.updateElevator(elevatorStatus);
                    this.updateElevatorFloors(elevatorStatus);
                    this.updateElevatorFloorButtons(elevatorStatus);

                    if (this.modeProperty.get() == Mode.AUTOMATIC) {
                        CompletableFuture.runAsync(() -> this.automaticModeStrategy.execute(elevatorListProperty.get()));
                    }
                } else {
                    isConnectedProperty.set(false);
                }
            });
        }
    }

    private void updateElevator(ElevatorStatus elevatorStatus) {
        final var elevator = elevatorStatus.getElevator();
        elevatorStatus.getTargetedFloor().ifPresent(elevator::setTargetedElevatorFloor);

        elevator.setCurrentElevatorFloor(elevatorStatus.getCurrentFloor());
        elevator.setDoorStatus(elevatorStatus.getDoorStatus());
        elevator.setVelocity(elevatorStatus.getVelocity());
        elevator.setPayload(elevatorStatus.getPayload());
        elevator.setDirection(elevatorStatus.getDirection());
    }

    private void updateElevatorFloors(ElevatorStatus elevatorStatus) {
        for (var elevatorFloorStatus : elevatorStatus.getElevatorFloorStatuses()) {
            var elevatorFloor = elevatorFloorStatus.getElevatorFloor();

            elevatorFloor.setUpRequest(elevatorFloorStatus.isUpRequested());
            elevatorFloor.setDownRequest(elevatorFloorStatus.isDownRequested());
            elevatorFloor.setServiceEnabled(elevatorFloorStatus.isServiced());
        }
    }

    private void updateElevatorFloorButtons(ElevatorStatus elevatorStatus) {
        final var elevator = elevatorStatus.getElevator();
        final var elevatorFloorButtons = elevator.getElevatorFloorButtons();
        final var elevatorFloorButtonsStatus = elevatorStatus.getElevatorButtonStatuses();

        for (int buttonNumber = 0; buttonNumber < elevatorFloorButtons.length; buttonNumber++) {
            var elevatorFloorButton = elevatorFloorButtons[buttonNumber];
            var hasButtonBeenPressed = elevatorFloorButtonsStatus[buttonNumber];

            elevatorFloorButton.setHasBeenPressed(hasButtonBeenPressed);
        }
    }

    @Override
    public ListProperty<Elevator> getElevatorListProperty() {
        return elevatorListProperty;
    }

    @Override
    public BooleanProperty getIsConnectedProperty() {
        return isConnectedProperty;
    }

    @Override
    public ObjectProperty<Mode> getModeProperty() {
        return modeProperty;
    }
}
