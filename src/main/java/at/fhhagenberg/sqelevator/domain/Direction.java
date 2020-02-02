package at.fhhagenberg.sqelevator.domain;

public enum Direction {
    UP("up"),
    DOWN("down"),
    UNCOMMITED("uncommited");

    private String direction;

    private Direction(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return this.direction;
    }
}
