package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.data.IElevatorClient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticElevatorMode implements IAutomaticModeStrategy {
    private List<Elevator> elevators;
    private IElevatorClient client;
    private List<Integer> outsideRequests;
    private List<Integer>[] insideRequests;
    private Integer[] currentTargets;
    private boolean isFullyInitialized = false;
    private long[] systemMilliSecSinceLastMoved;
    private Integer[] lastKnownPosition;

    @Override
    public void execute(List<Elevator> elevators) {
        this.elevators = elevators;
        isFullyInitialized = this.isFullyInitialized();

        if (isFullyInitialized) {
            try {
                initialize();

                this.freeUpTargets();

                this.setOutsideRequestsFromClient();
                this.setInsideRequestsFromClient();
                startElevatorRoutine();
            } catch (RemoteException e) { }
        }
    }

    @Override
    public void setClient(IElevatorClient client) {
        this.client = client;
    }

    private void setInsideRequestsFromClient() throws RemoteException {
        for (Elevator elevator : elevators) {
            // otherwise find new inside requests
            boolean[] floorButtonsStatus = client.getElevatorFloorButtonsStatus(elevator);
            for (int i = 0; i < this.client.getFloorNum(); i++) {
                if (floorButtonsStatus[i]) {
                    addInsideRequest(elevator, i);
                }
            }
        }
    }

    private void setOutsideRequestsFromClient() throws RemoteException {
        for (int i = 0; i < this.client.getFloorNum(); i++) {
            if (client.getFloorButtonDown(i) || client.getFloorButtonUp(i)) {
                addOutsideRequestIfNew(i);
            }
        }
    }

    private void freeUpTargets() throws RemoteException {
        for (int i = 0; i < elevators.size(); i++) {
            int currentFloorNumber = client.getCurrentFloor(elevators.get(i)).getFloor().getFloorNumber();

            if (lastKnownPosition[i] != currentFloorNumber) {
                lastKnownPosition[i] = currentFloorNumber;
                systemMilliSecSinceLastMoved[i] = System.currentTimeMillis();
            }

            long notMovedTime = System.currentTimeMillis() - systemMilliSecSinceLastMoved[i];
            if (notMovedTime > 300) {
                client.setTarget(elevators.get(i), currentFloorNumber);
                try {
                    Thread.sleep(100);
                    if (currentTargets[i] != null) {
                        client.setTarget(elevators.get(i), currentTargets[i]);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (currentTargets[i] != null
                && currentFloorNumber == currentTargets[i]
                && client.getElevatorDoorStatus(elevators.get(i)) == DoorStatus.OPEN) {
                currentTargets[i] = null;
            }

            if (client.getElevatorDoorStatus(elevators.get(i)) == DoorStatus.OPEN) {
                insideRequests[i] = insideRequests[i].stream().filter(r -> r == currentFloorNumber)
                        .collect(Collectors.toList());
            }
        }
    }

    private void initialize() throws RemoteException {
        if (client != null && insideRequests == null) {
            insideRequests = new List[client.getElevators().size()];
            if (currentTargets == null) {
                currentTargets = new Integer[insideRequests.length];
            }

            if (lastKnownPosition == null) {
                lastKnownPosition = new Integer[insideRequests.length];
                systemMilliSecSinceLastMoved = new long[insideRequests.length];
            }

            for (int i = 0; i < client.getElevators().size(); i++) {
                insideRequests[i] = new ArrayList();

                if (lastKnownPosition[i] == null) {
                    lastKnownPosition[i] = client.getCurrentFloor(elevators.get(i)).getFloor().getFloorNumber();
                    systemMilliSecSinceLastMoved[i] = System.currentTimeMillis();
                }
            }
        }

        if (outsideRequests == null) {
            outsideRequests = new ArrayList();
        }
    }


    private void addInsideRequest(Elevator e, int floor) throws RemoteException {
        if (!insideRequests[e.getElevatorNumber()].contains(floor)) {
            insideRequests[e.getElevatorNumber()].add(floor);
        }
    }

    private void addOutsideRequestIfNew(int floor) throws RemoteException {
        if (!outsideRequests.contains(floor)) {
            outsideRequests.add(floor);
        }
    }

    private void startElevatorRoutine() throws RemoteException {
        if (client != null && elevators != null && !elevators.isEmpty()) {
            for (int i = 0; i < elevators.size(); i++) {
                if (!insideRequests[i].isEmpty()) {
                    targetElevatorToNextInsideRequest(i, elevators.get(i));
                }
            }

            if (!outsideRequests.isEmpty()) {
                targetNearestElevatorToOutsideRequest();
            }
        }
    }

    private void targetNearestElevatorToOutsideRequest() throws RemoteException {
        for (Integer outsideRequest : outsideRequests) {
            Elevator elevator = checkElevatorAlreadyAddressed(outsideRequest);
            if (elevator == null) {
                List<Elevator> availableElevators = this.findAvailableElevators();

                if (availableElevators != null && !availableElevators.isEmpty()) {
                    elevator = this.findClosestElevator(availableElevators, outsideRequest);

                    // target closest elevator to outsideRequest floor
                    if (elevator != null) {
                        client.setTarget(elevator, outsideRequest);
                    }
                }
            }
        }
    }

    private List<Elevator> findAvailableElevators() throws RemoteException {
        List<Elevator> elevators = new ArrayList<>();

        for (int i = 0; i < elevators.size(); i++) {
            if (client.getDirection(elevators.get(i)) == Direction.UNCOMMITED) {
                elevators.add(elevators.get(i));
            }
        }

        return elevators;
    }

    private Elevator findClosestElevator(List<Elevator> availableElevators, Integer outsideRequest) throws RemoteException {
        Elevator elevator = null;
        int distance = elevators.stream().map(e -> e.getElevatorFloors().size()).max(Integer::compare).get();

        for (Elevator availableElevator : availableElevators) {
            if (availableElevator.getCurrentElevatorFloor() != null) {
                int offset = Math.abs(client.getCurrentFloor(availableElevator).getFloor().getFloorNumber() - outsideRequest);
                if (offset < distance) {
                    distance = offset;
                    elevator = availableElevator;
                }
            }
        }

        return elevator;
    }

    private void targetElevatorToNextInsideRequest(int index, Elevator elevator) throws RemoteException {
        Integer nearestFloor = currentTargets[index];
        int currentFloor = this.client.getCurrentFloor(elevator).getFloor().getFloorNumber();

        if (nearestFloor == null) {
            nearestFloor = this.findNearestInsideRequest(index, elevator);
            currentTargets[index] = nearestFloor;
        }

        nearestFloor = findNearestInsideRequestToCurrentRequest(index, nearestFloor, currentFloor);
        nearestFloor = findNearestIntermediateOutsideRequest(nearestFloor, currentFloor);

        if (nearestFloor != null) {
            currentTargets[index] = nearestFloor;
            client.setTarget(elevator, currentFloor);
            client.setTarget(elevator, nearestFloor);
        }
    }

    private Integer findNearestInsideRequest(int index, Elevator elevator) throws RemoteException {
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

    private Integer findNearestInsideRequestToCurrentRequest(int index, Integer nearestFloor, int currentFloor) throws RemoteException {
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
                            calculateDirection(currentFloor, nearestFloor)
                                    .equals(calculateDirection(currentFloor, request))) &&
                    checkElevatorAlreadyAddressed(request) == null) {
                dist = offset;
                nearestFloor = request;
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

    private Integer findNearestIntermediateOutsideRequest(Integer nearestFloor, int currentFloor) throws RemoteException {
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
                    checkElevatorAlreadyAddressed(outsideRequest) == null)
            {
                dist = offset;
                nearestFloor = outsideRequest;
            }
        }

        return nearestFloor;
    }

    private boolean isOutsideRequestOnWay(int currentFloor, int nearestFloor, int outsideRequest) throws RemoteException {
        Direction currentDirection = calculateDirection(currentFloor, nearestFloor);
        return currentDirection.equals(calculateDirection(currentFloor, outsideRequest)) &&
                isCurrentDirectionAndFloorButtonDirectionEqual(currentDirection, nearestFloor);
    }

    private boolean isCurrentDirectionAndFloorButtonDirectionEqual(Direction currentDirection,
                                                                   int floorNumber) throws RemoteException {
        return (currentDirection == Direction.UP && client.getFloorButtonUp(floorNumber)) ||
                (currentDirection == Direction.DOWN && client.getFloorButtonDown(floorNumber));
    }

    private Direction calculateDirection(int currentFloor, int nearestFloor) {
        if (currentFloor < nearestFloor) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    private Elevator checkElevatorAlreadyAddressed(Integer toFloor) throws RemoteException {
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
                        client.getTargetedFloor(elevator).get().getFloor().getFloorNumber() == toFloor) {
                    return elevator;
                }
            }
        }

        return null;
    }

    private boolean isFullyInitialized() {
        if (this.isFullyInitialized) {
            return true;
        }

        return !this.elevators.stream().anyMatch(e -> e.getCurrentElevatorFloor() == null);
    }
}