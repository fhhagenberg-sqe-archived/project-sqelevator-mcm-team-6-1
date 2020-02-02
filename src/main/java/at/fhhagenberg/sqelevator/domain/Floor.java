package at.fhhagenberg.sqelevator.domain;

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
        return Integer.compare(this.getFloorNumber(), o.getFloorNumber());
    }
}
