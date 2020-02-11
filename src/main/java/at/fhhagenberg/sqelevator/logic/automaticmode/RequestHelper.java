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
                if (Direction.UNCOMMITED == elevator.directionProperty().get() && null != elevator.getCurrentElevatorFloor() &&
                        client.getCurrentFloor(elevator).getFloor().getFloorNumber() == toFloor) {
                    return elevator;
                }
            }

            // check if there is an elevator already on the way to this floor
            for (Elevator elevator : elevators) {
                if (!(Direction.UNCOMMITED == elevator.directionProperty().get())) {
                    var targetedFloor = client.getTargetedFloor(elevator).orElse(null);

                    if (targetedFloor != null && targetedFloor.getFloor().getFloorNumber() == toFloor) {
                        return elevator;
                    }
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

    public List<Elevator> findAvailableElevators(List<Elevator> elevators) throws RemoteException {
        List<Elevator> availableElevators = new ArrayList<>();

        for (Elevator elevator : elevators) {
            if (client.getDirection(elevator) == Direction.UNCOMMITED) {
                availableElevators.add(elevator);
            }
        }

        return availableElevators;
    }

    public boolean areAllElevatorsInitialized(boolean isFullyInitialized, List<Elevator> elevators) {
        if (isFullyInitialized) {
            return true;
        }

        return elevators.stream().noneMatch(e -> e.getCurrentElevatorFloor() == null);
    }
}
