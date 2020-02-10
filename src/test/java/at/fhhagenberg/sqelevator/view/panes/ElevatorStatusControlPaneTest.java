package at.fhhagenberg.sqelevator.view.panes;

import at.fhhagenberg.sqelevator.data.IElevatorClient;
import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.domain.ElevatorFloor;
import at.fhhagenberg.sqelevator.domain.Floor;
import at.fhhagenberg.sqelevator.logic.ElevatorStatusPollingService;
import at.fhhagenberg.sqelevator.logic.automaticmode.IAutomaticModeStrategy;
import at.fhhagenberg.sqelevator.logic.RemoteConsoleViewModel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class ElevatorStatusControlPaneTest {

    private RemoteConsoleViewModel viewModel;
    private List<Elevator> elevators;
    private Elevator elevator;

    @Mock
    private IElevatorClient elevatorClient;

    @Mock
    private IAutomaticModeStrategy automaticModeStrategy;

    @Mock
    private ElevatorStatusPollingService elevatorStatusPollingService;

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
        elevator2.setElevatorFloors(floors);
        elevators.add(elevator2);
    }

    @Start
    public void start(Stage stage) {
        this.dataSetup();
        Mockito.mockitoSession().initMocks(this).strictness(Strictness.LENIENT).startMocking();
        Mockito.when(elevatorClient.getElevators()).thenReturn(elevators);
        viewModel = new RemoteConsoleViewModel(elevatorClient, automaticModeStrategy, elevatorStatusPollingService);

        var pane = new ElevatorStatusControlPane(elevator, viewModel);
        var scene = new Scene(pane, 500, 550);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var speedLabel = (Label) robot.lookup("#SpeedControlLabel").queryAll().iterator().next();
        var payloadLabel = (Label) robot.lookup("#PayloadControlLabel").queryAll().iterator().next();
        var manual = (Label)robot.lookup("#Manual").queryAll().iterator().next();
        var automatic = (Label)robot.lookup("#Automatic").queryAll().iterator().next();
        var connected = (Circle)robot.lookup("#IsConnectedStatus").queryAll().iterator().next();
        var open = (Label)robot.lookup("#Open").queryAll().iterator().next();
        var closed = (Label)robot.lookup("#Closed").queryAll().iterator().next();

        assertNotNull(speedLabel);
        assertNotNull(payloadLabel);
        assertNotNull(manual);
        assertNotNull(automatic);
        assertNotNull(connected);
        assertNotNull(open);
        assertNotNull(closed);
    }
}
