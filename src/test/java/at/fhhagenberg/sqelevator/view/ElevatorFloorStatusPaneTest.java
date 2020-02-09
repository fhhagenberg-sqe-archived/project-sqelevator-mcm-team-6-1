package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.logic.RemoteConsoleViewModel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class ElevatorFloorStatusPaneTest {

    private Elevator elevator;
    private ElevatorFloor elevatorFloor;

    private RemoteConsoleViewModel viewModel;
    private List<Elevator> elevators;

    @Mock
    private IElevatorClient elevatorClient;


    @Start
    public void start(Stage stage) {
        elevators = new ArrayList<>();
        elevator = new Elevator();
        elevators.add(elevator);
        Mockito.when(elevatorClient.getElevators()).thenReturn(elevators);
        elevatorFloor = new ElevatorFloor(new Floor(1));
        viewModel = new RemoteConsoleViewModel(elevatorClient);

        var pane = new ElevatorFloorStatusPane(elevator, elevatorFloor, viewModel);
        var scene = new Scene(pane, 500, 550);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var upLabel = (Label) robot.lookup("#UpLabel").queryAll().iterator().next();
        var downLabel = (Label) robot.lookup("#DownLabel").queryAll().iterator().next();
        var serviceLabel = (Label)robot.lookup("#ServiceEnabled").queryAll().iterator().next();
        var floorName = (Label)robot.lookup("#FloorNameNumber").queryAll().iterator().next();
        var rightRect = (Rectangle) robot.lookup("#RightDoorPart").queryAll().iterator().next();
        var leftRect = (Rectangle) robot.lookup("#LeftDoorPart").queryAll().iterator().next();

        assertNotNull(rightRect);
        assertNotNull(leftRect);
        assertNotNull(upLabel);
        assertNotNull(downLabel);
        assertNotNull(serviceLabel);
        assertNotNull(floorName);
    }

    @Test
    public void testFloorClick(FxRobot robot) throws RemoteException {
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);

            assertEquals(elevator, arg0);
            assertEquals(elevatorFloor.getFloor(), arg1);
            return null;
        }).when(elevatorClient).setTarget(any(), any());
        robot.clickOn("#ElevatorFloorStatus");
    }
}
