package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import at.fhhagenberg.sqelevator.domain.*;
import java.rmi.RemoteException;
import java.util.List;

public class RemoteConsoleViewModel implements ElevatorStatusObserver {

    private IElevatorClient client;
    private IAutomaticModeStrategy automaticModeStrategy;

    public ListProperty<Elevator> elevatorListProperty;
    public ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.MANUAL);
    public BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);

    public RemoteConsoleViewModel(IElevatorClient client, IAutomaticModeStrategy automaticModeStrategy, IElevatorStatusPollingService pollingService) {
        this.client = client;
        this.automaticModeStrategy = automaticModeStrategy;

        var elevators = this.client.getElevators();
        elevatorListProperty = new SimpleListProperty<>(FXCollections.observableList(elevators));

        pollingService.addObserver(this);
    }

    public void targetFloor(Elevator elevator, Floor floor) {
        try {
            client.setTarget(elevator, floor);
        } catch (RemoteException e) {
            isConnectedProperty.set(false);
        }
    }

    @Override
    public void update(Elevator elevator, ElevatorStatus elevatorStatus) {
        Platform.runLater(() -> {
            if(elevatorStatus.isConnected()) {
                this.isConnectedProperty.set(true);

                this.updateElevator(elevator, elevatorStatus);
                this.updateElevatorFloors(elevator, elevatorStatus);
                this.updateElevatorFloorButtons(elevator, elevatorStatus);

                if (modeProperty.get() == Mode.AUTOMATIC) {
                    automaticModeStrategy.execute(elevatorListProperty.get());
                }
            }
            else {
                isConnectedProperty.set(false);
            }
        });
    }

    private void updateElevator(Elevator elevator, ElevatorStatus elevatorStatus) {
        elevatorStatus.getTargetedFloor().ifPresent(targetedFloor -> elevator.setTargetedElevatorFloor(targetedFloor));

        elevator.setCurrentElevatorFloor(elevatorStatus.getCurrentFloor());
        elevator.setDoorsStatus(elevatorStatus.getDoorStatus());
        elevator.setVelocity(elevatorStatus.getVelocity());
        elevator.setPayload(elevatorStatus.getPayload());
        elevator.setDirection(elevatorStatus.getDirection());
    }

    private void updateElevatorFloors(Elevator elevator, ElevatorStatus elevatorStatus)  {
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
}
