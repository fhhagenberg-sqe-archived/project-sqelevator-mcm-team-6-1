package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.domain.Mode;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class ModeControlTest {

    SimpleObjectProperty<Mode> property = new SimpleObjectProperty<>(Mode.MANUAL);
    @Start
    public void start(Stage stage) {
        var pane = new ModeControl(property);
        var scene = new Scene(pane, 250, 250);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void startInit(FxRobot robot) {
        var manual = (Label)robot.lookup("#Manual").queryAll().iterator().next();
        var automatic = (Label)robot.lookup("#Automatic").queryAll().iterator().next();
        assertEquals(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)), manual.getBackground());
        assertEquals(Background.EMPTY, automatic.getBackground());
    }

    @Test
    public void testChangeMode(FxRobot robot) {
        robot.clickOn("#Automatic");
        var manual = (Label)robot.lookup("#Manual").queryAll().iterator().next();
        var automatic = (Label)robot.lookup("#Automatic").queryAll().iterator().next();
        robot.interrupt();
        assertEquals(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)), automatic.getBackground());
        assertEquals(Background.EMPTY, manual.getBackground());

    }
}
