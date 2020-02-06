package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FloorTest {

    @Test
    public void testConstructor() {
        Floor floor = new Floor(2);
        assertEquals(2, floor.getFloorNumber());
    }

    @Test
    public void testSetter() {
        Floor floor = new Floor(-5);
        assertEquals(-5, floor.getFloorNumber());
        floor.setFloorNumber(4);
        assertEquals(4, floor.getFloorNumber());
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,0",
            "2,1,1",
            "0,3,-1"
    })
    public void testCompareFloors(int floorNumber1, int floorNumber2, int expected) {
        Floor floor1 = new Floor(floorNumber1);
        Floor floor2 = new Floor(floorNumber2);
        assertEquals(expected, floor1.compareTo(floor2));
    }
}
