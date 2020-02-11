package at.fhhagenberg.sqelevator.domain;

import java.util.Optional;

public class ElevatorStatus {

    private Elevator elevator;
    private boolean isConnected = true;

    private int position;
    private double velocity;
    private double payload;
    private boolean[] elevatorButtonStatuses;

    private ElevatorFloor targetedFloor;
    private ElevatorFloor currentFloor;
    private ElevatorFloorStatus[] elevatorFloorStatuses;
    private Direction direction;
    private DoorStatus doorStatus;

    private ElevatorStatus(Elevator elevator) {
        this.elevator = elevator;
        this.elevatorButtonStatuses = new boolean[0];
        this.elevatorFloorStatuses = new ElevatorFloorStatus[0];
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getPayload() {
        return payload;
    }

    public boolean[] getElevatorButtonStatuses() {
        return elevatorButtonStatuses;
    }

    public Optional<ElevatorFloor> getTargetedFloor() {
        return Optional.ofNullable(targetedFloor);
    }

    public ElevatorFloor getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorFloorStatus[] getElevatorFloorStatuses() {
        return elevatorFloorStatuses;
    }

    public Direction getDirection() {
        return direction;
    }

    public DoorStatus getDoorStatus() {
        return doorStatus;
    }

    public Elevator getElevator() {
        return this.elevator;
    }

    public static ElevatorStatusBuilder build(Elevator elevator) {
        return new ElevatorStatusBuilder(elevator);
    }

    public static class ElevatorStatusBuilder {

        private ElevatorStatus status;

        private ElevatorStatusBuilder(Elevator elevator) {
            status = new ElevatorStatus(elevator);
        }

        public ElevatorStatusBuilder position(int position) {
            status.position = position;
            return this;
        }

        public ElevatorStatusBuilder velocity(double velocity) {
            status.velocity = velocity;
            return this;
        }

        public ElevatorStatusBuilder payload(double payload) {
            status.payload = payload;
            return this;
        }

        public ElevatorStatusBuilder buttonStatus(boolean[] buttonStatus) {
            status.elevatorButtonStatuses = buttonStatus;
            return this;
        }

        public ElevatorStatusBuilder targetedFloor(ElevatorFloor targetedFloor) {
            status.targetedFloor = targetedFloor;
            return this;
        }

        public ElevatorStatusBuilder currentFloor(ElevatorFloor currentFloor) {
            status.currentFloor = currentFloor;
            return this;
        }

        public ElevatorStatusBuilder elevatorFloorStatus(ElevatorFloorStatus[] floorStatuses) {
            status.elevatorFloorStatuses = floorStatuses;
            return this;
        }

        public ElevatorStatusBuilder direction(Direction direction) {
            status.direction = direction;
            return this;
        }

        public ElevatorStatusBuilder doorStatus(DoorStatus doorStatus) {
            status.doorStatus = doorStatus;
            return this;
        }

        public ElevatorStatus notConnected() {
            status.isConnected = false;
            return status;
        }

        public ElevatorStatus get() {
            return status;
        }
    }
}
