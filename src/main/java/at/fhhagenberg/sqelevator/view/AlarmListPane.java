package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.BorderStyle;
import at.fhhagenberg.sqelevator.domain.Alarm;
import at.fhhagenberg.sqelevator.domain.Elevator;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AlarmListPane extends VBox {

    public AlarmListPane(Elevator elevator) {

        this.setSpacing(10.0);
        Label alarmListLabel = new Label("Alarm List");
        alarmListLabel.setStyle("-fx-font-size: 22;");

        ListView<Alarm> alarmList = new ListView<>();
        alarmList.setBorder(BorderStyle.THIN_BLACK.value());
        alarmList.itemsProperty().bind(elevator.getAlarmList());
//        alarmList.setItems(remoteConsole.getAlarms());
        VBox.setVgrow(alarmList, Priority.SOMETIMES);

        alarmList.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(Alarm alarm, boolean b) {
                super.updateItem(alarm, b);

                if (alarm != null) {
                    setText(alarm.getTimestamp() + " " + alarm.getMessage());
                    setFont(Font.font(14.0));
                }
            }
        });

        this.getChildren().addAll(alarmListLabel, alarmList);
    }
}
