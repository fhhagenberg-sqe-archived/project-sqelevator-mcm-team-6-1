package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorStatusTest {

    @Test
    public void testSpeed() {
        ElevatorStatus elevatorStatus = new ElevatorStatus();
        elevatorStatus.setSpeed(5);
        assertEquals(5, elevatorStatus.speedProperty().get());
        assertEquals(5, elevatorStatus.getSpeed());
    }

    @Test
    public void testPosition() {
        ElevatorStatus elevatorStatus = new ElevatorStatus();
        elevatorStatus.setPosition(4);
        assertEquals(4, elevatorStatus.positionProperty().get());
        assertEquals(4, elevatorStatus.getPosition());
    }

    @Test
    public void testDirection() {
        ElevatorStatus elevatorStatus = new ElevatorStatus();
        elevatorStatus.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, elevatorStatus.directionProperty().get());
        assertEquals(Direction.DOWN, elevatorStatus.getDirection());
    }

    @Test
    public void testCurrentPayload() {
        ElevatorStatus elevatorStatus = new ElevatorStatus();
        elevatorStatus.setCurrentPayload(12334);
        assertEquals(12334, elevatorStatus.getCurrentPayload());
        assertEquals(12334, elevatorStatus.currentPayloadProperty().get());
    }

    @Test
    public void testDoorStatus() {
        ElevatorStatus elevatorStatus = new ElevatorStatus();
        elevatorStatus.setDoorStatus(1);
        assertEquals(1, elevatorStatus.getDoorStatus());
        assertEquals(1, elevatorStatus.doorStatusProperty().get());
    }
}
