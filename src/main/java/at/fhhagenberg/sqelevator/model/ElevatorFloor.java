package at.fhhagenberg.sqelevator.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ElevatorFloor {

    private BooleanProperty downRequest = new SimpleBooleanProperty();
    private BooleanProperty upRequest = new SimpleBooleanProperty();
    private BooleanProperty serviceEnabled = new SimpleBooleanProperty();
    private Floor floor;

    public ElevatorFloor(Floor floor) {
        this.floor = floor;
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

    public BooleanProperty serviceEnabledProperty() {
        return serviceEnabled;
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        this.serviceEnabled.set(serviceEnabled);
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
