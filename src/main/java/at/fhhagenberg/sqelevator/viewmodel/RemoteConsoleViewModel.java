package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.ElevatorFloor;
import at.fhhagenberg.sqelevator.model.Floor;
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

    public ObjectProperty<Elevator> selectedElevatorProperty = new SimpleObjectProperty<>();
    public ListProperty<Elevator> elevatorListProperty;

    public ObjectProperty<ElevatorFloor> currentElevatorFloorProperty = new SimpleObjectProperty<>();
    public BooleanProperty doorsStatusProperty = new SimpleBooleanProperty();
    public DoubleProperty velocityProperty = new SimpleDoubleProperty();
    public DoubleProperty payloadProperty = new SimpleDoubleProperty();

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
            elevator.setElevatorNumber(i + 1);
            elevator.setMaximumPayload(client.getElevatorCapacity(i));
            elevator.setElevatorFloors(floors);

            elevators.add(elevator);
        }

        elevatorListProperty = new SimpleListProperty<>(FXCollections.observableList(elevators));
        selectedElevatorProperty.set(elevatorListProperty.get(0));
    }

    public void startPollingStatus() {
        this.pollingStatusThread = new Thread(() -> {
            while (true) {
                try {
                    pollStatus();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.pollingStatusThread.setDaemon(true);
        this.pollingStatusThread.start();
    }

    public void stopPollingStatus() {
        this.pollingStatusThread.interrupt();
    }

    private void pollStatus() {
        final Elevator currentElevator = selectedElevatorProperty.get();
        final int currentElevatorNumber = currentElevator.getElevatorNumber();

        Platform.runLater(() -> {
            try {
                this.currentElevatorFloorProperty.set(getCurrentFloor(currentElevator));
                this.doorsStatusProperty.set(client.getElevatorDoorStatus(currentElevatorNumber) == 1);
                this.velocityProperty.set(client.getElevatorSpeed(currentElevatorNumber));
                this.payloadProperty.set(client.getElevatorWeight(currentElevatorNumber));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private ElevatorFloor getCurrentFloor(Elevator currentElevator) throws RemoteException {
        var floorsOfCurrentElevator = currentElevator.getElevatorFloors();
        var currentFloorNumber = client.getElevatorFloor(currentElevator.getElevatorNumber());
        return floorsOfCurrentElevator.get(floorsOfCurrentElevator.size() - currentFloorNumber - 1);
    }
}
