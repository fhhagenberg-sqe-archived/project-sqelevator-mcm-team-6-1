package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Alarm;
import at.fhhagenberg.sqelevator.domain.Elevator;

public interface AlarmObservable {
    void addObserver(AlarmObserver observer);

    void notifyAll(Elevator elevator, Alarm alarm);
}
