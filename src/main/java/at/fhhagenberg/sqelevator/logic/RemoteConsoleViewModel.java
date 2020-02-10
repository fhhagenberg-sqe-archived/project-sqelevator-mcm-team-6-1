package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import at.fhhagenberg.sqelevator.domain.*;

import java.rmi.RemoteException;

public class RemoteConsoleViewModel implements IRemoteConsoleViewModel, ElevatorStatusObserver {

    private IElevatorClient client;
    private IAutomaticModeStrategy automaticModeStrategy;

    private ListProperty<Elevator> elevatorListProperty;
    private ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.AUTOMATIC);
    private BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);

    public RemoteConsoleViewModel(IElevatorClient client, IAutomaticModeStrategy automaticModeStrategy, IElevatorStatusPollingService pollingService) {
        this.client = client;
        this.automaticModeStrategy = automaticModeStrategy;

        var elevators = this.client.getElevators();
        elevatorListProperty = new SimpleListProperty<>(FXCollections.observableList(elevators));

        pollingService.addObserver(this);
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
    public void update(Elevator elevator, ElevatorStatus elevatorStatus) {
        Platform.runLater(() -> {
            if (elevatorStatus.isConnected()) {
                this.isConnectedProperty.set(true);

                this.updateElevator(elevator, elevatorStatus);
                this.updateElevatorFloors(elevator, elevatorStatus);
                this.updateElevatorFloorButtons(elevator, elevatorStatus);

                if (modeProperty.get() == Mode.AUTOMATIC) {
                    automaticModeStrategy.execute(elevatorListProperty.get());
                }
            } else {
                isConnectedProperty.set(false);
            }
        });
    }

    private void updateElevator(Elevator elevator, ElevatorStatus elevatorStatus) {
        elevatorStatus.getTargetedFloor().ifPresent(elevator::setTargetedElevatorFloor);

        elevator.setCurrentElevatorFloor(elevatorStatus.getCurrentFloor());
        elevator.setDoorStatus(elevatorStatus.getDoorStatus());
        elevator.setVelocity(elevatorStatus.getVelocity());
        elevator.setPayload(elevatorStatus.getPayload());
        elevator.setDirection(elevatorStatus.getDirection());
    }

    private void updateElevatorFloors(Elevator elevator, ElevatorStatus elevatorStatus) {
        final var elevatorFloors = elevator.getElevatorFloors().toArray(ElevatorFloor[]::new);
        final var floorStatuses = elevatorStatus.getElevatorFloorStatuses();

        for (int floorNumber = 0; floorNumber < elevatorFloors.length; floorNumber++) {
            var elevatorFloor = elevatorFloors[floorNumber];
            var floorStatus = floorStatuses[floorNumber];

            elevatorFloor.setUpRequest(floorStatus.isUpRequested());
            elevatorFloor.setDownRequest(floorStatus.isDownRequested());
            elevatorFloor.setServiceEnabled(floorStatus.isServiced());
        }
    }

    private void updateElevatorFloorButtons(Elevator elevator, ElevatorStatus elevatorStatus) {
        var elevatorFloorButtons = elevator.getElevatorFloorButtons();
        var elevatorFloorButtonsStatus = elevatorStatus.getElevatorButtonStatuses();

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
