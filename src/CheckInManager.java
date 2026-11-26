public class CheckInManager {
    private boolean checkInPending = false;
    private int requestTime = 0;
    private int responseTimeout = 10;

    public void requestCheckIn(int currentTime, int timeout) {
        if (!checkInPending) {
            checkInPending = true;
            requestTime = currentTime;
            responseTimeout = timeout;

            System.out.println("Safety check-in required.");
            System.out.println("Please confirm that you are safe.");
        }
    }
    public boolean isTimedOut(int currentTime) {
        return checkInPending && currentTime - requestTime >= responseTimeout;
    }
    public void confirmSafety() {
        checkInPending = false;
    }

    public boolean isCheckInPending() {
        return checkInPending;
    }

    public void resetCheckIn() {
        checkInPending = false;
        requestTime = 0;
    }
}