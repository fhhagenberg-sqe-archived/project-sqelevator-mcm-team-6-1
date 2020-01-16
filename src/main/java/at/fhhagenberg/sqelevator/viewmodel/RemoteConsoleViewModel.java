package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.ElevatorFloor;
import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.model.Mode;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RemoteConsoleViewModel {

    private IElevator client;
    private Thread pollingStatusThread;

    //    public ObjectProperty<Elevator> selectedElevatorProperty = new SimpleObjectProperty<>();
    public ListProperty<Elevator> elevatorListProperty;
    public ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.MANUAL);
    public BooleanProperty isConnectedProperty = new SimpleBooleanProperty(true);


    public RemoteConsoleViewModel(IElevator client) throws RemoteException {
        this.client = client;

        List<Elevator> elevators = new ArrayList<>();

        var floors = IntStream.rangeClosed(1, client.getFloorNum())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .map(i -> new ElevatorFloor(new Floor(i)))
                .collect(Collectors.toList());

        for (int i = 0; i < client.getElevatorNum(); i++) {
            Elevator elevator = new Elevator();
            elevator.setElevatorNumber(i);
            elevator.setMaximumPayload(client.getElevatorCapacity(i));
            elevator.setElevatorFloors(floors);

            elevators.add(elevator);
        }

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

    private void pollStatus(Elevator elevator) {
        final int currentElevatorNumber = elevator.getElevatorNumber();
        final List<ElevatorFloor> floors = elevator.getElevatorFloors();

        Platform.runLater(() -> {
            try {
                elevator.currentElevatorFloorProperty.set(getCurrentFloor(elevator));
                elevator.doorsStatusProperty.set(client.getElevatorDoorStatus(currentElevatorNumber) == 1);
                elevator.velocityProperty.set(client.getElevatorSpeed(currentElevatorNumber));
                elevator.payloadProperty.set(client.getElevatorWeight(currentElevatorNumber));
                elevator.directionProperty.set(client.getCommittedDirection(currentElevatorNumber));

                for (ElevatorFloor elevatorFloor : floors) {
                    elevatorFloor.serviceEnabled.set(client.getServicesFloors(elevator.getElevatorNumber(), elevatorFloor.floorProperty.get().getFloorNumber()));
                }

                isConnectedProperty.set(true);
            } catch (RemoteException e) {
                isConnectedProperty.set(false);
            }
        });
    }

    private ElevatorFloor getCurrentFloor(Elevator currentElevator) throws RemoteException {
        var floorsOfCurrentElevator = currentElevator.getElevatorFloors();
        var currentFloorNumber = client.getElevatorFloor(currentElevator.getElevatorNumber());
        return floorsOfCurrentElevator.get(floorsOfCurrentElevator.size() - currentFloorNumber - 1);
    }
}
