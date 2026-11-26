public class GPSSimulator {
    private String currentLocation; 
    public GPSSimulator(String currentLocation) {
        this.currentLocation = currentLocation; 
    }
    public void updateLocation(String location) {
        this.currentLocation = location; 
    }
    public String getCurrentLocation() {
        return currentLocation; 
    }
}