package at.fhhagenberg.sqelevator.data;

import at.fhhagenberg.sqelevator.domain.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public interface IElevatorClient extends IElevatorStatusClient {

    void setTarget(Elevator elevator, Floor floor) throws RemoteException;

    List<Elevator> getElevators();

    Optional<Elevator> getElevatorByNumber(int elevatorNumber);

    Optional<ElevatorFloor> getFloorByNumber(Elevator elevator, int floorNumber);

    int getFloorNum() throws RemoteException;
}
