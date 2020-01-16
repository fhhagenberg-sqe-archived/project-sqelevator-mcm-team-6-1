package at.fhhagenberg.sqelevator.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ElevatorFloor implements Comparable<ElevatorFloor> {

    public BooleanProperty downRequest = new SimpleBooleanProperty();
    public BooleanProperty upRequest = new SimpleBooleanProperty();
    public BooleanProperty serviceEnabled = new SimpleBooleanProperty();
    public ObjectProperty<Floor> floorProperty = new SimpleObjectProperty<>();

    public ElevatorFloor(Floor floor) {
        this.floorProperty.set(floor);
    }

    public boolean isDownRequest() {
        return downRequest.get();
    }

    public BooleanProperty downRequestProperty() {
        return downRequest;
    }

    public void setDownRequest(boolean downRequest) {
        this.downRequest.set(downRequest);
    }

    public boolean isUpRequest() {
        return upRequest.get();
    }

    public BooleanProperty upRequestProperty() {
        return upRequest;
    }

    public void setUpRequest(boolean upRequest) {
        this.upRequest.set(upRequest);
    }

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
