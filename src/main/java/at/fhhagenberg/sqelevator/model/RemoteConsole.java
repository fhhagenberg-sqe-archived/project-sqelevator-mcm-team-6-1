package at.fhhagenberg.sqelevator.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RemoteConsole {
    private ObservableList<Alarm> alarms = FXCollections.observableArrayList();
    private ObservableList<Elevator> elevators = FXCollections.observableArrayList();

    public ObservableList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ObservableList<Alarm> alarms) {
        this.alarms = alarms;
    }

    public ObservableList<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(ObservableList<Elevator> elevators) {
        this.elevators = elevators;
    }

    public void addElevator(Elevator elevator) {
        this.elevators.add(elevator);
    }
}
