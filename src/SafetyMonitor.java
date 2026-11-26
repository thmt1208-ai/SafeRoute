public class SafetyMonitor {
    private int routeRisk = 0; 
    private int checkInRisk = 0; 
    private int emergencyRisk = 0; 
    private boolean previousOffRoute = false; 
    public boolean isOffRoute(Trip trip, String currentLocation) {
        for(String location : trip.getPlannedRoute()) {
            if (location.equalsIgnoreCase(currentLocation)) {
                return false;
            }
        }
        return true; 
    }
    public void updateRouteRisk(Trip trip, String currentLocation) {
        boolean offRoute = isOffRoute(trip, currentLocation); 

        if (offRoute) {
            routeRisk = Math.min(100, routeRisk + 10);
            if (!previousOffRoute) {
                System.out.println("Route deviation detected.");
            }
        } else {
            if (previousOffRoute) {
                System.out.println("Back on planned route.");
            }
            routeRisk = 0; 
        }
        previousOffRoute = offRoute;
    }
    public void markCheckInMissed() {
        checkInRisk = 30;
    }
    public void clearCheckInRisk() {
        checkInRisk = 0;
    }
    public void activateEmergencyRisk() {
        emergencyRisk = 100;
    }
    public int getRiskScore() {
        return Math.min(100, routeRisk + checkInRisk + emergencyRisk);
    }

    public String getRiskLevel() {
        int score = getRiskScore(); 

        if (score >= 70) {
            return "CRITICAL";
        } else if (score >= 40) {
            return "HIGH";
        } else if (score >= 10) {
            return "Medium";
        } else {
            return "LOW"; 
        }  
    }
}