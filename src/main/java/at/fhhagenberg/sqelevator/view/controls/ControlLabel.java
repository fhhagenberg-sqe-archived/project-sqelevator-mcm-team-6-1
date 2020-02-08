package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.BorderStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class ControlLabel extends Label {

    public ControlLabel(String text) {
        this.setText(text);
        this.setAlignment(Pos.CENTER);
        this.setBorder(BorderStyle.THIN_BLACK.value());
        this.setStyle("-fx-font-size: 20;");
        this.setPrefWidth(120.0);
        this.setPadding(new Insets(20, 0, 20, 0));
    }
}
