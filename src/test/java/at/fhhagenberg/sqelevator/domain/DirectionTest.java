package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {

    @Test
    public void testUP() {
        Direction direction = Direction.UP;
        assertEquals("up", direction.toString());
    }

    @Test
    public void testDOWN() {
        Direction direction = Direction.DOWN;
        assertEquals("down", direction.toString());
    }

    @Test
    public void testUNCOMMITED() {
        Direction direction = Direction.UNCOMMITED;
        assertEquals("uncommited", direction.toString());
    }
}
