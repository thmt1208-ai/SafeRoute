public class SafetyMonitor {
    private int routeRisk = 0; 
    private int checkInRisk = 0; 
    private int emergencyRisk = 0; 
    private int arrivalRisk = 0;
    private boolean previousOffRoute = false; 
    public synchronized boolean isOffRoute(Trip trip, String currentLocation) {
        for(String location : trip.getPlannedRoute()) {
            if (location.equalsIgnoreCase(currentLocation)) {
                return false;
            }
        }
        return true; 
    }
    public synchronized void updateRouteRisk(Trip trip, String currentLocation) {
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
    public synchronized void markCheckInMissed() {
        checkInRisk = 30;
    }
    public synchronized void clearCheckInRisk() {
        checkInRisk = 0;
    }
    public synchronized void activateEmergencyRisk() {
        emergencyRisk = 100;
    }
    public synchronized int getRiskScore() {
        return Math.min(100, routeRisk + checkInRisk + emergencyRisk + arrivalRisk);
    }

    public synchronized String getRiskLevel() {
        int score = getRiskScore(); 

        if (score >= 70) {
            return "CRITICAL";
        } else if (score >= 40) {
            return "HIGH";
        } else if (score >= 10) {
            return "MEDIUM";
        } else {
            return "LOW"; 
        }  
    }
    public synchronized void resetRisk() {
        routeRisk = 0;
        checkInRisk = 0;
        emergencyRisk = 0;
        arrivalRisk = 0;
        previousOffRoute = false;
    }
    public synchronized void increaseArrivalRisk() {
        arrivalRisk = Math.min(100, arrivalRisk + 20);
    }
    public synchronized void clearArrivalRisk() {
        arrivalRisk = 0;
    }
}