public class GPSSimulator {
    private String currentLocation; 
    public GPSSimulator(String currentLocation) {
        this.currentLocation = currentLocation; 
    }
    public synchronized void updateLocation(String location) {
        this.currentLocation = location;
    }
    public synchronized String getCurrentLocation() {
        return currentLocation;
    }
}