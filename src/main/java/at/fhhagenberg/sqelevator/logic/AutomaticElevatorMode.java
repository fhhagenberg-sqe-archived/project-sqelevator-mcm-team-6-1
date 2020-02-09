package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.data.IElevatorClient;
import javafx.collections.FXCollections;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticElevatorMode implements ElevatorObserver {
    private List<Elevator> elevators;
    private Mode mode;
    private boolean isConnected;
    private IElevatorClient client;
    private List<Integer> outsideRequests;
    private List<Floor>[] insideRequests;
    private Integer[] currentInsideRequests;
    private boolean isFullyInitialized = false;

    public AutomaticElevatorMode(IElevatorClient client) {
        this.client = client;
    }

    private void onElevatorsChanged() {
        initialize();

        if (isConnected) {
            try {
                this.setOutsideRequestsFromClient();

                for (Elevator elevator : elevators) {
                    // if the elevator is already on the floor of an inside or outside request, process that first
                    if (this.hasInsideRequestOnCurrentFloorAndIsAvailable(elevator)) {
                        this.handleInsideRequestOnCurrentFloor(elevator);
                    }
                    if (this.hasOutsideRequestOnCurrentFloorAndIsAvailable(elevator)) {
                        this.handleOutsideRequestOnCurrentFloor(elevator);
                    }

                    // otherwise find new inside requests
                    for (int i = 0; i < this.client.getFloorNum(); i++) {
                        if (elevator.getFloorRequests() == null) {
                            elevator.setFloorRequests(FXCollections.observableArrayList());
                        }
                        if (elevator.isFloorRequested(i)) {
                            addInsideRequest(elevator, this.client.getFloorByNumber(elevator, i).get().getFloor());
                        }
                    }
                }

                startElevatorRoutine();
            } catch (RemoteException e) {
                isConnected = false;
            }
        }
    }

    private boolean hasInsideRequestOnCurrentFloorAndIsAvailable(Elevator elevator) {
        return insideRequests != null &&
                insideRequests[elevator.getElevatorNumber()].contains(elevator.getCurrentFloorNumber()) &&
                elevator.directionProperty.get() == Direction.UNCOMMITED;
    }

    private boolean hasOutsideRequestOnCurrentFloorAndIsAvailable(Elevator elevator) {
        return outsideRequests != null &&
                outsideRequests.contains(elevator.getCurrentFloorNumber()) &&
                elevator.directionProperty.get() == Direction.UNCOMMITED;
    }

    private void handleInsideRequestOnCurrentFloor(Elevator elevator) throws RemoteException {
        client.setTarget(elevator, elevator.getCurrentElevatorFloor().get().getFloor());
        insideRequests[elevator.getElevatorNumber()].remove((Integer) elevator.getElevatorNumber());
    }

    private void handleOutsideRequestOnCurrentFloor(Elevator elevator) throws RemoteException {
        client.setTarget(elevator, elevator.getCurrentElevatorFloor().get().getFloor());
        outsideRequests.remove((Integer) elevator.getCurrentElevatorFloor().get().getFloor().getFloorNumber());
    }

    private void setOutsideRequestsFromClient() throws RemoteException {
        for (int i = 0; i < this.client.getFloorNum(); i++) {
            if (client.getFloorButtonDown(i) || client.getFloorButtonUp(i)) {
                addOutsideRequestIfNew(i);
            }
        }
    }

    private void initialize() {
        if (client != null && insideRequests == null) {
            insideRequests = new List[client.getElevators().size()];
            currentInsideRequests = new Integer[insideRequests.length];

            for (int i = 0; i < client.getElevators().size(); i++) {
                insideRequests[i] = new ArrayList();
            }
        }

        if (outsideRequests == null) {
            outsideRequests = new ArrayList();
        }

        if (!isConnected && client != null && elevators != null && !elevators.isEmpty()) {
            isConnected = true;
        }
    }


    private void addInsideRequest(Elevator e, Floor floor) throws RemoteException {
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

    private List<Elevator> findAvailableElevators() {
        return elevators.stream()
                .filter(e -> e.directionProperty.get() == Direction.UNCOMMITED)
                .collect(Collectors.toList());
    }

    private Elevator findClosestElevator(List<Elevator> availableElevators, Integer outsideRequest) {
        Elevator elevator = null;
        int distance = elevators.stream().map(e -> e.getElevatorFloors().size()).max(Integer::compare).get();

        for (Elevator availableElevator : availableElevators) {
            if (availableElevator.getCurrentElevatorFloor() != null) {
                int offset = Math.abs(availableElevator.getCurrentFloorNumber() - outsideRequest);
                if (offset < distance) {
                    distance = offset;
                    elevator = availableElevator;
                }
            }
        }

        return elevator;
    }

    private void targetElevatorToNextInsideRequest(int index, Elevator elevator) throws RemoteException {
        int nearestFloor;
        Integer currentInsideRequest = currentInsideRequests[index];
        boolean intermediateInsideRequestFound = false;

        if (currentInsideRequest != null) {
            nearestFloor = this.findNearestInsideRequestToCurrentRequest(index, elevator, currentInsideRequest);
            intermediateInsideRequestFound = true;
        } else {
            nearestFloor = this.findNearestInsideRequest(index, elevator);
        }

        int nearestOutsideRequestFloor = findNearestIntermediateOutsideRequest(elevator, nearestFloor);
        if (intermediateInsideRequestFound) {
            currentInsideRequests[index] = nearestFloor;
        } else if (nearestFloor == nearestOutsideRequestFloor) {
            currentInsideRequests[index] = null;
        } else {
            currentInsideRequests[index] = nearestFloor;
        }

        client.setTarget(elevator, nearestOutsideRequestFloor);
    }

    private Integer findNearestInsideRequest(int index, Elevator elevator) {
        Integer nearestFloor = null;
        for (int j = 0; j < insideRequests[index].size(); j++) {
            int request = insideRequests[index].get(j).getFloorNumber();
            if (isRequestBetweenExistingRequestAndCurrentFloor(elevator.getCurrentFloorNumber(), nearestFloor, request)) {
                nearestFloor = insideRequests[index].get(j).getFloorNumber();
            }
        }

        return nearestFloor;
    }

    private Integer findNearestInsideRequestToCurrentRequest(int index, Elevator elevator, int currentRequest) {
        int nearestFloor = currentRequest;
        for (int j = 0; j < insideRequests[index].size(); j++) {
            int request = insideRequests[index].get(j).getFloorNumber();
            if (request != currentRequest &&
                    isRequestBetweenExistingRequestAndCurrentFloor(elevator.getCurrentFloorNumber(), nearestFloor, request)) {
                nearestFloor = insideRequests[index].get(j).getFloorNumber();
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

    private Integer findNearestIntermediateOutsideRequest(Elevator e, Integer nearestFloor) throws RemoteException {
        int dist = client.getFloorNum();
        for (Integer outsideRequest : outsideRequests) {
            int offset = Math.abs(outsideRequest - e.getCurrentFloorNumber());
            if (offset < dist &&
                    ((e.getCurrentFloorNumber() < outsideRequest
                            && outsideRequest < nearestFloor &&
                            client.getFloorButtonUp(outsideRequest)) ||
                            (e.getCurrentFloorNumber() > outsideRequest
                                    && outsideRequest > nearestFloor &&
                                    client.getFloorButtonDown(outsideRequest))) &&
                    checkElevatorAlreadyAddressed(outsideRequest) == null) {
                dist = offset;
                nearestFloor = outsideRequest;
            }
        }

        return nearestFloor;
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

    @Override
    public void update(Object arg) {
        if (arg instanceof Mode) {
            this.mode = (Mode) arg;
            if (isFullyInitialized) {
                this.onElevatorsChanged();
            }
        } else if (arg instanceof Boolean) {
            this.isConnected = (Boolean) arg;
        } else if (arg instanceof List) {
            this.elevators = (List<Elevator>) arg;
            isFullyInitialized = this.isFullyInitialized();

            if (this.mode == Mode.AUTOMATIC && isFullyInitialized)
                this.onElevatorsChanged();
        }
    }

    private boolean isFullyInitialized() {
        if (this.isFullyInitialized) {
            return true;
        }

        return !this.elevators.stream().anyMatch(e -> e.getCurrentElevatorFloor() == null);
    }
}