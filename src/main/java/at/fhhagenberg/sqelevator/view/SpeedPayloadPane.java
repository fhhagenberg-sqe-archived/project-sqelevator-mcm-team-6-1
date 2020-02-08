package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.domain.Elevator;
import at.fhhagenberg.sqelevator.view.controls.PayloadControl;
import at.fhhagenberg.sqelevator.view.controls.SpeedControl;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;

public class SpeedPayloadPane extends BorderPane {
    public SpeedPayloadPane(Elevator elevator) {
        this.setLeft(new SpeedControl(elevator));
        this.setRight(new PayloadControl((elevator)));
    }
}
