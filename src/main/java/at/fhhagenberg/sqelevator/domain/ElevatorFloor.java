package at.fhhagenberg.sqelevator.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ElevatorFloor implements Comparable<ElevatorFloor> {

    private BooleanProperty downRequestProperty = new SimpleBooleanProperty();
    private BooleanProperty upRequestProperty = new SimpleBooleanProperty();
    private BooleanProperty serviceEnabled = new SimpleBooleanProperty();
    private ObjectProperty<Floor> floorProperty = new SimpleObjectProperty<>();

    public ElevatorFloor(Floor floor) {
        this.floorProperty.set(floor);
    }

    public Floor getFloor() {
        return this.floorProperty.get();
    }

    public BooleanProperty getUpRequest() {
        return upRequestProperty;
    }

    public void setUpRequest(boolean upRequest) {
        this.upRequestProperty.set(upRequest);
    }

    public BooleanProperty getDownRequest() { return this.downRequestProperty; }

    public void setDownRequest(boolean downRequest) { this.downRequestProperty.set(downRequest); }

    public void unsetDownRequest() { this.downRequestProperty.set(false); }

    public BooleanProperty getServiceEnabled() {
        return serviceEnabled;
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        this.serviceEnabled.set(serviceEnabled);
    }

    @Override
    public int compareTo(ElevatorFloor o) {
        return Integer.compare(this.floorProperty.get().getFloorNumber(), o.floorProperty.get().getFloorNumber());
    }
}
