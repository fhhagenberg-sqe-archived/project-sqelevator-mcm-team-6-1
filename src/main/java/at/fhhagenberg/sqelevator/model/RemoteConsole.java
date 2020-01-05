package at.fhhagenberg.sqelevator.model;

import javafx.collections.ObservableList;

public class RemoteConsole {
    private ObservableList<Alarm> alarms;

    public ObservableList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ObservableList<Alarm> alarms) {
        this.alarms = alarms;
    }
}
