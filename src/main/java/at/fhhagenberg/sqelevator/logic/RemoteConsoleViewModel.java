package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import at.fhhagenberg.sqelevator.domain.*;
import java.rmi.RemoteException;
import java.util.List;

public class RemoteConsoleViewModel {

    private IElevatorClient client;
    private Thread pollingStatusThread;
    private IAutomaticModeStrategy automaticModeStrategy;

    public ListProperty<Elevator> elevatorListProperty;
    public ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.MANUAL);
    public BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);


    public RemoteConsoleViewModel(IElevatorClient client, IAutomaticModeStrategy automaticModeStrategy) {
        this.client = client;
        this.automaticModeStrategy = automaticModeStrategy;
        this.automaticModeStrategy.setClient(client);

        var elevators = this.client.getElevators();

        elevatorListProperty = new SimpleListProperty<>(FXCollections.observableList(elevators));
    }

    public void startPollingStatus() {
        this.pollingStatusThread = new Thread(() -> {
            while (true) {
                for (Elevator elevator : elevatorListProperty) {
                    try {
                        pollStatus(elevator);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (modeProperty.get() == Mode.AUTOMATIC) {
                    automaticModeStrategy.execute(elevatorListProperty.get());
                }
            }
        });

        this.pollingStatusThread.setDaemon(true);
        this.pollingStatusThread.start();
    }

    public void stopPollingStatus() {
        this.pollingStatusThread.interrupt();
    }

    public void targetFloor(Elevator elevator, Floor floor) {
        try {
            client.setTarget(elevator, floor);
        } catch (RemoteException e) {
            isConnectedProperty.set(false);
        }
    }

    private void pollStatus(Elevator elevator) {
        final List<ElevatorFloor> floors = elevator.getElevatorFloors();

        Platform.runLater(() -> {
            try {
                client.getTargetedFloor(elevator).ifPresent(targetedFloor -> elevator.setTargetedElevatorFloor(targetedFloor));

                elevator.setCurrentElevatorFloor(client.getCurrentFloor(elevator));
                elevator.setDoorsStatus(client.getElevatorDoorStatus(elevator));
                elevator.setVelocity(client.getCurrentVelocity(elevator));
                elevator.setPayload(client.getCurrentWeightLoad(elevator));
                elevator.setDirection(client.getDirection(elevator));

                for (ElevatorFloor elevatorFloor : floors) {
                    elevatorFloor.setUpRequest(client.hasFloorBeenRequestedUp(elevatorFloor.getFloor()));
                    elevatorFloor.setDownRequest(client.hasFloorBeenRequestedDown(elevatorFloor.getFloor()));
                    elevatorFloor.setServiceEnabled(client.isServiceEnabled(elevator, elevatorFloor.getFloor()));
                }

                var elevatorFloorButtons = elevator.getElevatorFloorButtons();
                var elevatorFloorButtonStatus = client.getElevatorFloorButtonsStatus(elevator);

                for (int i = 0; i < elevatorFloorButtons.length; i++) {
                    var elevatorFloorButton = elevatorFloorButtons[i];
                    var hasButtonBeenPressed = elevatorFloorButtonStatus[i];

                    elevatorFloorButton.setHasBeenPressed(hasButtonBeenPressed);
                }

                isConnectedProperty.set(true);
            } catch (RemoteException e) {
                isConnectedProperty.set(false);
            }
        });
    }
}
