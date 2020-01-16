package at.fhhagenberg.sqelevator.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;

public class Elevator {
    private int elevatorNumber;
    private int maximumPayload;
    private ElevatorStatus elevatorStatus;
    private ListProperty<ElevatorFloor> elevatorFloors = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ObjectProperty<ElevatorFloor> currentElevatorFloorProperty = new SimpleObjectProperty<>();
    public BooleanProperty doorsStatusProperty = new SimpleBooleanProperty();
    public DoubleProperty velocityProperty = new SimpleDoubleProperty();
    public DoubleProperty payloadProperty = new SimpleDoubleProperty();
    public IntegerProperty directionProperty = new SimpleIntegerProperty();
    public ListProperty<Integer> floorRequestProperty = new SimpleListProperty<>();
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

    @Override
    public String toString() {
        return "Elevator " + elevatorNumber;
    }
}
