package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorStatus;

public interface ElevatorStatusObservable {
    void addObserver(ElevatorStatusObserver observer);
    void notifyAll(Elevator elevator, ElevatorStatus elevatorStatus);
}
