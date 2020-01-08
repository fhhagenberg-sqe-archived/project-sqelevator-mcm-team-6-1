package at.fhhagenberg.sqelevator.model;

public class Floor implements Comparable<Floor>{
    private int floorNumber;

    public Floor() {

    }

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public int compareTo(Floor o) {
        if (this.getFloorNumber() < o.getFloorNumber()) {
            return -1;
        }

        if (this.getFloorNumber() == o.getFloorNumber()) {
            return 0;
        }

        return 1;
    }
}
