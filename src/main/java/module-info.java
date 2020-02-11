module sqelevator {
    requires javafx.controls;
    requires java.rmi;

    opens sqelevator;
    opens at.fhhagenberg.sqelevator.data;
    opens at.fhhagenberg.sqelevator.domain;
    opens at.fhhagenberg.sqelevator.view;

    //    exports sqelevator;
    exports at.fhhagenberg.sqelevator;
}