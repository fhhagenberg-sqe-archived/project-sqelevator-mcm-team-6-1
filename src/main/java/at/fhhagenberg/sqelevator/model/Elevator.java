package at.fhhagenberg.sqelevator.model;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private int elevatorNumber;
    private int maximumPayload;
    private ElevatorStatus elevatorStatus;
    private List<ElevatorFloor> elevatorFloors = new ArrayList<>();

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
        this.elevatorFloors = elevatorFloors;
    }

    public void addElevatorFloor(ElevatorFloor elevatorFloor) {
        this.elevatorFloors.add(elevatorFloor);
    }

    @Override
    public String toString() {
        return "Elevator " + elevatorNumber;
    }
}
