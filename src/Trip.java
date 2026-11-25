
import java.util.ArrayList;

public class Trip {
        private String startLocation; 
        private String destination; 
        private ArrayList<String> plannedRoute;
        private Vehicle vehicle;
        public Trip(String startLocation, String destination) {
            this.startLocation = startLocation; 
            this.destination = destination; 
            this.plannedRoute = new ArrayList<>();

        }
        public void addLocation(String location) {
            plannedRoute.add(location); 
        }
        public String getStartLocation() {
            return startLocation; 
        }
        public String getDestination() {
            return destination; 
        }
        public ArrayList<String> getPlannedRoute() {
            return plannedRoute; 
        }
        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }
        public Vehicle getVehicle() {
            return vehicle;
        }

        
    }