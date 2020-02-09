package at.fhhagenberg.sqelevator.view.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ElevatorFloorButton {

    private int floorNumber;

    private BooleanProperty hasBeenPressedProperty;

    public ElevatorFloorButton(int floorNumber) {
        this(floorNumber, false);
    }

    public ElevatorFloorButton(int floorNumber, boolean hasBeenPressed) {
        this.floorNumber = floorNumber;
        this.hasBeenPressedProperty = new SimpleBooleanProperty(hasBeenPressed);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean hasBeenPressed() {
        return this.hasBeenPressedProperty.get();
    }

    public void setHasBeenPressed(boolean hasBeenPressed) {
        this.hasBeenPressedProperty.set(hasBeenPressed);
    }

    public BooleanProperty hasBeenPressedProperty() {
        return hasBeenPressedProperty;
    }
}
