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
import java.util.List;

public class RemoteConsoleViewModel {

    private IElevator client;
    private Thread pollingStatusThread;

    public ObjectProperty<Elevator> selectedElevatorProperty = new SimpleObjectProperty<>();
    public ListProperty<Elevator> elevatorListProperty;

    public DoubleProperty velocityProperty = new SimpleDoubleProperty();
    public DoubleProperty payloadProperty = new SimpleDoubleProperty();

    public RemoteConsoleViewModel(IElevator client) throws RemoteException {
        this.client = client;

        List<Elevator> elevators = new ArrayList<>();
        List<ElevatorFloor> floors = new ArrayList<>();

        for (int j = 0; j < client.getFloorNum(); j++) {
            Floor floor = new Floor(j);
            floors.add(new ElevatorFloor(floor));
        }

        for (int i = 0; i < client.getElevatorNum(); i++) {
            Elevator elevator = new Elevator();
            elevator.setElevatorNumber(i);
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
                this.velocityProperty.set(client.getElevatorSpeed(currentElevatorNumber));
                this.payloadProperty.set(client.getElevatorWeight(currentElevatorNumber));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
