package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class InsideRequestManager {
    IElevatorClient client;
    List<Integer>[] insideRequests;
    RequestHelper helper;

    public InsideRequestManager(IElevatorClient client, RequestHelper helper) {
        this.client = client;
        this.helper = helper;
    }

    public List<Integer>[] getInsideRequestsFromClient(List<Elevator> elevators) throws RemoteException {
        for (Elevator elevator : elevators) {
            boolean[] floorButtonsStatus = client.getElevatorFloorButtonsStatus(elevator);
            for (int i = 0; i < this.client.getFloorNum(); i++) {
                if (floorButtonsStatus[i]) {
                    addInsideRequest(elevator, i);
                }
            }
        }

        return insideRequests;
    }

    public Integer findNearestInsideRequestToCurrentRequest(
            int index, Integer nearestFloor,
            int currentFloor, List<Integer>[] insideRequests,
            List<Elevator> elevators)
            throws RemoteException {
        int dist;
        if (nearestFloor != null) {
            dist = Math.abs(nearestFloor - currentFloor);
        } else {
            dist = client.getFloorNum();
        }

        for (int j = 0; j < insideRequests[index].size(); j++) {
            int request = insideRequests[index].get(j);
            int offset = Math.abs(request - currentFloor);
            if (offset < dist &&
                    (nearestFloor == null ||
                            helper.calculateDirection(currentFloor, nearestFloor)
                                    .equals(helper.calculateDirection(currentFloor, request))) &&
                    helper.checkElevatorAlreadyAddressed(request, elevators) == null) {
                dist = offset;
                nearestFloor = request;
            }
        }

        return nearestFloor;
    }

    public Integer findNearestInsideRequest(int index, Elevator elevator, List<Integer>[] insideRequests) throws RemoteException {
        Integer nearestFloor = null;
        for (int j = 0; j < insideRequests[index].size(); j++) {
            int request = insideRequests[index].get(j);
            int currentFloor = client.getCurrentFloor(elevator).getFloor().getFloorNumber();

            if (request != currentFloor &&
                    isRequestBetweenExistingRequestAndCurrentFloor(currentFloor, nearestFloor, request)) {
                nearestFloor = insideRequests[index].get(j);
            }
        }

        return nearestFloor;
    }

    private boolean isRequestBetweenExistingRequestAndCurrentFloor(int currentFloor, Integer existingRequest, int newRequest) {
        if (existingRequest == null) {
            return true;
        }

        return (newRequest > currentFloor && newRequest < existingRequest) ||
                (newRequest < currentFloor && newRequest > existingRequest);
    }

    public List<Integer>[] initialize()
    {
        insideRequests = new List[client.getElevators().size()];

        for (int i = 0; i < client.getElevators().size(); i++) {
            insideRequests[i] = new ArrayList();
        }

        return insideRequests;
    }

    private void addInsideRequest(Elevator e, int floor) {
        if (!insideRequests[e.getElevatorNumber()].contains(floor)) {
            insideRequests[e.getElevatorNumber()].add(floor);
        }
    }
}
