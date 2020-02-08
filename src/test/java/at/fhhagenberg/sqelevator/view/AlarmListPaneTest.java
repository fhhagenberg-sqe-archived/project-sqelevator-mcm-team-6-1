package at.fhhagenberg.sqelevator.view;


import at.fhhagenberg.sqelevator.domain.Alarm;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AlarmListPaneTest {
    private static Elevator elevator;

    @BeforeAll
    public static void setup() {
        elevator = new Elevator();
        var alarm = new Alarm("test message", LocalDateTime.MAX);
        var alarm2 = new Alarm("test message 2", LocalDateTime.MIN);
        ArrayList<Alarm> list = new ArrayList<Alarm>();
        list.add(alarm);
        list.add(alarm2);
        elevator.setAlarmList(list);
    }

    @Start
    public void start(Stage stage) {
        var pane = new AlarmListPane(this.elevator);
        var scene = new Scene(pane, 250, 600);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testSettingProperties(FxRobot robot) {
        var listView = (ListView<Alarm>) robot.lookup("#AlarmList").queryAll().iterator().next();
        assertFalse(listView.isEditable());
        assertEquals(SelectionMode.SINGLE, listView.selectionModelProperty().get().selectionModeProperty().get());
        assertNull(listView.selectionModelProperty().get().getSelectedItem());
    }

    @Test
    public void testElementsAppearInAlarmList(FxRobot robot) {
        var listView = (ListView<Alarm>)robot.lookup("#AlarmList").queryAll().iterator().next();
        assertEquals(2, listView.getItems().size());

        assertEquals(LocalDateTime.MAX, listView.getItems().get(0).getTimestamp());
        assertEquals("test message", listView.getItems().get(0).getMessage());

        assertEquals(LocalDateTime.MIN, listView.getItems().get(1).getTimestamp());
        assertEquals("test message 2", listView.getItems().get(1).getMessage());
    }

    @Test
    public void testSelectElement(FxRobot robot) {
        var listView = (ListView<Alarm>)robot.lookup("#AlarmList").queryAll().iterator().next();
        listView.selectionModelProperty().get().select(0);
        assertEquals("test message", listView.selectionModelProperty().get().getSelectedItem().getMessage());

        listView.selectionModelProperty().get().select(1);
        assertEquals("test message 2", listView.selectionModelProperty().get().getSelectedItem().getMessage());
    }
}
