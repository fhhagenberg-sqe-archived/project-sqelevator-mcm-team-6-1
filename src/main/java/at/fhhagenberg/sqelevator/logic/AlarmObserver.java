package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Alarm;
import at.fhhagenberg.sqelevator.domain.Elevator;

public interface AlarmObserver {
    void update(Elevator elevator, Alarm alarm);
}
