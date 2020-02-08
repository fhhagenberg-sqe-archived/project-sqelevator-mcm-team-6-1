package at.fhhagenberg.sqelevator.view.control;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.view.controls.DoorsControl;
import at.fhhagenberg.sqelevator.view.controls.PayloadControl;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class PayloadControlTest {
    private Elevator elevator = new Elevator();

    @Start
    public void start(Stage stage) {
        var pane = new PayloadControl(elevator);
        var scene = new Scene(pane, 250, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var label = (Label)robot.lookup("#PayloadControlLabel").queryAll().iterator().next();
        assertEquals("0.0 kg", label.getText());
    }

    @Test
    public void testChangePayload(FxRobot robot) {
        var label = (Label)robot.lookup("#PayloadControlLabel").queryAll().iterator().next();
        Platform.runLater(() -> elevator.setPayload(1.212));
        robot.interrupt();
        assertEquals("1.212 kg", label.textProperty().get());
    }
}
