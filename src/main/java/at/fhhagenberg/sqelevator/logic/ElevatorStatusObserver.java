package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.ElevatorStatus;

import java.util.List;

public interface ElevatorStatusObserver {
    void update(List<ElevatorStatus> elevatorStatuses);
}
