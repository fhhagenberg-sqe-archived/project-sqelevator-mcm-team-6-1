package at.fhhagenberg.sqelevator.view.panes;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.view.panes.SpeedPayloadPane;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class SpeedPayloadPaneTest {

    private Elevator elevator = new Elevator();

    @Start
    public void start(Stage stage) {
        var pane = new SpeedPayloadPane(elevator);
        var scene = new Scene(pane, 500, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testPayloadInit(FxRobot robot) {
        var label = (Label)robot.lookup("#PayloadControlLabel").queryAll().iterator().next();
        assertEquals("0.0 kg", label.getText());
    }

    @Test
    public void testSpeedInit(FxRobot robot) {
        var label = (Label)robot.lookup("#SpeedControlLabel").queryAll().iterator().next();
        assertEquals("0.0 km/h", label.getText());
    }

    @Test
    public void testSpeedAndPayloadChange(FxRobot robot) {
        var payloadLabel = (Label)robot.lookup("#PayloadControlLabel").queryAll().iterator().next();
        Platform.runLater(() -> elevator.setPayload(-12));
        robot.interrupt();
        assertEquals("-12.0 kg", payloadLabel.textProperty().get());

        var speedLabel = (Label)robot.lookup("#SpeedControlLabel").queryAll().iterator().next();
        Platform.runLater(() -> elevator.setVelocity(-13));
        robot.interrupt();
        assertEquals("-13.0 km/h", speedLabel.textProperty().get());
    }
}
