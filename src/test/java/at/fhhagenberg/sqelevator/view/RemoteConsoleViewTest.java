package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.logic.IAutomaticModeStrategy;
import at.fhhagenberg.sqelevator.logic.IElevatorStatusPollingService;
import at.fhhagenberg.sqelevator.logic.RemoteConsoleViewModel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class RemoteConsoleViewTest {


    private RemoteConsoleViewModel viewModel;
    private List<Elevator> elevators;
    private Elevator elevator;

    @Mock
    private IElevatorClient elevatorClient;

    @Mock
    private IAutomaticModeStrategy automaticModeStrategy;

    private void dataSetup() {
        elevators = new ArrayList<>();
        ElevatorFloor elevatorFloor = new ElevatorFloor(new Floor(0));
        ElevatorFloor elevatorFloor2 = new ElevatorFloor(new Floor(1));
        ArrayList<ElevatorFloor> floors = new ArrayList<>();
        floors.add(elevatorFloor);
        floors.add(elevatorFloor2);

        elevator = new Elevator();
        elevator.setElevatorFloors(floors);
        elevators.add(elevator);
        Elevator elevator2 = new Elevator();
        elevator2.setElevatorNumber(1);
        elevator2.setElevatorFloors(floors);
        elevators.add(elevator2);
    }

    @Start
    public void start(Stage stage) {
        this.dataSetup();
        Mockito.when(elevatorClient.getElevators()).thenReturn(elevators);
        viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy);

        var pane = new RemoteConsoleView(viewModel).createView();
        Scene scene = new Scene(pane, 1000, 600);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        robot.interrupt();
        var elevator1 = (Label)robot.lookup("Elevator 0").queryAll().iterator().next();
        assertNotNull(elevator1);
        var elevator2 = (Label)robot.lookup("Elevator 1").queryAll().iterator().next();
        assertNotNull(elevator2);
    }
}
