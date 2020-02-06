package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModeTest {

    @Test
    public void testManual() {
        Mode mode = Mode.MANUAL;
        assertEquals("manual", mode.toString());
    }

    @Test
    public void testAutomatic() {
        Mode mode = Mode.AUTOMATIC;
        assertEquals("automatic", mode.toString());
    }
}
