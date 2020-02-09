package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorStatus;

public interface ElevatorStatusObserver {
    void update(Elevator elevator, ElevatorStatus elevatorStatus);
}
