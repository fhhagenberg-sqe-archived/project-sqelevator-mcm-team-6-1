package at.fhhagenberg.sqelevator.domain;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Elevator {

    private int elevatorNumber;
    private int maximumPayload;
    private ElevatorStatus elevatorStatus;

    private ListProperty<ElevatorFloor> elevatorFloors = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ObjectProperty<ElevatorFloor> currentElevatorFloor = new SimpleObjectProperty<>();
    public ObjectProperty<ElevatorFloor> targetedElevatorFloorProperty = new SimpleObjectProperty<>();
    public ObjectProperty<DoorStatus> doorsStatusProperty = new SimpleObjectProperty<>(DoorStatus.OPEN);
    public DoubleProperty velocityProperty = new SimpleDoubleProperty();
    public DoubleProperty payloadProperty = new SimpleDoubleProperty();
    public ObjectProperty<Direction> directionProperty = new SimpleObjectProperty<>(Direction.UNCOMMITED);

    private ListProperty<Integer> floorRequests = new SimpleListProperty<>();
    public ListProperty<Alarm> alarmListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

    public int getElevatorNumber() {
        return elevatorNumber;
    }

    public void setElevatorNumber(int elevatorNumber) {
        this.elevatorNumber = elevatorNumber;
    }

    public int getMaximumPayload() {
        return maximumPayload;
    }

    public void setMaximumPayload(int maximumPayload) {
        this.maximumPayload = maximumPayload;
    }

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public List<ElevatorFloor> getElevatorFloors() {
        return elevatorFloors;
    }

    public void setElevatorFloors(List<ElevatorFloor> elevatorFloors) {
        this.elevatorFloors.addAll(elevatorFloors);
    }

    public void addElevatorFloor(ElevatorFloor elevatorFloor) {
        this.elevatorFloors.add(elevatorFloor);
    }

    public ElevatorFloor getCurrentElevatorFloor() {
        return currentElevatorFloor.get();
    }

    public ObjectProperty<ElevatorFloor> currentElevatorFloorProperty() {
        return currentElevatorFloor;
    }

    public void setCurrentElevatorFloor(ElevatorFloor currentElevatorFloor) {
        this.currentElevatorFloor.set(currentElevatorFloor);
    }

    public ObservableList<Integer> getFloorRequests() {
        return floorRequests.get();
    }

    public ListProperty<Integer> floorRequestsProperty() {
        return floorRequests;
    }

    public void setFloorRequests(ObservableList<Integer> floorRequests) {
        this.floorRequests.set(floorRequests);
    }

    public boolean isFloorRequested(Integer floor) {
        return this.getFloorRequests().contains(floor);
    }

    public int getCurrentFloorNumber() {
        return this.getCurrentElevatorFloor().getFloor().getFloorNumber();
    }

    @Override
    public String toString() {
        return "Elevator " + elevatorNumber;
    }
}
