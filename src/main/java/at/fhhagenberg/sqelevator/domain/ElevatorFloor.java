package at.fhhagenberg.sqelevator.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ElevatorFloor implements Comparable<ElevatorFloor> {

    public BooleanProperty downRequestProperty = new SimpleBooleanProperty();
    public BooleanProperty upRequestProperty = new SimpleBooleanProperty();
    public BooleanProperty serviceEnabled = new SimpleBooleanProperty();
    public ObjectProperty<Floor> floorProperty = new SimpleObjectProperty<>();

    public ElevatorFloor(Floor floor) {
        this.floorProperty.set(floor);
    }

    public Floor getFloor() {
        return this.floorProperty.get();
    }

    public boolean isUpRequest() {
        return upRequestProperty.get();
    }

    public void setUpRequest() {
        this.upRequestProperty.set(true);
    }

    public void unsetUpRequest() { this.downRequestProperty.set(false); }

    public boolean isDownRequest() { return this.downRequestProperty.get(); }

    public void setDownRequest() { this.downRequestProperty.set(true); }

    public void unsetDownRequest() { this.downRequestProperty.set(false); }

    public boolean isServiceEnabled() {
        return serviceEnabled.get();
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        this.serviceEnabled.set(serviceEnabled);
    }

    @Override
    public int compareTo(ElevatorFloor o) {
        return Integer.compare(this.floorProperty.get().getFloorNumber(), o.floorProperty.get().getFloorNumber());
    }
}
