package at.fhhagenberg.sqelevator.domain;

public class ElevatorFloorStatus {
    private boolean isUpRequested;
    private boolean isDownRequested;
    private boolean isServiced;

    public boolean isUpRequested() {
        return isUpRequested;
    }

    public boolean isDownRequested() {
        return isDownRequested;
    }

    public boolean isServiced() {
        return isServiced;
    }

    public static ElevatorFloorStatusBuilder build() {
        return new ElevatorFloorStatusBuilder();
    }

    public static class ElevatorFloorStatusBuilder {
        private ElevatorFloorStatus status = new ElevatorFloorStatus();

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
