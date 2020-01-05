package at.fhhagenberg.sqelevator.model;

public class Elevator {
    private int elevatorNumber;
    private int maximumPayload;
    private ElevatorStatus elevatorStatus;

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
}
