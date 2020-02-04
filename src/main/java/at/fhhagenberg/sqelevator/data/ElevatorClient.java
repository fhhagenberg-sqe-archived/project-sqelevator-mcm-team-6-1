package at.fhhagenberg.sqelevator.data;

import at.fhhagenberg.sqelevator.domain.*;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ElevatorClient implements IElevatorClient {

    private IElevator client;
    private List<Elevator> elevators;

    public ElevatorClient(IElevator client) throws RemoteException {
        this.client = client;
        this.elevators = createElevators();
    }

    private List<Elevator> createElevators() throws RemoteException {
        var elevators = new ArrayList<Elevator>();

        var floors = IntStream.range(0, client.getFloorNum())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .map(i -> new ElevatorFloor(new Floor(i)))
                .collect(Collectors.toList());

        for (int i = 0; i < client.getElevatorNum(); i++) {
            Elevator elevator = new Elevator();
            elevator.setElevatorNumber(i);
            elevator.setMaximumPayload(client.getElevatorCapacity(i));
            elevator.setElevatorFloors(floors);

            elevators.add(elevator);
        }

        return elevators;
    }

    @Override
    public void setTarget(Elevator elevator, Floor floor) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        if(floor == null) {
            throw new IllegalArgumentException("Floor must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();
        var floorNumber = floor.getFloorNumber();

        this.client.setTarget(elevatorNumber, floorNumber);
    }

    @Override
    public void setTarget(Elevator elevator, int floorNumber) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        this.client.setTarget(elevator.getElevatorNumber(), floorNumber);
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return this.client.getFloorNum();
    }

    @Override
    public List<Elevator> getElevators() {
        return this.elevators;
    }

    @Override
    public boolean getFloorButtonUp(int floorNumber) throws RemoteException {
        return this.client.getFloorButtonUp(floorNumber);
    }

    @Override
    public boolean getFloorButtonDown(int floorNumber) throws RemoteException {
        return this.client.getFloorButtonDown(floorNumber);
    }

    @Override
    public Optional<Elevator> getElevatorByNumber(int elevatorNumber) {
        if (elevatorNumber < 0) {
            throw new IllegalArgumentException(String.format("Elevator number must not be negative but was: %d", elevatorNumber));
        }

        return this.elevators.stream()
                .filter(elevator -> elevator.getElevatorNumber() == elevatorNumber)
                .findAny();
    }

    @Override
    public Optional<ElevatorFloor> getFloorByNumber(Elevator elevator, int floorNumber) {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        if(floorNumber < 0) {
            throw new IllegalArgumentException(String.format("Floor number must not be negative but was: %d", floorNumber));
        }

        return elevator.getElevatorFloors().stream()
                .filter(floor -> floor.getFloor().getFloorNumber() == floorNumber)
                .findAny();
    }

    @Override
    public Optional<ElevatorFloor> getTargetedFloor(Elevator elevator) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();
        var currentTargetFloorNumber = this.client.getTarget(elevatorNumber);

        return this.getFloorByNumber(elevator, currentTargetFloorNumber);
    }

    @Override
    public ElevatorFloor getCurrentFloor(Elevator elevator) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();
        var currentFloorNumber = this.client.getElevatorFloor(elevatorNumber);

        return this.getFloorByNumber(elevator, currentFloorNumber).get();
    }

    @Override
    public DoorStatus getElevatorDoorStatus(Elevator elevator) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();
        var doorStatus = this.client.getElevatorDoorStatus(elevatorNumber);

        if (doorStatus == 2) {
            return DoorStatus.CLOSED;
        }
        else {
            return DoorStatus.OPEN;
        }
    }

    @Override
    public double getCurrentVelocity(Elevator elevator) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();

        return this.client.getElevatorAccel(elevatorNumber);
    }

    @Override
    public double getCurrentWeightLoad(Elevator elevator) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();

        return this.client.getElevatorWeight(elevatorNumber);
    }

    @Override
    public Direction getDirection(Elevator elevator) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();
        var direction = this.client.getCommittedDirection(elevatorNumber);

        switch(direction) {
            case 0:
                return Direction.UP;

            case 1:
                return Direction.DOWN;

            default:
                return Direction.UNCOMMITED;
        }
    }

    @Override
    public boolean hasFloorBeenRequestedUp(Floor floor) throws RemoteException {
        if (floor == null) {
            throw new IllegalArgumentException("Floor must not be null!");
        }

        var floorNumber = floor.getFloorNumber();

        return this.client.getFloorButtonUp(floorNumber);
    }

    @Override
    public boolean hasFloorBeenRequestedDown(Floor floor) throws RemoteException {
        if (floor == null) {
            throw new IllegalArgumentException("Floor must not be null!");
        }

        var floorNumber = floor.getFloorNumber();

        return this.client.getFloorButtonDown(floorNumber);
    }

    @Override
    public boolean isServiceEnabled(Elevator elevator, Floor floor) throws RemoteException {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator must not be null!");
        }

        if (floor == null) {
            throw new IllegalArgumentException("Floor must not be null!");
        }

        var elevatorNumber = elevator.getElevatorNumber();
        var floorNumber = floor.getFloorNumber();

        return this.client.getServicesFloors(elevatorNumber, floorNumber);
    }
}
