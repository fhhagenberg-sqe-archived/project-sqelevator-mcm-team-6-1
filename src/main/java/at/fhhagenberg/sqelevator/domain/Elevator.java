package at.fhhagenberg.sqelevator.domain;

import at.fhhagenberg.sqelevator.view.controls.ElevatorFloorButton;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Elevator {

    private int elevatorNumber;
    private double maximumPayload;
    private ElevatorStatus elevatorStatus;

    private ListProperty<ElevatorFloor> elevatorFloors = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<ElevatorFloorButton> floorButtons = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ObjectProperty<ElevatorFloor> currentElevatorFloor = new SimpleObjectProperty<>();
    private ObjectProperty<ElevatorFloor> targetedElevatorFloor = new SimpleObjectProperty<>();

    private ObjectProperty<DoorStatus> doorStatus = new SimpleObjectProperty<>(DoorStatus.OPEN);
    private DoubleProperty velocity = new SimpleDoubleProperty();
    private DoubleProperty payload = new SimpleDoubleProperty();
    private ObjectProperty<Direction> direction = new SimpleObjectProperty<>(Direction.UNCOMMITED);
    private ListProperty<Integer> floorRequests = new SimpleListProperty<>();
    private ListProperty<Alarm> alarms = new SimpleListProperty<>(FXCollections.observableArrayList());

    public int getElevatorNumber() {
        return elevatorNumber;
    }

    public void setElevatorNumber(int elevatorNumber) {
        this.elevatorNumber = elevatorNumber;
    }

    public double getMaximumPayload() {
        return maximumPayload;
    }

    public void setMaximumPayload(double maximumPayload) {
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

        createElevatorButtons(elevatorFloors.size());
    }

    private void createElevatorButtons(int numberOfFloors) {
        this.floorButtons.clear();

        var elevatorButtons = IntStream.range(0, numberOfFloors)
                .boxed()
                .map(index -> new ElevatorFloorButton(index))
                .collect(Collectors.toList());

        this.floorButtons.addAll(elevatorButtons);
    }

    public ElevatorFloorButton[] getElevatorFloorButtons() {
        return this.floorButtons.get().toArray(ElevatorFloorButton[]::new);
    }

    public ObservableList<Integer> getFloorRequests() {
        return floorRequests.get();
    }

    public void setFloorRequests(ObservableList<Integer> floorRequests) {
        this.floorRequests.set(floorRequests);
    }

    public boolean isFloorRequested(Integer floor) {
        return this.getFloorRequests().contains(floor);
    }

    public int getCurrentFloorNumber() {
        if (this.getCurrentElevatorFloor() == null) {
            return -1;
        }
        return this.getCurrentElevatorFloor().getFloor().getFloorNumber();
    }

    @Override
    public String toString() {
        return "Elevator " + elevatorNumber;
    }

    public ListProperty<ElevatorFloor> elevatorFloorsProperty() {
        return elevatorFloors;
    }

    public void setElevatorFloors(ObservableList<ElevatorFloor> elevatorFloors) {
        this.elevatorFloors.set(elevatorFloors);
    }

    public ObservableList<ElevatorFloorButton> getFloorButtons() {
        return floorButtons.get();
    }

    public ListProperty<ElevatorFloorButton> floorButtonsProperty() {
        return floorButtons;
    }

    public void setFloorButtons(ObservableList<ElevatorFloorButton> floorButtons) {
        this.floorButtons.set(floorButtons);
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

    public ElevatorFloor getTargetedElevatorFloor() {
        return targetedElevatorFloor.get();
    }

    public ObjectProperty<ElevatorFloor> targetedElevatorFloorProperty() {
        return targetedElevatorFloor;
    }

    public void setTargetedElevatorFloor(ElevatorFloor targetedElevatorFloor) {
        this.targetedElevatorFloor.set(targetedElevatorFloor);
    }

    public DoorStatus getDoorStatus() {
        return doorStatus.get();
    }

    public ObjectProperty<DoorStatus> doorStatusProperty() {
        return doorStatus;
    }

    public void setDoorStatus(DoorStatus doorStatus) {
        this.doorStatus.set(doorStatus);
    }

    public double getVelocity() {
        return velocity.get();
    }

    public DoubleProperty velocityProperty() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity.set(velocity);
    }

    public double getPayload() {
        return payload.get();
    }

    public DoubleProperty payloadProperty() {
        return payload;
    }

    public void setPayload(double payload) {
        this.payload.set(payload);
    }

    public Direction getDirection() {
        return direction.get();
    }

    public ObjectProperty<Direction> directionProperty() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction.set(direction);
    }

    public ListProperty<Integer> floorRequestsProperty() {
        return floorRequests;
    }

    public ObservableList<Alarm> getAlarms() {
        return alarms.get();
    }

    public ListProperty<Alarm> alarmsProperty() {
        return alarms;
    }

    public void setAlarms(ObservableList<Alarm> alarms) {
        this.alarms.set(alarms);
    }
}
