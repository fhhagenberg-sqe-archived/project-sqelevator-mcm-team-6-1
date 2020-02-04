package at.fhhagenberg.sqelevator.logic;

import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.data.IElevatorClient;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class AutomaticElevatorMode implements Observer {
    private List<Elevator> elevators;
    private Mode mode;
    private boolean isConnected;
    private IElevatorClient client;
    private List<Integer> outsideRequests;
    private List<Floor>[] insideRequests;

    public AutomaticElevatorMode(IElevatorClient client) {
        this.client = client;
    }

    private void elevatorsChanged() {
        initialize();

        if (isConnected) {
            try {
                // set outside requests
                for (int i = 0; i < this.client.getFloorNum(); i++) {
                    if (client.getFloorButtonDown(i) || client.getFloorButtonUp(i)) {
                        addOutsideRequestIfNew(i);
                    }
                }

                // handle button presses inside elevators
                for (Elevator elevator : elevators) {
                    // if elevator is already in target floor: make sure direction gets set to uncommitted
                    if (insideRequests != null && insideRequests[elevator.getElevatorNumber()]
                            .contains(elevator.getCurrentElevatorFloor().getFloor().getFloorNumber()) && elevator.directionProperty.get() == Direction.UNCOMMITED) {
                        client.setTarget(elevator, elevator.getCurrentElevatorFloor().getFloor());
                        insideRequests[elevator.getElevatorNumber()].remove((Integer) elevator.getElevatorNumber());
                    }
                    if (outsideRequests != null &&
                            outsideRequests.contains(elevator.getCurrentElevatorFloor().getFloor().getFloorNumber()) && elevator.directionProperty.get() == Direction.UNCOMMITED) {
                        client.setTarget(elevator, elevator.getCurrentElevatorFloor().getFloor());
                        outsideRequests.remove((Integer) elevator.getCurrentElevatorFloor().getFloor().getFloorNumber());
                    }

                    for (int i = 0; i < this.client.getFloorNum(); i++) {
                        if (elevator.isFloorRequested(i)) {
                            addInsideRequest(elevator, this.client.getFloorByNumber(elevator, i).get().getFloor());
                        }
                    }
                }
            } catch (RemoteException e) {
                isConnected = false;
            }
        }
    }

    private void initialize() {
        if (client != null && insideRequests == null) {
            insideRequests = new List[client.getElevators().size()];
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
        // if current request is not on respective list: add it
        if (!insideRequests[e.getElevatorNumber()].contains(floor)) {
            insideRequests[e.getElevatorNumber()].add(floor);
        }
        startElevatorRoutine();
    }

    private void addOutsideRequestIfNew(int floor) throws RemoteException {
        if (!outsideRequests.contains(floor)) {
            outsideRequests.add(floor);
        }

        startElevatorRoutine();
    }

    private void startElevatorRoutine() throws RemoteException {
        if (client != null && elevators != null && !elevators.isEmpty()) {
            for (int i = 0; i < elevators.size(); i++) {
                if (!insideRequests[i].isEmpty()) {
                    targetNearestElevatorToInsideRequest(i, elevators.get(i));
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
            int offset = Math.abs(availableElevator.getCurrentFloorNumber() - outsideRequest);
            if (offset < distance) {
                distance = offset;
                elevator = availableElevator;
            }
        }

        return elevator;
    }

    private void targetNearestElevatorToInsideRequest(int index, Elevator elevator) throws RemoteException {
        int nearestFloor = 0;
        int floorNumber = client.getFloorNum();

        // get nearest requested floor
        for (int j = 0; j < insideRequests[index].size(); j++) {
            int offset = Math.abs(insideRequests[index].get(j).getFloorNumber() - elevator.getCurrentFloorNumber());
            if (offset < floorNumber) {
                floorNumber = offset;
                nearestFloor = insideRequests[index].get(j).getFloorNumber();
            }
        }

        // go to the next outside request instead if it is before toFloor
        nearestFloor = findNearestIntermediateOutsideRequest(elevator, nearestFloor);

        client.setTarget(elevator, nearestFloor);
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
                if (elevator.directionProperty.get() == Direction.UNCOMMITED &&
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
    public void update(Observable o, Object arg) {
        if (arg instanceof Mode) {
            this.mode = (Mode) arg;
        } else if (arg instanceof Boolean) {
            this.isConnected = (Boolean) arg;
        } else if (arg instanceof List) {
            this.elevators = (List<Elevator>) arg;
            this.elevatorsChanged();
        }
    }
}
