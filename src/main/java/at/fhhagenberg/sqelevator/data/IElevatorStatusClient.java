package at.fhhagenberg.sqelevator.data;

import at.fhhagenberg.sqelevator.domain.*;

import java.rmi.RemoteException;
import java.util.Optional;

public interface IElevatorStatusClient {

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

    boolean getFloorButtonUp(int floorNumber) throws RemoteException;

    boolean getFloorButtonDown(int floorNumber) throws RemoteException;
}
