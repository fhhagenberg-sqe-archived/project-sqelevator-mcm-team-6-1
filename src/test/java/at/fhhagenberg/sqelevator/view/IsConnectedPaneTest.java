package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.Elevator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class IsConnectedPaneTest {

    private SimpleBooleanProperty property = new SimpleBooleanProperty();

    @Start
    public void start(Stage stage) {
        property.setValue(false);
        var pane = new IsConnectedPane(property);
        var scene = new Scene(pane, 500, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var circle = (Circle)robot.lookup("#IsConnectedStatus").queryAll().iterator().next();
        assertEquals(Color.RED, circle.getFill());
    }

    @Test
    public void testChangeConnectionStatus(FxRobot robot) {
        var circle = (Circle) robot.lookup("#IsConnectedStatus").queryAll().iterator().next();
        property.setValue(true);
        assertEquals(Color.GREEN, circle.getFill());
    }
}
