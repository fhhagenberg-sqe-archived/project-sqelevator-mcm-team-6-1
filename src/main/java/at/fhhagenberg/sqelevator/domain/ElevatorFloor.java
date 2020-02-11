package at.fhhagenberg.sqelevator.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ElevatorFloor implements Comparable<ElevatorFloor> {

    private BooleanProperty downRequestProperty = new SimpleBooleanProperty();
    private BooleanProperty upRequestProperty = new SimpleBooleanProperty();
    private BooleanProperty serviceEnabled = new SimpleBooleanProperty();
    private ObjectProperty<Floor> floor = new SimpleObjectProperty<>();

    public ElevatorFloor(Floor floor) {
        this.floor.set(floor);
    }

    public BooleanProperty getUpRequest() {
        return upRequestProperty;
    }

    public void setUpRequest(boolean upRequest) {
        this.upRequestProperty.set(upRequest);
    }

    public BooleanProperty getDownRequest() { return this.downRequestProperty; }

    public void setDownRequest(boolean downRequest) { this.downRequestProperty.set(downRequest); }

    public BooleanProperty getServiceEnabled() {
        return serviceEnabled;
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        this.serviceEnabled.set(serviceEnabled);
    }

    public Floor getFloor() {
        return floor.get();
    }

    public ObjectProperty<Floor> floorProperty() {
        return floor;
    }

    @Override
    public int compareTo(ElevatorFloor o) {
        return Integer.compare(this.floorProperty().get().getFloorNumber(), o.floorProperty().get().getFloorNumber());
    }
}
