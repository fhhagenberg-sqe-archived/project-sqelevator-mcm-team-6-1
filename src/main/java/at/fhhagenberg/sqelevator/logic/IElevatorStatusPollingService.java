package at.fhhagenberg.sqelevator.logic;

public interface IElevatorStatusPollingService extends ElevatorStatusObservable {
    void startPollingService();

    void stopPollingService();
}
