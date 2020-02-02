package sqelevator;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import sqelevator.IElevatorClient;

import java.rmi.RemoteException;
import java.util.List;

public class RemoteConsoleViewModel {

    private IElevatorClient client;
    private Thread pollingStatusThread;

    //    public ObjectProperty<Elevator> selectedElevatorProperty = new SimpleObjectProperty<>();
    public ListProperty<Elevator> elevatorListProperty;
    public ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.MANUAL);
    public BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);


    public RemoteConsoleViewModel(IElevatorClient client) throws RemoteException {
        this.client = client;

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
            }
        });

        this.pollingStatusThread.setDaemon(true);
        this.pollingStatusThread.start();
    }

    public void stopPollingStatus() {
        this.pollingStatusThread.interrupt();
    }

    public void targetFloor(Elevator elevator, int floorNumber) {
//        try {
//            client.setTarget(elevator.getElevatorNumber(), floorNumber);
//        } catch (RemoteException e) {
//            isConnectedProperty.set(false);
//        }
    }

    private void pollStatus(Elevator elevator) {
        final List<ElevatorFloor> floors = elevator.getElevatorFloors();

        Platform.runLater(() -> {
            try {
                client.getTargetedFloor(elevator).ifPresent(targetedFloor -> elevator.targetedElevatorFloorProperty.set(targetedFloor));

                elevator.currentElevatorFloorProperty.set(client.getCurrentFloor(elevator));
                elevator.doorsStatusProperty.set(client.getElevatorDoorStatus(elevator));
                elevator.velocityProperty.set(client.getCurrentVelocity(elevator));
                elevator.payloadProperty.set(client.getCurrentWeightLoad(elevator));
                elevator.directionProperty.set(client.getDirection(elevator));

                for (ElevatorFloor elevatorFloor : floors) {
                    elevatorFloor.upRequestProperty.set(client.hasFloorBeenRequestedUp(elevatorFloor.getFloor()));
                    elevatorFloor.downRequestProperty.set(client.hasFloorBeenRequestedDown(elevatorFloor.getFloor()));
                    elevatorFloor.serviceEnabled.set(client.isServiceEnabled(elevator, elevatorFloor.getFloor()));
                }

                isConnectedProperty.set(true);
            } catch (RemoteException e) {
                isConnectedProperty.set(false);
            }
        });
    }
}
