package at.fhhagenberg.sqelevator.domain;

public class ElevatorFloorStatus {

    private ElevatorFloor elevatorFloor;

    private boolean isUpRequested;
    private boolean isDownRequested;
    private boolean isServiced;

    private ElevatorFloorStatus(ElevatorFloor elevatorFloor) {
        this.elevatorFloor = elevatorFloor;
    }

    public boolean isUpRequested() {
        return isUpRequested;
    }

    public boolean isDownRequested() {
        return isDownRequested;
    }

    public boolean isServiced() {
        return isServiced;
    }

    public ElevatorFloor getElevatorFloor() { return elevatorFloor; }

    public static ElevatorFloorStatusBuilder build(ElevatorFloor elevatorFloor) {
        return new ElevatorFloorStatusBuilder(elevatorFloor);
    }

    public static class ElevatorFloorStatusBuilder {

        private ElevatorFloorStatus status;

        public ElevatorFloorStatusBuilder(ElevatorFloor elevatorFloor) {
            this.status = new ElevatorFloorStatus(elevatorFloor);
        }

        public ElevatorFloorStatusBuilder upRequested(boolean isUpRequested) {
            status.isUpRequested = isUpRequested;
            return this;
        }

        public ElevatorFloorStatusBuilder downRequested(boolean isDownRequested) {
            status.isDownRequested = isDownRequested;
            return this;
        }

        public ElevatorFloorStatusBuilder serviced(boolean isServiced) {
            status.isServiced = isServiced;
            return this;
        }

        public ElevatorFloorStatus get() {
            return status;
        }
    }
}
