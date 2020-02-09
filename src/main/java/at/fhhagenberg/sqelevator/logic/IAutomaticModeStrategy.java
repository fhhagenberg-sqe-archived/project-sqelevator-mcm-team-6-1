package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;

import java.util.List;

public interface IAutomaticModeStrategy {
    void execute (List<Elevator> elevators);
    void setClient (IElevatorClient client);
}
