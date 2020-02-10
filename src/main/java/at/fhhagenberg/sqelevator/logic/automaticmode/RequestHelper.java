package at.fhhagenberg.sqelevator.logic.automaticmode;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Direction;
import at.fhhagenberg.sqelevator.domain.Elevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RequestHelper {
    IElevatorClient client;

    public RequestHelper(IElevatorClient client) {
        this.client = client;
    }

    public Elevator checkElevatorAlreadyAddressed(Integer toFloor, List<Elevator> elevators) throws RemoteException {
        if (elevators != null) {
            // check if there is an elevator available whose target floor is already set on our target floor
            for (Elevator elevator : elevators) {
                if (elevator.directionProperty.get() == Direction.UNCOMMITED && elevator.getCurrentElevatorFloor() != null &&
                        client.getCurrentFloor(elevator).getFloor().getFloorNumber() == toFloor) {
                    return elevator;
                }
            }

            // check if there is an elevator already on the way to this floor
            for (Elevator elevator : elevators) {
                if (!(elevator.directionProperty.get() == Direction.UNCOMMITED) &&
                        client.getTargetedFloor(elevator).isPresent() &&
                        client.getTargetedFloor(elevator).get().getFloor().getFloorNumber() == toFloor) {
                    return elevator;
                }
            }
        }

        return null;
    }

    public Direction calculateDirection(int currentFloor, int nearestFloor) {
        if (currentFloor < nearestFloor) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public List<Elevator> findAvailableElevators() throws RemoteException {
        List<Elevator> elevators = new ArrayList<>();

        for (int i = 0; i < elevators.size(); i++) {
            if (client.getDirection(elevators.get(i)) == Direction.UNCOMMITED) {
                elevators.add(elevators.get(i));
            }
        }

        return elevators;
    }

    public boolean areAllElevatorsInitialized(boolean isFullyInitialized, List<Elevator> elevators) {
        if (isFullyInitialized) {
            return true;
        }

        return !elevators.stream().anyMatch(e -> e.getCurrentElevatorFloor() == null);
    }
}
