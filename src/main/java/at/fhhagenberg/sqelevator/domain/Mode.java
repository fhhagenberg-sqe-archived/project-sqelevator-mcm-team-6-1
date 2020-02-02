package at.fhhagenberg.sqelevator.domain;

public enum Mode {
    MANUAL("manual"),
    AUTOMATIC("automatic");

    private String mode;

    private Mode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return this.mode;
    }
}
