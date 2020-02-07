package sqelevator;

import at.fhhagenberg.sqelevator.domain.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public interface IElevatorClient {

    void setTarget(Elevator elevator, Floor floor) throws RemoteException;

    List<Elevator> getElevators();

    Optional<Elevator> getElevatorByNumber(int elevatorNumber);

    Optional<ElevatorFloor> getFloorByNumber(Elevator elevator, int floorNumber);

    Optional<ElevatorFloor> getTargetedFloor(Elevator elevator) throws RemoteException;

    ElevatorFloor getCurrentFloor(Elevator elevator) throws RemoteException;

    DoorStatus getElevatorDoorStatus(Elevator elevator) throws RemoteException;

    double getCurrentVelocity(Elevator elevator) throws RemoteException;

    double getCurrentWeightLoad(Elevator elevator) throws RemoteException;

    Direction getDirection(Elevator elevator) throws RemoteException;

    boolean[] getElevatorFloorButtonsStatus(Elevator elevator) throws RemoteException;

    boolean hasFloorBeenRequestedUp(Floor floor) throws RemoteException;

    boolean hasFloorBeenRequestedDown(Floor floor) throws RemoteException;

    boolean isServiceEnabled(Elevator elevator, Floor floor) throws RemoteException;
}
