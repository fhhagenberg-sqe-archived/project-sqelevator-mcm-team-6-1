package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.Elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorObservable {
    public List<ElevatorObserver> observers = new ArrayList<>();

    public void notifyObservers(Object arg) {
        for (ElevatorObserver observer : observers) {
            observer.update(arg);
        }
    }

    public void addObserver(ElevatorObserver observer) {
        if (!this.observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(ElevatorObserver observer) {
        if (this.observers.contains(observer)) {
            this.observers.remove(observer);
        }
    }
}
