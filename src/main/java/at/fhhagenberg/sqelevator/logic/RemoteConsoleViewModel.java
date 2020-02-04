package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Mode;
import at.fhhagenberg.sqelevator.logic.AutomaticElevatorMode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Observable;

public class RemoteConsoleViewModel extends Observable {

    private IElevatorClient client;
    private Thread pollingStatusThread;

    //    public ObjectProperty<Elevator> selectedElevatorProperty = new SimpleObjectProperty<>();
    public ListProperty<Elevator> elevatorListProperty;
    public ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.MANUAL);
    public BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);


    public RemoteConsoleViewModel(IElevatorClient client) throws RemoteException {
        this.client = client;

        var automaticElevatorMode = new AutomaticElevatorMode(this.client);
        this.addObserver(automaticElevatorMode);

        this.modeProperty.addListener(
                (observable, oldvalue, newvalue) ->
                { this.setChanged(); this.notifyObservers(newvalue);}
        );

        var elevators = this.client.getElevators();
        this.setChanged();
        this.notifyObservers(elevators);

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

                this.setChanged();
                this.notifyObservers(elevatorListProperty.get());
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

                elevator.currentElevatorFloorProperty().set(client.getCurrentFloor(elevator));
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
