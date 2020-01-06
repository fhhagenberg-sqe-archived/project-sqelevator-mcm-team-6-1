package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.ElevatorFloor;
import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.model.RemoteConsole;

public class DummyRemoteConsole {
    public static RemoteConsole generate() {
        RemoteConsole remoteConsole = new RemoteConsole();

        Elevator elevator1 = new Elevator();
        elevator1.setElevatorNumber(1);
        remoteConsole.addElevator(elevator1);

        ElevatorFloor elevatorFloor1 = new ElevatorFloor();
        elevatorFloor1.setFloor(new Floor(3));
        elevator1.addElevatorFloor(elevatorFloor1);

        ElevatorFloor elevatorFloor2 = new ElevatorFloor();
        elevatorFloor2.setFloor(new Floor(1));
        elevator1.addElevatorFloor(elevatorFloor2);

        ElevatorFloor elevatorFloor3 = new ElevatorFloor();
        elevatorFloor3.setFloor(new Floor(2));
        elevator1.addElevatorFloor(elevatorFloor3);

        ElevatorFloor elevatorFloor4 = new ElevatorFloor();
        elevatorFloor4.setFloor(new Floor(4));
        elevator1.addElevatorFloor(elevatorFloor4);

        ElevatorFloor elevatorFloor5 = new ElevatorFloor();
        elevatorFloor5.setFloor(new Floor(5));
        elevator1.addElevatorFloor(elevatorFloor5);

        elevatorFloor2.setUpRequest(true);
        elevatorFloor3.setDownRequest(true);

        Elevator elevator2 = new Elevator();
        elevator2.setElevatorNumber(2);
        remoteConsole.addElevator(elevator2);

        Elevator elevator3 = new Elevator();
        elevator3.setElevatorNumber(3);
        remoteConsole.addElevator(elevator3);
        return remoteConsole;
    }
}
