package at.fhhagenberg.sqelevator.logic.automaticmode;

import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.data.IElevatorClient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AutomaticElevatorMode implements IAutomaticModeStrategy {

    private static final Logger LOGGER = Logger.getLogger("AutomaticElevatorMode");

    private List<Elevator> elevators;
    private IElevatorClient client;
    private List<Integer> outsideRequests;
    private List<Integer>[] insideRequests;
    private Integer[] currentTargets;
    private boolean isFullyInitialized = false;
    private long[] systemMilliSecSinceLastMoved;
    private Integer[] lastKnownPosition;
    private OutsideRequestManager outsideRequestManager;
    private InsideRequestManager insideRequestManager;
    private RequestHelper helper;

    public AutomaticElevatorMode(IElevatorClient client) {
        this.setClient(client);
    }

    private boolean isElevatorAvailable(Elevator elevator) {
        return elevator.getCurrentElevatorFloor() == elevator.getTargetedElevatorFloor() && elevator.getDoorStatus() == DoorStatus.OPEN;
    }

    @Override
    public void execute(List<Elevator> elevators) {
        this.elevators = elevators;
        isFullyInitialized = this.helper.areAllElevatorsInitialized(isFullyInitialized, elevators);

        if (isFullyInitialized) {
            try {
                initialize();

                this.freeUpTargetsAndWorkaroundForStuckElevators();

                this.outsideRequests = this.outsideRequestManager.getOutsideRequestsFromClient();
                this.insideRequests = this.insideRequestManager.getInsideRequestsFromClient(elevators);
                startElevatorRoutine();
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
            }
        }
    }


    private void freeUpTargetsAndWorkaroundForStuckElevators() throws RemoteException {
        for (int i = 0; i < elevators.size(); i++) {
            int currentFloorNumber = client.getCurrentFloor(elevators.get(i)).getFloor().getFloorNumber();
            this.workaroundForStuckElevators(currentFloorNumber, i);

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

    @Override
    public void setClient(IElevatorClient client) {
        this.client = client;
        this.helper = new RequestHelper(client);
        this.outsideRequestManager = new OutsideRequestManager(client, helper);
        this.insideRequestManager = new InsideRequestManager(client, helper);
    }

    private void workaroundForStuckElevators(int currentFloorNumber, int i) throws RemoteException {
        if (lastKnownPosition[i] != currentFloorNumber) {
            lastKnownPosition[i] = currentFloorNumber;
            systemMilliSecSinceLastMoved[i] = System.currentTimeMillis();
        }

        long notMovedTime = System.currentTimeMillis() - systemMilliSecSinceLastMoved[i];
        if (notMovedTime > 300) {
            client.setTarget(elevators.get(i), currentFloorNumber);
            try {
                Thread.sleep(300);
                if (currentTargets[i] != null) {
                    client.setTarget(elevators.get(i), currentTargets[i]);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void initialize() throws RemoteException {
        if (client != null && insideRequests == null) {
            this.insideRequests = this.insideRequestManager.initialize();

            if (currentTargets == null) {
                currentTargets = new Integer[insideRequests.length];
            }

            if (lastKnownPosition == null) {
                lastKnownPosition = new Integer[insideRequests.length];
                systemMilliSecSinceLastMoved = new long[insideRequests.length];
            }

            for (int i = 0; i < client.getElevators().size(); i++) {
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
            Elevator elevator = this.helper.checkElevatorAlreadyAddressed(outsideRequest, elevators);
            if (elevator == null) {
                List<Elevator> availableElevators = this.helper.findAvailableElevators();

                if (availableElevators != null && !availableElevators.isEmpty()) {
                    elevator = this.outsideRequestManager.findClosestElevator(availableElevators, outsideRequest, elevators);

                    if (elevator != null) {
                        var maybeElevatorFloor = client.getFloorByNumber(elevator, outsideRequest);

                        if(maybeElevatorFloor.isPresent()) {
                            client.setTarget(elevator, maybeElevatorFloor.get().getFloor());
                        }
                    }
                }
            }
        }

    }

    private void targetElevatorToNextInsideRequest(int index, Elevator elevator) throws RemoteException {
        Integer nearestFloor = currentTargets[index];
        int currentFloor = this.client.getCurrentFloor(elevator).getFloor().getFloorNumber();

        if (nearestFloor == null) {
            nearestFloor = this.insideRequestManager.findNearestInsideRequest(index, elevator, insideRequests);
            currentTargets[index] = nearestFloor;
        }

        nearestFloor = insideRequestManager.findNearestInsideRequestToCurrentRequest(
                index, nearestFloor, currentFloor, insideRequests, elevators);
        nearestFloor = outsideRequestManager.findNearestIntermediateOutsideRequest(
                nearestFloor, currentFloor, outsideRequests, elevators);

        if (nearestFloor != null) {
            currentTargets[index] = nearestFloor;
            client.setTarget(elevator, currentFloor);
            client.setTarget(elevator, nearestFloor);
        }
    }
}