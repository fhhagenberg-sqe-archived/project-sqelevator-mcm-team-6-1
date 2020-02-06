package at.fhhagenberg.sqelevator.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlarmTest {

    @Test
    public void testConstructor() {
        String test = "test";
        LocalDateTime dateTime = LocalDateTime.of(2017, Month.JUNE, 15, 13, 39);
        Alarm alarm = new Alarm(test, dateTime);

        assertEquals(test, alarm.getMessage());
        assertEquals(dateTime, alarm.getTimestamp());
    }

    @Test
    public void testMessageSetter() {
        Alarm alarm = new Alarm("", LocalDateTime.MIN);

        assertEquals("", alarm.getMessage());
        assertEquals(LocalDateTime.MIN, alarm.getTimestamp());

        alarm.setMessage("test");
        alarm.setTimestamp(LocalDateTime.MAX);

        assertEquals("test", alarm.getMessage());
        assertEquals(LocalDateTime.MAX, alarm.getTimestamp());
    }
}
