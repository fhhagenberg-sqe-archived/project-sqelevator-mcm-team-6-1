package at.fhhagenberg.sqelevator.view.style;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

public enum BorderStyle {
    THIN_BLACK(new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                    null, new BorderWidths(1)))),
    STANDARD_DARKBLUE(new Border(
            new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                    null, new BorderWidths(2)))),
    THIN_GREY(new Border(
            new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID,
                    null, new BorderWidths(1)))),
    THIN_GREEN(new Border(
            new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
                    null, new BorderWidths(1))));

    private Border border;

    BorderStyle(Border border) {
        this.border = border;
    }

    public Border value() {
        return this.border;
    }
}
