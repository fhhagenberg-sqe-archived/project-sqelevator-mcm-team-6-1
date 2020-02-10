package at.fhhagenberg.sqelevator;

import at.fhhagenberg.sqelevator.logic.*;
import at.fhhagenberg.sqelevator.view.RemoteConsoleView;
import at.fhhagenberg.sqelevator.data.ElevatorClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sqelevator.IElevator;

import java.rmi.Naming;

public class ElevatorApplication extends Application {

    IRemoteConsoleViewModel viewModel;
    RemoteConsoleView view;

    @Override
    public void start(Stage primaryStage) throws Exception {
        var controller = (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
        var client = new ElevatorClient(controller);
        var automaticElevatorMode = new AutomaticElevatorMode(client);
        var pollingService = new ElevatorStatusPollingService(client, client.getElevators(), 100);

        pollingService.startPollingService();

        this.viewModel = new RemoteConsoleViewModel(client, automaticElevatorMode);
        this.view = new RemoteConsoleView(viewModel);

        var weightAlarmObservable = new WeightAlarmObserver();

        pollingService.addObserver(weightAlarmObservable);
        pollingService.addObserver(viewModel);

        primaryStage.setTitle("Elevator Remote Console");
        primaryStage.setScene( new Scene(this.view.createView(), 1200, 900, Color.WHITE));

        primaryStage.show();
    }
}
