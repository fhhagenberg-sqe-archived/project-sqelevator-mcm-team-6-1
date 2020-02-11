package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.ElevatorStatus;

import java.util.List;

public interface ElevatorStatusObservable {
    void addObserver(ElevatorStatusObserver observer);
    void notifyAll(List<ElevatorStatus> elevatorStatuses);
}
