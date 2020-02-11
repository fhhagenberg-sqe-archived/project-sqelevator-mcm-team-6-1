package at.fhhagenberg.sqelevator.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {

    @Test
    public void testElevatorNumber() {
        Elevator elevator = new Elevator();
        assertEquals(0, elevator.getElevatorNumber());
        elevator.setElevatorNumber(3);
        assertEquals(3, elevator.getElevatorNumber());
    }

    @Test
    public void testMaximumPayload() {
        Elevator elevator = new Elevator();
        assertEquals(0, elevator.getMaximumPayload());
        elevator.setMaximumPayload(12345);
        assertEquals(12345, elevator.getMaximumPayload());
    }

    @Test
    public void testElevatorFloors() {
        Elevator elevator = new Elevator();
        ElevatorFloor elevatorFloor1 = new ElevatorFloor(new Floor(1));
        ElevatorFloor elevatorFloor2 = new ElevatorFloor(new Floor(-1));
        ArrayList list = new ArrayList<ElevatorFloor>();
        list.add(elevatorFloor1);
        list.add(elevatorFloor2);

        elevator.setElevatorFloors(list);
        assertEquals(list, elevator.getElevatorFloors());
    }

    @Test
    public void testCurrentElevatorFloor() {
        Elevator elevator = new Elevator();
        assertNull(elevator.getCurrentElevatorFloor());
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));
        elevator.setCurrentElevatorFloor(elevatorFloor);
        assertEquals(elevatorFloor, elevator.getCurrentElevatorFloor());
    }

    @Test
    public void testTargetedElevatorFloor() {
        Elevator elevator = new Elevator();
        assertNull(elevator.getTargetedElevatorFloor());
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(1));
        elevator.setTargetedElevatorFloor(elevatorFloor);
        assertEquals(elevatorFloor, elevator.getTargetedElevatorFloor());
    }

    @Test
    public void testDoorStatus() {
        Elevator elevator = new Elevator();
        assertEquals(DoorStatus.OPEN, elevator.getDoorStatus());

        elevator.setDoorStatus(DoorStatus.CLOSED);
        assertEquals(DoorStatus.CLOSED, elevator.getDoorStatus());
    }

    @Test
    public void testVelocity() {
        Elevator elevator = new Elevator();
        assertEquals(0.0, elevator.getVelocity());
        elevator.setVelocity(1.234);
        assertEquals(1.234, elevator.getVelocity());
    }

    @Test
    public void testPayload() {
        Elevator elevator = new Elevator();
        assertEquals(0.0, elevator.getPayload());
        elevator.setPayload(1.432);
        assertEquals(1.432, elevator.getPayload());
    }

    @Test
    public void testDirection() {
        Elevator elevator = new Elevator();
        assertEquals(Direction.UNCOMMITED, elevator.getDirection());
        elevator.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, elevator.getDirection());
    }

    @Test
    public void testAlarms() {
        Elevator elevator = new Elevator();
        assertEquals(List.of(), elevator.getAlarms());
        Alarm alarm = new Alarm("test", LocalDateTime.now());
        ArrayList<Alarm> list = new ArrayList<Alarm>();
        list.add(alarm);
        elevator.setAlarms(FXCollections.observableList(list));
        assertEquals(list, elevator.getAlarms());
    }

    @Test
    public void testFloorRequests() {
        Elevator elevator = new Elevator();
        assertNull(elevator.getFloorRequests());

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        elevator.setFloorRequests(FXCollections.observableList(list));
        assertEquals(list, elevator.getFloorRequests());
    }
}
