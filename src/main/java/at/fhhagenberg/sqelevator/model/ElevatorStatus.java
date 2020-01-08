package at.fhhagenberg.sqelevator.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;


public class ElevatorStatus {

    // lookup further information on databinding with JavaFx on https://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
    private IntegerProperty position = new SimpleIntegerProperty();
    private IntegerProperty speed = new SimpleIntegerProperty();
    private IntegerProperty doorStatus = new SimpleIntegerProperty();
    private IntegerProperty currentPayload = new SimpleIntegerProperty();
    private ObjectProperty<Direction> direction = new SimpleObjectProperty<>();

    public final int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public final void setPosition(int position) {
        this.position.set(position);
    }

    public int getSpeed() {
        return speed.get();
    }

    public IntegerProperty speedProperty() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

    public int getDoorStatus() {
        return doorStatus.get();
    }

    public IntegerProperty doorStatusProperty() {
        return doorStatus;
    }

    public void setDoorStatus(int doorStatus) {
        this.doorStatus.set(doorStatus);
    }

    public int getCurrentPayload() {
        return currentPayload.get();
    }

    public IntegerProperty currentPayloadProperty() {
        return currentPayload;
    }

    public void setCurrentPayload(int currentPayload) {
        this.currentPayload.set(currentPayload);
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
}
