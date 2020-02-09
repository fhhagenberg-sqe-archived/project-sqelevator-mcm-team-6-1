package at.fhhagenberg.sqelevator.view.controls;

import at.fhhagenberg.sqelevator.view.style.BorderStyle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ServiceEnabledLabel extends Label {
    public ServiceEnabledLabel(BooleanProperty serviceEnabled) {

        this.setText("Service\nenabled");
        this.setPadding(new Insets(5));
        this.setTextAlignment(TextAlignment.CENTER);
        this.setId("ServiceEnabled");
        this.borderProperty().bind(
                Bindings.when(serviceEnabled)
                        .then(BorderStyle.THIN_GREEN.value())
                        .otherwise(BorderStyle.THIN_GREY.value())

        );

        this.textFillProperty().bind(
                Bindings.when(serviceEnabled)
                        .then(Color.BLACK)
                        .otherwise(Color.GRAY));

    }
}
