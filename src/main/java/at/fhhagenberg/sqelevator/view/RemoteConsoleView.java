package at.fhhagenberg.sqelevator.view;

import at.fhhagenberg.sqelevator.logic.IRemoteConsoleViewModel;
import at.fhhagenberg.sqelevator.domain.*;
import at.fhhagenberg.sqelevator.view.panes.AlarmListPane;
import at.fhhagenberg.sqelevator.view.panes.ElevatorStatusControlPane;
import at.fhhagenberg.sqelevator.view.panes.ElevatorStatusPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RemoteConsoleView {

    private IRemoteConsoleViewModel viewModel;

    public RemoteConsoleView(IRemoteConsoleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public TabPane createView() {

        TabPane tabPane = new TabPane();

        for (Elevator elevator : viewModel.getElevatorListProperty()) {
            Tab tab = new Tab();
            tab.setText(String.format("Elevator %d", elevator.getElevatorNumber()));
            tab.setClosable(false);

            var masterPane = new HBox();

            HBox statusControlPane = new HBox();
            statusControlPane.getChildren().addAll(new ElevatorStatusPane(elevator, viewModel),
                    new ElevatorStatusControlPane(elevator, viewModel));
            statusControlPane.setSpacing(20);

            masterPane.getChildren().addAll(statusControlPane, new AlarmListPane(elevator));
            masterPane.setSpacing(20);
            masterPane.setPadding(new Insets(20));

            tab.setContent(new ScrollPane(masterPane));
            tabPane.getTabs().add(tab);
        }

        return tabPane;
    }
}
