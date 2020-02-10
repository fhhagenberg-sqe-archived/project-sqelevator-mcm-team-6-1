package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public interface IRemoteConsoleViewModel {
    void targetFloor(Elevator elevator, Floor floor);

    ListProperty<Elevator> getElevatorListProperty();

    BooleanProperty getIsConnectedProperty();

    ObjectProperty<Mode> getModeProperty();
}
