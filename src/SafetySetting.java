public class SafetySetting {
    private static final int ROUTE_CHECK_INTERVAL = 5;
    private static final int WARNING_THRESHOLD = 40;
    private int checkInTimeout = 10;

    public int getRouteCheckInterval() {
        return ROUTE_CHECK_INTERVAL;
    }
    public int getWarningThreshold() {
        return WARNING_THRESHOLD; 
    }
    public int getCheckInTimeout() {
        return checkInTimeout; 
    }
    public boolean setCheckInTimeout(int minutes) {
        if (minutes == 5 || minutes == 10 ||
            minutes == 15 || minutes == 20) {
                this.checkInTimeout = minutes;
                return true;
            }
        return false;
    }
}