package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.domain.Mode;
import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import at.fhhagenberg.sqelevator.view.style.ControlLabel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ModeControl extends VBox {
    public ModeControl(ObjectProperty<Mode> modeProperty) {

        Label modeLabel = new Label("Mode");
        modeLabel.setPrefWidth(100.0);
        modeLabel.setStyle("-fx-font-size: 20;");
        modeLabel.setPadding(new Insets(20, 0, 20, 0));

        Label automatic = new ControlLabel("Automatic");
        automatic.setId("Automatic");
        automatic.setOnMouseClicked(handler -> modeProperty.set(Mode.AUTOMATIC));
        automatic.backgroundProperty().bind(
                Bindings.when(modeProperty.isEqualTo(Mode.AUTOMATIC))
                        .then(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        Label manual = new ControlLabel("Manual");
        manual.setId("Manual");
        manual.setOnMouseClicked(handler -> modeProperty.set(Mode.MANUAL));
        manual.backgroundProperty().bind(
                Bindings.when(modeProperty.isEqualTo(Mode.MANUAL))
                        .then(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                        .otherwise(Background.EMPTY));

        this.getChildren().addAll(modeLabel, automatic, manual);
        this.setPadding(new Insets(10));
        this.setBorder(BorderStyle.THIN_BLACK.value());
    }
}
