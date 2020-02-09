package at.fhhagenberg.sqelevator.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoteConsoleTest {

    @Test
    public void testElevators() {
        RemoteConsole remoteConsole = new RemoteConsole();
        Elevator elevator1 = new Elevator();
        Elevator elevator2 = new Elevator();
        ObservableList list = FXCollections.observableArrayList();
        list.add(elevator1);
        list.add(elevator2);

        assertEquals(0, remoteConsole.getElevators().size());
        remoteConsole.setElevators(list);
        assertEquals(2, remoteConsole.getElevators().size());
    }

    @Test
    public void testAlarms() {
        RemoteConsole remoteConsole = new RemoteConsole();
        Alarm alarm1 = new Alarm("test1", LocalDateTime.now());
        Alarm alarm2 = new Alarm("test2", LocalDateTime.now());
        ObservableList list = FXCollections.observableArrayList();
        list.add(alarm1);
        list.add(alarm2);

        assertEquals(0, remoteConsole.getAlarms().size());
        remoteConsole.setAlarms(list);
        assertEquals(2, remoteConsole.getAlarms().size());
    }
}
