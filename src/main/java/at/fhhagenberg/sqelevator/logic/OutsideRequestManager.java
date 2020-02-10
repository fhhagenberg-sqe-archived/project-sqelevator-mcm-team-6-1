package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Direction;
import at.fhhagenberg.sqelevator.domain.Elevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OutsideRequestManager {
    IElevatorClient client;
    RequestHelper helper;

    public OutsideRequestManager(IElevatorClient client, RequestHelper helper) {
        this.client = client;
        this.helper = helper;
    }

    public Integer findNearestIntermediateOutsideRequest(
            Integer nearestFloor, int currentFloor,
            List<Integer> outsideRequests, List<Elevator> elevators)
            throws RemoteException {
        int dist;
        if (nearestFloor != null) {
            dist = Math.abs(nearestFloor - currentFloor);
        } else {
            dist = client.getFloorNum();
        }

        for (Integer outsideRequest : outsideRequests) {
            int offset = Math.abs(outsideRequest - currentFloor);


            if ((nearestFloor == null ||
                    isOutsideRequestOnWay(currentFloor, nearestFloor, outsideRequest) && offset < dist) &&
                    helper.checkElevatorAlreadyAddressed(outsideRequest, elevators) == null)
            {
                dist = offset;
                nearestFloor = outsideRequest;
            }
        }

        return nearestFloor;
    }

    public List<Integer> getOutsideRequestsFromClient() throws RemoteException {
        List<Integer> requests = new ArrayList<>();

        for (int i = 0; i < this.client.getFloorNum(); i++) {
            if (client.getFloorButtonDown(i) || client.getFloorButtonUp(i)) {
                requests = addOutsideRequestIfNew(i, requests);
            }
        }

        return requests;
    }

    public Elevator findClosestElevator(
            List<Elevator> availableElevators, Integer outsideRequest, List<Elevator> elevators)
            throws RemoteException {
        Elevator elevator = null;
        Optional<Integer> distanceOptional = elevators.stream().map(e -> e.getElevatorFloors().size()).max(Integer::compare);
        if (distanceOptional.isPresent()) {
            int distance = distanceOptional.get();
            for (Elevator availableElevator : availableElevators) {
                if (availableElevator.getCurrentElevatorFloor() != null) {
                    int offset = Math.abs(client.getCurrentFloor(availableElevator).getFloor().getFloorNumber() - outsideRequest);
                    if (offset < distance) {
                        distance = offset;
                        elevator = availableElevator;
                    }
                }
            }
        }
        return elevator;
    }

    private boolean isOutsideRequestOnWay(int currentFloor, int nearestFloor, int outsideRequest) throws RemoteException {
        Direction currentDirection = helper.calculateDirection(currentFloor, nearestFloor);
        return currentDirection.equals(helper.calculateDirection(currentFloor, outsideRequest)) &&
                isCurrentDirectionAndFloorButtonDirectionEqual(currentDirection, nearestFloor);
    }

    private boolean isCurrentDirectionAndFloorButtonDirectionEqual(Direction currentDirection,
                                                                   int floorNumber) throws RemoteException {
        return (currentDirection == Direction.UP && client.getFloorButtonUp(floorNumber)) ||
                (currentDirection == Direction.DOWN && client.getFloorButtonDown(floorNumber));
    }


    private List<Integer> addOutsideRequestIfNew(int floor, List<Integer> outsideRequests) {
        if (!outsideRequests.contains(floor)) {
            outsideRequests.add(floor);
        }

        return outsideRequests;
    }
}
