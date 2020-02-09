package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class ServiceEnabledLabelTest {

    private SimpleBooleanProperty property = new SimpleBooleanProperty(false);

    @Start
    public void start(Stage stage) {
        var label = new ServiceEnabledLabel(property);
        var pane = new BorderPane();
        pane.getChildren().add(label);
        var scene = new Scene(pane, 250, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testInit(FxRobot robot) {
        var label = (Label)robot.lookup("#ServiceEnabled").queryAll().iterator().next();

        assertEquals(Color.GRAY, label.getTextFill());
        assertEquals(BorderStyle.THIN_GREY.value(), label.getBorder());
    }

    @Test
    public void testSetEnabled(FxRobot robot) {
        var label = (Label)robot.lookup("#ServiceEnabled").queryAll().iterator().next();
        property.setValue(true);
        assertEquals(Color.BLACK, label.getTextFill());
        assertEquals(BorderStyle.THIN_GREEN.value(), label.getBorder());
    }
}
