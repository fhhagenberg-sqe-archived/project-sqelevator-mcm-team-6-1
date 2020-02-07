package at.fhhagenberg.sqelevator.domain;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Elevator {

    private int elevatorNumber;
    private int maximumPayload;
    private ElevatorStatus elevatorStatus;

    public ListProperty<ElevatorFloor> elevatorFloors = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ListProperty<ElevatorFloorButton> floorButtonsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

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

    public void addElevatorFloor(ElevatorFloor elevatorFloor) {
        this.elevatorFloors.add(elevatorFloor);
    }

    public void setElevatorFloors(List<ElevatorFloor> elevatorFloors) {
        this.elevatorFloors.addAll(elevatorFloors);

        createElevatorButtons(elevatorFloors.size());
    }

    private void createElevatorButtons(int numberOfFloors) {
        this.floorButtonsProperty.clear();

        var elevatorButtons = IntStream.range(0, numberOfFloors)
                .boxed()
                .map(index -> new ElevatorFloorButton(index))
                .collect(Collectors.toList());

        this.floorButtonsProperty.addAll(elevatorButtons);
    }

    public ElevatorFloorButton[] getElevatorFloorButtons() {
        return this.floorButtonsProperty.get().toArray(ElevatorFloorButton[]::new);
    }


    public ObjectProperty<ElevatorFloor> currentElevatorFloorProperty() {
        return currentElevatorFloor;
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
        if (this.getCurrentElevatorFloor() == null) {
            return -1;
        }
        return this.getCurrentElevatorFloor().get().getFloor().getFloorNumber();
    }

    @Override
    public String toString() {
        return "Elevator " + elevatorNumber;
    }

    public ObjectProperty<ElevatorFloor> getCurrentElevatorFloor() {
        return this.currentElevatorFloor;
    }

    public void setCurrentElevatorFloor(ElevatorFloor currentFloor) {
        this.currentElevatorFloor.setValue(currentFloor);
    }

    public ObjectProperty<ElevatorFloor> getTargetedElevatorFloor() {
        return targetedElevatorFloorProperty;
    }

    public void setTargetedElevatorFloor(ElevatorFloor targetedElevatorFloor) {
        this.targetedElevatorFloorProperty.set(targetedElevatorFloor);
    }

    public ObjectProperty<DoorStatus> getDoorsStatus() {
        return doorsStatusProperty;
    }

    public void setDoorsStatus(DoorStatus doorsStatus) {
        this.doorsStatusProperty.set(doorsStatus);
    }

    public DoubleProperty getVelocity() {
        return velocityProperty;
    }

    public void setVelocity(double velocity) {
        this.velocityProperty.set(velocity);
    }

    public DoubleProperty getPayload() {
        return payloadProperty;
    }

    public void setPayload(double payload) {
        this.payloadProperty.set(payload);
    }

    public ObjectProperty<Direction> getDirection() {
        return directionProperty;
    }

    public void setDirection(Direction direction) {
        this.directionProperty.set(direction);
    }

    public ListProperty<Integer> getFloorRequest() {
        return floorRequests;
    }

    public void setFloorRequest(List<Integer> floorRequest) {
        this.floorRequests.set(FXCollections.observableList(floorRequest));
    }

    public ListProperty<Alarm> getAlarmList() {
        return alarmListProperty;
    }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmListProperty.set(FXCollections.observableList(alarmList));
    }
}
